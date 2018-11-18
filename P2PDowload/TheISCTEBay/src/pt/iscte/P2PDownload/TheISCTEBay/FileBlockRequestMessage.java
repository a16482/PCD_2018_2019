package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.Serializable;

public class FileBlockRequestMessage implements Serializable {
	private static final long serialVersionUID = 2343L;
	
	private FileDetails detalhesFicheiro;
	private int offset;
	private int length;
	
	public FileBlockRequestMessage(FileDetails f, int o, int l) {
		detalhesFicheiro = f;
		offset= o;
		length = l;
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
}
