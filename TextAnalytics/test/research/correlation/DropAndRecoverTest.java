package research.correlation;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import orm.StockData;

public class DropAndRecoverTest {

	@Test
	public void testReadDayIndexes() throws IOException {
		List<Integer> result = DropAndRecover.readDayIndexes("c:/no_backup/temp/dayIndex.csv");
		
		assertNotNull(result);
		assertTrue(result.size() > 0);
		
		for (Integer dayIndex : result) {
			System.out.println(dayIndex);
		}
	}
	
	@Test
	public void testGetStockData() {
		List<StockData> result = DropAndRecover.getSortedStockData(1, 14000);
		
		assertNotNull(result);
		assertTrue(result.size() > 0);
		assertTrue(result.get(1).getDayIndex() > result.get(0).getDayIndex());
		
		for (int i = 0; i < 100; i++) {
			System.out.println(result.get(i));
		}
	}
	
	private static List<StockData> buildStockDataList() {
		List<StockData> sdList = new LinkedList<>();
		
		StockData sd1 = new StockData();
		sd1.setDayIndex(0);
		sdList.add(sd1);
		
		sd1 = new StockData();
		sd1.setDayIndex(3);
		sdList.add(sd1);
		
		return sdList;
	}

	
	@Test
	public void testGetStockDataAtOrAfterDayIndex() {
		List<StockData> sdList = buildStockDataList();
		Iterator<StockData> iter = sdList.iterator();
		
		StockData sd = DropAndRecover.getStockDataAtOrAfterDayIndex(2, iter);
		assertNotNull(sd);
		assertEquals(sdList.get(1).getDayIndex(), sd.getDayIndex());
		
		iter = sdList.iterator();
		sd = DropAndRecover.getStockDataAtOrAfterDayIndex(3, iter);
		assertNotNull(sd);
		assertEquals(sdList.get(1).getDayIndex(), sd.getDayIndex());
		
		iter = sdList.iterator();
		sd = DropAndRecover.getStockDataAtOrAfterDayIndex(4, iter);
		assertNull(sd);
		
		iter = sdList.iterator();
		while(iter.hasNext()) {iter.next();}
		sd = DropAndRecover.getStockDataAtOrAfterDayIndex(4, iter);
		assertNull(sd);
	}

}
