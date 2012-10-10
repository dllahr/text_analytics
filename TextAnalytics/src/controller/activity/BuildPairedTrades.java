package controller.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import controller.util.Utilities;

import orm.Activity;
import orm.SessionManager;

public class BuildPairedTrades {
	private static final String queryStr = "SELECT activity_date, transaction, description, symbol, SUM(qty) qty," +
			" CAST(SUM(fill_price*qty)/SUM(qty) AS FLOAT) ave_fill_price, CAST(SUM(commission) AS FLOAT) commission," +
			" CAST(SUM(net_amount) AS FLOAT) net_amount FROM activity" +
			" WHERE transaction is not null" +
			" GROUP BY activity_date, transaction, description, symbol" +
			" ORDER BY activity_date";
	
	private static final String openStr = "open";
	
	public void build() {
		Map<String, InventoryItem> descriptionInventoryMap = new HashMap<String, InventoryItem>();
		
		List<Activity> activityList = buildAggregateActivityList();
		
		List<PairedTrade> pairedTradeList = new LinkedList<>();
		
		for (Activity activity : activityList) {
			InventoryItem invItem = descriptionInventoryMap.get(activity.getDescription());
			if (null == invItem) {
				invItem = new InventoryItem(activity.getDescription());
				descriptionInventoryMap.put(activity.getDescription(), invItem);
			}
			
			if (activity.getTransaction().toLowerCase().trim().endsWith(openStr)) {
				invItem.add(activity.getQuantity(), activity.getNetAmount());
			} else {
				final double cost = invItem.subtract(activity.getQuantity());
				pairedTradeList.add(new PairedTrade(activity.getDescription(), activity.getActivityDate(), activity.getQuantity(), cost, activity.getNetAmount()));
				
				if (invItem.getQuantity() == 0) {
					descriptionInventoryMap.remove(activity.getDescription());
				}
			}
		}
		
		for (PairedTrade pairedTrade : pairedTradeList) {
			System.out.println(pairedTrade.toString());
		}
	}
	
	private static List<Activity> buildAggregateActivityList() {
		List<Object[]> rowList = Utilities.convertGenericList(SessionManager.createSqlQuery(queryStr).list());

		List<Activity> result = new ArrayList<>(rowList.size());
		
		int id = 0;
		for (Object[] row : rowList) {
			Activity activity = buildBaseActivity(row);
			activity.setId(id++);
			
			if (row[ActivityBuilder.transactionCol] != null) {
				activity.setTransaction(row[ActivityBuilder.transactionCol].toString());
				activity.setSymbol(row[ActivityBuilder.symbolCol].toString());
				
				activity.setQuantity(((BigDecimal)row[ActivityBuilder.qtyCol]).intValue());
				
				if (row[ActivityBuilder.fillPriceCol] != null) {
					activity.setFillPrice(((BigDecimal)row[ActivityBuilder.fillPriceCol]).doubleValue());
				}
				
				if (row[ActivityBuilder.commissionCol] != null) {
					activity.setCommission(((BigDecimal)row[ActivityBuilder.commissionCol]).doubleValue());
				}
			}
			
			result.add(activity);
			System.out.println(activity);
		}
		
		return result;
	}
	
	private static Activity buildBaseActivity(Object[] row) {
		Activity activity = new Activity();
		
		activity.setActivityDate((Date)row[ActivityBuilder.activityDateCol]);
		activity.setDescription(row[ActivityBuilder.descriptionCol].toString());
		activity.setNetAmount(((BigDecimal)row[ActivityBuilder.netAmountCol]).doubleValue());
		
		return activity;
	}
}
