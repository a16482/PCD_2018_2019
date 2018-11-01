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

	public Diretorio(String eDiretorio, int pDiretorio,int pUtilizador){
		enderecoDiretorio = eDiretorio;
		portoDiretorio = pDiretorio;

		try {
			enderecoUtilizador = InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			e.printStackTrace();
		}

		portoUtilizador = pUtilizador;
		System.out.println("Endereço do Utilizador= " + enderecoUtilizador); 
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
				System.out.println("Registo no Diretório efetuado com sucesso");
			}
			registo.close();
			confirmacao.close();
			socket.close();		
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
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
			System.out.println("Erro: " + e.getMessage());
		}
	}
	
	public List<Utilizador> getListaUtilizadores() {
		return listaUtilizadores;
	}
}
