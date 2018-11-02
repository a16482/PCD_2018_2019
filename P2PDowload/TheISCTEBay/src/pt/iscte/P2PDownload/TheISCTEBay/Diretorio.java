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
	
	public Diretorio(String eDiretorio, int pDiretorio,int pUtilizador){
		enderecoDiretorio = eDiretorio;
		portoDiretorio = pDiretorio;

		try {
			enderecoUtilizador = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		portoUtilizador = pUtilizador;
		msgInfo = "Endereço do Utilizador= " + enderecoUtilizador;
		System.out.println(msgInfo); 
		IGMsgBox.info(msgInfo);
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
				IGMsgBox.info(msgInfo, enderecoUtilizador);
			}
			registo.close();
			confirmacao.close();
			socket.close();		
		} catch (Exception e) {
			msgErro = "Erro: " + e.getMessage();
			System.out.println(msgErro); 
			IGMsgBox.erro(msgErro);
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
			IGMsgBox.erro(msgErro);
		}
	}
	
	public List<Utilizador> getListaUtilizadores() {
		return listaUtilizadores;
	}
	
//	public List<String> getListaUtilizString() {
//		List<Utilizador> listInUtiliz = getListaUtilizadores();
//		List<String> listOutUtiliz = new ArrayList<String>();
//		
//		int i = 0;
//		while (i < listInUtiliz.size()) {
//			Utilizador uIn = listInUtiliz.get(i);
//			String uOut = uIn.ipUtilizador() + " " + uIn.portoUtilizador();
//			listOutUtiliz.add(uOut);
//			i++;
//		}
//		return listOutUtiliz;
//	}
}
