package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.Serializable;

public class WordSearchMessage implements Serializable {
	
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
