package controller.integration;

import static org.junit.Assert.*;

import gate.util.GateException;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import controller.articleIntegration.CountStemsAndReadDate;
import controller.articleIntegration.readAndSplitRawFile.BuildMetaDataMap;
import controller.articleIntegration.readAndSplitRawFile.SplitArticle;
import controller.dateExtractionConversion.DateExtractor;
import controller.dateExtractionConversion.DateOnSingleLine;
import controller.stemCountArticles.StemCounterTest;

public class CountStemsAndReadDateTest {

	@Test
	public void testDoAll() throws IOException, GateException {
		BuildMetaDataMap buildMetaDataMap = new BuildMetaDataMap();
		Map<String, Boolean> isLabelMap = buildMetaDataMap.build(new File("resources/meta_data_info.txt"));
		
		List<DateExtractor> dsList = new LinkedList<>();
		dsList.add(new DateOnSingleLine());
		
		CountStemsAndReadDate countStemsAndReadDate = new CountStemsAndReadDate(isLabelMap, dsList, 30);
		
		List<File> fileList = new LinkedList<>();
		fileList.add(new File("test/resources/mdlz-2013-05-10.txt"));
		
		List<SplitArticle> result = countStemsAndReadDate.doAll(fileList);
		
		assertEquals(13, result.size());
		
		SplitArticle first = result.get(0);
		System.out.println(first.articleDate);
		
		StemCounterTest.printMap(first.stemCountMap);
	}

}
