package controller.readRawFile;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class MultipleArticleSplitterTest {

	@Test
	public void test() throws IOException {
		MultiplelArticleSplitter parser = new MultiplelArticleSplitter();
		
		File inputFile = new File("test/controller/readRawFile/mdlz-2013-05-10.txt");
		
		List<RawArticle> articles = parser.readAndParse(inputFile);

		assertEquals(13, articles.size());
		
		System.out.println("first article");
		for (String line : articles.get(0).lines) {
			System.out.println(line);
		}
		
		System.out.println("last article");
		for (String line : articles.get(12).lines) {
			System.out.println(line);
		}
		
	}
}
