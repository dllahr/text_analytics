package controller.predictFromArticles;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.HibernateException;
import orm.SessionManager;

import tools.Data;
import tools.SaveData;

public class ExportPredictionData {

	private final static String queryColumns =  "c.stock_symbol, p.article_id, p.prediction_model_id, p.stock_price, to_char(p.day_time) \"date\", e.sort_index eig_sort_index, " +
			"spc.day_offset, spc.average, spc.fwhm";
	private final static String queryStr = "select " + queryColumns + " " +
			"from prediction p " +
			"join prediction_model pm on pm.id=p.prediction_model_id " +
			"join stockprice_change spc on spc.id=pm.stockprice_change_id " +
			"join eigenvalue e on e.id=spc.eigenvalue_id " +
			"join company c on c.id=e.company_id " +
			"order by c.stock_symbol, e.sort_index, spc.day_offset";
	
	public void export(File exportFile) {
		Data data;
		try {
			data = retrieveData();
		} catch (HibernateException | SQLException e1) {
			System.err.println("ExportPredictiondata export failed to read data from database");
			e1.printStackTrace();
			return;
		}
		
		SaveData saver = new SaveData();
		
		try {
			saver.saveCsvWithHeaderRow(data, exportFile);
		} catch (IOException e) {
			System.err.println("ExportPredictionData export failed to write data to file");
			e.printStackTrace();
		}
	}

	static Data retrieveData() throws HibernateException, SQLException {
		final String[] columnHeaders = getColumnHeaders();
		final int numCols = columnHeaders.length;
		
		ResultSet rs = SessionManager.resultSetQuery(queryStr);
		List<String[]> rowList = new LinkedList<>();
		while (rs.next()) {
			String[] row = new String[numCols];
			for (int i = 0; i < numCols; i++) {
				row[i] = rs.getString(i+1);
			}
			rowList.add(row);
		}
		rs.close();
		
		
		final int numRows = rowList.size();
		
		
		final String[][] rawData = new String[numRows][];
		
		int rowInd = 0;
		for (String[] row : rowList) {
			rawData[rowInd] = row;
			rowInd++;
		}
		
		return new Data(rawData, columnHeaders);
	}
	
	private static String[] getColumnHeaders() {
		String[] raw = queryColumns.split(",");
		String[] result = new String[raw.length];
		
		for (int i = 0; i < raw.length; i++) {
			raw[i] = raw[i].trim();
			final String rawSplitChar;
			if (raw[i].contains(" ")) {
				rawSplitChar = " ";
			} else if (raw[i].contains(".")) {
				rawSplitChar = "\\.";
			} else {
				rawSplitChar = null;
			}

			if (rawSplitChar != null) {
				String[] rawSub = raw[i].split(rawSplitChar);
				result[i] = rawSub[1];
			} else {
				result[i] = raw[i];
			}
		}
		
		return result;
	}
}
