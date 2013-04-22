package research.regression;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;

import orm.SessionManager;

public class GetArticlePcValueDayAggregated {

	private static final long[] articleIdRange = {132420, 132766};
	
	private static final String outputFilePath = "oos_articlePcValueAggregated_8.csv";

	private static final String delimeter = ",";
	
	private static final int dayIndexColInd = 0;
	private static final int eigenvalueIdColInd = 1;
	private static final int valueColind = 2;
	private static final String lowerDayIndexParam = "lowerDayIndex";
	private static final String upperDayIndexParam = "upperDayIndex";
	private static final String queryString = "select art.day_index, e.id, to_char(sum(apv.value)/count(*)/e.value) ave_div_e "
			+ " from article_pc_value apv "
			+ " join article art on art.id = apv.article_id "
			+ " join eigenvalue e on e.id = apv.eigenvalue_id "
			+ " where article_id >= :" + lowerDayIndexParam + " and article_id <= :" + upperDayIndexParam + " "
			+ " group by art.day_index, e.id, e.value "
			+ " order by art.day_index, e.id";
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Query query = SessionManager.createSqlQuery(queryString);
		query.setLong(lowerDayIndexParam, articleIdRange[0]);
		query.setLong(upperDayIndexParam, articleIdRange[1]);
		
		@SuppressWarnings("unchecked")
		List<Object[]> rowList = query.list();

		BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

		Map<Integer, Integer> eigIdRelativeColIndMap = buildEigIdRelativeColIndexMapAndWriteHeaders(rowList, writer);

		int prevDayIndex = Integer.valueOf(rowList.get(0)[dayIndexColInd].toString());
		
		double[] curValues = new double[eigIdRelativeColIndMap.size()];

		for (Object[] row : rowList) {
			final int curDayIndex = Integer.valueOf(row[0].toString());
			
			if (curDayIndex != prevDayIndex) {
				writer.write(String.valueOf(prevDayIndex));
				writer.write(delimeter);
				for (int i = 0; i < curValues.length-1; i++) {
					writer.write(String.valueOf(curValues[i]));
					writer.write(delimeter);
				}
				writer.write(String.valueOf(curValues[curValues.length-1]));
				writer.newLine();
				
				curValues = new double[eigIdRelativeColIndMap.size()];
				prevDayIndex = curDayIndex;
			}
			
			final int eigId = Integer.valueOf(row[eigenvalueIdColInd].toString());
			final int relColind = eigIdRelativeColIndMap.get(eigId);
			
			curValues[relColind] = Double.valueOf(row[valueColind].toString());
		}

		writer.close();
		SessionManager.closeAll();
	}

	private static Map<Integer, Integer> buildEigIdRelativeColIndexMapAndWriteHeaders(List<Object[]> rowList, BufferedWriter writer) 
			throws IOException {

		writer.write("day_index");
		writer.write(delimeter);

		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		
		Set<Integer> eigIdSet = new HashSet<>();
		for (Object[] row : rowList) {
			eigIdSet.add(Integer.valueOf(row[eigenvalueIdColInd].toString()));
		}
		
		List<Integer> sortEigIdList = new ArrayList<>(eigIdSet);
		Collections.sort(sortEigIdList);
		
		int relativeColInd = 0;
		for (Integer eigId : sortEigIdList) {
			result.put(eigId, relativeColInd);
			relativeColInd++;
			
			writer.write("eig");
			writer.write(eigId.toString());
			writer.write(delimeter);
		}
		writer.newLine();
		
		return result;
	}
}
