package controller.stockPrices.update;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import orm.StockData;

class StockDataBuilder {
	
	private final DateFormat dateFormat;
	
	StockDataBuilder() {
		dateFormat = new SimpleDateFormat(InputFormatConstants.dateFormat);
	}

	void updateStockDataFieldsFromLine(String[] lineArray, StockData stockData) {
		stockData.setOpen(Double.valueOf(lineArray[InputFormatConstants.openCol]));
		stockData.setHigh(Double.valueOf(lineArray[InputFormatConstants.highCol]));
		stockData.setLow(Double.valueOf(lineArray[InputFormatConstants.lowCol]));
		stockData.setClose(Double.valueOf(lineArray[InputFormatConstants.closeCol]));
		stockData.setVolume(Long.valueOf(lineArray[InputFormatConstants.volumeCol]));
		stockData.setAdjustedClose(Double.valueOf(lineArray[InputFormatConstants.adjCloseCol]));
	}
	
	Date parseDate(String[] lineArray) throws ParseException {
		return dateFormat.parse(lineArray[InputFormatConstants.dateCol]);
	}
}
