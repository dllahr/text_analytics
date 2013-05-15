package controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import orm.ScoringModel;
import orm.RegressionModel;

import controller.activity.LoadActivityData;
import controller.regressionPrediction.RegressionModelPredictor;
import controller.regressionPrediction.RegressionPredictionData;
import controller.stemCountArticles.LoadAndScoreArticles;
import controller.stockUpdate.StockUpdate;

public class Controller {

	private final StockUpdate stockUpdate;
	
	private final LoadAndScoreArticles loadAndScoreArticles;
	
	private final RegressionModelPredictor regressionModelPredictor;
	
	public Controller() {
		stockUpdate = new StockUpdate();
		loadAndScoreArticles = new LoadAndScoreArticles();
		regressionModelPredictor = new RegressionModelPredictor();
	}
	
	public void updateStock() {
		try {
			stockUpdate.updateStockPrices();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("done");
	}
	
	
	public void loadArticlesWithoutDates(ScoringModel company, File indivArticleDir) {
		loadAndScoreArticles.setScoringModel(company);
		loadAndScoreArticles.setIndividualArticleDir(indivArticleDir);
		loadAndScoreArticles.loadWithoutDate();
		System.out.println("Controller loadArticlesWithoutDates done");
	}

	
	public void loadActivity(File activityFile) {
		LoadActivityData loadActivityData = new LoadActivityData();
		
		try {
			loadActivityData.load(activityFile);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}
	
	public List<RegressionPredictionData> makeRegressionPrediction(RegressionModel rm) {
		return regressionModelPredictor.predict(rm);
	}
}
