package controller.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import orm.Activity;

public class ActivityBuilder {

	static final int activityDateCol = 0;
	static final int transactionCol = 1;
	static final int descriptionCol = 2;
	static final int symbolCol = 3;
	static final int qtyCol = 4;
	static final int fillPriceCol = 5;
	static final int commissionCol = 6;
	static final int netAmountCol = 7;
	
	private static final String dateFormatString = "yyyy/MM/dd";
	
	private DateFormat dateFormat;
	
	public ActivityBuilder() {
		dateFormat = new SimpleDateFormat(dateFormatString);
	}
	
	public Activity build(String[] activityRow) throws ParseException {
		Activity activity = new Activity();
		
		activity.setActivityDate(dateFormat.parse(activityRow[activityDateCol]));
		activity.setTransaction(activityRow[transactionCol]);
		activity.setDescription(activityRow[descriptionCol]);
		activity.setSymbol(activityRow[symbolCol]);
		
		if (activityRow[qtyCol] != null && !activityRow[qtyCol].trim().equals("")) {
			activity.setQuantity((int)(double)Double.valueOf(activityRow[qtyCol]));
		}
		
		if (activityRow[fillPriceCol] != null && !activityRow[fillPriceCol].trim().equals("")) {
			activity.setFillPrice(Double.valueOf(activityRow[fillPriceCol]));
		}
		
		if (activityRow[commissionCol] != null && !activityRow[commissionCol].trim().equals("")) {
			activity.setCommission(Double.valueOf(activityRow[commissionCol]));
		}
		
		if (activityRow[netAmountCol] != null && !activityRow[netAmountCol].trim().equals("")) {
			activity.setNetAmount(Double.valueOf(activityRow[netAmountCol]));
		}
		
		return activity;
	}

}
