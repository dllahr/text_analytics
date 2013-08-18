package controller.prediction.regressionModel;

import orm.Constants;
import orm.PredictionModel;



public class Prediction {
	private static final String priceDisplayPrecision = "%.5G";

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
		builder.append(String.format(priceDisplayPrecision, pricePercentile25)).append(Constants.toStringDelimeter);
		builder.append(String.format(priceDisplayPrecision, pricePercentile50)).append(Constants.toStringDelimeter);
		builder.append(String.format(priceDisplayPrecision, pricePercentile75)).append(Constants.toStringDelimeter);
		builder.append(predictionModel.getId()).append(Constants.toStringDelimeter);
		builder.append(String.format(priceDisplayPrecision, result));

		return builder.toString();
	}
}
