package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class ServidorFicheiros extends Thread implements Runnable {

	private ServerSocket fileServer;
	private int portoProprio = TheISCTEBay.devolvePortoUtilizador();
	private ArrayList<Thread> listaDeThreads = new ArrayList<Thread>();

	@Override
	public void run() {
		try {
			fileServer= new ServerSocket(portoProprio);
			serve();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	private void serve() throws IOException{
		//...
		Thread t;
		listaDeThreads.clear();
		//...
		while(true){
			System.out.println("Servidor iniciado:" + portoProprio);
			Socket s=fileServer.accept();
			System.out.println("Ligação efetuada");
			t = new TrataPedidos(s.getInputStream(), s.getOutputStream());
			t.start();
			//...
			listaDeThreads.add(t);
			//...
		}

//		Iterator<Thread> iListaDeThreads =  listaDeThreads.iterator();
//
//		while (iListaDeThreads.hasNext()) {
//			iListaDeThreads.next().join();
//			iListaDeThreads.remove();
//		}	
		
		

	}


	private class TrataPedidos extends Thread {

		private ObjectInputStream inStream;
		private ObjectOutputStream outStream;
		private WordSearchMessage palavraChave;

		public TrataPedidos(InputStream in, OutputStream out) throws IOException {
			super();
			this.inStream = new ObjectInputStream(in);
			this.outStream = new ObjectOutputStream(out);
		}


		@Override
		public void run() {
			try {
				while(true){
					System.out.println("à espera...");

					// RECEÇÃO da MSG:
					palavraChave=(WordSearchMessage)inStream.readObject();
					System.out.println("Recebido: " + palavraChave.getPalavraChave());
					outStream.flush();  //limpeza

					// Falta chamar método para pesquisar ficheiros
					outStream.writeObject(new FileDetails("img",80));
					outStream.close();
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

}
