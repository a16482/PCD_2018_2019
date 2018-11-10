package pt.iscte.P2PDownload.TheISCTEBay;

public class Utilizador {
	private String enderecoIP="";
	private String porto="";
//	private String diretoriaTransferencias="";

	public Utilizador(String u) {
		int inicioPorto=0;
		for(int i = 4;u.charAt(i) != ' '; i++) {
			enderecoIP += u.charAt(i);
			inicioPorto = i+2;
		}
		porto= u.substring(inicioPorto,inicioPorto+4);
//		diretoriaTransferencias= u.substring(inicioPorto+6, u.length -1);
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
	
//	public String dirTransfUtilizador() {
//		return diretoriaTransferencias;
//	}
	

}
