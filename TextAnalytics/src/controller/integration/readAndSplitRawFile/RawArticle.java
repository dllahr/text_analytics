package controller.integration.readAndSplitRawFile;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class RawArticle {
	public final List<String> lines;
	public final File file;
	
	public RawArticle(File file) {
		this.lines = new LinkedList<>();
		this.file = file;
	}
}
