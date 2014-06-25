package controller.stockPrices;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import orm.StockData;

public class ExtremaUtility {
	public static ListIterator<StockData> buildIteratorWithDayIndexEqualsOrGreater(int dayIndex, List<StockData> sdList) {
		ListIterator<StockData> iter = sdList.listIterator();
		StockData cur = iter.next();
		
		while (cur.getDayIndex() < dayIndex) {
			if (iter.hasNext()) {
				cur = iter.next();
			} else {
				throw new RuntimeException("IteratorBuilder buildIteratorWithDayIndexEqualsOrGreater ran out of items in sdList before reaching dayIndex");
			}
		}
		iter.previous();
		
		return iter;
	}
	
	public static StockData[] findMinLowAndMaxHigh(int minDay, int maxDay, Iterator<StockData> iter) {
		
		StockData minLow = new StockData();
		minLow.setLow(Double.POSITIVE_INFINITY);
		
		StockData maxHigh = new StockData();
		maxHigh.setHigh(Double.NEGATIVE_INFINITY);

		StockData sd;
		while (iter.hasNext() && (sd = iter.next()).getDayIndex() <= maxDay) {
			if (sd.getDayIndex() >= minDay) {
				if (sd.getHigh() >= maxHigh.getHigh()) {
					maxHigh = sd;
				}
				if (sd.getLow() <= minLow.getLow()) {
					minLow = sd;
				}
			}
		}
		
		return new StockData[]{minLow, maxHigh};
	}
}
