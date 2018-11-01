package pt.iscte.p2pdownload.Diretorio.domain;

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {

	private static int portoDiretorio;
	private ServerSocket server;

	public void init() throws IOException {
		server = new ServerSocket(portoDiretorio);
	}

	private List<Cliente> diretorio = new ArrayList<>();


	public void serve() throws IOException{
		while(true){
			System.out.println("Servidor iniciado:" + portoDiretorio);

			Socket s=server.accept();
			System.out.println("Ligação efetuada");
			new TrataMsg(s.getInputStream(), s.getOutputStream()).start();

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
			System.out.println("CLI " + diretorio.get(i).ipCliente() + " " + diretorio.get(i).portoCliente());
		}
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.err.println("É necessário indicar o porto de escuta do Diretório");
			System.exit(1);
		}
		portoDiretorio = Integer.parseInt(args[0]);
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
		private ObjectOutputStream out;
		private String tipoMsg;
		private Cliente cliente;

		public TrataMsg(InputStream in, OutputStream out) throws IOException {
			super();
			this.in = new ObjectInputStream(in);
			this.out = new ObjectOutputStream(out);
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
							out.flush();
							out.writeObject(new String("ok"));
							out.close();
							break;
						case ("CLT"):
							out.flush();
							for (Cliente d : diretorio) {
								out.writeObject("CLT "+ d.ipCliente() + " " + d.portoCliente());
							}
							out.writeObject("END");
							out.close();
							informaDiretorio(cliente);
							break;
						default:
							out.flush();
							out.writeObject(new String("Mensagem com formato desconhecido"));
							out.close();
							System.out.println("Recebida a seguinte mensagem com formato desconhecido:\n" + msg);
							break;
					}
					System.out.println("....................");
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
