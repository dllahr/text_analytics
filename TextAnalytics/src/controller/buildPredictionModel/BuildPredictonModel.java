package controller.buildPredictionModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import controller.util.Utilities;

import orm.Company;
import orm.Eigenvalue;
import orm.PredictionModel;
import orm.SessionManager;
import orm.StockPriceChange;
import orm.StockPriceChangeCalculation;

public class BuildPredictonModel {

	private static final String companyParam = "company";
	
	private static final String spcQueryStr = "from StockPriceChange where useForPrediction='" + StockPriceChange.TRUE_USE_FOR_PREDICTION
			+ "' and eigenvalue.company=:" + companyParam;

	private static final String eigenvalueParam = "eigenvalue";
	private static final String eigvectValQueryStr = "select value from EigenvectorValue where eigenvalue=:" + eigenvalueParam
			+ " order by value asc";

	public void build(Company company) {

		List<StockPriceChange> spcList = getStockPriceChangeList(company);
		
		int pmId = Utilities.getMaxId("PredictionModel") + 1;
		
		Map<StockPriceChangeCalculation, Map<Eigenvalue, ThresholdPair>> thresholdMap = calcThresholds(company, spcList);
		
		for (StockPriceChange spc : spcList) {
			PredictionModel pm = new PredictionModel();
			pm.setId(pmId);
			pm.setStockPriceChange(spc);
			
			StockPriceChangeCalculation spcc = spc.getStockPriceChangeCalculation();
			
			ThresholdPair pair = thresholdMap.get(spcc).get(spc.getEigenvalue());
			
			pm.setThresholdLower(pair.getLower());
			pm.setThresholdUpper(pair.getUpper());

			SessionManager.persist(pm);
			
			pmId++;
		}
		
		SessionManager.commit();
	}
	
	private Map<StockPriceChangeCalculation, Map<Eigenvalue, ThresholdPair>> calcThresholds(Company company, 
			List<StockPriceChange> spcList) {
		
		final long eigenvectorLength = EigenvectorStats.lookupEigenvectorLength(company);
		
		Map<StockPriceChangeCalculation, Map<Eigenvalue, ThresholdPair>> result = new HashMap<>();
		
		for (StockPriceChange spc : spcList) {
			StockPriceChangeCalculation spcc = spc.getStockPriceChangeCalculation();
			
			Map<Eigenvalue, ThresholdPair> pairMap = result.get(spcc);
			if (null == pairMap) {
				pairMap = new HashMap<>();
				result.put(spcc, pairMap);
			}
			
			if (! pairMap.containsKey(spc.getEigenvalue())) {
				final Double[] pctThresholdArray = {spcc.getPercentageThresholdLower(), spcc.getPercentageThresholdUpper()};
				final Double[] thresholdArray = new Double[2];
				for (int i = 0; i < 2; i++) {
					if (pctThresholdArray[i] != null) {
						final int eigvectValIndex = -1 + (int)(pctThresholdArray[i]*(double)eigenvectorLength);

						Query eigvectValQuery = SessionManager.createQuery(eigvectValQueryStr);
						eigvectValQuery.setParameter(eigenvalueParam, spc.getEigenvalue());
						eigvectValQuery.setFirstResult(eigvectValIndex);
						eigvectValQuery.setMaxResults(1);

						final double eigvectVal = (double)eigvectValQuery.list().get(0);

						thresholdArray[i] = spc.getEigenvalue().getValue() * eigvectVal;
					}
				}
				
				ThresholdPair threshold = new ThresholdPair(thresholdArray[0], thresholdArray[1]);
				
				pairMap.put(spc.getEigenvalue(), threshold);
			}
		}
		
		return result;
	}
	
	private List<StockPriceChange> getStockPriceChangeList(Company company) {
		Query query = SessionManager.createQuery(spcQueryStr);
		query.setParameter(companyParam, company);
		
		return Utilities.convertGenericList(query.list());
	}
	
	private class ThresholdPair {
		private final Double lower;
		private final Double upper;
		public ThresholdPair(Double lower, Double upper) {
			this.lower = lower;
			this.upper = upper;
		}
		public Double getLower() {
			return lower;
		}
		public Double getUpper() {
			return upper;
		}
	}
}
