package controller.integration.readAndSplitRawFile;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import controller.articleIntegration.readAndSplitRawFile.ArticleSectionSplitter;
import controller.articleIntegration.readAndSplitRawFile.RawArticle;
import controller.articleIntegration.readAndSplitRawFile.SplitArticle;

public class ArticleSectionSplitterTest {

	@Test
	public void test() throws IOException {
		final String bodyLabel = "full text";
		final String nonBodyLabel = "credit";
		
		Map<String, Boolean> metaDataLabelIsBodyMap = new HashMap<String, Boolean>();
		metaDataLabelIsBodyMap.put(bodyLabel, true);
		metaDataLabelIsBodyMap.put(nonBodyLabel, false);
		
		ArticleSectionSplitter articleSectionSplitter = new ArticleSectionSplitter(metaDataLabelIsBodyMap);
		
		RawArticle rawArticle = new RawArticle(new File("fake test file"), 0);
		
		final int linesPerSection = 5;
		
		int sectionIndex = 0;
		
		for (int i = 0; i < 3*linesPerSection; i++) {
			rawArticle.lines.add("line_" + i);
			
			if ((i+1)%linesPerSection == 0) {
				
				if (sectionIndex == 0) {
					rawArticle.lines.add(bodyLabel + " so the body starts here");
				} else if (sectionIndex == 1) {
					rawArticle.lines.add(nonBodyLabel + " so the non-body section starts here");
				}

				sectionIndex++;
			}
		}
		
		printLines(rawArticle.lines);
		System.out.println();

		
		SplitArticle splitArticle = articleSectionSplitter.split(rawArticle);
		
		assertEquals(linesPerSection, splitArticle.linesBeforeBody.size());
		assertEquals(linesPerSection+1, splitArticle.bodyLines.size());
		assertEquals(linesPerSection+1, splitArticle.linesAfterBody.size());
		
		
		printLines(splitArticle.linesBeforeBody);
		System.out.println("ArticleSectionSplitterTest");
		printLines(splitArticle.bodyLines);
		System.out.println("ArticleSectionSplitterTest");
		printLines(splitArticle.linesAfterBody);
	}

	private static void printLines(List<String> lines) {
		for(String line : lines) {
			System.out.println(line);
		}
	}
}
