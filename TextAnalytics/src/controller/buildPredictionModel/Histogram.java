package controller.buildPredictionModel;

public class Histogram {
	
	private final int bins[];

	private final double limits[][];
	
	public Histogram(final int numBins, final double lowerLimit, 
			final double upperLimit) {
		
		bins = new int[numBins];
		limits = new double[numBins][2];
		
		final double increment = (upperLimit - lowerLimit) / ((double)numBins - 2);
		
		limits[0][0] = Double.NEGATIVE_INFINITY;
		limits[0][1] = lowerLimit;
		limits[numBins-1][0] = upperLimit;
		limits[numBins-1][1] = Double.POSITIVE_INFINITY;
		
		
		for (int i = 1; i < (numBins - 1); i++) {
			limits[i][0] = increment*(i-1) + lowerLimit;
			limits[i][1] = increment*(i) + lowerLimit;
		}
	}
	
	public void addData(double data) {
		boolean doContinue = true;
		int i = 0;
		while (doContinue && i < limits.length) {
			if (data >= limits[i][0] && data < limits[i][1]) {
				bins[i]++;
				doContinue = false;
			}
			i++;
		}
		
		if (doContinue) {
			bins[bins.length - 1]++;
		}
	}
	
	public int[] getBins() {
		int[] result = new int[bins.length];
		
		for (int i = 0; i < bins.length; i++) {
			result[i] = bins[i];
		}
		
		return result;
	}
	
	
	public double[] getBinLimits(final int binIndex) { 
		double[] result = new double[2];
		
		result[0] = limits[binIndex][0];
		result[1] = limits[binIndex][1];
		
		return result;
	}
	
	public int getNumBins() {
		return bins.length;
	}
}
