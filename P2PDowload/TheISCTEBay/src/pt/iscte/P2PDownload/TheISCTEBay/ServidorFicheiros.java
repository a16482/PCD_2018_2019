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
	//	private ObjectInputStream inStream=null;
	//	private ObjectOutputStream outStream = null;
	//	private Object msg;


	//Servidor sempre à escuta
	@Override
	public void run() {
		try {
			fileServer = new ServerSocket(portoProprio);
			serve();
			System.out.println("Servidor de Ficheiros iniciado no porto: " + portoProprio);
		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
		}
	}

	private void serve() {
		Socket s = null;
		ThreadPool pool = new ThreadPool(limitePedidos);
		while (true) {
			System.out.println("Servidor de Ficheiros à escuta no porto: " + portoProprio);

			try {
				s = fileServer.accept();
				int port = s.getPort();
				System.out.println("Novo Pedido do porto: "+port);
				ObjectInputStream inStream = new ObjectInputStream(s.getInputStream());
				ObjectOutputStream outStream = new ObjectOutputStream(s.getOutputStream());

				Object msg=inStream.readObject();

				if (msg instanceof String) {
					String mensagem = (String)msg;
					System.out.println("Mensagem recebida: " + mensagem);
					outStream.flush();  //limpeza
					String resposta = TheISCTEBay.devolvePortoUtilizador() + " Estou vivo!";
					outStream.writeObject(resposta);
					System.out.println("A minha resposta: " + resposta);
					outStream.close();
				} else if (msg instanceof FileBlockRequestMessage) {
					FileBlockRequestMessage bloco = (FileBlockRequestMessage)msg;
					System.out.println("Pedido do bloco: " + bloco.getNumeroDoBloco() + " do ficheiro: " + bloco.getFileDetails().nomeFicheiro());
					t = new TrataPedidos(bloco, inStream, outStream);
					pool.execute(t);
				} else if (msg instanceof WordSearchMessage) {
					WordSearchMessage palavraChave=(WordSearchMessage)msg;
					System.out.println("Pedido para procurar ficheiros por: " + palavraChave.getPalavraChave());
					t = new TrataPedidos(palavraChave, inStream, outStream);
					pool.execute(t);
				}

			}catch (ClassNotFoundException | IOException e) {
				System.out.println("ServidorFicheiros linha 75 - IOException: " + e.getMessage());
				System.out.println("ServidorFicheiros vai ser lançado outra vez");
				try {
					s.close();
				} catch (IOException e1) {
					System.out.println("Socket já estava fechado - ServidorFicheiros linha 80 - IOException: " + e1.getMessage());
					e1.printStackTrace();
				}
			}
			//			finally{
			//				try {
			//					inStream.close();
			//				} catch (IOException e) {
			//					e.printStackTrace();
			//				}
			//			}
		}
	}
	//
	//	private void respondeDiretorio(Socket soc) {
	//		ObjectInputStream inStream=null;
	//		ObjectOutputStream outStream = null;
	//		try {
	//			inStream = new ObjectInputStream(soc.getInputStream());
	//			outStream= new ObjectOutputStream(soc.getOutputStream());
	//
	//			String mensagem = (String) inStream.readObject();
	//			System.out.println("Mensagem recebida: " + mensagem);
	//			outStream.flush();  //limpeza
	//			String resposta = TheISCTEBay.devolvePortoUtilizador() + " Estou vivo!";
	//			outStream.writeObject(resposta);
	//			System.out.println("A minha resposta: " + resposta);
	//		} catch (ClassNotFoundException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} catch (IOException e) {
	//			// Não fazer nada... Leitura acabou
	//			System.out.println("Ligação caiu.\nTeste do Diretório para saber se o cliente está vivo");
	//		} finally{
	//			try {
	//				outStream.close();
	//				inStream.close();
	//			} catch (IOException e) {
	//				e.printStackTrace();
	//			}
	//		}
	//	}

	//pedido pode ser Detalhes dos Ficheiros que estão na pasta local ou
	//Parte de um determinado ficheiro da pasta local
	private class TrataPedidos implements Runnable {
		private WordSearchMessage palavraChave=null;
		private FileBlockRequestMessage bloco=null;
		private ObjectInputStream ois=null;
		private ObjectOutputStream oos=null;
		private String tipoPedido=null;

		public TrataPedidos(FileBlockRequestMessage b, ObjectInputStream inSream, ObjectOutputStream outStream){
			super();
			bloco = b;
			ois = inSream;
			oos = outStream;
			tipoPedido = "Bloco";
		}

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
			System.out.println("Pedido a ser tratado");

			try {
				if (tipoPedido.equals("Bloco")) {
					while(true) {
						byte[] parteFicheiro = parteDoFicheiroPedido(bloco);
						oos.flush();  //limpeza
						oos.writeObject(parteFicheiro);
						System.out.println("Bloco enviado: " + bloco.getNumeroDoBloco());
						// Próximo bloco
						bloco = (FileBlockRequestMessage)ois.readObject();
					}
					//					oos.close();
				}else if (tipoPedido.equals("PalavraChave")) {
					oos.flush();  //limpeza
					listaFicheirosEncontrados = procuraFicheirosLocais(palavraChave);
					Iterator<FileDetails> iListaFicheiros = listaFicheirosEncontrados.iterator();
					while(iListaFicheiros.hasNext()) {
						FileDetails fd = iListaFicheiros.next();
						oos.writeObject(fd);
						System.out.println("Ficheiro encontrado: " + fd.nomeFicheiro());
					}
					//					oos.close();
				}
			} catch (IOException | ClassNotFoundException e){
				System.out.println("Ligação caiu... - ServidorFicheiros linha 211 - IOException ou ClassNotFoundException: " + e.getMessage());
			} finally {
				try {
					ois.close();
					oos.close();
				} catch (IOException e) {
					System.out.println("Streams já estavam fechados - ServidorFicheiros linha 217 - IOException: " + e.getMessage());

					e.printStackTrace();
				}
			}
		}
	}
}

//			try {
//				while(true){
//					// RECEÇÃO da MSG:
//					System.out.println("Há espera de receber o Pedido.");
//					msg=inStream.readObject();
//					System.out.println("Pedido Recebido");
//					if (msg instanceof WordSearchMessage) {
//						palavraChave=(WordSearchMessage)msg;
//						System.out.println("Pedido para procurar ficheiros por: " + palavraChave);
//						outStream.flush();  //limpeza
//
//						// Falta chamar método para pesquisar ficheiros
//						listaFicheirosEncontrados = procuraFicheirosLocais(palavraChave);
//						Iterator<FileDetails> iListaFicheiros = listaFicheirosEncontrados.iterator();
//						while(iListaFicheiros.hasNext()) {
//							outStream.writeObject(iListaFicheiros.next());
//						}
//						outStream.close();
//					} else if (msg instanceof FileBlockRequestMessage){
//						System.out.println("Pedido de bloco de ficheiro");
//						pedidoParteFicheiro = (FileBlockRequestMessage)msg;
//						byte[] parteFicheiro = parteDoFicheiroPedido(pedidoParteFicheiro);
//						outStream.writeObject(parteFicheiro);
//						//System.out.println(this.getName() + " - Bloco: " + pedidoParteFicheiro.getNumeroDoBloco());
//					}
//					//					else if (msg instanceof String){
//					//						String mensagem = (String)msg;
//					//						System.out.println("Mensagem recebida: " + mensagem);
//					//						outStream.flush();  //limpeza
//					//						String resposta = "Eu, " + TheISCTEBay.devolveIPUtilizador() + " : " + 	TheISCTEBay.devolveIPUtilizador() + ", estou vivo e bem vivo!";
//					//						outStream.writeObject(resposta);
//					////						System.out.println("A minha resposta: " + resposta);
//					//					}
//				}
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				// Não fazer nada... Leitura acabou
//				System.out.println("Ligação caiu.\nTeste do Diretório para saber se o cliente está vivo");
//			} finally{
//				try {
//					outStream.close();
//					inStream.close();
//					s.close();
//					//					fileServer.notify();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}
//}