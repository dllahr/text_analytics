package controller.articleIntegration.readAndSplitRawFile;

import java.io.File;
import java.util.Date;
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
	public final int startLineNumber;
	
	public Map<String, Integer> stemCountMap;
	
	public Date articleDate;
	
	public SplitArticle(File file, String lineDelimeter, int startLineNumber) {
		this.file = file;
		this.lineDelimeter = lineDelimeter;
		this.startLineNumber = startLineNumber;

		this.linesBeforeBody = new LinkedList<>();
		this.bodyLines = new LinkedList<>();
		this.linesAfterBody = new LinkedList<>();
	}
	
	public SplitArticle(File file, int startLineNumber) {
		this(file, defaultDelimeter, startLineNumber);
	}
	
	public String convertBodyLinesToString() {
		StringBuilder builder = new StringBuilder();

		for (String string : bodyLines) {
			builder.append(string).append(lineDelimeter);
		}

		return builder.toString();
	}
}
