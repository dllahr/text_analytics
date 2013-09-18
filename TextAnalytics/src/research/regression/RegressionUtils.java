package research.regression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import orm.Eigenvalue;
import orm.EigenvectorValue;
import research.correlation.SumData;

public class RegressionUtils {
	public static List<Integer> calculateUniqueArticleDays(List<EigenvectorValue> evvList) {
		Set<Integer> articleDaySet = new HashSet<>();
		Set<Integer> nullDayIndexArticleIdSet = new HashSet<>();
		
		for (EigenvectorValue evv : evvList) {
			final Integer dayIndex = evv.getArticle().getDayIndex();
			
			if (dayIndex != null) {
				articleDaySet.add(evv.getArticle().getDayIndex());
			} else {
				nullDayIndexArticleIdSet.add(evv.getArticle().getId());
			}
		}
		System.out.println("\tarticles with null dayIndex:  " + nullDayIndexArticleIdSet.size());
		
		List<Integer> articleDayIndexList = new ArrayList<>(articleDaySet);
		Collections.sort(articleDayIndexList);
		return articleDayIndexList;
	}
	
	public static Map<Eigenvalue, Map<Integer, SumData>> calculateEigMap(List<EigenvectorValue> evvList) {
		Map<Eigenvalue, Map<Integer, SumData>> result = new HashMap<Eigenvalue, Map<Integer, SumData>>();
		
		for (EigenvectorValue evv : evvList) {
			Map<Integer, SumData> dayValueMap = result.get(evv.getEigenvalue());
			if (null == dayValueMap) {
				dayValueMap = new HashMap<>();
				result.put(evv.getEigenvalue(), dayValueMap);
			}
			
			SumData sumData = dayValueMap.get(evv.getArticle().getDayIndex());
			if (null == sumData) {
				sumData = new SumData();
				dayValueMap.put(evv.getArticle().getDayIndex(), sumData);
			}
			
			sumData.value += evv.getValue();
			sumData.count++;
		}
		
		return result;
	}
}
