package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;


public class ServidorFicheiros extends Thread implements Runnable {

	private ServerSocket fileServer;
	int portoProprio = TheISCTEBay.devolvePortoUtilizador();

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
		while(true){
			System.out.println("Servidor iniciado:" + portoProprio);
			Socket s=fileServer.accept();
			System.out.println("Liga��o efetuada");
			new TrataPedidos(s.getInputStream(), s.getOutputStream()).start();
		}
		
//		public void init() {
//			//new Thread(this, "ServidorFicheiros").start();
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
			try {
				while(true){
					System.out.println("� espera...");

					// RECE��O da MSG:
					palavraChave=(WordSearchMessage)inStream.readObject();
					System.out.println("Recebido: " + palavraChave.getPalavraChave());
					outStream.flush();
					listaFicheirosEncontrados = procuraFicheirosPorPalavraChave (palavraChave);
					Iterator<FileDetails> iListaFicheiros = listaFicheirosEncontrados.iterator();
					while(iListaFicheiros.hasNext()) {
						outStream.writeObject(iListaFicheiros.next());
					}
					outStream.close();
					System.out.println("....................");
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// N�o fazer nada... Leitura acabou
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
