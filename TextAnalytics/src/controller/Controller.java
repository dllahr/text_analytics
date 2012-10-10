package controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import orm.Company;

import controller.activity.LoadActivityData;
import controller.buildPredictionModel.BuildPredictonModel;
import controller.buildPredictionModel.EigenvectorStats;
import controller.buildPredictionModel.ReferenceStats;
import controller.predictFromArticles.ExportPredictionData;
import controller.predictFromArticles.LoadAndScoreArticles;
import controller.stockUpdate.StockUpdate;

public class Controller {

	private final StockUpdate stockUpdate;
	
	private final LoadAndScoreArticles loadAndScoreArticles;
	
	public Controller() {
		stockUpdate = new StockUpdate();
		loadAndScoreArticles = new LoadAndScoreArticles();
	}
	
	public void updateStock() {
		try {
			stockUpdate.updateStockPrices();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("done");
	}
	
	public void makePredictions(Company company, File indivArticleDir) {
		loadAndScoreArticles.setCompany(company);
		loadAndScoreArticles.setIndividualArticleDir(indivArticleDir);
		loadAndScoreArticles.loadAndScore();
		System.out.println("Controller loadAndScoreArticles done");
	}
	
	public void calculateReferenceStatistics(Company company) {
		ReferenceStats.calcStats(company);
	}
	
	public void calculateArticleStockStatistics(Company company) {
		EigenvectorStats eigStats = new EigenvectorStats();
		eigStats.doCalc(company);
	}

	public void createPredictionModel(Company company) {
		BuildPredictonModel build = new BuildPredictonModel();
		build.build(company);
	}

	public void savePredictions(File exportFile) {
		ExportPredictionData exporter = new ExportPredictionData();
		exporter.export(exportFile);
	}
	
	public void loadActivity(File activityFile) {
		LoadActivityData loadActivityData = new LoadActivityData();
		
		try {
			loadActivityData.load(activityFile);
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
