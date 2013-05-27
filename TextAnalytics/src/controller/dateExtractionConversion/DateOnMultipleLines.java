package controller.dateExtractionConversion;

import java.util.Date;
import java.util.List;

import controller.dateExtractionConversion.ReadDateFromArticle.DateLineStyle;

public class DateOnMultipleLines implements DateExtractor {
	
	private final DateLineStyle[] styleArray = {DateLineStyle.newStyle, DateLineStyle.original};
	
	private final ReadDateFromArticle reader;
	
	public DateOnMultipleLines(boolean shouldDisplayProgress) {
		reader = new ReadDateFromArticle(shouldDisplayProgress);
	}

	@Override
	public Date extract(List<String> articleLineList) {
		for (DateLineStyle dateLineStyle : styleArray) {
			Date result = reader.readDate(articleLineList, dateLineStyle);
			
			if (result != null) {
				return result;
			}
		}

		return null;
	}

}
