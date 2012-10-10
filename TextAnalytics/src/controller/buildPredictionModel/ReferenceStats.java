package controller.buildPredictionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.hibernate.Query;

import controller.util.Utilities;

import orm.Company;
import orm.SessionManager;
import orm.StockPriceChange;
import orm.StockPriceChangeCalculation;

public class ReferenceStats {
	private static final String companyParam = "company";
	
	private static final String minMaxDayIndexQueryStr = "select max(dayIndex), min(dayIndex) " +
			"from Article where company=(:" + companyParam + ")";
	
	private static final int minDayIndexCol = 1;
	private static final int maxDayIndexcol = 0;
	
	private static final int daySpacing = 5;
	
	
	
	public static void calcStats(Company company) {
		Query query = SessionManager.createQuery(minMaxDayIndexQueryStr);
		query.setParameter(companyParam, company);

		Object[] minMaxDayIndexRow = (Object[]) query.list().get(0);
		
		int minDayIndex = (int)minMaxDayIndexRow[minDayIndexCol];
		int maxDayIndex = (int)minMaxDayIndexRow[maxDayIndexcol];
		
		int numDays = (int)((maxDayIndex - minDayIndex) / daySpacing);
		
		Random rand = new Random();
		
		List<Integer> dayIndexList = new ArrayList<Integer>(numDays);
		for (int i = 0; i < numDays; i++) {
			int dayIndex = minDayIndex + (i*daySpacing);
			dayIndex += (rand.nextInt(2*daySpacing)) - daySpacing;
			
			dayIndexList.add(dayIndex);
		}
		
		
		StockPriceChangeCalculation spcc = new StockPriceChangeCalculation();
		spcc.setCompany(company);
		spcc.setId(Utilities.getMaxId("StockPriceChangeCalculation")+1);
		
		spcc.setLowerStockDayIndex(minDayIndex);
		spcc.setUpperStockDayIndex(maxDayIndex);
		spcc.setNumSamples(numDays);
		
		CalculateStockStatistics calcStockStats = new CalculateStockStatistics(false, dayIndexList.get(0), company);
		spcc.setHistogramRangeLower(calcStockStats.getLowerLimit());
		spcc.setHistogramRangeUpper(calcStockStats.getUpperLimit());
		spcc.setNumBins(calcStockStats.getNumBins());
		
		SessionManager.persist(spcc);
		
		List<StockPriceChange> spcList = calcStockStats.doCalc(dayIndexList);
		
		int id = Utilities.getMaxId("StockPriceChange") + 1;
		for (StockPriceChange spc : spcList) {
			spc.setStockPriceChangeCalculation(spcc);
			spc.setEigenvalue(null);
			spc.setId(id);
			
			id++;
			
			SessionManager.persist(spc);
		}
		
		SessionManager.commit();
	}
}
