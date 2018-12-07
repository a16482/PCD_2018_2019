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
	private ArrayList<User> threadUsers = new ArrayList<User>();
	private char[] estadoBlocos;
	private int nrBlocosPorUser;
	private long timeStart;
	private int blocosDescarregados;
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
		users = f.getUtilizadors();
		numeroDeUsers = users.size();
		estadoBlocos = new char [numeroBlocosCompletos+1];
		Arrays.fill(estadoBlocos, '0');
		nrBlocosPorUser = numeroTotalBlocos/numeroDeUsers;
		System.out.println("Número de Partes: " + numeroTotalBlocos);
		w=wdw;
		blocosDescarregados=0;
	}

	@Override
	public void run() {
		//		int contadorPartes=0;
		int u=0;

		//Cria uma nova thread por cada User que tenha o ficheiro
		while(u < numeroDeUsers) {
			User user = new User(users.get(u), u*nrBlocosPorUser);
			threadUsers.add(user);
			user.start();
			u++;
		}

		String msg = "ficheiro: " + ficheiro.toString() + "\nDescarga completa. \n";
		Iterator<User> iUsers = threadUsers.iterator();
		while(iUsers.hasNext()) {
			User uThread = iUsers.next();
			try {
				uThread.join();
				msg += "Fornecedor [endereco=/" + uThread.getUserIp() + ", porto=" + uThread.getUserPorto() + "]:" + uThread.getNumeroBlocos() + "\n";
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			Files.write(Paths.get(TheISCTEBay.devolvePastaTransferencias()+"/"+ficheiro.nomeFicheiro()), ficheiroDescarregado);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		w.atualizabarraDeProgresso(100);
		msg += "Total de blocos: " + numeroTotalBlocos + "\n";
		msg += "Tempo decorrido: " + (System.currentTimeMillis()- timeStart) + " ms";
		MsgBox.info(msg);
	}

	private class User extends Thread{
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

		public User (Utilizador u, int i) {
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

		private FileBlockRequestMessage tipoDeBloco(int nrBloco) {
			//se não é o último bloco
			if (nrBloco < numeroBlocosCompletos) {
				return new FileBlockRequestMessage(ficheiro, nrBloco*tamanhoDosBlocos, tamanhoDosBlocos, nrBloco);

				//se é o último bloco
			} else {
				return new FileBlockRequestMessage(ficheiro, nrBloco*tamanhoDosBlocos, tamanhoDoUltimoBloco, nrBloco);
			}
		}

		private void incrementaPesquisa() {
			pesquisa = pesquisa + sentidoPesquisa;
			if (pesquisa >= numeroTotalBlocos) pesquisa = 0;
			if (pesquisa < 0) pesquisa = numeroTotalBlocos-1;
		}

		private synchronized FileBlockRequestMessage proximoBloco() {
			if (estadoBlocos[pesquisa] == '0') {
				FileBlockRequestMessage bloco= tipoDeBloco(pesquisa);
				incrementaPesquisa();
				return bloco;
			}
			if (primeiraInversao) {
				pesquisa = inicioDownload;
				sentidoPesquisa = -1;
				primeiraInversao = false;
			}
			incrementaPesquisa();
			int pd = indexOf(estadoBlocos, '0', pesquisa);
			if (pd != -1) {
				estadoBlocos[pd]='P';
				pesquisa = pd;
				return tipoDeBloco(pd);
			}

			int des = indexOf(estadoBlocos, 'P', pesquisa);
			if (des != -1) {
				pesquisa = des;
				return tipoDeBloco(des);
			}
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
					if (pedidoBloco.getNumeroDoBloco() == -1) break;
					oos.flush();
					oos.writeObject(pedidoBloco);
					parteCarregada = (byte[])ois.readObject();
					if (estadoBlocos[pedidoBloco.getNumeroDoBloco()] == 'D') continue;
					synchronized (ficheiroDescarregado) { 
						System.arraycopy(parteCarregada, 0, ficheiroDescarregado, pedidoBloco.getOffset(), pedidoBloco.getLength());
					}
					estadoBlocos[pedidoBloco.getNumeroDoBloco()] = 'D';
					System.out.println(this.getName() + " baixou o bloco " + pedidoBloco.getNumeroDoBloco());
					blocosDescarregadosThread++;
					blocosDescarregados++;
					w.atualizabarraDeProgresso((blocosDescarregados*100)/numeroTotalBlocos);
					sleep(100);
				}
			} catch (NumberFormatException | IOException | ClassNotFoundException | InterruptedException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}finally {
				try {
					oos.close();
					ois.close();
					s.close();	
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}
}