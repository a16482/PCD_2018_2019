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
	}

	//Remoção de um utilizador do diretorio do cliente

	public void removeUtilizador() throws InterruptedException {
		try {
			Socket socket = new Socket(enderecoDiretorio, portoDiretorio);
			ObjectOutputStream remocao = new ObjectOutputStream(socket.getOutputStream());
			remocao.flush();
			remocao.writeObject(new String("RMV " + enderecoUtilizador + " " + portoUtilizador));	
			ObjectInputStream confirma_remocao = new ObjectInputStream(socket.getInputStream());
			confirma_remocao.readObject();
			remocao.close();
			confirma_remocao.close();
			socket.close();
		} catch (Exception e) {
			msgErro = "Erro ao estabelecer a ligação com o servidor." + NEW_LINE + 
					"Mensagem de erro original: " + e.getMessage() + NEW_LINE + 
					"A aplicação TheISCTEBay vai terminar."; 
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
				//TODO:
			} else if (conf.equals("existe")) {
				String msg = "Já há um utilizador no porto " + portoUtilizador + NEW_LINE +  
						"A aplicação TheISCTEBay vai terminar.";
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
					break;
				}
				Utilizador u = new Utilizador(utilizadorString); 
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

	private synchronized FileDetails adicionaListaFicheirosEncontrados(FileDetails f) {
		FileDetails ficheiroDaLista;
		int i=0;

		synchronized (listaFicheirosEncontrados) {
			//Ciclo para veridicar se o FileDetails "f" já existe em "listaFicheirosEncontrados"
			while (i< listaFicheirosEncontrados.size()) {
				ficheiroDaLista = listaFicheirosEncontrados.getElementAt(i);
				String nomeFicheiroLista = ficheiroDaLista.nomeFicheiro();
				Long bytesFicheiroLista = ficheiroDaLista.bytesFicheiro();
				// se o ficheiro já existir na lista de encontrados, devolve o objeto = objeto procurado que está na lista
				if(f.nomeFicheiro().equals(nomeFicheiroLista) && f.bytesFicheiro() == bytesFicheiroLista) {
					return ficheiroDaLista;
				}
				i++;
			}
			listaFicheirosEncontrados.addElement(f);
		}
		return f;
	}

	public DefaultListModel<FileDetails> procuraFicheirosPorPalavraChave (WordSearchMessage keyWord)  {
		Utilizador utilizadorLista;
		listaFicheirosEncontrados.removeAllElements();
		ArrayList<ThreadUtilizador> tUtilizadores = new ArrayList<ThreadUtilizador>();

		consultaUtilizadores();  //recarrega a lista de utilizadores no Diretorio

		Iterator<Utilizador> iListaUtilizadores = getListaUtilizadores().iterator();

		while (iListaUtilizadores.hasNext()) {
			utilizadorLista = iListaUtilizadores.next();

			// exclui o próprio (que também é membro do diretório da pesquisa
			if (!(utilizadorLista.ipUtilizador().equals(TheISCTEBay.devolveIPUtilizador()) 
					&& utilizadorLista.portoUtilizador().equals(String.valueOf(TheISCTEBay.devolvePortoUtilizador())))){
				ThreadUtilizador tu = new ThreadUtilizador (keyWord, utilizadorLista);
				tu.setName("User Porto "+ utilizadorLista.portoUtilizador());
				tUtilizadores.add(tu);
				tu.start();
			}			
		}
		
		Iterator <ThreadUtilizador> iUtilizador = tUtilizadores.iterator();
		while (iUtilizador.hasNext()) {
			ThreadUtilizador threadU = iUtilizador.next();
			try {
				threadU.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
		return listaFicheirosEncontrados;
	}

	private class ThreadUtilizador extends Thread {
		private WordSearchMessage word;
		private Utilizador user;
		private FileDetails ficheiroEncontrado;
		private Socket s=null;
		private ObjectOutputStream oos=null;
		private ObjectInputStream ois=null;

		public ThreadUtilizador (WordSearchMessage w, Utilizador u) {
			word = w;
			user = u;
		}

		public void run () {
			try {
				s = new Socket(user.ipUtilizador(), Integer.parseInt(user.portoUtilizador()));
				//se não houver resposta, sai com IOEXception e passa ao próximo utilizador.
				oos = new ObjectOutputStream(s.getOutputStream());
				ois = new ObjectInputStream(s.getInputStream());
				oos.flush();
				s.setSoTimeout(2000);
				oos.writeObject(word);
				while (true) {
					try {
						ficheiroEncontrado = (FileDetails)ois.readObject();
						//Verificar se o ficheiro encontrado já existe na nossa lista
						FileDetails ficheiroLista = adicionaListaFicheirosEncontrados(ficheiroEncontrado);
						//adiciona utilizador iterado à lista de utilizadores detentores do ficheiro no objeto FileDetails:
						ficheiroLista.setUtilizador(user);

					} catch (EOFException e) {
						break;
					}
				}
			} catch (NumberFormatException | IOException | ClassNotFoundException e1) {
				//NOTHING TO DO - vai para o próximo registo
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
}