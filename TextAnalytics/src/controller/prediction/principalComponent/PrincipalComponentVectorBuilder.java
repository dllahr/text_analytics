package controller.prediction.principalComponent;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;

import orm.Eigenvalue;
import orm.SessionManager;

class PrincipalComponentVectorBuilder {

	public List<PrincipalComponentVector> build(int scoringModelId, int numStems) {
		System.out.println("retrieving principal components");

		List<Object[]> rowList = retrieveRawValues(scoringModelId);
		
		return processRawValues(rowList, numStems);
	}
	
	static List<PrincipalComponentVector> processRawValues(List<Object[]> rowList, int numStems) {
		Eigenvalue prevEig = findEigenvalue(((BigDecimal)rowList.get(0)[0]).intValue());
		
		List<PrincipalComponentVector> result = new LinkedList<>();
		PrincipalComponentVector vector = new PrincipalComponentVector(prevEig, numStems);
		result.add(vector);
		
		int stemIndex = 0;

		for (Object[] row : rowList) {
			Integer curEigId = ((BigDecimal)row[0]).intValue();
			
			if (! prevEig.getId().equals(curEigId)) {
				System.out.println("current eigenvalue id:  " + curEigId);
				
				prevEig = findEigenvalue(curEigId);
				vector = new PrincipalComponentVector(prevEig, numStems);
				result.add(vector);
				
				stemIndex = 0;
			}
			
			vector.vector.setEntry(stemIndex, ((BigDecimal)row[1]).doubleValue());
			
			stemIndex++;
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	static List<Object[]> retrieveRawValues(int scoringModelId) {
		Query query = SessionManager.createSqlQuery("select eigenvalue_id, value from principal_component " +
				"where eigenvalue_id in (select id from eigenvalue where scoring_model_id = :smId) " +
				"order by eigenvalue_id, stem_id");
		query.setInteger("smId", scoringModelId);
		
		return query.list();
	}

	static Eigenvalue findEigenvalue(int id) {
		Query query = SessionManager.createQuery("from Eigenvalue where id = :id");
		query.setInteger("id", id);
		
		return (Eigenvalue)query.list().get(0);
	}
}
