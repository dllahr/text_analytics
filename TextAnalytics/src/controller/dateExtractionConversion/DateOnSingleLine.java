package controller.dateExtractionConversion;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DateOnSingleLine implements DateExtractor {

	private static final String dateKey = "publication date";
	
	private static final String dateFormatString = "MMM d, yyyy";
	
	private final DateFormat dateFormat;
	
	public DateOnSingleLine() {
		dateFormat = new SimpleDateFormat(dateFormatString);
	}
	
	@Override
	public Date extract(List<String> articleLineList) {
		for (String line : articleLineList) {
			final int keyIndex = line.toLowerCase().indexOf(dateKey);
			if (keyIndex >= 0) {
				final int maxIndex = line.length() - dateFormatString.length();
				
				for (int index = keyIndex + dateKey.length(); index <= maxIndex; index++) {
					Date result = dateFormat.parse(line, new ParsePosition(index));
					
					if (result != null) {
						return result;
					}
				}
			}
		}
		return null;
	}

}
