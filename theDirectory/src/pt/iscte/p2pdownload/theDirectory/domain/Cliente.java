package pt.iscte.p2pdownload.theDirectory.domain;

public class Cliente {
	private String enderecoIP;
	private int porto;

	public Cliente(String ip, int p) {
		enderecoIP = ip;
		porto = p;
	}

	public String devolveIPcliente(Cliente c) {
		return c.enderecoIP;
	}

	public Integer devolvePortocliente(Cliente c) {
		return c.porto;
	}
}
