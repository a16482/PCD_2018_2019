package pt.iscte.p2pdownload.theDirectory.domain;

import java.util.List;


// Esta classe n�o est� a ser usada
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
