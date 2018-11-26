package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.Serializable;

public class FileBlockRequestMessage implements Serializable {
	private static final long serialVersionUID = 2343L;
	
	private FileDetails detalhesFicheiro;
	private int offset;
	private int length;
	private int numeroDoBloco;
	
	public FileBlockRequestMessage(FileDetails f, int o, int l, int n) {
		detalhesFicheiro = f;
		offset= o;
		length = l;
		numeroDoBloco = n;
	}
	
	public FileDetails getFileDetails() {
		return detalhesFicheiro;
	}
	
	public int getOffset() {
		return offset;
	}

	public int getLength() {
		return length;
	}
	
	public int getNumeroDoBloco() {
		return numeroDoBloco;
	}
}
