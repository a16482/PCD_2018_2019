package pt.iscte.p2pdownload.Diretorio.domain;

public class Msg {
	//private String msg;

	private String tipoMsg;
	private String ip="";
	private String porto;
	private Cliente cliente;

	public Msg (String s) {
		if(s.length() < 4) {
			tipoMsg=s;
		}else {
			tipoMsg=s.substring(0, 4);
		}
		System.out.println("tipoMsg: " + tipoMsg);
		int inicioPorto=0;
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