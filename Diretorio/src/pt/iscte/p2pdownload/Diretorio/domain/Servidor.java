package pt.iscte.p2pdownload.Diretorio.domain;

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {

	private static int port;
	private ServerSocket server;

	public void init() throws IOException {
		server = new ServerSocket(port);
	}

	private List<Cliente> diretorio = new ArrayList<>();


	public void serve() throws IOException{
		while(true){
			System.out.println("Servidor iniciado:" + port);
			
			Socket s=server.accept();
			System.out.println("Ligação efetuada");
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
	
	public synchronized void informaDiretorio(Cliente c) { 
		for (int i = 0; i < diretorio.size(); i++) {
			System.out.println("CLI " + diretorio.get(i).devolveIPcliente(c) + " " + diretorio.get(i).devolvePortocliente(c));
		}
	}

	public static void main(String[] args) {
		port = Integer.parseInt(args[0]);
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
		private String tipoMsg;
		private Cliente cliente;
		
		public TrataMsg(InputStream in) throws IOException {
			super();
			this.in = new ObjectInputStream(in);
		}
		
		@Override
		public void run() {
			try {
				while(true){
					System.out.println("à espera...");
					
					// RECEÇÃO da MSG:
					String msg=(String)in.readObject();
					System.out.println("Recebido: " + msg);
					
					//--> cria uma instância de Msg e VERIFICA o tipo de MSG:
					Msg m = new Msg(msg);
					tipoMsg = m.getTipoMsg();
					cliente = m.getClienteMsg();
					
					switch (tipoMsg) {
						case ("INSC"): 
							adicionaCliente(cliente);
							break;
						case ("CLT"):
							informaDiretorio(cliente);
							break;
						default:
							break;
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// Não fazer nada... Leitura acabou
				System.out.println("Cliente desligou-se.");
				//removeCliente(c);
			} finally{
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
