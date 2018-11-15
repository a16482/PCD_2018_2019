package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.EOFException;
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
				msgInfo = "Remoção do Utilizador do Diretório efetuada com sucesso" ;
				System.out.println(msgInfo); 
			}
			remocao.close();
			confirma_remocao.close();
			socket.close();
		} catch (Exception e) {
			msgErro = "Erro ao estabelecer a ligação com o servidor." + NEW_LINE + 
					"Mensagem de erro original: " + e.getMessage() + NEW_LINE + 
					"A aplicação TheISCTEBay vai terminar.";
			System.out.println(msgErro); 
			MsgBox.erro(msgErro);
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
			listaUtilizadores.clear();
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
	
	public ArrayList<FileDetails> procuraFicheiros(WordSearchMessage keyWord)  {
		ArrayList<FileDetails> listaFicheirosEncontrados = new ArrayList<FileDetails> ();
		Utilizador utilizadorLista;
		FileDetails ficheiroEncontrado;
		Socket s;
		consultaUtilizadores();  //recarrega a lista de utilizadores no Diretorio

		Iterator<Utilizador> iListaUtilizadores = getListaUtilizadores().iterator();
		
		while (iListaUtilizadores.hasNext()) {
			utilizadorLista = iListaUtilizadores.next();
			try {
				if (!(utilizadorLista.ipUtilizador().equals(TheISCTEBay.devolveIPUtilizador()) 
						&& utilizadorLista.portoUtilizador().equals(String.valueOf(TheISCTEBay.devolvePortoUtilizador())))){
					// exclui o próprio (que também é membro do diretório da pesquisa
					s = new Socket(utilizadorLista.ipUtilizador(), Integer.parseInt(utilizadorLista.portoUtilizador()));
					ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
					oos.flush();
					oos.writeObject(keyWord);
					ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
					while (true) {
						try {
							ficheiroEncontrado = (FileDetails)ois.readObject();
							listaFicheirosEncontrados.add(ficheiroEncontrado);
						} catch (EOFException e) {
							break;
						}
					}
					oos.close();
					ois.close();
					s.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return listaFicheirosEncontrados;
	}

}

