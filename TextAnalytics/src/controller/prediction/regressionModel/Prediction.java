package controller.prediction.regressionModel;

import orm.Constants;
import orm.PredictionModel;



public class Prediction {
	public final int initialDayIndex;
	
	public final int predictionDayIndex;
	
	public final double pricePercentile25;
	public final double pricePercentile50;
	public final double pricePercentile75;
	
	public Double result;
	
	public final PredictionModel predictionModel;
	
	public Prediction(int initialDayIndex, int predictionDayIndex, double pricePercentile25, double pricePercentile50,
			double pricePercentile75, PredictionModel predictionModel) {

		this.initialDayIndex = initialDayIndex;
		this.predictionDayIndex = predictionDayIndex;
		
		this.pricePercentile25 = pricePercentile25;
		this.pricePercentile50 = pricePercentile50;
		this.pricePercentile75 = pricePercentile75;
		
		this.predictionModel = predictionModel;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(initialDayIndex).append(Constants.toStringDelimeter);
		builder.append(predictionDayIndex).append(Constants.toStringDelimeter);
		builder.append(pricePercentile25).append(Constants.toStringDelimeter);
		builder.append(pricePercentile50).append(Constants.toStringDelimeter);
		builder.append(pricePercentile75).append(Constants.toStringDelimeter);
		builder.append(predictionModel.getId()).append(Constants.toStringDelimeter);
		builder.append(result);

		return builder.toString();
	}
}
