package research;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.ListIterator;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import orm.SessionManager;
import orm.StockData;
import controller.stockPrices.ExtremaUtility;
import controller.util.Utilities;

public class MainExtremaPrice {
	private static final String outputFilePrefix = "extrema_";
	//find the max stock price that occurred in the given window around an article's publication date
	//does the stock price respond before the article is published!?!?!?!  "insider info" leaking out?!?!

	//find first stock price after article.dayIndex + minDay.  check stock prices between then and article.dayIndex + maxDay
	//calculate log fraction change.  write article.dayIndex, fraction change
	public static void main(String[] args) throws HibernateException, SQLException, IOException {
		System.out.println("MainPeakPrice start");
		
		final int companyId = Integer.valueOf(args[0]);
		final int scoringModelId = Integer.valueOf(args[1]);
		final int minDay = Integer.valueOf(args[2]);  //minimum day after article publish date for window to search for extreme stock price
		final int maxDay = Integer.valueOf(args[3]);  //maximum day after article publish date for window to search for extreme stock price
		
		System.out.println("companyId:  " + companyId);
		System.out.println("scoringModelId:  " + scoringModelId);
		System.out.println("minDay to maxDay:  " + minDay + " - " + maxDay);
		
		Query query = SessionManager.createQuery("from StockData where company.id = :companyId order by dayIndex");
		query.setParameter("companyId", companyId);
		
		List<StockData> stockDataList = Utilities.convertGenericList(query.list());
		
		System.out.println("found stock data. size:  " + stockDataList.size());
		
		List<Integer> dayIndexes = MainVolatility.getArticleDayIndexes(scoringModelId);
		System.out.println("found day indexes for scoring model.  size:  " + dayIndexes.size());
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePrefix + companyId + "_" + scoringModelId + ".csv"));
		writer.write("article_day_index,stock_day_index,day_offset_high,log_change_high,day_offset_low,log_change_low");
		writer.newLine();
		
		for (Integer dayInd : dayIndexes) {
			ListIterator<StockData> iter = ExtremaUtility.buildIteratorWithDayIndexEqualsOrGreater(dayInd, stockDataList);
			StockData startSd = iter.next();
			
			final int curMinDay = startSd.getDayIndex() + minDay;
			final int curMaxDay = startSd.getDayIndex() + maxDay;
			StockData[] minLowMaxHigh = ExtremaUtility.findMinLowAndMaxHigh(curMinDay, curMaxDay, iter);
			StockData minLow = minLowMaxHigh[0];
			StockData maxHigh = minLowMaxHigh[1];
			
			int maxHighOffset = maxHigh.getDayIndex() - startSd.getDayIndex();
			double adjustment = (maxHigh.getAdjustedClose() / maxHigh.getClose());
			double maxHighLogRatio = Math.log(adjustment * maxHigh.getHigh() / startSd.getAdjustedClose());
			
			int minLowOffset = minLow.getDayIndex() - startSd.getDayIndex();
			adjustment = (minLow.getAdjustedClose() / minLow.getClose());
			double minLowLogRatio = Math.log(adjustment * minLow.getLow() / startSd.getAdjustedClose());
			
			writer.write(dayInd + "," + startSd.getDayIndex() + "," + maxHighOffset + "," + maxHighLogRatio + "," 
					+ minLowOffset + "," + minLowLogRatio);
			writer.newLine();
		}
		
		writer.close();
		SessionManager.closeAll();
	}
}
