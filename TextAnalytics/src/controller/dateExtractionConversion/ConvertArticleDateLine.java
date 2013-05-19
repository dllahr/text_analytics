package controller.dateExtractionConversion;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertArticleDateLine {
	private static final String[] dateFormatStringArray = {"MMM d, yyyy.", "MMM d, yyyy"};
	
	private final boolean shouldDisplayProgress;
	
	public ConvertArticleDateLine(boolean shouldDisplayProgress) {
		this.shouldDisplayProgress = shouldDisplayProgress;
	}

	public Date convertToDate(String dateLine) {
		Date result = null;
		boolean doContinue = true;
		for (int i = 0; doContinue && i < dateFormatStringArray.length; i++) {
			final String dateFormatStr = dateFormatStringArray[i];
			
			if (shouldDisplayProgress) {
				System.out.print(dateFormatStr + " ");
			}
			
			DateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
			
			for (int startPos = dateLine.length() - dateFormatStr.length(); doContinue && startPos >= 0; startPos--) {
				if (shouldDisplayProgress) {
					System.out.print(startPos + " ");
				}
				
				result = dateFormat.parse(dateLine, new ParsePosition(startPos));
				if (result != null) {
					doContinue = false;
				}
			}
			if (shouldDisplayProgress) {
				System.out.println();
			}
		}

		return result;
	}
}
