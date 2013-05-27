package controller.dateExtractionConversion;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import orm.Article;

public class DateOnMultipleLinesTest {

	@Test
	public void test() throws IOException {
		DateOnMultipleLines doml = new DateOnMultipleLines(false);
		
		List<String> lines = readTestDataFile("000_doc01_Document1of32.txt");
		assertTrue(lines.size() > 0);
		
		Date result = doml.extract(lines);
		assertNotNull(result);
		assertEquals(15281, Article.calculateDayIndex(result));
	}
	
	public static List<String> readTestDataFile(String filename) throws IOException {
		List<String> result = new LinkedList<>();
		
		BufferedReader reader = new BufferedReader(new FileReader("test/resources/" + filename));
		
		String curLine;
		while ((curLine = reader.readLine()) != null) {
			result.add(curLine);
		}
		
		return result;
	}

}
