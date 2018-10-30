package pt.iscte.p2pdownload.Diretorio.domain;

public class Msg {
	//private String msg;
	
	private String tipoMsg;
	private String ip="";
	private String porto;
	private int inicioPorto;

	private Cliente cliente;
	
	public Msg (String s) {
		tipoMsg=s.substring(0, 4);
		if (s.length() > 3 ) {
			for(int i = 5;s.charAt(i) != ' '; i++) {
				ip += s.charAt(i);
				inicioPorto = i+2;
			}
			porto= s.substring(inicioPorto,inicioPorto+4);
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