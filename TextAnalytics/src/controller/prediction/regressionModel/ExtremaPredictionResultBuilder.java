package controller.prediction.regressionModel;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Query;

import orm.Company;
import orm.SessionManager;
import orm.StockData;
import controller.stockPrices.ExtremaUtility;
import controller.util.Utilities;

public class ExtremaPredictionResultBuilder implements PredictionResultBuilder {

	@Override
	public void build(List<Prediction> predictionList) {
		List<StockData> stockDataList = getStockData(predictionList.get(0).predictionModel.getRegressionModel().getCompany(), 
				predictionList.get(0).initialDayIndex);
		
		for (Prediction pred : predictionList) {
			final int minDay = pred.initialDayIndex + 1;
			final int maxDay = pred.predictionDayIndex;
			
			Iterator<StockData> iter = ExtremaUtility.buildIteratorWithDayIndexEqualsOrGreater(minDay, stockDataList);
			
			StockData[] minLowMaxHigh = ExtremaUtility.findMinLowAndMaxHigh(minDay, maxDay, iter);
			
			StockData maxHigh = minLowMaxHigh[1];
			final double adjustment = maxHigh.getAdjustedClose() / maxHigh.getClose();
			
			pred.result = adjustment * maxHigh.getHigh();
		}

	}
	
	static List<StockData> getStockData(Company c, int minDayIndex) {
		Query q = SessionManager.createQuery("from StockData where company = :company and dayIndex >= :minDayIndex order by dayIndex");
		q.setParameter("company", c);
		q.setParameter("minDayIndex", minDayIndex);
		
		return Utilities.convertGenericList(q.list());
	}
}
