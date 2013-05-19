package controller.integration.readAndSplitRawFile;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class RawArticle {
	public final List<String> lines;
	
	/**
	 * file containing the file
	 */
	public final File file;
	
	/**
	 * line number that the article starts at within the file
	 */
	public final int startLineNumber;
	
	/**
	 * @param file file that contains the artilce
	 * @param startLineNumber  line number within the file where the article starts
	 */
	public RawArticle(File file, int startLineNumber) {
		this.lines = new LinkedList<>();
		this.file = file;
		this.startLineNumber = startLineNumber;
	}
}
