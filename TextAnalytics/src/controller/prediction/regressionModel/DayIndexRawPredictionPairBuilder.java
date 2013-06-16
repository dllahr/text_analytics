package controller.prediction.regressionModel;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import controller.util.Utilities;

import orm.RegressionModel;
import orm.RegressionModelCoef;
import orm.SessionManager;

import math.linearAlgebra.DenseFixedVector;
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

	static Vector buildRegressionModelCoefVector(int regressionModelId) {
		Query query = SessionManager.createQuery("from RegressionModelCoef where regressionModel.id = :rmId "
				+ "and eigenvalue is not null "
				+ "order by eigenvalue.sortIndex");
		query.setInteger("rmId", regressionModelId);
		
		List<RegressionModelCoef> coefList = Utilities.convertGenericList(query.list());
		
		Vector result = new DenseFixedVector(coefList.size());
		
		int index = 0;
		for (RegressionModelCoef coef : coefList) {
			result.setEntry(index, coef.getCoef());

			index++;
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
