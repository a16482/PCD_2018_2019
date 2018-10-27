package pt.iscte.p2pdownload.theDirectory.domain;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.ArrayList;

public class Servidor {

	public final static int port = 8091;
	private ServerSocket server;

	public void init() throws IOException {
		server = new ServerSocket(port);
	}

	private List<Cliente> diretorio = new ArrayList<>();

	public void serve() throws IOException{
		while(true){
			System.out.println("Servidor iniciado:" + port);
			
			Socket s=server.accept();
			new TrataMsg(s.getInputStream()).start();
		}
	}

	public synchronized void adicionaCliente(Cliente c) {
		diretorio.add(c);
	}
	
	// este método não está ainda a ser usado
	public synchronized void removeCliente(Cliente c) {
		diretorio.remove(c);
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

	public class TrataMsg extends Thread {
		
		private ObjectInputStream in;
		
		public TrataMsg(InputStream in) throws IOException {
			super();
			this.in = new ObjectInputStream(in);
		}
		
		@Override
		public void run() {
			try {
				while(true){
					System.out.println("À espera...");
					
					// é preciso testar o conteúdo da msg INSC
					String msg=(String)in.readObject();
					
					//--> é preciso partir a string criada com o cast na classe msg
					Msg m = new Msg(msg);
					
					//Cliente c=(Cliente)in.readObject();
				
					//adicionaCliente(c);
					System.out.println("Recebido: " + msg);
				}
			} catch (ClassNotFoundException e) {
			} catch (IOException e) {
				// Não fazer nada... Leitura acabou
				System.out.println("Cliente desligou-se.");
				//removeCliente(c);
			} finally{
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}

	}
}
