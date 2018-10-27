package pt.iscte.p2pdownload.theDirectory.domain;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

public class Servidor {

	public final static int port = 8090;
	private ServerSocket server;

	public void init() throws IOException {
		server = new ServerSocket(port);
	}

	private List<Diretorio> diretorios = new ArrayList<>();

	public void serve() throws IOException{
		while(true){
			Socket s=server.accept();
			new TrataCliente(s.getInputStream()).start();
		}
	}

	public synchronized void adicionaDiretorio(Diretorio d) {
		diretorios.add(d);
	}

	public static void main(String[] args) {
		final Servidor s = new Servidor();
		try {
			s.init();
			s.serve();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class TrataCliente extends Thread {
		
		private ObjectInputStream in;
		
		public TrataCliente(InputStream in) throws IOException {
			super();
			this.in = new ObjectInputStream(in);
		}
		
		@Override
		public void run() {
			try {
				while(true){
					Diretorio d=(Diretorio)in.readObject();
					adicionaDiretorio(d);
					System.out.println("Recebido:"+d);
				}
			} catch (ClassNotFoundException e) {
			} catch (IOException e) {
				// Não fazer nada... Leitura acabou
				System.out.println("Cliente desligou-se.");
			} finally{
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}

	}
}
