package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import controller.prediction.principalComponent.ArticleData;
import controller.prediction.principalComponent.ArticleStemCountVectorBuilder;
import controller.prediction.principalComponent.MeanStemCountVector;
import controller.prediction.principalComponent.MeanStemVectorBuilder;



public class MainGeneratePredictions {

	private static final String dateFormatString = "yyyy-MM-dd";

	
	public static void main(String[] args) throws ParseException {
		final int scoringModelId = Integer.valueOf(args[0]);
		final int companyId = Integer.valueOf(args[1]);
		final Date minDate = (new SimpleDateFormat(dateFormatString)).parse(args[2]);
		
//		ScoringModel sm = (ScoringModel)SessionManager.createQuery("from ScoringModel where id = :id").setInteger("id", scoringModelId).list().get(0);
		
		MeanStemCountVector meanVect = (new MeanStemVectorBuilder()).build(scoringModelId);
		
		List<ArticleData> articleDataList = (new ArticleStemCountVectorBuilder()).retrieve(scoringModelId, minDate, meanVect.minStemId);
		
		
	}

}
