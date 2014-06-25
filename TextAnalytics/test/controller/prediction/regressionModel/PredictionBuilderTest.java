package controller.prediction.regressionModel;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import controller.stockPrices.MockSmoothedStockPrices;
import controller.stockPrices.SmoothedStockPrices;

import orm.PredictionModel;
import orm.PredictionModelStockSmoothingCoef;

public class PredictionBuilderTest {
	private static final double eps = 1e-5;

	@Test
	public void testFilterRawPredictions() {

		List<DayIndexRawPredictionPair> list = new LinkedList<>();
		DayIndexRawPredictionPair p8 = new DayIndexRawPredictionPair(11, 13, 0.8);
		list.add(p8);
		DayIndexRawPredictionPair p9 = new DayIndexRawPredictionPair(8, 10, 0.9);
		list.add(p9);
		DayIndexRawPredictionPair p10 = new DayIndexRawPredictionPair(5, 7, 1.0);
		list.add(p10);
		
		PredictionModel pm = new PredictionModel();
		pm.setLowerThreshold(0.85);

		List<DayIndexRawPredictionPair> result = PricePredictionBuilder.filterRawPredictions(list, pm);
		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.contains(p9));
		assertTrue(result.contains(p10));
		
		pm.setUpperThreshold(0.95);
		result = PricePredictionBuilder.filterRawPredictions(list, pm);
		assertNotNull(result);
		assertEquals(1, result.size());
		assertEquals(p9, result.get(0));
		
		pm.setLowerThreshold(null);
		result = PricePredictionBuilder.filterRawPredictions(list, pm);
		assertNotNull(result);
		assertEquals(2, result.size());
		assertTrue(result.contains(p8));
		assertTrue(result.contains(p9));
	}

	@Test
	public void testDetermineParams() {
		List<PredictionModelStockSmoothingCoef> coefList = new LinkedList<>();
		coefList.add(new PredictionModelStockSmoothingCoef(null, 1, 3.3));
		coefList.add(new PredictionModelStockSmoothingCoef(null, -3, 1.1));
		coefList.add(new PredictionModelStockSmoothingCoef(null, 3, 4.4));
		coefList.add(new PredictionModelStockSmoothingCoef(null, -1, 2.2));
		
		PricePredictionBuilder.SmoothedStockPriceParams params = PricePredictionBuilder.determineParams(coefList, 100);
		assertNotNull(params);
		assertNotNull(params.weights);
		assertEquals(7, params.weights.length);
		assertEquals(1.1, params.weights[0], eps);
		assertEquals(2.2, params.weights[2], eps);
		assertEquals(3.3, params.weights[4], eps);
		assertEquals(4.4, params.weights[6], eps);
		
		assertEquals(100 - 3 - 7, params.minDayIndex);
	}
	
	@Test
	public void testBuildPredictions() {
		PredictionModel pm = new PredictionModel();
		pm.setPercentile25Value(0.98);
		pm.setPercentile50Value(1.01);
		pm.setPercentile75Value(1.05);
		
		List<DayIndexRawPredictionPair> rawList = new LinkedList<>();
		rawList.add(new DayIndexRawPredictionPair(1, 3, Double.NaN));
		rawList.add(new DayIndexRawPredictionPair(4, 6, Double.NaN));
		rawList.add(new DayIndexRawPredictionPair(10, 12, Double.NaN));
		
		Map<Integer, Double> priceMap = new HashMap<Integer, Double>();
		priceMap.put(1, 200.0);
		priceMap.put(4, 300.0);
		priceMap.put(10, 500.0);
		SmoothedStockPrices smoothedStockPrices = new MockSmoothedStockPrices(priceMap);
		
		List<Prediction> list = PricePredictionBuilder.buildPredictions(smoothedStockPrices, rawList, pm);
		assertNotNull(list);
		assertEquals(3, list.size());
		
		Prediction pred = list.get(0);
		assertEquals(1, pred.initialDayIndex);
		assertEquals(3, pred.predictionDayIndex);
		assertEquals(200.0*0.98, pred.percentile25, eps);
		assertEquals(200.0*1.01, pred.percentile50, eps);
		assertEquals(200.0*1.05, pred.percentile75, eps);
		
		pred = list.get(1);
		assertEquals(4, pred.initialDayIndex);
		assertEquals(6, pred.predictionDayIndex);
		assertEquals(300.0*0.98, pred.percentile25, eps);
		assertEquals(300.0*1.01, pred.percentile50, eps);
		assertEquals(300.0*1.05, pred.percentile75, eps);
		
		pred = list.get(2);
		assertEquals(10, pred.initialDayIndex);
		assertEquals(12, pred.predictionDayIndex);
		assertEquals(500.0*0.98, pred.percentile25, eps);
		assertEquals(500.0*1.01, pred.percentile50, eps);
		assertEquals(500.0*1.05, pred.percentile75, eps);
	}
}
