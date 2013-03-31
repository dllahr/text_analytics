package controller.predictFromArticles;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;

import controller.StockClosePriceRetriever;
import controller.util.Utilities;

import orm.Article;
import orm.Prediction;
import orm.PredictionModel;
import orm.SessionManager;

public class GeneratePredictions {

	private static final long millisPerDay = 24*60*60*1000;
	
	protected static final String articleIdParamName = "articleIdList";
	
	private static final int predictionModelColumnIndex = 0;
	private static final int articleColumnIndex = 1;
	private static final int stockPriceChangeAverageColumnIndex = 2;
	private static final int stockPriceChangeDayOffsetColumnIndex = 3;
	
	protected static final String baseQueryStr = "SELECT pm, art, spc.average, spc.dayOffset " 
			+ "FROM ArticlePcValue artpc, StockPriceChange spc, PredictionModel pm, Article art "  
			+ "WHERE spc.eigenvalue.id=artpc.eigenvalue.id "
			+ "AND pm.stockPriceChange.id=spc.id "
			+ "AND art.id=artpc.article.id " 
			+ "AND artpc.article.id in (:" + articleIdParamName + ") AND ";
  		
	protected static final String lowerThresholdCondition = "artpc.value >= pm.thresholdLower AND pm.thresholdUpper is null";
	private static final String upperThresholdCondition = "artpc.value <= pm.thresholdUpper AND pm.thresholdLower is null";
	private static final String bothThresholdConditions = "artpc.value >= pm.thresholdLower AND artpc.value <= pm.thresholdUpper";
	
	public static void generatePredictions(List<Article> articleList) {
		List<Integer> articleIdList = new ArrayList<>(articleList.size());
		for (Article article : articleList) {
			articleIdList.add(article.getId());
		}
		
		String[] conditionArray = {lowerThresholdCondition, upperThresholdCondition, bothThresholdConditions};
		List<Object[]> result = new LinkedList<>();
		for (String condition : conditionArray) {
			Query query = SessionManager.createQuery(baseQueryStr + condition);
			query.setParameterList(articleIdParamName, articleIdList);
			
			List<Object[]> curResult = Utilities.convertGenericList(query.list());
			
			result.addAll(curResult);
		}

		StockClosePriceRetriever retriever = new StockClosePriceRetriever(articleList.get(0).getScoringModel());

		for (Object[] row : result) {
			Article article = (Article)row[articleColumnIndex];
			
			Double priceOnArticleDay = retriever.getStockAdjustedClosePrice(article.getDayIndex());
			if (priceOnArticleDay != null) {
				PredictionModel pm = (PredictionModel)row[predictionModelColumnIndex];
				
				Double aveChange = (Double)row[stockPriceChangeAverageColumnIndex];
				Integer dayOffset = (Integer)row[stockPriceChangeDayOffsetColumnIndex];
				
				Double predictionStockPrice = (1.0 + aveChange) * priceOnArticleDay;
				
				Long predictionMillis = article.getPublishDate().getTime() + (dayOffset*millisPerDay);
				
				Prediction prediction = new Prediction(article, pm, predictionStockPrice, new Date(predictionMillis));
				SessionManager.persist(prediction);
			} else {
				System.err.println("cannot make prediction for article because stock price not in " + article.getFilename());
			}
			
		}
	}

}
