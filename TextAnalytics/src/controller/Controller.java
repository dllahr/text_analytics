package controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import orm.RegressionModel;

import controller.activity.LoadActivityData;
import controller.regressionPrediction.RegressionModelPredictor;
import controller.regressionPrediction.RegressionPredictionData;

public class Controller {

	
	private final RegressionModelPredictor regressionModelPredictor;
	
	public Controller() {
		regressionModelPredictor = new RegressionModelPredictor();
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
