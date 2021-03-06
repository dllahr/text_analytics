package controller.integration.readAndSplitRawFile;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import controller.articleIntegration.readAndSplitRawFile.MultiplelArticleSplitter;
import controller.articleIntegration.readAndSplitRawFile.RawArticle;

public class MultipleArticleSplitterTest {

	@Test
	public void test() throws IOException {
		MultiplelArticleSplitter parser = new MultiplelArticleSplitter();
		
		File inputFile = new File("test/resources/mdlz-2013-05-10.txt");
		
		List<RawArticle> articles = parser.readAndParse(inputFile);

		assertEquals(13, articles.size());
		
		RawArticle rawArticle = articles.get(0);
		
		System.out.println("first article");
		assertEquals(3, rawArticle.startLineNumber);
		
		for (String line : rawArticle.lines) {
			System.out.println(line);
		}


		rawArticle = articles.get(12);
		assertEquals(7830, rawArticle.startLineNumber);
		
		System.out.println("last article");
		for (String line : rawArticle.lines) {
			System.out.println(line);
		}
	}
}
