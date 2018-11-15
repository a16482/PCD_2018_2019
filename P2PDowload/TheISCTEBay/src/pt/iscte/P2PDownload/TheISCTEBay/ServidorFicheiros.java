package pt.iscte.P2PDownload.TheISCTEBay;

<<<<<<< HEAD
=======
import java.io.File;
>>>>>>> 5331b2e08d1f36528661985e798523c7037ca2bb
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
<<<<<<< HEAD
=======
import java.util.Iterator;
>>>>>>> 5331b2e08d1f36528661985e798523c7037ca2bb


public class ServidorFicheiros extends Thread implements Runnable {

	private ServerSocket fileServer;
<<<<<<< HEAD
	private int portoProprio = TheISCTEBay.devolvePortoUtilizador();
	private ArrayList<Thread> listaDeThreads = new ArrayList<Thread>();
=======
	int portoProprio = TheISCTEBay.devolvePortoUtilizador();
>>>>>>> 5331b2e08d1f36528661985e798523c7037ca2bb

	@Override
	public void run() {
		try {
			fileServer= new ServerSocket(portoProprio);
			serve();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

<<<<<<< HEAD

	}

	private void serve() throws IOException{
		//...
		Thread t;
		listaDeThreads.clear();
		//...
=======
		
	}

	private void serve() throws IOException{
>>>>>>> 5331b2e08d1f36528661985e798523c7037ca2bb
		while(true){
			System.out.println("Servidor iniciado:" + portoProprio);
			Socket s=fileServer.accept();
			System.out.println("Ligação efetuada");
<<<<<<< HEAD
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


=======
			new TrataPedidos(s.getInputStream(), s.getOutputStream()).start();
		}
		
//		public void init() {
//			//new Thread(this, "ServidorFicheiros").start();
//		}

	}
		
	
>>>>>>> 5331b2e08d1f36528661985e798523c7037ca2bb
	private class TrataPedidos extends Thread {

		private ObjectInputStream inStream;
		private ObjectOutputStream outStream;
		private WordSearchMessage palavraChave;

		public TrataPedidos(InputStream in, OutputStream out) throws IOException {
			super();
			this.inStream = new ObjectInputStream(in);
			this.outStream = new ObjectOutputStream(out);
		}
<<<<<<< HEAD


		@Override
		public void run() {
=======
		
		private ArrayList<FileDetails> procuraFicheirosPorPalavraChave (WordSearchMessage pChave) {
			FileDetails ficheiroEncontrado;
			ArrayList<FileDetails> listaFicheirosEncontrados = new ArrayList<FileDetails>();
			String procurarPalavra = pChave.getPalavraChave().toLowerCase();
			File[] files = new File("./" + TheISCTEBay.devolvePastaTransferencias()).listFiles();
			for (File file: files) {
				String nomeFicheiro = file.getName().toLowerCase();
				Boolean encontrei = nomeFicheiro.contains(procurarPalavra);
				if (encontrei) {
					ficheiroEncontrado = new FileDetails(file.getName(),file.length());
					listaFicheirosEncontrados.add(ficheiroEncontrado);
				}
			}
			return listaFicheirosEncontrados;
		}
		

		@Override
		public void run() {
			ArrayList<FileDetails> listaFicheirosEncontrados;
>>>>>>> 5331b2e08d1f36528661985e798523c7037ca2bb
			try {
				while(true){
					System.out.println("à espera...");

					// RECEÇÃO da MSG:
					palavraChave=(WordSearchMessage)inStream.readObject();
					System.out.println("Recebido: " + palavraChave.getPalavraChave());
<<<<<<< HEAD
					outStream.flush();  //limpeza

					// Falta chamar método para pesquisar ficheiros
					outStream.writeObject(new FileDetails("img",80));
=======
					outStream.flush();
					listaFicheirosEncontrados = procuraFicheirosPorPalavraChave (palavraChave);
					Iterator<FileDetails> iListaFicheiros = listaFicheirosEncontrados.iterator();
					while(iListaFicheiros.hasNext()) {
						outStream.writeObject(iListaFicheiros.next());
					}
>>>>>>> 5331b2e08d1f36528661985e798523c7037ca2bb
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
<<<<<<< HEAD

=======
	
>>>>>>> 5331b2e08d1f36528661985e798523c7037ca2bb
}
