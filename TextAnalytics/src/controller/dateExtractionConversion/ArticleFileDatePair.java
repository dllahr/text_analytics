package controller.dateExtractionConversion;

import java.io.File;
import java.util.Date;

public class ArticleFileDatePair {
	private final File file;
	private final Date date;
	
	public ArticleFileDatePair(File file, Date date) {
		this.file = file;
		this.date = date;
	}

	public File getFile() {
		return file;
	}

	public Date getDate() {
		return date;
	}
	
}
