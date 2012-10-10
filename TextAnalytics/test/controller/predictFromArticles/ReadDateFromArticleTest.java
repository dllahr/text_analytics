package controller.predictFromArticles;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Date;

import org.junit.Test;

import controller.dateExtractionConversion.ReadDateFromArticle;

public class ReadDateFromArticleTest {
	private static final String[] fileUrlArray = {"test/resources/doc01_8.txt", "test/resources/doc01_9.txt"};
	
	@Test
	public void test() {
		for (String fileUrl : fileUrlArray) {
			Date result = ReadDateFromArticle.readDate(new File(fileUrl), ReadDateFromArticle.DateLineStyle.original);
			assertNotNull(result);
		}
	}

}
