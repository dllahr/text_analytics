package controller.prediction.regressionModel;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import controller.util.Utilities;

import orm.RegressionModel;
import orm.RegressionModelCoef;
import orm.SessionManager;

import math.linearAlgebra.SparseVector;
import math.linearAlgebra.Vector;

public class DayIndexRawPredictionPairBuilder {
	
	public List<DayIndexRawPredictionPair> build(RegressionModel rm, List<DayPrincipalComponentValueVector> dayPcValVectList) {

		final double intercept = getRegressionModelIntercept(rm.getId());
		Vector coefVect = buildRegressionModelCoefVector(rm.getId());
		
		List<DayIndexRawPredictionPair> result = new ArrayList<>(dayPcValVectList.size());
		
		for (DayPrincipalComponentValueVector dayPcValVect : dayPcValVectList) {
			double prediction = intercept + dayPcValVect.prinCompValueVect.vectorMultiply(coefVect);
			
			result.add(new DayIndexRawPredictionPair(dayPcValVect.dayIndex, dayPcValVect.dayIndex + rm.getDayOffset(), prediction));
		}
		
		return result;
	}

	/**
	 * 
	 * @param regressionModelId
	 * @return position in vector corresponds to sort index of eigenvalue
	 */
	static Vector buildRegressionModelCoefVector(int regressionModelId) {
		Query query = SessionManager.createQuery("from RegressionModelCoef where regressionModel.id = :rmId "
				+ "and eigenvalue is not null");
		query.setInteger("rmId", regressionModelId);
		
		List<RegressionModelCoef> coefList = Utilities.convertGenericList(query.list());
		
		Vector result = new SparseVector();
		
		for (RegressionModelCoef coef : coefList) {
			result.setEntry(coef.getEigenvalue().getSortIndex(), coef.getCoef());
		}

		return result;
	}
	
	static double getRegressionModelIntercept(int regressionModelId) {
		Query query = SessionManager.createQuery("select coef from RegressionModelCoef where regressionModel.id = :rmId and eigenvalue is null");
		query.setInteger("rmId", regressionModelId);
		
		return (double)query.list().get(0);
	}
	
	static int getRegressionModelDayOffset(int regressionModelId) {
		Query query = SessionManager.createQuery("select dayOffset from RegressionModel where id = :rmId");
		query.setInteger("rmId", regressionModelId);
		
		return (int)query.list().get(0);
	}
}
