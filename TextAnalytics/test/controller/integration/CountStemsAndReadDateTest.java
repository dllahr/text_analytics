package controller.integration;

import static org.junit.Assert.*;

import gate.util.GateException;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import controller.integration.readAndSplitRawFile.BuildMetaDataMap;
import controller.integration.readAndSplitRawFile.SplitArticle;
import controller.stemCountArticles.StemCounterTest;

public class CountStemsAndReadDateTest {

	@Test
	public void testDoAll() throws IOException, GateException {
		BuildMetaDataMap buildMetaDataMap = new BuildMetaDataMap();
		Map<String, Boolean> isLabelMap = buildMetaDataMap.build(new File("resources/meta_data_info.txt"));
		
		CountStemsAndReadDate countStemsAndReadDate = new CountStemsAndReadDate(isLabelMap);
		
		List<File> fileList = new LinkedList<>();
		fileList.add(new File("test/resources/mdlz-2013-05-10.txt"));
		
		List<SplitArticle> result = countStemsAndReadDate.doAll(fileList);
		
		assertEquals(13, result.size());
		
		SplitArticle first = result.get(0);
		System.out.println(first.articleDate);
		
		StemCounterTest.printMap(first.stemCountMap);
	}

}
