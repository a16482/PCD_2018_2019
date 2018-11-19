package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;


public class Download {
	private FileDetails ficheiro;

	public Download(FileDetails f) {
		ficheiro = f;
		pedirFicheiro();
	}
	
	//pedir ficheiro aos utilizadores que o têm disponível
	public void pedirFicheiro() {
		int contadorPartes=0;
		int tamanhoDasPartes = 5000;
		int tamanhoDoFicheiro = (int)ficheiro.bytesFicheiro();
		int numeroDePartesCompletas = tamanhoDoFicheiro/tamanhoDasPartes;
		int tamanhoDoUltimoFicheiro = tamanhoDoFicheiro % tamanhoDasPartes;
		ArrayList<Utilizador> users = ficheiro.getUtilizadors();
		int numeroDeUsers = users.size();

		while (contadorPartes < numeroDePartesCompletas) {
			int u=0;
			while(u < numeroDeUsers) {
				if (contadorPartes == numeroDePartesCompletas) {
					if (tamanhoDoUltimoFicheiro > 0) {
						FileBlockRequestMessage pedidoUltimaParte = new FileBlockRequestMessage(ficheiro, contadorPartes*tamanhoDasPartes, tamanhoDasPartes);
						Thread t = new PedeParte(users.get(u),pedidoUltimaParte);
						t.start();
					}
					break;
				} else {
					FileBlockRequestMessage pedidoDeParte = new FileBlockRequestMessage(ficheiro, contadorPartes*tamanhoDasPartes, tamanhoDasPartes);
					Thread t = new PedeParte(users.get(u),pedidoDeParte);
					t.start();
					contadorPartes++;
					u++;
				}
			}
		}		
	}
	
	private class PedeParte extends Thread implements Runnable{
		private Socket s;
		private Utilizador user;
		private FileBlockRequestMessage pedidoBloco; 
		
		public PedeParte (Utilizador u, FileBlockRequestMessage p) {
			super();
			user = u;
			pedidoBloco = p;
		}
		
		@Override
		public void run() {
			try {
				s = new Socket(user.ipUtilizador(), Integer.parseInt(user.portoUtilizador()));
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.flush();
				oos.writeObject(pedidoBloco);
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				byte[] parte = (byte[])ois.readObject();
				
				System.out.println("Nr de bytes: " + parte.length);
				wait();
			} catch (NumberFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
