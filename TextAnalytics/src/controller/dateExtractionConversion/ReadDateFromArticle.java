package controller.dateExtractionConversion;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

class ReadDateFromArticle {
	enum DateLineStyle {
		original,
		newStyle
	}

	private static final String dateLineMarkerNewStyle = "Publication date";
	private static final String[] nextLineMarkersArrayNewStyle = null;
	
	private static final String dateLineMarkerOriginal = "Publication title:";
	private static final String[] nextLineMarkersArrayOriginal = {"Source type:", "ProQuest document", "Text Word Count"};
	
	private final boolean shouldDisplayProgress;
	
	private final ConvertArticleDateLine convertArticleDateLine;
	
	ReadDateFromArticle(boolean shouldDisplayProgress) {
		this.shouldDisplayProgress = shouldDisplayProgress;
		
		convertArticleDateLine = new ConvertArticleDateLine(shouldDisplayProgress);
	}


	Date readDate(List<String> lineList, DateLineStyle dateLineStyle) {
		final String dateLineMarker, nextLineMarkersArray[];
		if (DateLineStyle.original == dateLineStyle) {
			dateLineMarker = dateLineMarkerOriginal;
			nextLineMarkersArray = nextLineMarkersArrayOriginal;
		} else {
			dateLineMarker = dateLineMarkerNewStyle;
			nextLineMarkersArray = nextLineMarkersArrayNewStyle;
		}

		
		final String dateLine = buildDateLineFromLineList(lineList, dateLineMarker, nextLineMarkersArray);
		
		if (shouldDisplayProgress) {
			System.out.println(dateLine);
		}
		
		return dateLine != null ? convertArticleDateLine.convertToDate(dateLine) : null;
	}
	
	
	
	static String buildDateLineFromLineList(List<String> lineList, String dateLineMarker, 
			String[] nextLineMarkersArray) {
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
}
