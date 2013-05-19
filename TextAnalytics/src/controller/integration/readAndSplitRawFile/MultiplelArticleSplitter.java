package controller.integration.readAndSplitRawFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MultiplelArticleSplitter {

	private static final String documentDelimeter = "Document [0-9]+ of [0-9]+";

	public List<RawArticle> readAndParse(File inputFile) throws IOException {
		List<RawArticle> result = new LinkedList<>();

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));

		LineAndIncrementPair startPair = advanceToFirstArticle(reader);
		int curLineNumber = startPair.lineIncrementNumber;
		
		RawArticle currentArticle = new RawArticle(inputFile, curLineNumber);
		result.add(currentArticle);
		currentArticle.lines.add(startPair.line);
		
		String curLine;

		while ((curLine = reader.readLine()) != null) {
			curLineNumber++;
			
			if (curLine.matches(documentDelimeter)) {
				currentArticle = new RawArticle(inputFile, curLineNumber);
				result.add(currentArticle);
			}
			
			currentArticle.lines.add(curLine);
		}
		
		reader.close();
		
		return result;
	}
	
	private static LineAndIncrementPair advanceToFirstArticle(BufferedReader reader) throws IOException {
		int increment = 0;
		
		String curLine;
		
		while ((curLine = reader.readLine()) != null) {
			increment++;
			
			if (curLine.matches(documentDelimeter)) {
				return new LineAndIncrementPair(curLine, increment);
			}
		}

		return null;
	}
	
	private static class LineAndIncrementPair {
		public final String line;
		public final int lineIncrementNumber;

		public LineAndIncrementPair(String line, int lineIncrementNumber) {
			this.line = line;
			this.lineIncrementNumber = lineIncrementNumber;
		}
	}
	
}
