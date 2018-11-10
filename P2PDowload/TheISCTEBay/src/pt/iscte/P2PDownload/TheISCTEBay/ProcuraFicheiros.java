package pt.iscte.P2PDownload.TheISCTEBay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JList;

//List<String> listaFicheirosUtilizadores = new LinkedList<String>();

/*
 * 
 List<String> supplierNames1 = new ArrayList<String>();
 List<String> supplierNames2 = new LinkedList<String>();
 List<String> supplierNames3 = new Vector<String>();
 */



public class ProcuraFicheiros extends Thread implements Runnable{
	private Diretorio dir;
	private List<String> listaFicheiros = new LinkedList<String>();
	private List<Utilizador> listaUtilizadores = new ArrayList<Utilizador>();

	@Override
	public void run() {
		dir.consultaUtilizadores();  //recarrega a lista de utilizadores no Diretor
		synchronized(this) {
			try {
				atualizaListaUtilizadores(dir);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				notifyAll();
			}
		}
		
	}

	private synchronized void atualizaListaUtilizadores(Diretorio d) throws InterruptedException {
		Utilizador utilizadorLista;
		String endIPUtilizadorLista="";
		String portoUtilizadorLista="";

		//String filePath = "//192.168.10.20";
		//  file://192.168.10.20/f$/MyDir/SubDir/text.doc
		//File[] ficheiros = new File(filePath).listFiles();

		Iterator<Utilizador> iListaUtilizadores = listaUtilizadores.iterator();
		while (iListaUtilizadores.hasNext()) {
			utilizadorLista = iListaUtilizadores.next();
			endIPUtilizadorLista=utilizadorLista.ipUtilizador();
			portoUtilizadorLista=utilizadorLista.portoUtilizador();		
		}
	}

//	public synchronized Diretorio loadDiretorio() throws InterruptedException {
//		Diretorio dir = TheISCTEBay.devolveDiretorio();
//		return dir;
//	}


	public void init() {
		new Thread(this, "ProcuraFicheiros").start();
	}
}
