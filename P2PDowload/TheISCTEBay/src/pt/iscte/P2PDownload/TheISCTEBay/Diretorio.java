package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;


public class Diretorio {
	private String enderecoDiretorio;
	private int portoDiretorio;
	private String enderecoUtilizador;
	private int portoUtilizador;

	private List<Utilizador> listaUtilizadores = new ArrayList<>();

	private String msgInfo;
	private String msgErro;

	private DefaultListModel<FileDetails> listaFicheirosEncontrados = new DefaultListModel<FileDetails>();
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
//		System.out.println(msgInfo); 
	}

	//Remoção de um utilizador do diretorio do cliente

	public void removeUtilizador() throws InterruptedException {
		try {
			Socket socket = new Socket(enderecoDiretorio, portoDiretorio);
			ObjectOutputStream remocao = new ObjectOutputStream(socket.getOutputStream());
			remocao.flush();
			remocao.writeObject(new String("RMV " + enderecoUtilizador + " " + portoUtilizador));	
			ObjectInputStream confirma_remocao = new ObjectInputStream(socket.getInputStream());
			String conf = (String)confirma_remocao.readObject();
			if (conf.equals("ok")) {
//				msgInfo = "Remoção do Utilizador do Diretório efetuada com sucesso" ;
//				System.out.println(msgInfo); 
			}
			remocao.close();
			confirma_remocao.close();
			socket.close();
		} catch (Exception e) {
			msgErro = "Erro ao estabelecer a ligação com o servidor." + NEW_LINE + 
					"Mensagem de erro original: " + e.getMessage() + NEW_LINE + 
					"A aplicação TheISCTEBay vai terminar.";
//			System.out.println(msgErro); 
			MsgBox.erro(msgErro);
		}
	}


	//Registo no diretório
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
//				System.out.println(msgInfo); 
			} else if (conf.equals("existe")) {
				String msg = "Já há um utilizador no porto " + portoUtilizador + NEW_LINE +  
						"A aplicação TheISCTEBay vai terminar.";
//				System.out.println(msg);
				MsgBox.erro(msg);
				System.exit(1);
			}
			registo.close();
			confirmacao.close();
			socket.close();
		} catch (Exception e) {
			msgErro = "Erro ao estabelecer a ligação com o servidor." + NEW_LINE + 
					"Mensagem de erro original: " + e.getMessage() + NEW_LINE + 
					"A aplicação TheISCTEBay vai terminar.";
//			System.out.println(msgErro); 
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
			listaUtilizadores.clear();
			while (true) {
				String utilizadorString = (String)utilizadores.readObject();
				if (utilizadorString.equals("END")) {
//					System.out.println("Fim da lista de utilizadores");
					break;
				}
//				System.out.println("Utilizador recebido: "+utilizadorString);
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
//			System.out.println(msgErro); 
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

	private FileDetails adicionaListaFicheirosEncontrados(FileDetails f) {
		FileDetails ficheiroDaLista;
		int i=0;

		//Ciclo para veridicar se o FileDetails "f" já existe em "listaFicheirosEncontrados"
		while (i< listaFicheirosEncontrados.size()) {
			ficheiroDaLista = listaFicheirosEncontrados.getElementAt(i);
			String nomeFicheiroLista = ficheiroDaLista.nomeFicheiro();
			Long bytesFicheiroLista = ficheiroDaLista.bytesFicheiro();

			if(f.nomeFicheiro().equals(nomeFicheiroLista) && f.bytesFicheiro() == bytesFicheiroLista) {
				return ficheiroDaLista;
			}
			i++;
		}
		listaFicheirosEncontrados.addElement(f);

		return f;
	}

	public DefaultListModel<FileDetails> procuraFicheirosPorPalavraChave (WordSearchMessage keyWord)  {
		Utilizador utilizadorLista;
		FileDetails ficheiroEncontrado;
		listaFicheirosEncontrados.removeAllElements();
		Socket s=null;
		ObjectOutputStream oos=null;
		ObjectInputStream ois=null;
		consultaUtilizadores();  //recarrega a lista de utilizadores no Diretorio

//		System.out.println("Quem tem ficheiros com a palavra \"" + keyWord.getPalavraChave()+"\"?");
		Iterator<Utilizador> iListaUtilizadores = getListaUtilizadores().iterator();

		while (iListaUtilizadores.hasNext()) {
			utilizadorLista = iListaUtilizadores.next();

			// exclui o próprio (que também é membro do diretório da pesquisa
			if (!(utilizadorLista.ipUtilizador().equals(TheISCTEBay.devolveIPUtilizador()) 
					&& utilizadorLista.portoUtilizador().equals(String.valueOf(TheISCTEBay.devolvePortoUtilizador())))){
				try {
					s = new Socket(utilizadorLista.ipUtilizador(), Integer.parseInt(utilizadorLista.portoUtilizador()));
					oos = new ObjectOutputStream(s.getOutputStream());
					ois = new ObjectInputStream(s.getInputStream());
					oos.flush();
//					System.out.println(utilizadorLista.portoUtilizador() + " tens ficheiros com \"" + keyWord.getPalavraChave() + "\"?");
					oos.writeObject(keyWord);
					while (true) {
						try {
//							System.out.println("À espera da resposta...");
							ficheiroEncontrado = (FileDetails)ois.readObject();
//							System.out.println("Ficheiro encontrado: " + ficheiroEncontrado.nomeFicheiro() + " pelo utilizador: " + utilizadorLista.portoUtilizador());

							//Verificar se o ficheiro encontrado já existe na nossa lista
							FileDetails ficheiroLista = adicionaListaFicheirosEncontrados(ficheiroEncontrado);
							ficheiroLista.setUtilizador(utilizadorLista);
						} catch (EOFException e) {
//							System.out.println("Fim dos ficheiros encontrados pelo utilizador "+utilizadorLista.portoUtilizador());
							break;
						}
					}
				} catch (NumberFormatException | IOException | ClassNotFoundException e1) {
//					System.out.println("Ligação caiu... "+utilizadorLista.portoUtilizador() + " Diretório - linha 221 - Exception: " + e1.getMessage());
				}finally {
					try {
						oos.close();
						ois.close();
						s.close();	
					} catch (IOException e) {
						e.printStackTrace();
					}			
				}
			}
		}
		return listaFicheirosEncontrados;
	}
}

