package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

// Classe que recebe os pedidos de Detalhes de todos os Ficheiros a Pasta e pedidos de uma parte de um Ficheiro
public class ServidorFicheiros extends Thread implements Runnable {

	private ServerSocket fileServer;
	private int portoProprio = TheISCTEBay.devolvePortoUtilizador();
	private TrataPedidos t;
	private int limitePedidos = TheISCTEBay.limitePedidos;
		

	//Servidor sempre à escuta
	@Override
	public void run() {
		try {
			fileServer = new ServerSocket(portoProprio);
			serve();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void serve() throws IOException {

		ThreadPool p = new ThreadPool(limitePedidos);

		while (true) {
			while (true) {
				System.out.println("Servidor iniciado:" + portoProprio);
				Socket s = fileServer.accept();
				System.out.println("Ligação efetuada");
				t = new TrataPedidos(s);
				p.submit(t);
			}
		}
	}


	//pedido pode ser Detalhes dos Ficheiros que estão na pasta local ou
	//Parte de um determinado ficheiro da pasta local
	private class TrataPedidos extends Thread implements Runnable {

		private ObjectInputStream inStream;
		private ObjectOutputStream outStream;
		private Socket s;

		public TrataPedidos(Socket soc){
			super();
			try {
				inStream = new ObjectInputStream(soc.getInputStream());
				outStream = new ObjectOutputStream(soc.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s=soc;
		}

		private ArrayList<FileDetails> procuraFicheirosLocais(WordSearchMessage pChave) {
			FileDetails ficheiroEncontrado;
			ArrayList<FileDetails> listaFicheirosEncontrados = new ArrayList<FileDetails>();
			String procurarPalavra = pChave.getPalavraChave().toLowerCase();
			File[] files = new File("./" + TheISCTEBay.devolvePastaTransferencias()).listFiles();
			for (File file : files) {
				String nomeFicheiro = file.getName().toLowerCase();
				Boolean encontrei = nomeFicheiro.contains(procurarPalavra);
				if (encontrei) {
					ficheiroEncontrado = new FileDetails(file.getName(), file.length());
					listaFicheirosEncontrados.add(ficheiroEncontrado);
				}
			}
			return listaFicheirosEncontrados;
		}


		private byte[] parteDoFicheiroPedido(FileBlockRequestMessage p) {
			String caminhoFicheiro = TheISCTEBay.devolvePastaTransferencias() + "/" + p.getFileDetails().nomeFicheiro();
			byte[] parteFicheiroPedido = new byte[p.getLength()];

			File f = new File(caminhoFicheiro);
			try {
				RandomAccessFile rf = new RandomAccessFile(f,"r");
				rf.seek(p.getOffset());
				rf.read(parteFicheiroPedido);
				rf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return parteFicheiroPedido;
		}

		@Override
		public void run() {
			ArrayList<FileDetails> listaFicheirosEncontrados;
			Object msg;
			WordSearchMessage palavraChave;
			FileBlockRequestMessage pedidoParteFicheiro;

			try {
				while(true){

					// RECEÇÃO da MSG:
					msg=inStream.readObject();
					if (msg instanceof WordSearchMessage) {
						palavraChave=(WordSearchMessage)msg;
						outStream.flush();  //limpeza

						// Falta chamar método para pesquisar ficheiros
						listaFicheirosEncontrados = procuraFicheirosLocais(palavraChave);
						Iterator<FileDetails> iListaFicheiros = listaFicheirosEncontrados.iterator();
						while(iListaFicheiros.hasNext()) {
							outStream.writeObject(iListaFicheiros.next());
						}
						outStream.close();
					} else if (msg instanceof FileBlockRequestMessage){
						pedidoParteFicheiro = (FileBlockRequestMessage)msg;
						byte[] parteFicheiro = parteDoFicheiroPedido(pedidoParteFicheiro);
						outStream.writeObject(parteFicheiro);
						System.out.println(this.getName() + " - Bloco: " + pedidoParteFicheiro.getNumeroDoBloco());
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// Não fazer nada... Leitura acabou
				System.out.println("Cliente desligou-se.");
			} finally{
				try {
					outStream.close();
					inStream.close();
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}