package controller.buildPredictionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import orm.ScoringModel;
import orm.StockData;
import orm.StockPriceChange;

public class CalculateStockStatistics {
	
	private static final int dayIndexOffsetIncrementDefault = 10;
	private static final int dayIndexOffsetMaxDefault = 160;
	
	private final int dayIndexOffsetIncrement;
	private final int dayIndexOffsetMax;
	
	private static final int numBinsDefault = 40;
	private static final double lowerLimitDefault = -0.8;
	private static final double upperLimitDefault = 0.8;

	private final int numBins;
	private final double lowerLimit;
	private final double upperLimit;
	
	private boolean doOutputHistogram;

	private final FindStockPrices findNextStockPrices;
	
	public CalculateStockStatistics(boolean doOutputHistogram, int minDayIndex, ScoringModel company) {
		this.doOutputHistogram = doOutputHistogram;
		
		this.dayIndexOffsetIncrement = dayIndexOffsetIncrementDefault;
		this.dayIndexOffsetMax = dayIndexOffsetMaxDefault;
		
		this.numBins = numBinsDefault;
		this.lowerLimit = lowerLimitDefault;
		this.upperLimit = upperLimitDefault;
		
		findNextStockPrices = new FindStockPrices(minDayIndex, company);
	}

	/**
	 * 
	 * @param dayIndexList  sorted list of day indexes to run calculation for
	 * @param company
	 * @return
	 */
	public List<StockPriceChange> doCalc(List<Integer> dayIndexList) {
				
		Map<Integer, StockData> dayIndexStockDataMap = findNextStockPrices.findNext(dayIndexList);
		
		List<StockPriceChange> stockPriceChangeList = new ArrayList<>(dayIndexList.size() * (int)(dayIndexOffsetMax / dayIndexOffsetIncrement));

		for (int dayIndexOffset = dayIndexOffsetIncrement; dayIndexOffset <= dayIndexOffsetMax; 
				dayIndexOffset += dayIndexOffsetIncrement) {
			
			List<Integer> dayIndexOffsetList = new ArrayList<>(dayIndexList.size());
			
			for (Integer dayIndex : dayIndexList) {
				dayIndexOffsetList.add(dayIndexOffset+dayIndex);
			}
			
			Map<Integer, StockData> dayIndexOffsetMap = findNextStockPrices.findNext(dayIndexOffsetList);
			
			Histogram hist = new Histogram(numBins, lowerLimit, upperLimit);
			
			int count = 0;
			double sum = 0.0;
			for (int i = 0; i < dayIndexList.size(); i++) {
				StockData offsetData = dayIndexOffsetMap.get(dayIndexOffsetList.get(i));
				if (offsetData != null && offsetData.getAdjustedClose() != null) {
					StockData data = dayIndexStockDataMap.get(dayIndexList.get(i));
					
					if (data != null && data.getAdjustedClose() != null) {
						double change = (offsetData.getAdjustedClose()/data.getAdjustedClose()) - 1.0;
						hist.addData(change);
						
						sum += change;
						count++;
					}
				}
			}
			
			final double average = sum / ((double)count);
			
			final double fwhm = CalculateFwhm.calculate(hist);
			if (doOutputHistogram) {
				outputHistogram(hist, dayIndexOffset);
			}
			
			StockPriceChange spc = new StockPriceChange();
			spc.setDayOffset((int)dayIndexOffset);
			spc.setAverage(average);
			spc.setFwhm(fwhm);
			
			stockPriceChangeList.add(spc);
		}
		
		return stockPriceChangeList;
	}

	private static void outputHistogram(Histogram hist, long dayOffset) {
		int[] bins = hist.getBins();
		
		double[] binLims = hist.getBinLimits(0);
		System.out.println(dayOffset + ", " + binLims[1] + ", " + bins[0]);
		
		for (int i = 1; i < (bins.length-1); i++) {
			binLims = hist.getBinLimits(i);
			final double binCenter = (binLims[0] + binLims[1]) / 2.0;
			System.out.println(dayOffset + ", " + binCenter + ", " + bins[i]);
		}
		
		binLims = hist.getBinLimits(bins.length-1);
		System.out.println(dayOffset + ", " + binLims[0] + ", " + bins[bins.length-1]);
	}

	public long getDayIndexOffsetIncrement() {
		return dayIndexOffsetIncrement;
	}

	public long getDayIndexOffsetMax() {
		return dayIndexOffsetMax;
	}

	public int getNumBins() {
		return numBins;
	}

	public double getLowerLimit() {
		return lowerLimit;
	}

	public double getUpperLimit() {
		return upperLimit;
	}

	public boolean isDoOutputHistogram() {
		return doOutputHistogram;
	}

	public void setDoOutputHistogram(boolean doOutputHistogram) {
		this.doOutputHistogram = doOutputHistogram;
	}

}
