package controller.integration.readAndSplitRawFile;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import controller.integration.readAndSplitRawFile.MultiplelArticleSplitter;
import controller.integration.readAndSplitRawFile.RawArticle;

public class MultipleArticleSplitterTest {

	@Test
	public void test() throws IOException {
		MultiplelArticleSplitter parser = new MultiplelArticleSplitter();
		
		File inputFile = new File("test/controller/integration/readAndSplitRawFile/mdlz-2013-05-10.txt");
		
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
