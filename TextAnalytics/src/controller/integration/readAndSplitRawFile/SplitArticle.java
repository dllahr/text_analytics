package controller.integration.readAndSplitRawFile;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SplitArticle {
	private static final String defaultDelimeter = "\r\n";

	public final List<String> linesBeforeBody;
	public final List<String> bodyLines;
	public final List<String> linesAfterBody;

	public final File file;
	public final String lineDelimeter;
	
	public Map<String, Integer> stemCountMap;
	
	public SplitArticle(File file, String lineDelimeter) {
		this.file = file;
		this.lineDelimeter = lineDelimeter;

		this.linesBeforeBody = new LinkedList<>();
		this.bodyLines = new LinkedList<>();
		this.linesAfterBody = new LinkedList<>();
	}
	
	public SplitArticle(File file) {
		this(file, defaultDelimeter);
	}
	
	public String convertBodyLinesToString() {
		StringBuilder builder = new StringBuilder();

		for (String string : bodyLines) {
			builder.append(string).append(lineDelimeter);
		}

		return builder.toString();
	}
}
