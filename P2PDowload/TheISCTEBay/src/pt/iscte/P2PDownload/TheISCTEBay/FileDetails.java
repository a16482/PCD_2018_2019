package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.Serializable;
import java.util.ArrayList;

public class FileDetails implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList <Utilizador> users = new ArrayList<Utilizador>();
	private String nomeFicheiro;
	private long bytesFicheiro;

	public FileDetails (String nome, long bytes) {
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
		users.add(u);
	}
	
	public ArrayList <Utilizador> getUtilizadors() {
		return users;
	}
	
//	public FileDetails getFileDetails(String n, Long b){
//		if(n.equals(nomeFicheiro) && b.equals(bytesFicheiro)) {
//			return this;
//		}else {
//			return null;
//		}
//		
//	}
	
}


