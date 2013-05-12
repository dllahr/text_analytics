package controller.readAndSplitRawFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MultiplelArticleSplitter {

	private static final String documentDelimeter = "Document [0-9]+ of [0-9]+";

	List<RawArticle> readAndParse(File inputFile) throws IOException {
		List<RawArticle> result = new LinkedList<>();

		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		
		RawArticle currentArticle = new RawArticle(inputFile);
		result.add(currentArticle);
		currentArticle.lines.add(advanceToFirstArticle(reader));
		
		String curLine;

		while ((curLine = reader.readLine()) != null) {
			if (curLine.matches(documentDelimeter)) {
				currentArticle = new RawArticle(inputFile);
				result.add(currentArticle);
			}
			
			currentArticle.lines.add(curLine);
		}
		
		reader.close();
		
		return result;
	}
	
	private static final String advanceToFirstArticle(BufferedReader reader) throws IOException {

		String curLine;
		while ((curLine = reader.readLine()) != null) {
			if (curLine.matches(documentDelimeter)) {
				return curLine;
			}
		}

		return null;
	}
	
}
