package controller.dateExtractionConversion;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import controller.dateExtractionConversion.ReadDateFromArticle;
import controller.dateExtractionConversion.ReadDateFromArticle.DateLineStyle;

public class ReadDateFromArticleTest {
	private static final String[] fileUrlArray = {"test/resources/doc01_8.txt", "test/resources/doc01_9.txt"};
	
	@Test
	public void testFile() throws IOException {
		System.out.println("ReadDateFromArticleTest testFile");
		
		for (String fileUrl : fileUrlArray) {
			Date result = ReadDateFromArticle.readDate(new File(fileUrl), ReadDateFromArticle.DateLineStyle.original);
			assertNotNull(result);
		}
		
		System.out.println();
	}
	
	
	@Test
	public void testList() {
		System.out.println("ReadDateFromArticleTest testList");
		
		List<String> lineList = new LinkedList<>();
		lineList.add("Publication year: 2013");
		lineList.add("");
		lineList.add("Publication date: May 10, 2013");
		lineList.add("");
		lineList.add("Year: 2013");
		
		Date result = ReadDateFromArticle.readDate(lineList, DateLineStyle.newStyle);
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		assertEquals("2013-05-10", df.format(result));
		
		System.out.println("result:  " + result);
		System.out.println();
	}

}
