package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.*;
import java.net.*;
import java.util.*;


public class Diretorio {
	private String enderecoDiretorio;
	private int portoDiretorio;
	private String enderecoUtilizador;
	private int portoUtilizador;
	private List<Utilizador> listaUtilizadores = new ArrayList<>();
	private String msgInfo;
	private String msgErro;
	
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
//		MsgBox.info(msgInfo);
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
//				MsgBox.info(msgInfo, enderecoUtilizador);
			}
			registo.close();
			confirmacao.close();
			socket.close();	// --------- GRANDE DÚVIDA!! -----------
		} catch (Exception e) {
			msgErro = "Erro: " + e.getMessage();
			System.out.println(msgErro); 
			MsgBox.erro(msgErro);
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
				Utilizador u = new Utilizador(utilizadorString);
				listaUtilizadores.add(u);
			}
			pedidoLista.close();
			utilizadores.close();
			socketLista.close();		
		} catch (Exception e) {
			msgErro = "Erro: " + e.getMessage();
			System.out.println(msgErro); 
			MsgBox.erro(msgErro);
		}
	}
	
	public List<Utilizador> getListaUtilizadores() {
		return listaUtilizadores;
	}
	
	public int getTotalUtilizadores() {
		return listaUtilizadores.size();
	}
	
	public Utilizador getUtilizadorNDaLista(int n) {
		return listaUtilizadores.get(n);
	}
	
	public List<String> getListaUtilizadoresStr() {
		List<String> listaUtilizadoresStr = new ArrayList<String>();
		listaUtilizadoresStr.clear();
		String membro ="";
		int i = 0;
		while (i < listaUtilizadores.size()) {
			membro = getUtilizadorNDaLista(i).ipUtilizador() + " " +  getUtilizadorNDaLista(i).portoUtilizador();
			listaUtilizadoresStr.add(membro);
			i++;		
		}
		return listaUtilizadoresStr;
	}
	
	public ArrayList<String> getListaUtilizadoresArrayStr() {
		ArrayList<String> listaUtilizadoresArrayStr = new ArrayList<String>();
		listaUtilizadoresArrayStr.clear();
		String elemento ="";
		int i = 0;
		while (i < listaUtilizadores.size()) {
			elemento = getUtilizadorNDaLista(i).ipUtilizador() + " " +  getUtilizadorNDaLista(i).portoUtilizador();
			listaUtilizadoresArrayStr.add(elemento);
			i++;		
		}
		return listaUtilizadoresArrayStr;
	}
}