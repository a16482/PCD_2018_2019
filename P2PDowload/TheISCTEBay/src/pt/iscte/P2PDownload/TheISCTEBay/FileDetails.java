package pt.iscte.P2PDownload.TheISCTEBay;

public class FileDetails {
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


