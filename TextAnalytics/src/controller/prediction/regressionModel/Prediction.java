package controller.prediction.regressionModel;

import orm.Constants;
import orm.PredictionModel;



public class Prediction {
	private static final String priceDisplayPrecision = "%.5G";

	public final int initialDayIndex;
	public final double initialPrice;
	
	public final int predictionDayIndex;
	
	public final double percentile25;
	public final double percentile50;
	public final double percentile75;
	
	public Double result;
	
	public final PredictionModel predictionModel;
	
	public Prediction(int initialDayIndex, double initialPrice, int predictionDayIndex, 
			double percentile25, double percentile50, double percentile75, PredictionModel predictionModel) {

		this.initialDayIndex = initialDayIndex;
		this.initialPrice = initialPrice;
		
		this.predictionDayIndex = predictionDayIndex;
		
		this.percentile25 = percentile25;
		this.percentile50 = percentile50;
		this.percentile75 = percentile75;
		
		this.predictionModel = predictionModel;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(initialDayIndex).append(Constants.toStringDelimeter);
		builder.append(String.format(priceDisplayPrecision, initialPrice)).append(Constants.toStringDelimeter);
		builder.append(predictionDayIndex).append(Constants.toStringDelimeter);
		builder.append(String.format(priceDisplayPrecision, percentile25)).append(Constants.toStringDelimeter);
		builder.append(String.format(priceDisplayPrecision, percentile50)).append(Constants.toStringDelimeter);
		builder.append(String.format(priceDisplayPrecision, percentile75)).append(Constants.toStringDelimeter);
		builder.append(predictionModel.getId()).append(Constants.toStringDelimeter);
		builder.append(String.format(priceDisplayPrecision, result));

		return builder.toString();
	}
	
	public static String toStringHeader() {
		StringBuilder builder = new StringBuilder();
		builder.append("initialDayIndex").append(Constants.toStringDelimeter);
		builder.append("initialPrice").append(Constants.toStringDelimeter);
		builder.append("predictionDayIndex").append(Constants.toStringDelimeter);
		builder.append("percentile25").append(Constants.toStringDelimeter);
		builder.append("percentile50").append(Constants.toStringDelimeter);
		builder.append("percentile75").append(Constants.toStringDelimeter);
		builder.append("predictionModelId").append(Constants.toStringDelimeter);
		builder.append("result").append(Constants.toStringDelimeter);
		return builder.toString();
	}
}
