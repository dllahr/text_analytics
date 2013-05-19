package main;

import gate.util.GateException;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import controller.dateExtractionConversion.DateOnSingleLine;
import controller.integration.CountStemsAndReadDate;
import controller.integration.readAndSplitRawFile.BuildMetaDataMap;
import controller.integration.readAndSplitRawFile.SplitArticle;
import controller.stemCountArticles.ArticleStemCountSaver;

import orm.ScoringModel;
import orm.SessionManager;

public class MainLoadArticles {
	private static final String metaDataMapFilepath = "resources/meta_data_info.txt";
	private static final int commitBatchSize = 1000;
	private static final int stemBatchSize = 500;

	public static void main(String[] args) throws IOException, GateException {
		final int scoringModelId = Integer.valueOf(args[0]);
		final File inputDir = new File(args[1]);
		
		ScoringModel scoringModel = lookupScoringModel(scoringModelId);
		
		Map<String, Boolean> metaDataMap = getMetaDataMap();
		
		CountStemsAndReadDate countStemsAndReadDate = new CountStemsAndReadDate(metaDataMap, new DateOnSingleLine(),
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
			ArticleStemCountSaver.saveStemCountToDatabase(splitArticle, scoringModel);
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
	}
	
	private static ScoringModel lookupScoringModel(int scoringModelId) {
		Query query = SessionManager.createQuery("from ScoringModel where id = :id");
		query.setInteger("id", scoringModelId);
		return (ScoringModel)query.list().get(0);
	}
	
	private static Map<String, Boolean> getMetaDataMap() throws IOException {
		BuildMetaDataMap buildMetaDataMap = new BuildMetaDataMap();
		return buildMetaDataMap.build(new File(metaDataMapFilepath));
	}
}
