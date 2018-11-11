package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Diretorio {
	private String enderecoDiretorio;
	private int portoDiretorio;
	private String enderecoUtilizador;
	private int portoUtilizador;
	
	private List<Utilizador> listaUtilizadores = new ArrayList<>();
	private List<FileDetails> listaFicheiros = new ArrayList<FileDetails>();
	private String palavraChave;
	private String msgInfo;
	private String msgErro;
	
	private static final String NEW_LINE = "\n";
	
	
	public Diretorio(String eDiretorio, int pDiretorio, int pUser){
		enderecoDiretorio = eDiretorio;
		portoDiretorio = pDiretorio;

		try {
			enderecoUtilizador = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		portoUtilizador = pUser;
		msgInfo = "Endereço do Utilizador= " + enderecoUtilizador;
		System.out.println(msgInfo); 
	}

	//Remoção de um utilizador do diretorio
	public void removeUtilizador() {
		try {
			Socket socket = new Socket(enderecoDiretorio, portoDiretorio);
			ObjectOutputStream registo = new ObjectOutputStream(socket.getOutputStream());
			registo.flush();
			registo.writeObject(new String("RMV " + enderecoUtilizador + " " + portoUtilizador));	
			ObjectInputStream confirmacao = new ObjectInputStream(socket.getInputStream());
			String conf = (String)confirmacao.readObject();
			if (conf.equals("ok")) {
				msgInfo = "Remoção do Utilizador do Diretório efetuada com sucesso" ;
				System.out.println(msgInfo); 
			}
			registo.close();
			confirmacao.close();
			socket.close();
		} catch (Exception e) {
			msgErro = "Erro ao estabelecer a ligação com o servidor." + NEW_LINE + 
					  "Mensagem de erro original: " + e.getMessage() + NEW_LINE + 
					  "A aplicação TheISCTEBay vai terminar.";
			System.out.println(msgErro); 
			MsgBox.erro(msgErro);
			System.exit(1);
		}
	}
	
	//Registo no diretorio
	public void registoDiretorio() {
		try {
			Socket socket = new Socket(enderecoDiretorio, portoDiretorio);
			ObjectOutputStream registo = new ObjectOutputStream(socket.getOutputStream());
			registo.flush();
			registo.writeObject(new String("INSC " + enderecoUtilizador + " " + portoUtilizador));	
			ObjectInputStream confirmacao = new ObjectInputStream(socket.getInputStream());
			String conf = (String)confirmacao.readObject();
			if (conf.equals("ok")) {
				msgInfo = "Registo no Diretório efetuado com sucesso" ;
				System.out.println(msgInfo); 
			}
			registo.close();
			confirmacao.close();
			socket.close();
		} catch (Exception e) {
			msgErro = "Erro ao estabelecer a ligação com o servidor." + NEW_LINE + 
					  "Mensagem de erro original: " + e.getMessage() + NEW_LINE + 
					  "A aplicação TheISCTEBay vai terminar.";
			System.out.println(msgErro); 
			MsgBox.erro(msgErro);
			System.exit(1);
		}
	}

	public void consultaUtilizadores() {
		try {
			Socket socketLista = new Socket(enderecoDiretorio, portoDiretorio);
			ObjectOutputStream pedidoLista = new ObjectOutputStream(socketLista.getOutputStream());
			pedidoLista.flush();
			pedidoLista.writeObject(new String("CLT"));
			ObjectInputStream utilizadores = new ObjectInputStream(socketLista.getInputStream());
			
			while (true) {
				String utilizadorString = (String)utilizadores.readObject();
				if (utilizadorString.equals("END")) {
					System.out.println("Fim da lista de utilizadores");
					break;
					}
				System.out.println(utilizadorString);
				Utilizador u = new Utilizador(utilizadorString); //alterar
				if (!existeUtilizador(u)) { 
					listaUtilizadores.add(u);
				}
			}
			pedidoLista.close();
			utilizadores.close();
			socketLista.close();		
		} catch (Exception e) {
			msgErro = "Erro ao estabelecer a ligação com o servidor." + NEW_LINE + 
					  "Mensagem de erro original: " + e.getMessage() + NEW_LINE + 
					  "A aplicação TheISCTEBay vai terminar.";
			System.out.println(msgErro); 
			MsgBox.erro(msgErro);
			System.exit(1);
		}
	}
	
	public boolean existeUtilizador (Utilizador u) {
		boolean existeUtilizador= false;
		Utilizador utilizadorLista;
		Iterator<Utilizador> iListaUtilizadores = listaUtilizadores.iterator();
		while (iListaUtilizadores.hasNext()) {
			utilizadorLista = iListaUtilizadores.next();
			if(utilizadorLista.ipUtilizador().equals(u.ipUtilizador()) && utilizadorLista.portoUtilizador().equals(u.portoUtilizador())) {
				existeUtilizador = true;
			}
		}
		return existeUtilizador;
	}
	
	public List<Utilizador> getListaUtilizadores() {
		return this.listaUtilizadores;
	}
	
	public int getTotalUtilizadores() {
		return this.listaUtilizadores.size();
	}
	
	public Utilizador getUtilizadorNDaLista(int n) {
		return this.listaUtilizadores.get(n);
	}
	
	
	//-------------------------------------------------------------------------
	// Classe em construção
	//-------------------------------------------------------------------------
	public class LookUpForFiles  extends Thread implements Runnable {
		// carrega a lista List<FileDetails> listaFicheiros
		@Override
		public void run() {
			try {
				File ficheiro_atual = new File("START/FROM/DIR"); 
				procuraFicheiros(0, ficheiro_atual, palavraChave);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				notifyAll();
			}
		}

		//procura de ficheiros
		public void procuraFicheiros(int profundidade, File ficheiro_atual, String palavraChave) throws IOException{
			//File ficheiro_atual = new File("."); //atual

			String nomeFicheiro = "";
			long bytesFicheiro= 0;

			for(File ficheiro : ficheiro_atual.listFiles()){ // obtém, mas não distingue ficheiros e diretorios
				if (!ficheiro.isDirectory()) { 
					 nomeFicheiro = ficheiro.getName();
					 bytesFicheiro= ficheiro.length();
					 System.out.println( nomeFicheiro + ", " + String.valueOf(bytesFicheiro) );
					 if ((ficheiroNaoExcluido(nomeFicheiro)) && (nomeFicheiro.contains(palavraChave))) {
						// colocar o ficheiro na lista
						 FileDetails ficheiroEncontrado = new FileDetails(nomeFicheiro,bytesFicheiro); 
						 listaFicheiros.add(ficheiroEncontrado);
					 }
						 
				} else {
					// invocação recursiva
					File[] ficheiros = ficheiro.listFiles(); 
					for (int i = 0; i < ficheiros.length; i++){
						procuraFicheiros(profundidade + 4, ficheiros[i], palavraChave);
					} 
				}
			}
		}
		
		//Exclusao de nomes
		boolean ficheiroNaoExcluido(String nomeFicheiro){
		    if (nomeFicheiro.charAt(0) == '.') {
		        return false;
		    }
		    if (nomeFicheiro.contains("svn")) {
		        return false;
		    }
		    //.
		    //. outras exclusões possíveis
		    //.
		    return true;
		}
		
		public class ListFiles {
		    public File[] findDirectories(File ficheiro) { 
		        return ficheiro.listFiles(new FileFilter() {
		            public boolean accept(File f) {
		                return f.isDirectory();
		            }});
		    }

		    public File[] findFiles(File ficheiro) {
		        return ficheiro.listFiles(new FileFilter() {
		            public boolean accept(File f) {
		                return f.isFile();
		            }});
		    }
		}

//		private void directory(File dir) {
//			File[] files = dir.listFiles();
//			for (File file : files) {
//				System.out.println(file.getAbsolutePath());
//				if (file.listFiles() != null)
//					directory(file);        
//			}
//		} 
	
		public void init() {
			new Thread(this, "LookUpForFiles").start();
		}
	}
}

