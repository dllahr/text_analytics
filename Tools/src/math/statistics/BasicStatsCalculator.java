package math.statistics;

public class BasicStatsCalculator {
	public static BasicStats calc(double[] values) {
		double sum = 0.0;
		double sumSq = 0.0;
		
		for (double val : values) {
			sum += val;
			sumSq += val*val;
		}
		
		double ave = sum / values.length;
		
		double sdev = Math.sqrt((sumSq/values.length) - (ave*ave));
		
		return new BasicStats(ave, sdev);
	}
}
