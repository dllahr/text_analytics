package controller.legacy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import controller.util.Utilities;

import orm.ScoringModel;
import orm.Eigenvalue;
import orm.SessionManager;
import orm.StockPriceChange;
import orm.StockPriceChangeCalculation;

public class LoadDistributionCalc {
	private static final String delimeter = ",";
	
	public static void loadDistributionCalc(File dataFile, ScoringModel company) {
		try {
			doWork(dataFile, company);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void doWork(File dataFile, final ScoringModel company) throws IOException {
		StockPriceChangeCalculation calc = createNewStockPriceChangeCalculation(company);

		Integer id = Utilities.getMaxId("StockPriceChange");
		if (null == id) {
			id = 1;
		} else {
			id++;
		}
		
		Integer eigValId = Utilities.getMaxId("Eigenvalue");
		if (null == eigValId) {
			eigValId = 1;
		} else {
			eigValId++;
		}
		Eigenvalue eigenvalue = null;
		
		BufferedReader reader = new BufferedReader(new FileReader(dataFile));

		String curLine;
		while ((curLine = reader.readLine()) != null) {
			String[] lineArray = curLine.split(delimeter);
			
			StockPriceChange change = new StockPriceChange();
			change.setId(id);
			id++;
			change.setStockPriceChangeCalculation(calc);
			
			int eigSortIndex = Integer.valueOf(lineArray[0]);
			if (null == eigenvalue || eigenvalue.getSortIndex() != eigSortIndex) {
				eigenvalue = new Eigenvalue();
				eigenvalue.setId(eigValId);
				eigenvalue.setScoringModel(company);
				eigenvalue.setSortIndex(eigSortIndex);
				SessionManager.persist(eigenvalue);
				eigValId++;
			}
			change.setEigenvalue(eigenvalue);
			
			change.setDayOffset(Integer.valueOf(lineArray[1]));
			change.setAverage(Double.valueOf(lineArray[2]));
			change.setFwhm(Double.valueOf(lineArray[3]));
			SessionManager.persist(change);
		}
		
		reader.close();
		
		SessionManager.commit();
	}
	
	private static StockPriceChangeCalculation createNewStockPriceChangeCalculation(ScoringModel company) {
		Integer id = Utilities.getMaxId("StockPriceChangeCalculation");
		if (null == id) {
			id = 1;
		} else {
			id++;
		}
		
		StockPriceChangeCalculation result = new StockPriceChangeCalculation();
		result.setId(id);
		result.setCompany(company);
		SessionManager.persist(result);
		return result;
	}
}
