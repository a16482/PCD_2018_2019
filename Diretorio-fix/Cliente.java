package pt.iscte.p2pdownload.Diretorio.domain;

public class Cliente {
	private String enderecoIP;
	private String porto;

	public Cliente(String ip, String p) {
		enderecoIP = ip;
		porto = p;
	}

	public String ipCliente() {
		return enderecoIP;
	}

	public String portoCliente() {
		return porto;
	}
	

}
