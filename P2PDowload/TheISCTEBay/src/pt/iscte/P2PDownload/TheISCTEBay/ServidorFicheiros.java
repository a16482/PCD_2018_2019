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

// Recebe os pedidos dos tipos "bloco", "PalavraChave" ou uma String do Diret�rio para saber se este Cliente est� vivo
public class ServidorFicheiros extends Thread implements Runnable {

	private ServerSocket fileServer;
	private int portoProprio = TheISCTEBay.devolvePortoUtilizador();
	private TrataPedidos t;
	private int limitePedidos = TheISCTEBay.limitePedidos;


	//Servidor sempre � escuta
	@Override
	public void run() {
		try {
			fileServer = new ServerSocket(portoProprio);
			serve();
			System.out.println("Servidor de Ficheiros iniciado no porto: " + portoProprio);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void serve() {
		Socket s = null;
		ThreadPool pool = new ThreadPool(limitePedidos);
		while (true) {
			System.out.println("Servidor de Ficheiros � escuta no porto: " + portoProprio);

			try {
				s = fileServer.accept();
				int port = s.getPort();
				System.out.println("Novo Pedido do porto: "+port);
				ObjectInputStream inStream = new ObjectInputStream(s.getInputStream());
				ObjectOutputStream outStream = new ObjectOutputStream(s.getOutputStream());

				Object msg=inStream.readObject();
				
				//Msg do Diret�rio para saber de este cliente est� vivo e responde a pedidos
				if (msg instanceof String) {
//					String mensagem = (String)msg;
//					System.out.println("Mensagem recebida: " + mensagem);
					outStream.flush();  //limpeza
					String resposta = TheISCTEBay.devolvePortoUtilizador() + " Estou vivo!";
					outStream.writeObject(resposta);
//					System.out.println("A minha resposta: " + resposta);
					outStream.close();
					
				//Pedido do tipo "bloco" - pede o envio de um bloco de um determinado ficheiro
				} else if (msg instanceof FileBlockRequestMessage) {
					FileBlockRequestMessage bloco = (FileBlockRequestMessage)msg;
//					System.out.println("Pedido do bloco: " + bloco.getNumeroDoBloco() + " do ficheiro: " + bloco.getFileDetails().nomeFicheiro());
					t = new TrataPedidos(bloco, inStream, outStream);
					pool.execute(t);
					
				//Pedido de pesquisa de ficheiros que contenham uma determinada palavra chave
				} else if (msg instanceof WordSearchMessage) {
					WordSearchMessage palavraChave=(WordSearchMessage)msg;
					System.out.println("Pedido para procurar ficheiros por: " + palavraChave.getPalavraChave());
					t = new TrataPedidos(palavraChave, inStream, outStream);
					pool.execute(t);
				}

			}catch (ClassNotFoundException | IOException e) {
				System.out.println("ServidorFicheiros linha 75 - IOException: " + e.getMessage());
				System.out.println("ServidorFicheiros vai ser lan�ado outra vez");
				try {
					s.close();
				} catch (IOException e1) {
					System.out.println("Socket j� estava fechado - ServidorFicheiros linha 80 - IOException: " + e1.getMessage());
					e1.printStackTrace();
				}
			}
		}
	}

	//pedido pode ser Detalhes dos Ficheiros que est�o na pasta local ou
	//Parte de um determinado ficheiro da pasta local
	private class TrataPedidos implements Runnable {
		private WordSearchMessage palavraChave=null;
		private FileBlockRequestMessage bloco=null;
		private ObjectInputStream ois=null;
		private ObjectOutputStream oos=null;
		private String tipoPedido=null;
		
		//Pedido do tipo "bloco" - pede o envio de um bloco de um determinado ficheiro
		public TrataPedidos(FileBlockRequestMessage b, ObjectInputStream inSream, ObjectOutputStream outStream){
			super();
			bloco = b;
			ois = inSream;
			oos = outStream;
			tipoPedido = "Bloco";
		}
		
		//Pedido de pesquisa de ficheiros que contenham uma determinada palavra chave
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
				e.printStackTrace();
			}
			return parteFicheiroPedido;
		}

		@Override
		public void run() {
			ArrayList<FileDetails> listaFicheirosEncontrados;

			try {
				
				//Pedido do tipo "bloco" - pede o envio de um bloco de um determinado ficheiro
				if (tipoPedido.equals("Bloco")) {
					while(true) {
						byte[] parteFicheiro = parteDoFicheiroPedido(bloco);
						oos.flush();  //limpeza
						oos.writeObject(parteFicheiro);
//						System.out.println("Bloco enviado: " + bloco.getNumeroDoBloco());
						// Pr�ximo bloco
						bloco = (FileBlockRequestMessage)ois.readObject();
					}
					
				//Pedido de pesquisa de ficheiros que contenham uma determinada palavra chave
				}else if (tipoPedido.equals("PalavraChave")) {
					oos.flush();  //limpeza
					listaFicheirosEncontrados = procuraFicheirosLocais(palavraChave);
					Iterator<FileDetails> iListaFicheiros = listaFicheirosEncontrados.iterator();
					while(iListaFicheiros.hasNext()) {
						FileDetails fd = iListaFicheiros.next();
						oos.writeObject(fd);
//						System.out.println("Ficheiro encontrado: " + fd.nomeFicheiro());
					}
				}
			} catch (IOException | ClassNotFoundException e){
				System.out.println("Liga��o caiu... - ServidorFicheiros linha 176 - IOException ou ClassNotFoundException: " + e.getMessage());
			} finally {
				try {
					ois.close();
					oos.close();
				} catch (IOException e) {
					System.out.println("Streams j� estavam fechados - ServidorFicheiros linha 182 - IOException: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}
	}
}