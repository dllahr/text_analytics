package controller.buildPredictionModel;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import controller.buildPredictionModel.Histogram;

public class HistogramTest {
	private static final double doubleCompareDelta = 1e-10;
	
	private static final int numTests = 100;
	
	private Random rand;
	
	@Before
	public void setup() {
		rand = new Random();
	}


	@Test
	public void limitTest() {
		for (int i = 0; i < numTests; i++) {
			final double lowerLim = -20.0*rand.nextDouble() - 1.0;
			final double upperLim = 20*rand.nextDouble() + 1.0;
			final int numBins = 3*rand.nextInt(5) + 7;

			final Histogram hist = new Histogram(numBins, lowerLim, upperLim);

			final double upperLimitLowestBin = hist.getBinLimits(0)[1];
			assertEquals(upperLimitLowestBin, hist.getBinLimits(1)[0], doubleCompareDelta);

			final double lowerLimitHighestBin = hist.getBinLimits(numBins - 1)[0];
			String message = "hist conditions " + lowerLim + " " + upperLim + " " + numBins;
			assertEquals(message, lowerLimitHighestBin, hist.getBinLimits(numBins - 2)[1], doubleCompareDelta);
		}
	}
	
	@Test
	public void countTestLower() {
		for (int i = 0; i < numTests; i++) {
			final double lowerLim = -20.0*rand.nextDouble() - 1.0;
			final double upperLim = 20*rand.nextDouble() + 1.0;
			int numBins = 3*rand.nextInt(5) + 7;

			final Histogram hist = new Histogram(numBins, lowerLim, upperLim);
			String message = "hist conditions " + lowerLim + " " + upperLim + " " + numBins;
			
			final int numVals = rand.nextInt(17) + 3;
			addToBin(numVals, lowerLim - 1.0, hist);
			assertEquals(message + " " + numVals, numVals, hist.getBins()[0]);
		}
	}
	
	@Test
	public void countTestUpper() {
		for (int i = 0; i < numTests; i++) {
			final double lowerLim = -20.0*rand.nextDouble() - 1.0;
			final double upperLim = 20*rand.nextDouble() + 1.0;
			int numBins = 3*rand.nextInt(5) + 7;

			final Histogram hist = new Histogram(numBins, lowerLim, upperLim);
			String message = "hist conditions " + lowerLim + " " + upperLim + " " + numBins;

			final int numVals = rand.nextInt(17) + 3;
			addToBin(numVals, upperLim + 1.0, hist);
			assertEquals(message + " " + numVals, numVals, hist.getBins()[numBins - 1]);
		}
	}
	
	@Test
	public void countTestMiddle() {
		for (int i = 0; i < numTests; i++) {
			final double lowerLim = -20.0*rand.nextDouble() - 1.0;
			final double upperLim = 20*rand.nextDouble() + 1.0;
			int numBins = 3*rand.nextInt(5) + 7;

			final Histogram hist = new Histogram(numBins, lowerLim, upperLim);
			String message = "hist conditions " + lowerLim + " " + upperLim + " " + numBins;
			
			final int numVals = rand.nextInt(17) + 3;
			final int middleBinIndex = numBins / 2;
			final double[] middleLimits = hist.getBinLimits(middleBinIndex);
			final double middleValue = (middleLimits[0] + middleLimits[1]) / 2.0;
			addToBin(numVals, middleValue, hist);
			assertEquals(message + " " + numVals, numVals, hist.getBins()[middleBinIndex]);
		}
	}
	
	static void addToBin(final int count, final double value, final Histogram hist) {
		for (int i = 0; i < count; i++) {
			hist.addData(value);
		}
	}
}
