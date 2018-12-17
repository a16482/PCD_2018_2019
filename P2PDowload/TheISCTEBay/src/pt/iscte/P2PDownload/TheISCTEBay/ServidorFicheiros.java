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

// Classe que recebe os pedidos dos tipos "bloco", "PalavraChave" ou msg do diretório a confirmar se este cliente está vivo
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
			e.printStackTrace();
		}
	}

	private void serve() {
		Socket s = null;
		ThreadPool pool = new ThreadPool(limitePedidos);
		while (true) {
			try {
				s = fileServer.accept();
				ObjectInputStream inStream = new ObjectInputStream(s.getInputStream());
				ObjectOutputStream outStream = new ObjectOutputStream(s.getOutputStream());

				Object msg=inStream.readObject();

				if (msg instanceof String) {
					outStream.flush();  //limpeza
					String resposta = TheISCTEBay.devolvePortoUtilizador() + " Estou vivo!";
					outStream.writeObject(resposta);
					outStream.close();
				} else if (msg instanceof FileBlockRequestMessage) {
					FileBlockRequestMessage bloco = (FileBlockRequestMessage)msg;
					t = new TrataPedidos(bloco, inStream, outStream);
					pool.execute(t);
				} else if (msg instanceof WordSearchMessage) {
					WordSearchMessage palavraChave=(WordSearchMessage)msg;
					t = new TrataPedidos(palavraChave, inStream, outStream);
					pool.execute(t);
				}

			}catch (ClassNotFoundException | IOException e) {
				try {
					s.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private class TrataPedidos implements Runnable {
		private WordSearchMessage palavraChave=null;
		private FileBlockRequestMessage bloco=null;
		private ObjectInputStream ois=null;
		private ObjectOutputStream oos=null;
		private String tipoPedido=null;

		//Pedido de um bloco de um determinado ficheiro da pasta local
		public TrataPedidos(FileBlockRequestMessage b, ObjectInputStream inSream, ObjectOutputStream outStream){
			super();
			bloco = b;
			ois = inSream;
			oos = outStream;
			tipoPedido = "Bloco";
		}
		
		//pedido de Detalhes dos Ficheiros que estão na pasta local
		public TrataPedidos(WordSearchMessage pChave, ObjectInputStream inSream, ObjectOutputStream outStream){
			super();
			palavraChave = pChave;
			ois = inSream;
			oos = outStream;
			tipoPedido = "PalavraChave";
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

			try {
				if (tipoPedido.equals("Bloco")) {
					while(true) {
						byte[] parteFicheiro = parteDoFicheiroPedido(bloco);
						oos.flush();  //limpeza
						oos.writeObject(parteFicheiro);
						// Próximo bloco
						bloco = (FileBlockRequestMessage)ois.readObject();
					}
				}else if (tipoPedido.equals("PalavraChave")) {
					oos.flush();  //limpeza
					listaFicheirosEncontrados = procuraFicheirosLocais(palavraChave);
					Iterator<FileDetails> iListaFicheiros = listaFicheirosEncontrados.iterator();
					while(iListaFicheiros.hasNext()) {
						FileDetails fd = iListaFicheiros.next();
						oos.writeObject(fd);
						// "Ficheiro encontrado: " + fd.nomeFicheiro();
					}
				}
			} catch (IOException | ClassNotFoundException e){
				//NOTHING TO DO
			} finally {
				try {
					ois.close();
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}