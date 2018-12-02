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
	private int tamanhoDasPartes = TheISCTEBay.tamanhoDasPartes;
	private int tamanhoDoFicheiro;
	private int numeroDePartesCompletas;
	private int tamanhoDoUltimoFicheiro;
	private ArrayList<Utilizador> users;
	private int numeroDeUsers;
	private ArrayList<User> threadUsers = new ArrayList<User>();
	private char[] estadoPartes;
	private int nrBlocosPorUser;
	private long timeStart;

	public Download(FileDetails f) {
		ficheiro = f;
		ficheiroDescarregado = new byte[(int) f.bytesFicheiro()];
		tamanhoDoFicheiro = (int)f.bytesFicheiro();
		numeroDePartesCompletas = tamanhoDoFicheiro/tamanhoDasPartes;
		tamanhoDoUltimoFicheiro = (tamanhoDoFicheiro % tamanhoDasPartes);
		users = f.getUtilizadors();
		numeroDeUsers = users.size();
		estadoPartes = new char [numeroDePartesCompletas+1];
		Arrays.fill(estadoPartes, '0');
		nrBlocosPorUser = (numeroDePartesCompletas+1)/numeroDeUsers;
		timeStart = System.currentTimeMillis();

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
		msg += "Tempo decorrido: " + (System.currentTimeMillis()- timeStart) + " ms";
		MsgBox.info(msg);
	}

	private class User extends Thread{
		private int contaBlocosDescarregados;
		private Socket s;
		private FileBlockRequestMessage pedidoBloco;
		private Utilizador user;
		private byte[] parteCarregada;
		private int inicioDownload;

		public User (Utilizador u, int i) {
			user = u;
			contaBlocosDescarregados=0;
			inicioDownload = i;
		}

		public String getUserIp() {
			return user.ipUtilizador();
		}

		public String getUserPorto() {
			return user.portoUtilizador();
		}

		public int getNumeroBlocos() {
			return contaBlocosDescarregados;
		}

		public int indexOf(char[] array, char c, int inicio) {
			int i= inicio;
			do {
				if (array[i] == c) return i;
				i++;
				if (i == array.length) i = 0;
			} while (i != inicio);

			return -1;
		}

		private synchronized FileBlockRequestMessage proximoBloco() {
			int pd = indexOf(estadoPartes, '0', inicioDownload);

			if (pd != -1) {
				if (pd < numeroDePartesCompletas) {
					return new FileBlockRequestMessage(ficheiro, pd*tamanhoDasPartes, tamanhoDasPartes, pd);
				} else {
					return new FileBlockRequestMessage(ficheiro, pd*tamanhoDasPartes, tamanhoDoUltimoFicheiro, pd);
				}
			}

			int des = indexOf(estadoPartes, 'P', inicioDownload);	
			if (des != -1) {
				if (des < numeroDePartesCompletas) {
					return new FileBlockRequestMessage(ficheiro, des*tamanhoDasPartes, tamanhoDasPartes, des);
				}else {
					return new FileBlockRequestMessage(ficheiro, des*tamanhoDasPartes, tamanhoDoUltimoFicheiro, des);
				}
			}
			return new FileBlockRequestMessage(ficheiro, -1, -1, -1);
		}

		@Override
		public void run() {
			try {
				s = new Socket(user.ipUtilizador(), Integer.parseInt(user.portoUtilizador()));
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				while(true) {
					pedidoBloco = proximoBloco();
					if (pedidoBloco.getNumeroDoBloco() == -1) break;
					oos.flush();
					oos.writeObject(pedidoBloco);
					estadoPartes[pedidoBloco.getNumeroDoBloco()] = 'P';
					parteCarregada = (byte[])ois.readObject();
					synchronized (ficheiroDescarregado) { 
						System.arraycopy(parteCarregada, 0, ficheiroDescarregado, pedidoBloco.getOffset(), pedidoBloco.getLength());
					}
					estadoPartes[pedidoBloco.getNumeroDoBloco()] = 'D';
					contaBlocosDescarregados++;
					if (inicioDownload<(numeroDePartesCompletas-1)) {
						inicioDownload++;
					} else inicioDownload =0;
				}
				oos.close();
				ois.close();
				s.close();
			} catch (NumberFormatException | IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}