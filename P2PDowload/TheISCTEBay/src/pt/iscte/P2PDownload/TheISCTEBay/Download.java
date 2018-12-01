package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;


public class Download extends Thread{
	private FileDetails ficheiro;
	private byte[] ficheiroDescarregado;
	int tamanhoDasPartes = TheISCTEBay.tamanhoDasPartes;

	public Download(FileDetails f) {
		ficheiro = f;
		ficheiroDescarregado = new byte[(int) f.bytesFicheiro()];
	}
	
	@Override
	public void run() {
		int contadorPartes=0;
		int tamanhoDoFicheiro = (int)ficheiro.bytesFicheiro();
		int numeroDePartesCompletas = tamanhoDoFicheiro/tamanhoDasPartes;
		int tamanhoDoUltimoFicheiro = (tamanhoDoFicheiro % tamanhoDasPartes);
		ArrayList<Utilizador> users = ficheiro.getUtilizadors();
		int numeroDeUsers = users.size();
		ArrayList<Parte> partes = new ArrayList<Parte>();

		while (contadorPartes < numeroDePartesCompletas) {
			int u=0;
			
			//pedir ficheiro aos utilizadores que o têm disponível
			while(u < numeroDeUsers) {
				if(numeroDePartesCompletas > contadorPartes){
					FileBlockRequestMessage pedidoDeParte = new FileBlockRequestMessage(ficheiro, contadorPartes*tamanhoDasPartes,
							tamanhoDasPartes, contadorPartes);
					Parte t = new Parte(users.get(u),pedidoDeParte);
					partes.add(pedidoDeParte.getNumeroDoBloco(), t);
					t.start();
				}
				contadorPartes ++;
				u++;
			}
		}
		
		//download da última parte
		if (tamanhoDoUltimoFicheiro > 0) {
			FileBlockRequestMessage pedidoUltimaParte = new FileBlockRequestMessage(ficheiro, contadorPartes*tamanhoDasPartes,
					tamanhoDoUltimoFicheiro, contadorPartes);
			Parte t = new Parte(users.get(0),pedidoUltimaParte);
			partes.add(t);
			t.start();
		}
	
		Iterator<Parte> iPartes = partes.iterator();
		while(iPartes.hasNext()) {
			try {
				Parte p = iPartes.next();
				p.join();
				byte[] parteEmBytes = p.getParteCarregada();
				System.arraycopy(parteEmBytes, 0, ficheiroDescarregado, p.getNumero()*tamanhoDasPartes, parteEmBytes.length);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		MsgBox.info(ficheiro.toString() + " descarregado com sucesso!");
		try {
			Files.write(Paths.get(TheISCTEBay.devolvePastaTransferencias()+"/"+ficheiro.nomeFicheiro()), ficheiroDescarregado);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	private class User extends Thread{
//		private Socket s;
//		private FileBlockRequestMessage pedidoBloco;
//		private Utilizador user;
//		private byte[] parteCarregada;
//		
//		public User (Utilizador u) {
//			user = u;
//		}
//		
//		public byte[] downloadPart(FileBlockRequestMessage p) {
//			try {
//				s = new Socket(user.ipUtilizador(), Integer.parseInt(user.portoUtilizador()));
//				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
//				oos.flush();
//				oos.writeObject(pedidoBloco);
//				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
//				parteCarregada = (byte[])ois.readObject();
//				
//			} catch (NumberFormatException | IOException | ClassNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return parteCarregada;
//		}
//	}
	
	private class Parte extends Thread{
		private byte[] parteCarregada;
		private Socket s;
		private Utilizador user;
		private FileBlockRequestMessage pedidoBloco;
		private int numeroBlocoPedido;
		
		public Parte (Utilizador u, FileBlockRequestMessage p) {
			user = u;
			pedidoBloco = p;
			numeroBlocoPedido = p.getNumeroDoBloco();
		}
		
		public byte[] getParteCarregada() {
			return parteCarregada;
		}
		
		public int getNumero() {
			return numeroBlocoPedido;
		}
		
		@Override
		public void run() {
			try {
				
				s = new Socket(user.ipUtilizador(), Integer.parseInt(user.portoUtilizador()));
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				oos.flush();
				oos.writeObject(pedidoBloco);
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				parteCarregada = (byte[])ois.readObject();
				
			} catch (NumberFormatException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
