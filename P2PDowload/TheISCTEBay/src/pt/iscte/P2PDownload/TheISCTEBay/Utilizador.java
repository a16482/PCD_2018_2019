package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.Serializable;

public class Utilizador implements Serializable{
	private static final long serialVersionUID = 15L;
	private String enderecoIP;
	private String porto;

	public Utilizador(String u) {
		String parte[] = u.split(" ");
		enderecoIP = parte[1];
		porto  = parte[2];
	}
	
	public Utilizador(String ip, String p) {
		enderecoIP = ip;
		porto = p;
	}

	public String ipUtilizador() {
		return enderecoIP;
	}

	public String portoUtilizador() {
		return porto;
	}
}