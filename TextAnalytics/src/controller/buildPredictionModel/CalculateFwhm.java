package controller.buildPredictionModel;

public class CalculateFwhm {
	public static double calculate(final Histogram histogram) {
		int[] binCounts = histogram.getBins();
		
		int maxVal = -1;
		int maxInd = 0;
		for (int i = 0; i < binCounts.length; i++) {
			if (binCounts[i] > maxVal) {
				maxVal = binCounts[i];
				maxInd = i;
			}
		}
		
		final double halfMaxVal = ((double)maxVal)/2.0;
		
		double fullWidth = halfMaxLocation(maxInd, halfMaxVal, 1, histogram);
		fullWidth -= halfMaxLocation(maxInd, halfMaxVal, -1, histogram);
		
		return fullWidth;
	}
	
	private static double halfMaxLocation(final int maxInd, final double halfMax, final int increment,
			Histogram histogram) {
		
		final int[] bins = histogram.getBins();

		final EdgeCondition edgeCond;
		if (increment > 0) {
			edgeCond = new EdgeCondition() {
				@Override
				public boolean test(int i) {
					return i < bins.length;
				}
			};
		} else {
			edgeCond = new EdgeCondition() {
				@Override
				public boolean test(int i) {
					return i >= 0;
				}
			};
		}
		
		Integer afterHalfMax = null;
		Integer beforeHalfMax = null;
		for (int i = maxInd; edgeCond.test(i); i += increment) {
			if (null == afterHalfMax && bins[i] < halfMax) {
				if (null == beforeHalfMax) {
					beforeHalfMax = i - increment;
				}
				afterHalfMax = i;
			}
			if (afterHalfMax != null && bins[i] > bins[afterHalfMax]) {
				if (bins[i] > halfMax || bins[i] >= bins[beforeHalfMax]) {
					afterHalfMax = null;
				} else {
					afterHalfMax = i;
				}
			}
		}

		return interpolatePosition(beforeHalfMax, afterHalfMax, halfMax, histogram);
	}
	
	private static double interpolatePosition(int index0, int index1, double targetCount, Histogram hist) {
		final double binCenter0 = calcBinCenter(index0, hist);

		final double binCenter1 = calcBinCenter(index1, hist);

		final int[] binCount = hist.getBins();
		final double count0 = (double)binCount[index0];
		final double count1 = (double)binCount[index1];
		
		final double slope = (count1 - count0) / (binCenter1 - binCenter0);
		final double intercept = count0 - (slope * binCenter0);
		
		return (targetCount - intercept) / slope;
	}
	
	private static double calcBinCenter(int index, Histogram hist) {
		final double result;
		if (0 == index || (hist.getNumBins()-1) == index) {
			result = Double.NaN;
		} else {
			final double[] binLim = hist.getBinLimits(index);
			result = (binLim[0] + binLim[1]) / 2.0;
		}
		
		return result;
	}
	
	private static interface EdgeCondition {
		public boolean test(final int i);
	}
}
