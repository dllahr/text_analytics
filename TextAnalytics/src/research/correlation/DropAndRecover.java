package research.correlation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;

import controller.util.Utilities;

import orm.SessionManager;
import orm.StockData;

public class DropAndRecover {
	public static void main (String[] args) throws IOException {
		System.out.println("start DropAndRecover");
		
		final int companyId = Integer.valueOf(args[0]);
		System.out.println("companyId:  " + companyId);
		final String dateIndexFilename = args[1];
		System.out.println("dateIndexFilename:  " + dateIndexFilename);
		final int dropDayOffset = Integer.valueOf(args[2]);
		System.out.println("dropDayOffset:  " + dropDayOffset);
		final int endDayOffset = Integer.valueOf(args[3]);
		System.out.println("endDayOffset:  " + endDayOffset);
		final double dropThreshold = Double.valueOf(args[4]);
		System.out.println("dropThreshold:  " + dropThreshold);
		
		List<Integer> dayIndexes = readDayIndexes(dateIndexFilename);
		
		List<StockData> stockDataList = getSortedStockData(companyId, Collections.min(dayIndexes));
		
		for (int dayIndex : dayIndexes) {
			final int dropDayIndex = dayIndex + dropDayOffset;
			final int endDayIndex = dayIndex + endDayOffset;
			
			Iterator<StockData> iter = stockDataList.iterator();
			
			StockData initial = getStockDataAtOrAfterDayIndex(dayIndex, iter);

			final double dropPrice = dropThreshold * initial.getAdjustedClose();

			StockData drop = getStockDataAtOrAfterDayIndex(dropDayIndex, iter);

			if (drop.getAdjustedClose() <= dropPrice) {
				StockData end = getStockDataAtOrAfterDayIndex(endDayIndex, iter);
				System.out.println(end.getAdjustedClose() / drop.getAdjustedClose());
			}

		}
		
		System.out.println("end DropAndRecover");
	}
	
	static StockData getStockDataAtOrAfterDayIndex(final int targetDayIndex, Iterator<StockData> iter) {
		StockData result = null;
		int currentDayIndex = targetDayIndex - 1;
		
		while (iter.hasNext() && currentDayIndex < targetDayIndex) {
			result = iter.next();
			currentDayIndex = result.getDayIndex();
		}
		
		if (currentDayIndex < targetDayIndex) {
			return null;
		} else {
			return result;
		}
	}

	
	static List<StockData> getSortedStockData(int companyId, int startDayIndex) {
		Query query = SessionManager.createQuery("from StockData where company.id = :companyId and dayIndex >= :dayIndex order by dayIndex");
		query.setInteger("companyId", companyId);
		query.setInteger("dayIndex", startDayIndex);
		
		return Utilities.convertGenericList(query.list());
	}
	
	static List<Integer> readDayIndexes(String filename) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(filename));
		
		List<Integer> result = new LinkedList<>();
		
		String curLine;
		while ((curLine = reader.readLine()) != null) {
			result.add(Integer.valueOf(curLine));
		}
		
		reader.close();
		
		return result;
	}
	
	
}
