package controller.readRawFile;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class SplitArticle {

	public final List<String> linesBeforeBody;
	public final List<String> bodyLines;
	public final List<String> linesAfterBody;

	public final File file;
	
	public SplitArticle(File file) {
		this.linesBeforeBody = new LinkedList<>();
		this.bodyLines = new LinkedList<>();
		this.linesAfterBody = new LinkedList<>();
		
		this.file = file;
	}
}
