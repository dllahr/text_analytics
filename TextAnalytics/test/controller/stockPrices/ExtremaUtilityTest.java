package controller.stockPrices;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

import orm.StockData;

public class ExtremaUtilityTest {

	@Test
	public void testBuildIteratorWithDayIndexEqualsOrGreater() {
		List<StockData> l = new LinkedList<>();
		
		for (int i = 0; i < 10; i += 2) {
//			System.out.print(i + " ");
			StockData sd = new StockData();
			sd.setDayIndex(i);
			l.add(sd);
		}
//		System.out.println();
		
		for (int i = 0; i < 9; i++) {
			int expected = 2 * ((i+1)/2);
			ListIterator<StockData> iter = ExtremaUtility.buildIteratorWithDayIndexEqualsOrGreater(i, l);
			StockData sd = iter.next();
			
//			System.out.println(expected + " " + sd.getDayIndex());
			
			assertEquals((long)expected, (long)sd.getDayIndex());
		}
	}
	
	@Test(expected=RuntimeException.class)
	public void testBuildIteratorWithDayIndexEqualsOrGreaterException() {
		List<StockData> l = new LinkedList<>();
		l.add(new StockData());
		l.get(0).setDayIndex(0);
		ExtremaUtility.buildIteratorWithDayIndexEqualsOrGreater(1, l);
	}
}
