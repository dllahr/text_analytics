package controller.stockPrices.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;

import controller.stockPrices.update.AddOrReplaceInfo.AddOrReplace;
import controller.util.Utilities;

import orm.SessionManager;
import orm.StockData;

public class AddOrReplaceDeterminer {
	
	private static final double eps = 1e-4;
	
	public AddOrReplaceInfo determine(int companyId, BufferedReader stockDataReader) throws IOException, ParseException {
		StockData newest = getNewestStockData(companyId);
		
		if (null == newest) {
			return new AddOrReplaceInfo(null, AddOrReplace.replaceAll);
		} else {
			System.out.println("newest stock data found in database:  " + newest.getDayIndex() + " " + newest.getAdjustedClose());

			AddOrReplace updateOrReplace = determineFromStockData(newest, stockDataReader);

			return new AddOrReplaceInfo(newest, updateOrReplace);
		}
	}

	
	static AddOrReplace determineFromStockData(StockData newest, BufferedReader stockDataReader) throws IOException, ParseException {

		DateFormat dateFormat = new SimpleDateFormat(InputFormatConstants.dateFormat);

		String[] split = null;
		int dayIndex = -1;
		boolean notFound = true;
		while (notFound) {
			String curLine = stockDataReader.readLine();

			if (curLine != null) {
				split = curLine.split(InputFormatConstants.delimeter);
				String dateString = split[InputFormatConstants.dateCol];
				Date date = dateFormat.parse(dateString);

				dayIndex = Utilities.calculateDayIndex(date);

				notFound = dayIndex > newest.getDayIndex();
			} else {
				notFound = false;
			}
		}

		if (newest.getDayIndex() == dayIndex) {
			final double adjClose = Double.valueOf(split[InputFormatConstants.adjCloseCol]);

			System.out.println("found matching dayIndex, adjusted close is:  " + adjClose);

			if ((adjClose - eps) <= newest.getAdjustedClose() && (adjClose + eps) >= newest.getAdjustedClose()) {
				System.out.println("matches - add new data to existing data");
				return AddOrReplace.onlyAddNew;
			} else {
				System.out.println("does not match - replace all existing data");
				return AddOrReplace.replaceAll;
			}
		} else {
			throw new RuntimeException("UpdateOrReplaceDeterminer determineFromStockData BufferedReader stockDataReader does not have date that matches existing newest StockData in database");
		}
	}
	

	static StockData getNewestStockData(int companyId) {
		Query query = SessionManager.createQuery("from StockData where company.id = :companyId order by dayIndex desc");
		query.setInteger("companyId", companyId);
		
		@SuppressWarnings("rawtypes")
		List list = query.list();
		
		if (list.size() > 0) {
			return (StockData)list.get(0);
		} else {
			return null;
		}
	}
}
