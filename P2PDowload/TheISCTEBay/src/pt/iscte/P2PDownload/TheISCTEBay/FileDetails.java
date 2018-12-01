package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class FileDetails implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private ArrayList <Utilizador> users = new ArrayList<Utilizador>();
	private String nomeFicheiro;
	private long bytesFicheiro;

	public FileDetails (String nome, long bytes) {
		super();
		nomeFicheiro = nome;
		bytesFicheiro = bytes;
	}

	public String nomeFicheiro() {
		return nomeFicheiro;
	}

	public long bytesFicheiro() {
		return bytesFicheiro;
	}
	
	public void setUtilizador(Utilizador u) {
		//Só adiciona user u se este ainda não estiver na sua ArrayList users
		Utilizador userDaLista;
		Iterator<Utilizador> iUsers = users.iterator();
		while (iUsers.hasNext()) {
			userDaLista = iUsers.next();
			String ipUserDaLista = userDaLista.ipUtilizador();
			String portoUserDaLista = userDaLista.portoUtilizador();
			
			if(u.ipUtilizador().equals(ipUserDaLista) && u.portoUtilizador().equals(portoUserDaLista)) {
				return;
			}
		}
		users.add(u);	
		return;
	}
	
	@Override
	public String toString() {
		return nomeFicheiro;
	}

	public String  numberOfBytesToString() {
		return String.valueOf(bytesFicheiro);
	}

	public ArrayList <Utilizador> getUtilizadors() {
		return users;
	}
	
}


