package controller.predictFromArticles;

import static org.junit.Assert.*;
import gate.util.GateException;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import controller.integration.readAndSplitRawFile.SplitArticle;

public class StemCounterTest {

	@Test
	public void testStemCounter() throws GateException, IOException {
		@SuppressWarnings("unused")
		StemCounter a = new StemCounter();
		
		@SuppressWarnings("unused")
		StemCounter b = new StemCounter();
	}

	@Test
	public void testCount() throws GateException, IOException {
		SplitArticle splitArticle = new SplitArticle(new File("fake file"), "\r\n");
		splitArticle.bodyLines.add("The dog jumped over");
		splitArticle.bodyLines.add("the moon.");
		
		String[] expectedStems = {"the", "dog", "jump", "over", "moon"};
		int[] expectedCounts = {2, 1, 1, 1, 1};
		
		List<SplitArticle> list = new LinkedList<>();
		list.add(splitArticle);
		
		StemCounter stemCounter = new StemCounter();
		stemCounter.count(list);
		
		for (int i = 0; i < expectedStems.length; i++) {
			final int count = splitArticle.stemCountMap.get(expectedStems[i]);
			assertEquals(expectedStems[i], expectedCounts[i], count);
		}
		
		
//		for (String stem : splitArticle.stemCountMap.keySet()) {
//			final int count = splitArticle.stemCountMap.get(stem);
//			
//			System.out.println(stem + " " + count);
//		}
	}

}
