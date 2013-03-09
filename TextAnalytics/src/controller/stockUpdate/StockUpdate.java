package controller.stockUpdate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import controller.util.Utilities;

import orm.Company;
import orm.SessionManager;
import orm.StockData;

public class StockUpdate {

	private static final String delimeter = ",";
	private static final int dateCol = 0;
	private static final int openCol = 1;
	private static final int highCol = 2;
	private static final int lowCol = 3;
	private static final int closeCol = 4;
	private static final int volumeCol = 5;
	private static final int adjCloseCol = 6;
	

	public void updateStockPrices() throws ParseException {
		Map<Company, Date> companyLastStockDateMap = retrieveCompanyList();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		int totalCount = 0;
		for (Company company : companyLastStockDateMap.keySet()) {
			System.out.print("retrieving data for " + company.getStockSymbol());
			Date lastDate = companyLastStockDateMap.get(company);
			
			DownloadStockData downloadStockData = new DownloadStockData(company.getStockSymbol());

			//skip header - should process this to identify column locations
			downloadStockData.next();

			int count = 0;
			boolean doContinue = true;
			while (downloadStockData.hasNext() && doContinue) {
				String[] lineArray = downloadStockData.next().split(delimeter);
				Date curDate = dateFormat.parse(lineArray[dateCol]);
				if (curDate.after(lastDate)) {
					StockData stockData = new StockData();
					stockData.setCompany(company);
					stockData.setDayTimeAndDayIndex(curDate);

					stockData.setOpen(Double.valueOf(lineArray[openCol]));
					stockData.setHigh(Double.valueOf(lineArray[highCol]));
					stockData.setLow(Double.valueOf(lineArray[lowCol]));
					stockData.setClose(Double.valueOf(lineArray[closeCol]));
					stockData.setVolume(Long.valueOf(lineArray[volumeCol]));
					stockData.setAdjustedClose(Double.valueOf(lineArray[adjCloseCol]));
					SessionManager.persist(stockData);
					count++;
				} else {
					doContinue = false;
				}
				
			}
			System.out.println(" " + count);
			totalCount += count;
		}
		
		if (totalCount > 0) {
			System.out.println("committing stock prices updates");
			SessionManager.commit();
		} else {
			System.out.println("no new data found, nothing to commit");
		}
	}

	private static Map<Company, Date> retrieveCompanyList() {
		List<Company> companyList = Utilities.convertGenericList(SessionManager.createQuery("from Company").list());
		Map<Company, Date> companyLastStockDateMap = new HashMap<>();
		for (Company company : companyList) {
			Query query = SessionManager.createQuery("select max(dayTime) from StockData where company=:company");
			query.setParameter("company", company);
			
			@SuppressWarnings("rawtypes")
			List dayTime = query.list();
			final Date result;
			if (dayTime != null && dayTime.size() > 0 && dayTime.get(0) != null) {
				result = (Date)dayTime.get(0);
			} else {
				result = new Date(0);
			}
			companyLastStockDateMap.put(company, result);
		}
		return companyLastStockDateMap;
	}
}
