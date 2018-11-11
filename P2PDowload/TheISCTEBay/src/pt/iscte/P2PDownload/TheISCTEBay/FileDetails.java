package pt.iscte.P2PDownload.TheISCTEBay;

public class FileDetails {
	private String nomeFicheiro;
	private int bytesFicheiro;

	public FileDetails (String nome, int bytes) {
		nomeFicheiro = nome;
		bytesFicheiro = bytes;
	}

	public String nomeFicheiro() {
		return nomeFicheiro;
	}

	public int bytesFicheiro() {
		return bytesFicheiro;
	}
}


