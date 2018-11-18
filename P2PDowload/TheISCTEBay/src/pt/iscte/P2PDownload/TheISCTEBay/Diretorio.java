package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.EOFException;
import java.io.File;
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

	private String msgInfo;
	private String msgErro;
	
	private ArrayList<FileDetails> listaFicheirosEncontrados = new ArrayList<FileDetails> ();
	
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
		msgInfo = "Endere�o do Utilizador= " + enderecoUtilizador;
		System.out.println(msgInfo); 
	}

	//Remo��o de um utilizador do diretorio do cliente

	public void removeUtilizador() throws InterruptedException {
		try {
			Socket socket = new Socket(enderecoDiretorio, portoDiretorio);
			ObjectOutputStream remocao = new ObjectOutputStream(socket.getOutputStream());
			remocao.flush();
			remocao.writeObject(new String("RMV " + enderecoUtilizador + " " + portoUtilizador));	
			ObjectInputStream confirma_remocao = new ObjectInputStream(socket.getInputStream());
			String conf = (String)confirma_remocao.readObject();
			if (conf.equals("ok")) {
				msgInfo = "Remo��o do Utilizador do Diret�rio efetuada com sucesso" ;
				System.out.println(msgInfo); 
			}
			remocao.close();
			confirma_remocao.close();
			socket.close();
		} catch (Exception e) {
			msgErro = "Erro ao estabelecer a liga��o com o servidor." + NEW_LINE + 
					"Mensagem de erro original: " + e.getMessage() + NEW_LINE + 
					"A aplica��o TheISCTEBay vai terminar.";
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
				msgInfo = "Registo no Diret�rio efetuado com sucesso" ;
				System.out.println(msgInfo); 
			}
			registo.close();
			confirmacao.close();
			socket.close();
		} catch (Exception e) {
			msgErro = "Erro ao estabelecer a liga��o com o servidor." + NEW_LINE + 
					  "Mensagem de erro original: " + e.getMessage() + NEW_LINE + 
					  "A aplica��o TheISCTEBay vai terminar.";
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
			msgErro = "Erro ao estabelecer a liga��o com o servidor." + NEW_LINE + 
					  "Mensagem de erro original: " + e.getMessage() + NEW_LINE + 
					  "A aplica��o TheISCTEBay vai terminar.";
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
	
	private FileDetails adicionaListaFicheirosEncontrados(FileDetails f) {
		FileDetails ficheiroDaLista;
		
		Iterator<FileDetails> iListaFicheiros = listaFicheirosEncontrados.iterator();
		while (iListaFicheiros.hasNext()) {
			ficheiroDaLista = iListaFicheiros.next();
			String nomeFicheiroLista = ficheiroDaLista.nomeFicheiro();
			Long bytesFicheiroLista = ficheiroDaLista.bytesFicheiro();
			
			if(f.nomeFicheiro().equals(nomeFicheiroLista) && f.bytesFicheiro() == bytesFicheiroLista) {
				return ficheiroDaLista;
			}
			
		}
		listaFicheirosEncontrados.add(f);
		
		return f;
	}
	
	public ArrayList<FileDetails> procuraFicheirosPorPalavraChave (WordSearchMessage keyWord)  {
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
					// exclui o pr�prio (que tamb�m � membro do diret�rio da pesquisa
					
					s = new Socket(utilizadorLista.ipUtilizador(), Integer.parseInt(utilizadorLista.portoUtilizador()));
					ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
					oos.flush();
					oos.writeObject(keyWord);
					ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
					while (true) {
						try {
							ficheiroEncontrado = (FileDetails)ois.readObject();
							
							//Verificar se o ficheiro encontrado j� existe na nossa lista
							FileDetails ficheiroLista = adicionaListaFicheirosEncontrados(ficheiroEncontrado);
							ficheiroLista.setUtilizador(utilizadorLista);							
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
	
	//pedir ficheiro aos utilizadores que o t�m dispon�vel
	public void pedirFicheiro(FileDetails f) {
		Socket s;
		
		//s� estamos a pedir ao primeiro utilizador que tem o ficheiro
		// TODO iterar todos os utilizadores que t�m o ficheiro e lan�ar o pedido de uma parte a cada um
		Utilizador user = f.getUtilizadors().get(0);
		FileBlockRequestMessage pedido = new FileBlockRequestMessage(f, 0, 5000);
		
		try {
			s = new Socket(user.ipUtilizador(), Integer.parseInt(user.portoUtilizador()));
			ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
			oos.flush();
			oos.writeObject(pedido);
			ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			
			//apenas para testar a rece��o da 1� parte
			File parte1 = (File)ois.readObject();
			
			System.out.println("Chegou isto: " + parte1.getName());		
			
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

