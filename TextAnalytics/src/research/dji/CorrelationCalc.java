package research.dji;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import orm.DjiFracChange;
import orm.Eigenvalue;
import orm.EigenvectorValue;
import orm.SessionManager;

import controller.util.Utilities;

public class CorrelationCalc {
	private static final String filename = "DjiCorr.csv";
	private static final String delimiter = ",";
	
	private static final String djiFracChangeQueryStr = "from DjiFracChange order by fracChange asc";
	private static final double outlierFrac = 0.1;
	
	private static final String dayIndexLowerParam = "dayIndexLower";
	private static final String dayIndexUpperParam = "dayIndexUpper";
	private static final String eigenVectorValueQueryStr = "from EigenvectorValue where article.dayIndex >= :" 
			+ dayIndexLowerParam + " and article.dayIndex <= :" + dayIndexUpperParam + " order by article.dayIndex asc";

	public static void main(String[] args) throws IOException {
		System.out.println("dji correlation calculation");
		final int minDayOffset, maxDayOffset;
		if (args.length != 0) {
			minDayOffset = Integer.valueOf(args[0]);
			maxDayOffset = Integer.valueOf(args[1]);
		} else {
			minDayOffset = 1;
			maxDayOffset = 80;
		}

		List<DjiFracChange> djiFracChangeList = getDjiFracChangeList();

		Query eigVectValQuery = SessionManager.createQuery(eigenVectorValueQueryStr);

		Map<Integer, Map<Eigenvalue, EigDayOffsetCorr>> map = new HashMap<>();

		for (DjiFracChange djiFracChange : djiFracChangeList) {
			System.out.println("dji frac change: " + djiFracChange.getFracChange());

			final int dayIndexUpper = djiFracChange.getDayIndex() - minDayOffset;
			final int dayIndexLower = djiFracChange.getDayIndex() - maxDayOffset;

			eigVectValQuery.setParameter(dayIndexLowerParam, dayIndexLower);
			eigVectValQuery.setParameter(dayIndexUpperParam, dayIndexUpper);
			
			List<EigenvectorValue> eigenvectorValueList = Utilities.convertGenericList(eigVectValQuery.list());

			for (EigenvectorValue eigVectVal : eigenvectorValueList) {
				final int dayOffset = dayIndexUpper + 1 - eigVectVal.getArticle().getDayIndex();
				
				Map<Eigenvalue, EigDayOffsetCorr> eigCorrMap = map.get(dayOffset);
				if (null == eigCorrMap) {
					eigCorrMap = new HashMap<>();
					map.put(dayOffset, eigCorrMap);
				}
				
				EigDayOffsetCorr corr = eigCorrMap.get(eigVectVal.getEigenvalue());
				if (null == corr) {
					corr = new EigDayOffsetCorr(eigVectVal.getEigenvalue(), dayOffset);
					eigCorrMap.put(eigVectVal.getEigenvalue(), corr);
				}
				
				corr.addToCorrelation(eigVectVal.getValue() * djiFracChange.getFracChange());
			}			
		}

		List<EigDayOffsetCorr> sortList = new LinkedList<>();
		for (Map<Eigenvalue, EigDayOffsetCorr> eigCorrMap : map.values()) {
			sortList.addAll(eigCorrMap.values());
		}

		Collections.sort(sortList, new Comparator<EigDayOffsetCorr>() {
			@Override
			public int compare(EigDayOffsetCorr o1, EigDayOffsetCorr o2) {
				if (o1.getDayOffset() != o2.getDayOffset()) {
					return Integer.compare(o1.getDayOffset(), o2.getDayOffset());
				} else {
					return o1.getCorrelation().compareTo(o2.getCorrelation());
				}
			}
		});
		
//		EigDayOffsetCorr forDisp = sortList.get(0);
//		System.out.print("min,max correlations: " + forDisp.getEigenvalue().getId() + " " + forDisp.getCorrelation());
//		forDisp = sortList.get(sortList.size() - 1);
//		System.out.println(" " + forDisp.getEigenvalue().getId() + " " + forDisp.getCorrelation());
		
		outputToFile(sortList);

		SessionManager.closeAll();
		System.out.println("dji correlation calculation done");
	}
	

	private static List<DjiFracChange> getDjiFracChangeList() {
		List<DjiFracChange> result = new LinkedList<>();
		
		Query query = SessionManager.createQuery("select count(*) from DjiFracChange");
		final int numDjiFracChange = (int)(long)(query.list().get(0));
		final int lowerLimitNumber = (int)(outlierFrac*numDjiFracChange);
		final int upperLimitNumber = (int)((1.0 - outlierFrac)*numDjiFracChange);
		final int[] lowerLimitsArray = {0, upperLimitNumber};
		final int[] upperLimitsArray = {lowerLimitNumber, numDjiFracChange};
		
		Query djiFracChangeQuery = SessionManager.createQuery(djiFracChangeQueryStr);
		
		for (int i = 0; i < lowerLimitsArray.length; i++) {
			
			djiFracChangeQuery.setFirstResult(lowerLimitsArray[i]);
			djiFracChangeQuery.setMaxResults(upperLimitsArray[i]);
			
			List<DjiFracChange> curList = Utilities.convertGenericList(djiFracChangeQuery.list());
			result.addAll(curList);
		}
		
		return result;
	}

	
	private static void outputToFile(List<EigDayOffsetCorr> resultList) throws IOException {
		FileWriter fileWriter = new FileWriter(new File(filename), true);
		BufferedWriter writer = new BufferedWriter(fileWriter);
		writer.write("day_offset" + delimiter + "eigenvalue_id" + delimiter + "correlation" 
				+ delimiter + "count");
		writer.newLine();
		
		for (EigDayOffsetCorr corr : resultList) {
			StringBuilder line = new StringBuilder();
			
			line.append(corr.getDayOffset());
			line.append(delimiter);
			line.append(corr.getEigenvalue().getId());
			line.append(delimiter);
			line.append(corr.getCorrelation() + "");
			line.append(delimiter);
			line.append(corr.getCount());
			
			writer.write(line.toString());
			writer.newLine();
		}
		
		writer.close();
	}
	
	
}
