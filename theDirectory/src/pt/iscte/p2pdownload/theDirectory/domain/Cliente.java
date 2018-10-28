package pt.iscte.p2pdownload.theDirectory.domain;

public class Cliente {
	private String enderecoIP;
	private String porto;

	public Cliente(String ip, String p) {
		enderecoIP = ip;
		porto = p;
	}

	public String devolveIPcliente(Cliente c) {
		return c.enderecoIP;
	}

	public String devolvePortocliente(Cliente c) {
		return c.porto;
	}
	

}
