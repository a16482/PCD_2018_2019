package pt.iscte.p2pdownload.Diretorio.domain;

public class Msg {
	//private String msg;
	
	private String tipoMsg;
	private String ip;
	private String porto;

	private Cliente cliente;
	
	public Msg (String s) {
		tipoMsg=s.substring(0, 4);
		if (s.length() > 3 ) {
			ip= s.substring(4,19);
			porto= s.substring(20,24);
			setClienteMsg();
		}
	}

	public String getTipoMsg() {
		return tipoMsg;
	}

	public String getIP () {
		return ip;
	}
			
	public String getPorto() {
		return porto;
	}
	
	public void setClienteMsg () {
		cliente = new Cliente(getIP(), getPorto());
	}
	
	public Cliente getClienteMsg () {
		return cliente;
	}
}