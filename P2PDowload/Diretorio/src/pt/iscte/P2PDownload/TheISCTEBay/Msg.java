package pt.iscte.P2PDownload.TheISCTEBay;

public class Msg {
	//private String msg;

	private String tipoMsg;
	private String ip;
	private String porto;
	private Cliente cliente;

	public Msg (String s) {
		
		String parte[]= s.split(" ");
		
		tipoMsg = parte[0];
		System.out.println("tipoMsg: " + tipoMsg);
		
		if(s.length() > 1) {
			ip = parte[1];
			porto= parte[2];
		}
		setClienteMsg();
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