package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;


public class Download extends Thread{
	private FileDetails ficheiro;
	private byte[] ficheiroDescarregado;
	private int tamanhoDosBlocos = TheISCTEBay.tamanhoDosBlocos;
	private int tamanhoDoFicheiro;
	private int numeroBlocosCompletos;
	private int numeroTotalBlocos;
	private int tamanhoDoUltimoBloco;
	private ArrayList<Utilizador> users;
	private int numeroDeUsers;
	private ArrayList<ThreadUser> threadUsers = new ArrayList<ThreadUser>();
	private char[] estadoBlocos;
	private int nrBlocosPorUser;
	private long timeStart;
	private int contadorBlocosDescarregados;
	private WinDownload w;

	public Download(FileDetails f, WinDownload wdw) {
		timeStart = System.currentTimeMillis();
		ficheiro = f;
		ficheiroDescarregado = new byte[(int) f.bytesFicheiro()];
		tamanhoDoFicheiro = (int)f.bytesFicheiro();
		numeroBlocosCompletos = tamanhoDoFicheiro/tamanhoDosBlocos;
		tamanhoDoUltimoBloco = (tamanhoDoFicheiro % tamanhoDosBlocos);
		numeroTotalBlocos = numeroBlocosCompletos;
		if (tamanhoDoUltimoBloco > 0) numeroTotalBlocos++;
		users = f.getUtilizadores();
		numeroDeUsers = users.size();
		estadoBlocos = new char [numeroTotalBlocos]; //numeroBlocosCompletos + 1, se houver resto
		Arrays.fill(estadoBlocos, '0');
		nrBlocosPorUser = numeroTotalBlocos/numeroDeUsers;
		w=wdw;
		contadorBlocosDescarregados=0;
	}

	@Override
	public void run() {
		int u=0;

		//Cria uma nova thread por cada User que tenha o ficheiro
		while(u < numeroDeUsers) {
			ThreadUser tuser = new ThreadUser(users.get(u), u*nrBlocosPorUser);
			threadUsers.add(tuser);
			tuser.start();
			u++;
		}

		String msg = "Ficheiro: " + ficheiro.toString() + "\nDescarga completa. \n";
		Iterator<ThreadUser> iUsers = threadUsers.iterator();
		while(iUsers.hasNext()) {
			ThreadUser uThread = iUsers.next();
			try {
				uThread.join();
				msg += "Fornecedor [endereco=/" + uThread.getUserIp() + ", porto=" + uThread.getUserPorto() + "]:" + uThread.getNumeroBlocos() + "\n";
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			Files.write(Paths.get(TheISCTEBay.devolvePastaTransferencias()+"/"+ficheiro.nomeFicheiro()), ficheiroDescarregado);
		} catch (IOException e) {
			e.printStackTrace();
		}
		w.atualizabarraDeProgresso(100);
		msg += "Total de blocos: " + numeroTotalBlocos + "\n";
		msg += "Tempo decorrido: " + (System.currentTimeMillis()- timeStart) + " ms";
		MsgBox.info(msg);
	}

	private class ThreadUser extends Thread{
		private int blocosDescarregadosThread;
		private FileBlockRequestMessage pedidoBloco;
		private Utilizador user;
		private byte[] parteCarregada;
		private int inicioDownload;
		private int pesquisa;
		private int sentidoPesquisa = 1;
		private boolean primeiraInversao = true;
		private Socket s;
		private ObjectOutputStream oos;
		private ObjectInputStream ois;

		public ThreadUser (Utilizador u, int i) {
			user = u;
			blocosDescarregadosThread=0;
			inicioDownload = i;
			pesquisa = i;
		}

		public String getUserIp() {
			return user.ipUtilizador();
		}

		public String getUserPorto() {
			return user.portoUtilizador();
		}

		public int getNumeroBlocos() {
			return blocosDescarregadosThread;
		}

		public synchronized int indexOf(char[] array, char c, int inicio) {
			int i= inicio;
			do {
				if (array[i] == c) return i;
				i = i + sentidoPesquisa;
				if (i >= array.length) i = 0;
				if (i < 0) i = array.length-1;
			} while (i != inicio);
			return -1;
		}

		private FileBlockRequestMessage devolveTipoDeBloco(int nrBloco) {
			//se não é o último bloco
			//offset = nrBloco*tamanhoDosBlocos
			if (nrBloco < numeroBlocosCompletos) {
				return new FileBlockRequestMessage(ficheiro, nrBloco*tamanhoDosBlocos, tamanhoDosBlocos, nrBloco);
			//se é o último bloco
			} else {
				return new FileBlockRequestMessage(ficheiro, nrBloco*tamanhoDosBlocos, tamanhoDoUltimoBloco, nrBloco);
			}
		}

		private void incrementaPesquisa() {
			pesquisa = pesquisa + sentidoPesquisa; // adiciona ou subtrai 1 ao índice do bloco
			if (pesquisa >= numeroTotalBlocos) pesquisa = 0; //chegou ao fim do total de blocos
			if (pesquisa < 0) pesquisa = numeroTotalBlocos-1; // fica posicionado no último bloco
		}

		private synchronized FileBlockRequestMessage proximoBloco() {
			if (estadoBlocos[pesquisa] == '0') {
				FileBlockRequestMessage bloco = devolveTipoDeBloco(pesquisa); //distingue se se trata do último bloco
				incrementaPesquisa();
				return bloco;
			}
			if (primeiraInversao) {
				pesquisa = inicioDownload;  //índice fica com o valor do índice do bloco inicial
				sentidoPesquisa = -1;
				primeiraInversao = false;
			}
			
			incrementaPesquisa();
			int pd = indexOf(estadoBlocos, '0', pesquisa);
			if (pd != -1) { //sinaliza o bloco como pedido e coloca o índice da pesquisa na posição do bloco
				estadoBlocos[pd]='P';
				pesquisa = pd;
				return devolveTipoDeBloco(pd);
			}
			// vai procurar nos pedidos mas não descarregados
			int des = indexOf(estadoBlocos, 'P', pesquisa);
			if (des != -1) {
				pesquisa = des;
				return devolveTipoDeBloco(des);
			}
			// no caso de todos estarem sinalizados como tendo sido descarregados, retorna tudo a -1
			return new FileBlockRequestMessage(ficheiro, -1, -1, -1);
		}

		@Override
		public void run() {
			if (numeroTotalBlocos == 0) return;
			try {
				s = new Socket(user.ipUtilizador(), Integer.parseInt(user.portoUtilizador()));
				oos = new ObjectOutputStream(s.getOutputStream());
				ois = new ObjectInputStream(s.getInputStream());
				while(true) {
					pedidoBloco = proximoBloco();
					if (pedidoBloco.getNumeroDoBloco() == -1) break; //se todos os blocos foram já descarregados
					oos.flush();
					oos.writeObject(pedidoBloco);
					parteCarregada = (byte[])ois.readObject();
					if (estadoBlocos[pedidoBloco.getNumeroDoBloco()] == 'D') continue;
					synchronized (ficheiroDescarregado) { 
						System.arraycopy(parteCarregada, 0, ficheiroDescarregado, pedidoBloco.getOffset(), pedidoBloco.getLength());
					}
					estadoBlocos[pedidoBloco.getNumeroDoBloco()] = 'D';
					blocosDescarregadosThread++;
					contadorBlocosDescarregados++;
					w.atualizabarraDeProgresso((contadorBlocosDescarregados*100)/numeroTotalBlocos);
				}
			} catch (NumberFormatException | IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}finally {
				try {
					oos.close();
					ois.close();
					s.close();	
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}