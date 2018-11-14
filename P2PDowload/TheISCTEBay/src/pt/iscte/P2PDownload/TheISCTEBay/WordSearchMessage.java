package pt.iscte.P2PDownload.TheISCTEBay;

public class WordSearchMessage {
	
	private String keyword;
	
	public WordSearchMessage (String k) {
		keyword = k;
	}
	
	public String getKeyword () {
		return keyword;
	}
}
