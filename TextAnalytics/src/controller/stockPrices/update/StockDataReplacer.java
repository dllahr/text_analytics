package controller.stockPrices.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;

import org.hibernate.Query;

import orm.Company;
import orm.SessionManager;
import orm.StockData;

public class StockDataReplacer extends StockDataBuilder {
	
	/**
	 * expected format in reader:
Date,Open,High,Low,Close,Volume,Adj Close
2013-07-11,29.60,30.20,29.45,30.17,8894500,30.17

	 * @param companyId
	 * @param stockDataReader
	 * @throws IOException
	 * @throws ParseException
	 */
	public void replaceStockData(int companyId, BufferedReader stockDataReader) throws IOException, ParseException {
		deleteExistingStockDataForCompany(companyId);
		
		Company company = Company.findById(companyId);
		
		int count = 0;
		
		String curLine;
		while ((curLine = stockDataReader.readLine()) != null) {
			String[] lineArray = curLine.split(InputFormatConstants.delimeter);
			
			StockData stockData = new StockData();
			stockData.setCompany(company);
			stockData.setDayTimeAndDayIndex(parseDate(lineArray));
			
			updateStockDataFieldsFromLine(lineArray, stockData);
			
			SessionManager.persist(stockData);

			count++;
			
			if (count%1000 == 0) {
				System.out.println("Commiting stock data.  count:  " + count);
				SessionManager.commit();
			}
		}
		
		if (count%1000 != 0) {
			System.out.println("Commiting stock data.  count:  " + count);
			SessionManager.commit();
		}
	}
	
	static void deleteExistingStockDataForCompany(int companyId) {
		Query query = SessionManager.createQuery("delete StockData where company.id = :companyId");
		query.setInteger("companyId", companyId);
		
		int numDeleted = query.executeUpdate();
		SessionManager.commit();
		System.out.println("deleted StockData for company " + companyId + "  deleted number of entries:  " + numDeleted);
	}

}
