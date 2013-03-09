package controller.buildPredictionModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;

import controller.util.Utilities;

import orm.ScoringModel;
import orm.Eigenvalue;
import orm.SessionManager;
import orm.StockPriceChange;
import orm.StockPriceChangeCalculation;

/**
 * WARNING attempted to update this code has not been tested
 * @author dlahr
 *
 */
public class EigenvectorStats {

	private static final String scoringModelParam = "scoringModel";
	private static final String eigenvectorLengthSqlQuery = "SELECT count(*) FROM eigenvector_value evv JOIN eigenvalue ev ON ev.id=evv.eigenvalue_id"
			+ " WHERE ev.scoring_model_id=:" + scoringModelParam + " AND ev.id=" 
			+ "(SELECT id FROM eigenvalue WHERE scoring_model_id=:" + scoringModelParam + " and rownum=1)";

	private static final String companyParam = "company";
	private static final String minDayIndexQuery = "SELECT min(dayIndex) FROM Article WHERE company in (" + companyParam + ")";
	
	private static final double upperThresholdFraction = 0.90;
	private static final double lowerThresholdFraction = 0.00;
	private static final double numArticlesFraction = 0.10;
	
	private static final String eigenvalueQuery = "FROM Eigenvalue WHERE scoringModel=:" + scoringModelParam;

	private static final String eigenvalueParam = "eigenvalue";

	private static final String dayIndexQuery = "SELECT article.dayIndex FROM EigenvectorValue WHERE eigenvalue=:"
			+ eigenvalueParam + " ORDER BY value";
	
	public void doCalc(ScoringModel scoringModel) {
		final Long numArticlesTotal = lookupEigenvectorLength(scoringModel);
		if (null == numArticlesTotal) {
			System.err.println("EigenvectorStats - no calculation performed because no articles found in database for company "
					+ scoringModel.getId() + " " + scoringModel.getNotes());
			return;
		}
		
		final Integer minDayIndex = lookupMinDayIndex(scoringModel);
		
		final int[] queryFirstResultArray = new int[2];
		queryFirstResultArray[0] = (int)(upperThresholdFraction*((double)numArticlesTotal));
		queryFirstResultArray[1] = (int)(lowerThresholdFraction*((double)numArticlesTotal));
		final int numArticles = (int)(numArticlesFraction*((double)numArticlesTotal));
		System.out.println(queryFirstResultArray[0] + " " + queryFirstResultArray[1] + " " + numArticles);
		
		final Double[][] spccThresholdInfo = {{upperThresholdFraction, null}, {null, numArticlesFraction}};
		
		Query query = SessionManager.createQuery(eigenvalueQuery);
		query.setParameter(scoringModelParam, scoringModel);
		List<Eigenvalue> eigenvalueList = Utilities.convertGenericList(query.list());
		
		CalculateStockStatistics calcStockStats = new CalculateStockStatistics(false, minDayIndex, scoringModel);

		int spcCalcId = Utilities.getMaxId("StockPriceChangeCalculation") + 1;
		int spcId = Utilities.getMaxId("StockPriceChange") + 1;
		
		query = SessionManager.createQuery(dayIndexQuery);
		query.setMaxResults(numArticles);
		
		for (int threshInd = 0; threshInd < queryFirstResultArray.length; threshInd++) {
			query.setFirstResult(queryFirstResultArray[threshInd]);
			
			StockPriceChangeCalculation spcc = new StockPriceChangeCalculation();
			spcc.setId(spcCalcId);
			spcCalcId++;

			spcc.setCompany(scoringModel);
			spcc.setHistogramRangeLower(calcStockStats.getLowerLimit());
			spcc.setHistogramRangeUpper(calcStockStats.getUpperLimit());
			spcc.setNumBins(calcStockStats.getNumBins());

			spcc.setPercentageThresholdLower(spccThresholdInfo[threshInd][0]);
			spcc.setPercentageThresholdUpper(spccThresholdInfo[threshInd][1]);

			SessionManager.persist(spcc);
			
			for (Eigenvalue eigenvalue : eigenvalueList) {
				query.setParameter(eigenvalueParam, eigenvalue);

				List<Integer> dayIndexList = Utilities.convertGenericList(query.list());
				
				if (dayIndexList.size() > 0) {
					Set<Integer> uniqueDayIndexSet = new HashSet<>(dayIndexList);
					uniqueDayIndexSet.remove(null);
					dayIndexList = new ArrayList<>(uniqueDayIndexSet);
					Collections.sort(dayIndexList);

					List<StockPriceChange> spcList = calcStockStats.doCalc(dayIndexList);
					for (StockPriceChange spc : spcList) {
						spc.setId(spcId);
						spcId++;

						spc.setStockPriceChangeCalculation(spcc);
						spc.setEigenvalue(eigenvalue);

						SessionManager.persist(spc);
					}
				}
			}
		}
		
		SessionManager.commit();
	}
	

	private Integer lookupMinDayIndex(ScoringModel scoringModel) {
		Query query = SessionManager.createQuery(minDayIndexQuery);
		query.setParameterList(companyParam, scoringModel.getCompanySet());
		
		@SuppressWarnings("rawtypes")
		List minList = query.list();
		
		if (minList.size() == 0) {
			return null;
		} else {
			return (int)minList.get(0);
		}
	}

	protected static Long lookupEigenvectorLength(ScoringModel company) {
		Query query = SessionManager.createSqlQuery(eigenvectorLengthSqlQuery);
		query.setParameter(scoringModelParam, company.getId());
		
		@SuppressWarnings("rawtypes")
		List numList = query.list();
		
		if(numList.size() == 0) {
			return null;
		} else {
			return ((BigDecimal)numList.get(0)).longValueExact(); 
			
		}
	}
}
