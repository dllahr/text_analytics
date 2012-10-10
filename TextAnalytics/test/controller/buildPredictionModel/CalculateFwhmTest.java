package controller.buildPredictionModel;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import controller.buildPredictionModel.CalculateFwhm;
import controller.buildPredictionModel.Histogram;

public class CalculateFwhmTest {
	
	

	@Test
	public void basic() {
		final double lowerLim = -3.5;
		final double upperLim = 3.5;
		final int numBins = 9;
		final int maxCount = 5;
		final Histogram hist = new Histogram(numBins, lowerLim, upperLim);
		
		final int middleBinIndex = numBins / 2;
		
		for (int i = 0; i < numBins; i++) {
			final double[] limits = hist.getBinLimits(i);
			final double pos = (limits[0] + limits[1]) / 2.0;
			
			final int count = maxCount - Math.abs(i - middleBinIndex);
			HistogramTest.addToBin(count, pos, hist);
			
//			System.out.println(limits[0] + " " + limits[1] + " " + hist.getBins()[i]);
		}

		final double fwhm = CalculateFwhm.calculate(hist);
		assertEquals(5.0, fwhm, 1e-10);
	}
	
	
	@Test
	public void notAtBinCenters() {
		final double lowerLim = -3.5;
		final double upperLim = 3.5;
		final int numBins = 9;
		final Histogram hist = new Histogram(numBins, lowerLim, upperLim);
		
		final double max = 100.0;
		
		for (int i = 0; i < numBins; i++) {
			final double[] limits = hist.getBinLimits(i);
			final double pos = (limits[0] + limits[1]) / 2.0;
			
			final int count = (int)(max*Math.exp(-(pos*pos/4.0)));
			HistogramTest.addToBin(count, pos, hist);
			
//			System.out.println(limits[0] + " " + limits[1] + " " + hist.getBins()[i]);
		}
		final double slope = (77.0 - 36.0) / (-1.0 - (-2.0));
		final double intercept = 36 - (-2.0*slope);
		final double negLoc = ((max/2.0) - intercept) / slope;
		final double expected = 2.0 * Math.abs(negLoc);
		
		final double fwhm = CalculateFwhm.calculate(hist);
		
		assertEquals(expected, fwhm, 1e-10);
	}

	@Test
	public void flatTop() {
		final double lowerLim = -3.0;
		final double upperLim = 3.0;
		final int numBins = 8;
		final Histogram hist = new Histogram(numBins, lowerLim, upperLim);
		
		List<Integer> countList = new ArrayList<>(numBins);
		
		for (int i = 0; i < numBins; i++) {
			final double[] limits = hist.getBinLimits(i);
			final double pos = (limits[0] + limits[1]) / 2.0;
			
			final int count = (int)(100.0*Math.exp(-(pos*pos/4.0)));
			HistogramTest.addToBin(count, pos, hist);
			
			countList.add(count);
			
//			System.out.println(limits[0] + " " + limits[1] + " " + hist.getBins()[i]);
		}
		final double max = (double)Collections.max(countList);
		final double slope = (56.0 - 20.0) / (-1.5 - (-2.5));
		final double intercept = 20 - (-2.5*slope);
		final double negLoc = ((max/2.0) - intercept) / slope;
		final double expected = 2.0 * Math.abs(negLoc);
		
		final double fwhm = CalculateFwhm.calculate(hist);
		
		assertEquals(expected, fwhm, 1e-10);
	}
	
	@Test
	public void offCenter() {
		final double lowerLim = -4.5;
		final double upperLim = 4.5;
		final int numBins = 11;
		final Histogram hist = new Histogram(numBins, lowerLim, upperLim);
		
		final double maxCoef = 100.0;
		final double offset = -2.2;
		
		final double[] posArray = new double[numBins];
		posArray[0] = lowerLim - 1.0;
		posArray[numBins-1] = upperLim + 1.0;
		for (int i = 1; i < numBins-1; i++) {
			final double[] limits = hist.getBinLimits(i);
			posArray[i] = ((limits[0] + limits[1]) / 2.0);
		}
		
		List<Integer> countList = new ArrayList<>(numBins);
		for (int i = 0; i < posArray.length; i++) {
			final double pos = posArray[i];
			
			final int count = (int)(maxCoef*Math.exp(-((pos-offset)*(pos-offset)/4.0)));
			HistogramTest.addToBin(count, pos, hist);
			
			countList.add(count);
			
//			System.out.println(posArray[i] + " " + count);
		}
		
		final double max = Collections.max(countList);
		
		final double slope0 = (85.0 - 44.0) / (-3.0 - (-4.0));
		final double intercept0 = 44.0 - (-4.0*slope0);
		final double negLoc = ((max/2.0) - intercept0) / slope0;
		
		final double slope1 = (29.0 - 69.0) / (0.0 - (-1.0));
		final double intercept1 = 69 - (-1.0*slope1);
		final double posLoc = ((max/2.0) - intercept1) / slope1;
		
//		System.out.println(negLoc + " " + posLoc);
		final double expected = posLoc - negLoc;
		
		final double fwhm = CalculateFwhm.calculate(hist);
		
		assertEquals(expected, fwhm, 1e-10);
	}
	
	@Test
	public void nonSimpleDecreaseThatDoesNotAffect() {
		final double lowerLim = -3.5;
		final double upperLim = 3.5;
		final int numBins = 9;
		final Histogram hist = new Histogram(numBins, lowerLim, upperLim);
		
		final double max = 100.0;
		
		HistogramTest.addToBin(25, 3.0, hist);
		
		double[] posArray = new double[numBins];
		posArray[0] = lowerLim - 1.0;
		posArray[numBins - 1] = upperLim + 1.0;
		for (int i = 1; i < (numBins-1); i++) {
			final double[] limits = hist.getBinLimits(i);
			posArray[i] = (limits[0] + limits[1]) / 2.0;
		}
		
		for (int i = 0; i < numBins; i++) {
			final double pos = posArray[i];
			
			final int count = (int)(max*Math.exp(-(pos*pos/4.0)));
			HistogramTest.addToBin(count, pos, hist);
			
//			System.out.println(pos + " " + hist.getBins()[i]);
		}
		final double slope = (77.0 - 36.0) / (-1.0 - (-2.0));
		final double intercept = 36 - (-2.0*slope);
		final double negLoc = ((max/2.0) - intercept) / slope;
		final double expected = 2.0 * Math.abs(negLoc);
		
		final double fwhm = CalculateFwhm.calculate(hist);
		
		assertEquals(expected, fwhm, 1e-10);
	}
	
	@Test
	public void nonSimpleDecreaseThatAffects1() {
		final double lowerLim = -3.5;
		final double upperLim = 3.5;
		final int numBins = 9;
		final Histogram hist = new Histogram(numBins, lowerLim, upperLim);
		
		final double max = 100.0;
		
		HistogramTest.addToBin(26, 3.0, hist);
		
		double[] posArray = new double[numBins];
		posArray[0] = lowerLim - 1.0;
		posArray[numBins - 1] = upperLim + 1.0;
		for (int i = 1; i < (numBins-1); i++) {
			final double[] limits = hist.getBinLimits(i);
			posArray[i] = (limits[0] + limits[1]) / 2.0;
		}
		
		for (int i = 0; i < numBins; i++) {
			final double pos = posArray[i];
			
			final int count = (int)(max*Math.exp(-(pos*pos/4.0)));
			HistogramTest.addToBin(count, pos, hist);
			
			System.out.println(pos + " " + hist.getBins()[i]);
		}
		final double slope0 = (77.0 - 36.0) / (-1.0 - (-2.0));
		final double intercept0 = 36 - (-2.0*slope0);
		final double negLoc = ((max/2.0) - intercept0) / slope0;
		
		final double slope1 = (36.0 - 77.0) / (2.0 - 1.0);
		final double intercept1 = 77.0 - (1.0*slope1);
		final double posLoc = ((max/2.0) - intercept1) / slope1;
		
		final double expected = posLoc - negLoc;
		
		final double fwhm = CalculateFwhm.calculate(hist);
		
		assertEquals(expected, fwhm, 1e-10);
	}
	
	@Test
	public void nonSimpleDecreaseThatAffects2() {
		final double lowerLim = -5.5;
		final double upperLim = 5.5;
		final int numBins = 13;
		final Histogram hist = new Histogram(numBins, lowerLim, upperLim);
		
		final double max = 100.0;
		
		HistogramTest.addToBin(41, 3.0, hist);
		HistogramTest.addToBin(7, 4.0, hist);
		
		double[] posArray = new double[numBins];
		posArray[0] = lowerLim - 1.0;
		posArray[numBins - 1] = upperLim + 1.0;
		for (int i = 1; i < (numBins-1); i++) {
			final double[] limits = hist.getBinLimits(i);
			posArray[i] = (limits[0] + limits[1]) / 2.0;
		}
		
		for (int i = 0; i < numBins; i++) {
			final double pos = posArray[i];
			
			final int count = (int)(max*Math.exp(-(pos*pos/4.0)));
			HistogramTest.addToBin(count, pos, hist);
			
//			System.out.println(pos + " " + hist.getBins()[i]);
		}
		final double slope0 = (77.0 - 36.0) / (-1.0 - (-2.0));
		final double intercept0 = 36 - (-2.0*slope0);
		final double negLoc = ((max/2.0) - intercept0) / slope0;
		
		final double slope1 = (8.0 - 77.0) / (4.0 - 1.0);
		final double intercept1 = 77.0 - (1.0*slope1);
		final double posLoc = ((max/2.0) - intercept1) / slope1;
		
		final double expected = posLoc - negLoc;
		
		final double fwhm = CalculateFwhm.calculate(hist);
		
		assertEquals(expected, fwhm, 1e-10);
	}
	
	
	@Test
	public void edgeProducesNan() {
		final double lowerLim = -4.5;
		final double upperLim = 4.5;
		final int numBins = 11;
		final Histogram hist = new Histogram(numBins, lowerLim, upperLim);
		
		final double maxCoef = 100.0;
		final double offset = 3.2;
		
		final double[] posArray = new double[numBins];
		posArray[0] = lowerLim - 1.0;
		posArray[numBins-1] = upperLim + 1.0;
		for (int i = 1; i < numBins-1; i++) {
			final double[] limits = hist.getBinLimits(i);
			posArray[i] = ((limits[0] + limits[1]) / 2.0);
		}
		
		List<Integer> countList = new ArrayList<>(numBins);
		for (int i = 0; i < posArray.length; i++) {
			final double pos = posArray[i];
			
			final int count = (int)(maxCoef*Math.exp(-((pos-offset)*(pos-offset)/4.0)));
			HistogramTest.addToBin(count, pos, hist);
			
			countList.add(count);
			
//			System.out.println(posArray[i] + " " + count);
		}
		
		final double fwhm = CalculateFwhm.calculate(hist);
		
		assertEquals(Double.NaN, fwhm, 1e-10);
	}
}
