package pt.iscte.p2pdownload.theDirectory.domain;

public class Msg {
	private String msg;
	
	private String tipoMsg;

	private String ip;
	private String porto;

	private Cliente cliente;
	
	public Msg (String m) {
		setMsg(m);
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getTipoMsg(String m) {
		tipoMsg=m.substring(0, 4);
		return tipoMsg;
	}

	public void setTipoMsg(String m) {
		this.tipoMsg=m.substring(0, 4);
	}

	public String getIPcliente (String m) {
		if (m.length() > 3 ) {
			ip= m.substring(4,19);
		}
		else{
			ip =null;
		}
		return ip;
	}
			
	public String getPortoCliente (String m) {
		if (m.length() > 3 ) {
			porto= m.substring(20,24);
		}
		else{
			porto =null;
		}
		return porto;
	}
	
	public void setClienteMsg (String m) {
		if (m.length() > 3 ) {
			this.cliente = new Cliente(getIPcliente(m), getPortoCliente(m));
		}
	}
	
	public Cliente getClienteMsg (String m) {
		Cliente cliente = new Cliente(getIPcliente(m), getPortoCliente(m));
		return cliente;
	}
}
