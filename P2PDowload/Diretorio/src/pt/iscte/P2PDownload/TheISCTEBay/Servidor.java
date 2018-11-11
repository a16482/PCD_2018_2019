package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {

	private static int portoDiretorio;
	private ServerSocket server;
	private List<Cliente> diretorio = new ArrayList<>();

	public void init() throws IOException {
		server = new ServerSocket(portoDiretorio);
	}

	public void serve() throws IOException{
		while(true){
			System.out.println("Servidor iniciado:" + portoDiretorio);

			Socket s=server.accept();
			System.out.println("Ligação efetuada");
			new TrataMsg(s.getInputStream(), s.getOutputStream()).start();
		}
	}

	public class TrataMsg extends Thread {

		private ObjectInputStream inStream;
		private ObjectOutputStream outStream;
		private String tipoMsg;
		private Cliente cliente;

		public TrataMsg(InputStream in, OutputStream out) throws IOException {
			super();
			this.inStream = new ObjectInputStream(in);
			this.outStream = new ObjectOutputStream(out);
		}
		
		public synchronized void adicionaCliente(Cliente c) {
			if (!existeCliente(c)) { 
				diretorio.add(c);
			}
		}

		public synchronized void removeCliente(Cliente c) {
			Cliente clienteDiretorio;
			boolean encontrado = false;
			Iterator<Cliente> iDiretorio = diretorio.iterator();
			while ((iDiretorio.hasNext()) && (!encontrado)) {
				clienteDiretorio = iDiretorio.next();
				if(clienteDiretorio.ipCliente().equals(c.ipCliente()) && clienteDiretorio.portoCliente().equals(c.portoCliente())) {
					diretorio.remove(c);
					encontrado= true;
				}
				
			}
		}
		
		public synchronized boolean existeCliente (Cliente c) {
			boolean existeCliente= false;
			Cliente clienteDiretorio;
			Iterator<Cliente> iDiretorio = diretorio.iterator();
			while (iDiretorio.hasNext()) {
				clienteDiretorio = iDiretorio.next();
				if(clienteDiretorio.ipCliente().equals(c.ipCliente()) && clienteDiretorio.portoCliente().equals(c.portoCliente())) {
					existeCliente = true;
				}
			}
			return existeCliente;
		}

		public synchronized void informaDiretorio() { 
			try {	
				for (int i = 0; i < diretorio.size(); i++) {
					System.out.println("CLI " + diretorio.get(i).ipCliente() + " " + diretorio.get(i).portoCliente());
				}
				outStream.flush();
				for (Cliente d : diretorio) {
					outStream.writeObject("CLT "+ d.ipCliente() + " " + d.portoCliente());
				}
				outStream.writeObject("END");
				outStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void run() {
			try {
				while(true){
					System.out.println("à espera...");

					// RECEÇÃO da MSG:
					String msg=(String)inStream.readObject();
					System.out.println("Recebido: " + msg);

					//--> cria uma instância de Msg do Cliente e VERIFICA o tipo de MSG:
					Msg m = new Msg(msg);
					tipoMsg = m.getTipoMsg();
					cliente = m.getClienteMsg();
					synchronized (diretorio) {
						switch (tipoMsg) {
							case ("INSC"): 
								adicionaCliente(cliente);
								outStream.flush();  //limpeza
								outStream.writeObject(new String("ok"));
								outStream.close();
								break;
							case ("CLT"):
								informaDiretorio();
								break;
							case ("RMV"):
								removeCliente(cliente);
								outStream.flush();  //limpeza
								outStream.writeObject(new String("ok"));
								outStream.close();
								break;
							default:
								outStream.flush();
								outStream.writeObject(new String("Mensagem com formato desconhecido"));
								outStream.close();
								System.out.println("Recebida a seguinte mensagem com formato desconhecido:\n" + msg);
								break;
						}
					}
					System.out.println("....................");
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// Não fazer nada... Leitura acabou
				System.out.println("Cliente desligou-se.");
			} finally{
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
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
			System.out.println("Erro no lançamento do Servidor: " + e.getMessage());
			System.out.println("Lançamento do Servidor cancelado.");
			System.exit(1);
		}
	}
}
