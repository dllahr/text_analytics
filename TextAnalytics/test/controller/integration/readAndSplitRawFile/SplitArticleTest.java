package controller.integration.readAndSplitRawFile;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import controller.articleIntegration.readAndSplitRawFile.SplitArticle;

public class SplitArticleTest {

	@Test
	public void testConvertToSingleString() {
		SplitArticle splitArticle = new SplitArticle(new File("fake file"), 0);

		splitArticle.bodyLines.add("a");
		splitArticle.bodyLines.add("b");
		splitArticle.bodyLines.add("c");
		
		String result = splitArticle.convertBodyLinesToString();
		
		assertEquals("a" + splitArticle.lineDelimeter + "b" + splitArticle.lineDelimeter + "c" + splitArticle.lineDelimeter, result);
		System.out.println(result);
	}

}
