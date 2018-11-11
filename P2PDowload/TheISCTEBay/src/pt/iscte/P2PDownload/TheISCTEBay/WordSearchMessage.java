package pt.iscte.P2PDownload.TheISCTEBay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public abstract class WordSearchMessage {

	

	 	public String getFile() {
		    return fileName;
		  }

	    public int getCount(String term) {
	        File folder = ...;
	        return getCountInFolder(folder);
	    }

	    /**
	     * Template method pattern
	     */
	    protected abstract int getCountInFile(File file, String term);

	    private int getCountInFolder(File folder, String term) {
	        int count = 0;
	        File[] files = folder.listFiles();
	        for (int j = 0; j < files.length; j++) {
	            if (files[j].isDirectory()) {
	                count = count + getCountInFolder(files[j], term);
	            } else {
	                count = count + getCountInFile(files[j], term);
	            }
	        }
	        return count;
	    }

	    ...

	}

	
	public class Searcher {
	    private WordSearch wordSearch;

	    public Searcher(String dir, WordSearch wordSearch) {
	        this.wordSearch = wordSearch;
	        ...
	    }

	    public int getCount(String term) {
	        return this.wordSearch.getCount(term);
	    }

	    public void setWordSearch(WordSearch wordSearch) {
	        this.wordSearch = wordSearch;
	    }

	    ...

}
