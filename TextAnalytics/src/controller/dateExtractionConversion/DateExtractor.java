package controller.dateExtractionConversion;

import java.util.Date;
import java.util.List;

public interface DateExtractor {
	public Date extract(List<String> articleLineList);
}
