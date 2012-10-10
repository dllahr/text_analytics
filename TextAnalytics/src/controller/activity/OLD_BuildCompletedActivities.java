package controller.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.hibernate.Query;

import controller.util.Utilities;

import orm.Activity;
import orm.SessionManager;

public class OLD_BuildCompletedActivities {
	
	private static final String queryStr = "SELECT activity_date, transaction, description, symbol, SUM(qty) qty," +
			" CAST(SUM(fill_price*qty)/SUM(qty) AS FLOAT) ave_fill_price, CAST(SUM(commission) AS FLOAT) commission," +
			" CAST(SUM(net_amount) AS FLOAT) net_amount FROM activity" +
			" GROUP BY activity_date, transaction, description, symbol" +
			" ORDER BY activity_date";
	


	public List<CompletedActivity> build() {
		List<CompletedActivity> result = new LinkedList<>();
		
		List<Object[]> rowList = Utilities.convertGenericList(SessionManager.createSqlQuery(queryStr).list());
		
		List<Activity> aggregateList = buildAggregateActivityList(rowList);
		
		Set<Integer> skipIdSet = new HashSet<>();
		
		ListIterator<Activity> iter = aggregateList.listIterator();
		while (iter.hasNext()) {
			Activity activity = iter.next();
			if (! skipIdSet.contains(activity.getId())) {
				CompletedActivity completedActivity = new CompletedActivity();

				if (activity.getTransaction() == null) {
					completedActivity.setEndActivity(activity);
					result.add(completedActivity);
				} else {
					completedActivity.setStartActivity(activity);

					List<Activity> endActivities = findEndActivities(aggregateList.listIterator(iter.nextIndex()), activity);
					for (Activity endActivity : endActivities) {
						if (endActivity.getId() != null) {
							skipIdSet.add(endActivity.getId());
						}
					}
				}
			}
		}
		
		return result;
	}
	
	private static List<Activity> findEndActivities(ListIterator<Activity> iter, final Activity startActivity) {
		List<Activity> result = new LinkedList<>();
		
		final boolean startIsPositiveQty = startActivity.getQuantity() > 0;
		
		int absRemainingQuantity = Math.abs(startActivity.getQuantity());
		
		while (absRemainingQuantity > 0 && iter.hasNext()) {
			Activity activity = iter.next();
			final boolean isPositiveQty = activity.getQuantity() > 0;
			
			//if it is the same security and the quantity is the opposite this activity is
			//the closing side of the provided start activity
			if (activity.getDescription().equals(startActivity.getDescription()) 
					&& (isPositiveQty != startIsPositiveQty)) {
				
				//check if the current found activity is wholly used up matching the start activity
				final int absQty = Math.abs(activity.getQuantity());
				if (absQty <= absRemainingQuantity) {
					absRemainingQuantity -= absQty;
					result.add(activity);
					
				} else { //otherwise it was only partly used up
					//make a copy representing the portion used up
					Activity copy = new Activity(activity);
					copy.setQuantity(absRemainingQuantity);
					copy.setId(null);
					result.add(copy);
					absRemainingQuantity = 0;
					
					//adjust the original's contents to remove the used up portion
					final int absNewQty = absQty - absRemainingQuantity;
					activity.setQuantity(absNewQty * activity.getQuantity() / absQty);
				}
			}
		}
		
		return result;
	}
	
	private static List<Activity> buildAggregateActivityList(List<Object[]> rowList) {
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
