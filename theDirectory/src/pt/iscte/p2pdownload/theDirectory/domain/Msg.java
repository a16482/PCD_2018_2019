package pt.iscte.p2pdownload.theDirectory.domain;

public class Msg {
	private String tipoMsg;
	private Cliente cliente;
		
	
	public Msg (String t, String ip, int port) {
		tipoMsg = t;
		cliente = new Cliente(ip, port);
	}
	
	public Msg (String t) {
		tipoMsg = t;
	}
	
	public String devolveTipoMsg (Msg m) {
		return m.tipoMsg;
	}
	
	public Cliente devolveCliente (Msg m) {
		return m.cliente;
	}
	
}
