package controller.dateExtractionConversion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ReadDateFromArticle {
	public enum DateLineStyle {
		original,
		newStyle
	}
	private static final String dateLineMarkerNewStyle = "Publication date";
	private static final String[] nextLineMarkersArrayNewStyle = null;
	
	private static final String dateLineMarkerOriginal = "Publication title:";
	private static final String[] nextLineMarkersArrayOriginal = {"Source type:", "ProQuest document", "Text Word Count"};
	
	private static boolean shouldDisplayProgress = true;
	
	public static Date readDate(File articleFile, DateLineStyle dateLineStyle) throws IOException {
		if (shouldDisplayProgress) {
			System.out.println("Attempting to parse date from file " + articleFile.getAbsolutePath());
		}
		
		List<String> lineList = new LinkedList<>();
		
		BufferedReader reader = new BufferedReader(new FileReader(articleFile));
		
		String curLine;
		while ((curLine = reader.readLine()) != null) {
			lineList.add(curLine);
		}
		
		reader.close();
		
		return readDate(lineList, dateLineStyle);
	}

	public static Date readDate(List<String> lineList, DateLineStyle dateLineStyle) {
		final String dateLineMarker, nextLineMarkersArray[];
		if (DateLineStyle.original == dateLineStyle) {
			dateLineMarker = dateLineMarkerOriginal;
			nextLineMarkersArray = nextLineMarkersArrayOriginal;
		} else {
			dateLineMarker = dateLineMarkerNewStyle;
			nextLineMarkersArray = nextLineMarkersArrayNewStyle;
		}

		
		final String dateLine;
		try {
			dateLine = readDateLineFromFile(lineList, dateLineMarker, nextLineMarkersArray);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		if (shouldDisplayProgress) {
			System.out.println(dateLine);
		}
		
		return dateLine != null ? ConvertArticleDateLine.convertToDate(dateLine) : null;
	}
	
	
	
	protected static String readDateLineFromFile(List<String> lineList, String dateLineMarker, 
			String[] nextLineMarkersArray) throws IOException {
		String result = null;
		
		Iterator<String> lineIterator = lineList.iterator();

		String curLine = null;
		boolean doContinue = lineIterator.hasNext();
		while (doContinue) { 
			curLine = lineIterator.next();
			
			doContinue = lineIterator.hasNext() && ! curLine.startsWith(dateLineMarker);
		}
		
		if (curLine != null && curLine.startsWith(dateLineMarker)) {
			StringBuilder builder = new StringBuilder();
			builder.append(curLine);

			doContinue = true;
			while (nextLineMarkersArray != null && doContinue && lineIterator.hasNext()) {
				curLine = lineIterator.next();

				for (String nextLineMarker : nextLineMarkersArray) {
					if (curLine.startsWith(nextLineMarker)) {
						doContinue = false;
					}	
				}

				if (doContinue) {
					builder.append(" ").append(curLine.trim());
				}
			}
			result = builder.toString();
		}
		
		return result;
	}
	
	public static void setShouldDisplayProgress(boolean shouldDisplayProgress) {
		ReadDateFromArticle.shouldDisplayProgress = shouldDisplayProgress;
		ConvertArticleDateLine.setShouldDisplayProgress(shouldDisplayProgress);
	}
}
