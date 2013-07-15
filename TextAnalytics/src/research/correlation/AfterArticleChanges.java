package research.correlation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import orm.StockData;

public class AfterArticleChanges {
	
	public static void main(String[] args) throws IOException {
		System.out.println("start DropAndRecoverAllOffset");
		
		final int companyId = Integer.valueOf(args[0]);
		System.out.println("companyId:  " + companyId);
		final String dateIndexFilename = args[1];
		System.out.println("dateIndexFilename:  " + dateIndexFilename);
		final int endDayOffset = Integer.valueOf(args[2]);
		System.out.println("endDayOffset:  " + endDayOffset);
		
		List<Integer> dayIndexes = DropAndRecover.readDayIndexes(dateIndexFilename);
		
		List<StockData> stockDataList = DropAndRecover.getSortedStockData(companyId, Collections.min(dayIndexes));
		
		Map<Integer, List<Double>> offsetFracChangeList = new HashMap<Integer, List<Double>>();
		
		for (int dayIndex : dayIndexes) {
			final int endDayIndex = dayIndex + endDayOffset;

			StockData end = DropAndRecover.getStockDataAtOrAfterDayIndex(endDayIndex, stockDataList.iterator());
			
			Iterator<StockData> iter = stockDataList.iterator();
			StockData current = DropAndRecover.getStockDataAtOrAfterDayIndex(dayIndex, iter);

			boolean doContinue = true;

			while (doContinue && current.getDayIndex() < endDayIndex) {

				final int curOffset = current.getDayIndex() - dayIndex;
				
				List<Double> fracChange = offsetFracChangeList.get(curOffset);
				if (null == fracChange) {
					fracChange = new LinkedList<>();
					offsetFracChangeList.put(curOffset, fracChange);
				}
				
				fracChange.add(end.getAdjustedClose() / current.getAdjustedClose());
				
				if (iter.hasNext()) {
					current = iter.next();
				} else {
					doContinue = false;
				}
			}
		}
		
		List<Integer> sortedOffsets = new ArrayList<>(offsetFracChangeList.keySet());
		Collections.sort(sortedOffsets);
		for (Integer offset : sortedOffsets) {
			List<Double> fracChanges = offsetFracChangeList.get(offset);
			Collections.sort(fracChanges);
			
			final double quarter = fracChanges.get(fracChanges.size() / 4);
			final double median = fracChanges.get(fracChanges.size() / 2);
			final double threeQuarters = fracChanges.get((3 * fracChanges.size()) / 4);
			
			System.out.println(offset + "\t" + fracChanges.size() + "\t" + quarter + "\t" + median + "\t" + threeQuarters);
		}
		
		System.out.println("start DropAndRecoverAllOffset");
	}
}
