package research.correlation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;

import controller.util.Utilities;

import orm.SessionManager;
import orm.StockData;

public class CorrMain {
	
	private static final String valueQueryStr = "SELECT eigenvalue.id, article.company.id, article.dayIndex, sum(value), count(*) " +
			" FROM EigenvectorValue " +
			" WHERE article.dayIndex IS NOT NULL " +
			" GROUP BY eigenvalue.id, article.company.id, article.dayIndex " +
			" ORDER BY eigenvalue.id, article.dayIndex ";
  		
	private static final int maxDayOffset = 180;
	private static final String minDayParam = "minDayParam";
	private static final String maxDayParam = "maxDayParam";
	private static final String companyIdParam = "companyIdParam";
	private static final String stockQueryStr = "FROM StockData WHERE dayIndex >= :" + minDayParam + " AND dayIndex <= :" + maxDayParam
			+ " AND company.id = :" + companyIdParam;
	
	private static final String outputFilename = "corrResult.csv";
	
	public static void main(String[] args) {
		System.out.println("CorrMain start");
		
		Set<Integer> eigIdSet = new HashSet<>();
		
		CorrData corrData = new CorrData();
		
		List<Object[]> valuesList = getValuesList();
		
		Query stockQuery = SessionManager.createQuery(stockQueryStr);
		
		for (Object[] valueArray : valuesList) {
			final int eigId = (int)valueArray[0];
			if (eigIdSet.add(eigId)) {
				System.out.println("started eigenvalue " + eigId);
			}
			
			final int dayIndex = (int)valueArray[2];
			final int companyId = (int)valueArray[1];
			final double value = (double)valueArray[3];
			
			stockQuery.setParameter(minDayParam, dayIndex);
			stockQuery.setParameter(maxDayParam, dayIndex+maxDayOffset);
			stockQuery.setParameter(companyIdParam, companyId);
			List<StockData> stockDataList = Utilities.convertGenericList(stockQuery.list());
			Iterator<StockData> stockIter = stockDataList.iterator();
			
			if (stockIter.hasNext()) {
				final StockData initialData = stockIter.next();
				
				while (stockIter.hasNext()) {
					final StockData currentData = stockIter.next();
					final double priceChange = currentData.getAdjustedClose() - initialData.getAdjustedClose();
					final int dayOffset = currentData.getDayIndex() - initialData.getDayIndex();
					
					final double corrValue = value*priceChange;
					
					corrData.addToCorr(eigId, dayOffset, corrValue);
				}
			}
		}
		
		try {
			outputResult(corrData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SessionManager.closeAll();
		System.out.println("CorrMain end");
	}

	private static void outputResult(CorrData corrData) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilename));
		writer.write("eigId,dayOffset,raw_corr,count");
		writer.newLine();
		
		List<Integer> eigIdList = new ArrayList<>(corrData.getEigIdSet());
		Collections.sort(eigIdList);
		
		for (Integer eigId : eigIdList) {
			List<Integer> dayOffsetList = new ArrayList<>(corrData.getDayOffsetSet(eigId));
			Collections.sort(dayOffsetList);
			
			for (Integer dayOffset : dayOffsetList) {
				SumData corr = corrData.getCorr(eigId, dayOffset);
				writer.write(eigId + "," + dayOffset + "," + corr.value + "," + corr.count);
				writer.newLine();
			}
		}
		
		writer.close();
	}

	private static List<Object[]> getValuesList() {
		return Utilities.convertGenericList(SessionManager.createQuery(valueQueryStr).list());
	}
}
