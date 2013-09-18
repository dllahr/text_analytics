package main;

import gate.util.GateException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import controller.dateExtractionConversion.DateExtractor;
import controller.dateExtractionConversion.DateOnMultipleLines;
import controller.dateExtractionConversion.DateOnSingleLine;
import controller.integration.CountStemsAndReadDate;
import controller.integration.readAndSplitRawFile.BuildMetaDataMap;
import controller.integration.readAndSplitRawFile.SplitArticle;
import controller.stemCountArticles.ArticleStemCountSaver;

import orm.SessionManager;

public class MainLoadArticles {
	private static final String metaDataMapFilepath = "resources/meta_data_info.txt";
	private static final int commitBatchSize = 1000;
	private static final int stemBatchSize = 500;

	public static void main(String[] args) throws IOException, GateException {
		final int articleSourceId = Integer.valueOf(args[0]);
		final File inputDir = new File(args[1]);
		
		System.out.println("MainLoadArticles");
		System.out.println("\tarticleSourceId:  " + articleSourceId);
		System.out.println("\tinputDir:  " + inputDir.getAbsolutePath());
		System.out.println("start time:  " + (new Date()));
		
		Map<String, Boolean> metaDataMap = getMetaDataMap();
		
		
		List<DateExtractor> dateExtractorList = new LinkedList<>();
		dateExtractorList.add(new DateOnSingleLine());
		dateExtractorList.add(new DateOnMultipleLines(false));
		
		CountStemsAndReadDate countStemsAndReadDate = new CountStemsAndReadDate(metaDataMap, dateExtractorList,
				stemBatchSize);
		
		List<File> fileList = Arrays.asList(inputDir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File arg0, String arg1) {
				return arg1.endsWith(".txt");
			}
		}));
		
		List<SplitArticle> splitArticleList = countStemsAndReadDate.doAll(fileList);
		
		int count = 0;
		for (SplitArticle splitArticle : splitArticleList) {
			ArticleStemCountSaver.saveStemCountToDatabase(splitArticle, articleSourceId);
			count++;
			
			if (count%commitBatchSize == 0) {
				System.out.println("saved to database " + count);
				SessionManager.commit();
			}
		}
		
		if (count%commitBatchSize > 0) {
			System.out.println("saved to database " + count);
			SessionManager.commit();
		}
		
		System.out.println("end time:  " + new Date());
	}
	
	private static Map<String, Boolean> getMetaDataMap() throws IOException {
		BuildMetaDataMap buildMetaDataMap = new BuildMetaDataMap();
		return buildMetaDataMap.build(new File(metaDataMapFilepath));
	}
}
