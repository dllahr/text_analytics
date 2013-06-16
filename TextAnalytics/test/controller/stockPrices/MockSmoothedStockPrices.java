package controller.stockPrices;

import java.util.Map;

public class MockSmoothedStockPrices extends SmoothedStockPrices {
	public MockSmoothedStockPrices(Map<Integer, Double> dayIndexSmoothedMap) {
		super(dayIndexSmoothedMap);
	}
}
