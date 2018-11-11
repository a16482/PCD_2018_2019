package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

		Iterator<Utilizador> iListaUtilizadores = listaUtilizadores.iterator();
		while (iListaUtilizadores.hasNext()) {
			utilizadorLista = iListaUtilizadores.next();
			endIPUtilizadorLista=utilizadorLista.ipUtilizador();
			portoUtilizadorLista=utilizadorLista.portoUtilizador();		
		}
	}

	// invoca procura de ficheiros
    public static void main( String [] args ) {
        File actual = new File(".");
        for( File f : actual.listFiles()){
            System.out.println( f.getName() );
        }
    }
    
//    ....
    public File[] findDirectories(File root) { 
		return root.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return f.isDirectory();
			}});
	}

	public File[] findFiles(File root) {
		return root.listFiles(new FileFilter() {
			public boolean accept(File f) {
				return f.isFile();
			}});
	}
	
	public static void directory(File dir) {
		File[] files = dir.listFiles();
		for (File file : files) {
			System.out.println(file.getAbsolutePath());
			if (file.listFiles() != null) {
				directory(file);        
			}

		}
	} 	
	
//	.....


	public void init() {
		new Thread(this, "ProcuraFicheiros").start();
	}
}
