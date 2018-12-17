package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.*;
import java.net.*;
import java.util.*;

public class Servidor {

	private static int portoDiretorio;
	private ServerSocket server;
	private List<Cliente> diretorio = new ArrayList<>();

	private void init() throws IOException {
		server = new ServerSocket(portoDiretorio);
	}

	private void serve() throws IOException{
		while(true){
			Socket s=server.accept();
			new TrataMsg(s.getInputStream(), s.getOutputStream()).start();
		}
	}

	private synchronized boolean adicionaCliente(Cliente c) {
		if (!existeCliente(c)) { 
			diretorio.add(c);
			return true;
		}else {
			return false;
		}
	}

	private synchronized void removeCliente(Cliente c) {
		Cliente clienteDiretorio;
		boolean encontrado = false;
		Iterator<Cliente> iDiretorio = diretorio.iterator();
		while ((iDiretorio.hasNext()) && (!encontrado)) {
			clienteDiretorio = iDiretorio.next();
			if(clienteDiretorio.ipCliente().equals(c.ipCliente()) && clienteDiretorio.portoCliente().equals(c.portoCliente())) {
				diretorio.remove(clienteDiretorio);
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

	private synchronized boolean confirmaLigacao(Cliente cliente) {
		boolean isAlive = false;
		Socket s=null;
		ObjectOutputStream oos=null;
		ObjectInputStream ois=null;
		@SuppressWarnings("unused")
		String msg;
		try {
			InetAddress addressCiente = InetAddress.getByName(cliente.ipCliente());
			int portCliente = Integer.parseInt(cliente.portoCliente());
			s = new Socket(addressCiente, portCliente);
			//System.out.println("Socket ligado!");
			s.setSoTimeout(2000);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			oos.flush();
			String pergunta = cliente.portoCliente() + " estás Vivo?";
			oos.writeObject(pergunta);
			msg = (String)ois.readObject();
			isAlive = true;
		} catch (IOException | ClassNotFoundException e){
			isAlive = false;
		}
		finally {
			try {
				oos.close();
				ois.close();
				s.close();
			} catch (Exception e) {
				isAlive = false;
			}
		}
		return isAlive;
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

		private synchronized void informaDiretorio() {
			try {	
				outStream.flush();
				Cliente cli;
				ListIterator<Cliente> iDiretorio = diretorio.listIterator();
				while (iDiretorio.hasNext()) {
					cli = iDiretorio.next();
					if (!confirmaLigacao(cli)) {
						iDiretorio.remove();
					}else {
						outStream.writeObject("CLT "+ cli.ipCliente() + " " + cli.portoCliente());
					}
				}
				outStream.writeObject("END");
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				try {
					outStream.close();
					inStream.close();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public void run() {
			String resposta;
			try {
				while(true){
					// RECEÇÃO da MSG:
					String msg=(String)inStream.readObject();

					//--> cria uma instância de Msg do Cliente e VERIFICA o tipo de MSG:
					Msg m = new Msg(msg);
					tipoMsg = m.getTipoMsg();
					cliente = m.getClienteMsg();
					synchronized (diretorio) {
						switch (tipoMsg) {
							case ("INSC"): 
								if(adicionaCliente(cliente)) {
									resposta = "ok";
								}else {
									resposta = "existe";
								}
								outStream.flush();  //limpeza
								outStream.writeObject(resposta);
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
								break;
						}
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// Não fazer nada... Leitura acabou
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
			System.exit(1);
		}
	}
}
