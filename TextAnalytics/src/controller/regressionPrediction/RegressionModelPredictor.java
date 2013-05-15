package controller.regressionPrediction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.hibernate.Query;



import controller.util.Utilities;

import orm.ArticlePcValue;
import orm.ScoringModel;
import orm.RegressionModel;
import orm.RegressionModelCoef;
import orm.SessionManager;

public class RegressionModelPredictor {
	private static final String regressionModelParam = "regressionModel";
	private static final String regressionModelCoefQuery = "from RegressionModelCoef where regressionModel = :" + regressionModelParam;
	
	private static final String companyParam = "company";
	private static final String startDayIndexParam = "startDayIndex";
	private static final String articlePcValueQuery = "from ArticlePcValue where eigenvalue.company = :" + companyParam
			+ " and article.dayIndex >= :" + startDayIndexParam;
	
	private final static long millisPerDay = 24*60*60*1000;
	
	public List<RegressionPredictionData> predict(RegressionModel rm) {
		List<RegressionPredictionData> result = new ArrayList<>(rm.getDayOffset());
		
		Query coefQuery = SessionManager.createQuery(regressionModelCoefQuery);
		coefQuery.setParameter(regressionModelParam, rm);
		
		List<RegressionModelCoef> coefList = Utilities.convertGenericList(coefQuery.list());
		
		Map<Integer, Map<Integer, SumCountPair>> dayIndexEigIdAverageValueMap = loadAndCalculateAveragePcVal(rm);
		
		
		
		
		return result;
	}
	
	private Map<Integer, Map<Integer, SumCountPair>> loadAndCalculateAveragePcVal(RegressionModel rm) {
		List<ArticlePcValue> pcValList = getArticlePcValueList(rm.getScoringModel(), rm.getDayOffset());
		
		return calculateAveragePcVal(pcValList);
	}
	
	List<ArticlePcValue> getArticlePcValueList(ScoringModel company, int dayOffset) {
		final int curDayIndex = (int)((new Date()).getTime() / millisPerDay);
		final int startDayIndex = curDayIndex - dayOffset;
		
		Query query = SessionManager.createQuery(articlePcValueQuery);
		query.setParameter(companyParam, company);
		query.setParameter(startDayIndexParam, startDayIndex);
		
		List<ArticlePcValue> pcValList = Utilities.convertGenericList(query.list());
		
		return pcValList;
	}
	
	
	Map<Integer, Map<Integer, SumCountPair>> calculateAveragePcVal(List<ArticlePcValue> pcValList) {
		Map<Integer, Map<Integer, SumCountPair>> result = new HashMap<>();

		for (ArticlePcValue articlePcValue : pcValList) {
			final int dayIndex = articlePcValue.getArticle().getDayIndex();
			Map<Integer, SumCountPair> eigIdSumCountPairMap = result.get(dayIndex);
			
			if (null == eigIdSumCountPairMap) {
				eigIdSumCountPairMap = new HashMap<>();
				result.put(dayIndex, eigIdSumCountPairMap);
			}
			
			final int eigId = articlePcValue.getEigenvalue().getId();
			SumCountPair sumCountPair = eigIdSumCountPairMap.get(eigId);
			if (null == sumCountPair) {
				sumCountPair = new SumCountPair();
				eigIdSumCountPairMap.put(eigId, sumCountPair);
			}
			
			sumCountPair.count++;
			sumCountPair.sum += articlePcValue.getValue();
		}
		
		return result;
	}

}
