package pt.iscte.p2pdownload.theDirectory.domain;

import java.util.List;

public class Diretorio {
	private List<Cliente> listaDir;

	public Diretorio (){
		
	}
	
	public List<Cliente> getList() {
		return listaDir;
	}

	public void setList(List<Cliente> l) {
		this.listaDir = l;
	}
	
}
