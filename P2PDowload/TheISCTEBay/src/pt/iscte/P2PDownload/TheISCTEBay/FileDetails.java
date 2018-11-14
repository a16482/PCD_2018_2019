package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.Serializable;

public class FileDetails implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String nomeFicheiro;
	private long bytesFicheiro;

	public FileDetails (String nome, long bytes) {
		nomeFicheiro = nome;
		bytesFicheiro = bytes;
	}

	public String nomeFicheiro() {
		return nomeFicheiro;
	}

	public long bytesFicheiro() {
		return bytesFicheiro;
	}
}


