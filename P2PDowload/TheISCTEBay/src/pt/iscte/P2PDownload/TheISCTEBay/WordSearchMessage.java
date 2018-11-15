package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.Serializable;

<<<<<<< HEAD
public class WordSearchMessage implements Serializable {
=======
public class WordSearchMessage implements Serializable{
>>>>>>> 5331b2e08d1f36528661985e798523c7037ca2bb
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String palavraChave;
	
	public WordSearchMessage(String keyWord) {
		palavraChave=keyWord;
	}
	
	public String getPalavraChave() {
		return palavraChave;
	}
	
}
