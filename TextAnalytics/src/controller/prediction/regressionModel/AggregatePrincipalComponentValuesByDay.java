package controller.prediction.regressionModel;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import controller.util.MapBuilder;
import controller.util.Utilities;
import controller.util.ValueOperator;

import orm.ArticlePcValue;
import orm.Eigenvalue;
import orm.SessionManager;

class AggregatePrincipalComponentValuesByDay {
	
	private final MapBuilder mapBuilder;
	
	public AggregatePrincipalComponentValuesByDay() {
		mapBuilder = new MapBuilder();
	}

	public List<DayPrincipalComponentValueVector> aggregate(int scoringModelId, int minDayIndex) {
		
		Map<DayEigenvalue, List<Double>> map = organizePcValByDayIndex(retrieveArticlePcValues(scoringModelId, minDayIndex));
		
		//TODO:  from above map, create vectors of pc values for each day
		
		return null;
	}
	
	
	Map<DayEigenvalue, List<Double>> organizePcValByDayIndex(List<ArticlePcValue> artPcValList) {
		ValueOperator<ArticlePcValue, DayEigenvalue, List<Double>> vo = new 
				ValueOperator<ArticlePcValue, AggregatePrincipalComponentValuesByDay.DayEigenvalue, List<Double>>() {
			@Override
			public DayEigenvalue getKey(ArticlePcValue t) {
				return new DayEigenvalue(t.getArticle().getDayIndex(), t.getEigenvalue());
			}
			@Override
			public List<Double> buildNew() {
				return new LinkedList<>();
			}
			@Override
			public void addValue(ArticlePcValue t, List<Double> aggregation) {
				aggregation.add(t.getValue());
			}
		};
		
		return mapBuilder.build(vo, artPcValList);
	}
	
	
	
	static List<ArticlePcValue> retrieveArticlePcValues(int scoringModelId, int minDayIndex) {
		Query query = SessionManager.createQuery("from ArticlePcValue where eigenvalue.scoringModel.id = :smId" +
				" and article.dayIndex >= :minDayIndex");
		query.setInteger("smId", scoringModelId);
		query.setInteger("minDayIndex", minDayIndex);
		
		return Utilities.convertGenericList(query.list());
	}
	
	static class DayEigenvalue {
		public final int dayIndex;
		public final Eigenvalue eig;
		public DayEigenvalue(int dayIndex, Eigenvalue eig) {
			this.dayIndex = dayIndex;
			this.eig = eig;
		}

		@Override
		public int hashCode() {
			return (31*dayIndex) + eig.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (null == obj) {
				return false;
			}
			if (! (obj instanceof DayEigenvalue)) {
				return false;
			}
			
			DayEigenvalue other = (DayEigenvalue)obj;
			
			return (dayIndex == other.dayIndex) && (eig == other.eig);
		}
	}
}
