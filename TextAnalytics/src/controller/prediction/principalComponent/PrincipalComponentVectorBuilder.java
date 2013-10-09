package controller.prediction.principalComponent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import orm.Eigenvalue;
import orm.SessionManager;

class PrincipalComponentVectorBuilder {

	public List<PrincipalComponentVector> build(int scoringModelId, int numStems) {
		System.out.println("retrieving principal components");

		System.out.println("get eigenvalues");
		Map<Integer, Eigenvalue> idEigenvalueMap = getIdEigenvalueMap(scoringModelId);
		
		System.out.println("initial query of raw values");
		List<Object[]> rowList = retrieveRawValues(scoringModelId);
		
		System.out.println("process raw values");
		Map<Integer, PrincipalComponentVector> eigIdPcVectMap = processRawValues(rowList, numStems, idEigenvalueMap);
		
		return buildListSortedById(eigIdPcVectMap);
	}
	
	static List<PrincipalComponentVector> buildListSortedById(Map<Integer, PrincipalComponentVector> eigIdPcVectMap) {
		
		
		List<Integer> idList = new ArrayList<>(eigIdPcVectMap.keySet());
		Collections.sort(idList);
		
		List<PrincipalComponentVector> result = new ArrayList<>(idList.size());
		for (Integer id : idList) {
			result.add(eigIdPcVectMap.get(id));
		}
		
		return result;
	}
	

	static Map<Integer, Eigenvalue> getIdEigenvalueMap(int scoringModelId) {
		Map<Integer, Eigenvalue> result = new HashMap<>();
		
		for (Eigenvalue eig : Eigenvalue.getEigenvalueByScoringModel(scoringModelId)) {
			result.put(eig.getId(), eig);
		}
		
		return result;
	}
	
	static Map<Integer, PrincipalComponentVector> processRawValues(List<Object[]> rowList, int numStems, 
			Map<Integer, Eigenvalue> idEigMap) {
		
		Map<Integer, PrincipalComponentVector> eigIdPcVectMap = new HashMap<Integer, PrincipalComponentVector>();

		int progress = 0;
		for (Object[] row : rowList) {
			Integer eigId = ((BigDecimal)row[0]).intValueExact();

			PrincipalComponentVector pcVect = eigIdPcVectMap.get(eigId);
			if (null == pcVect) {
				pcVect = new PrincipalComponentVector(idEigMap.get(eigId), numStems);
				eigIdPcVectMap.put(eigId, pcVect);
			}
			
			Integer stemId = ((BigDecimal)row[1]).intValueExact();
			Double value = ((BigDecimal)row[2]).doubleValue();
			pcVect.vector.setEntry(stemId-1, value);
			
			progress++;
			if (progress%1000000 == 0) {
				System.out.println("processed raw rows:  " + progress);
			}
		}
		
		return eigIdPcVectMap;
	}
	
	@SuppressWarnings("unchecked")
	static List<Object[]> retrieveRawValues(int scoringModelId) {
		Query query = SessionManager.createSqlQuery("select eigenvalue_id, stem_id, value from principal_component " +
				"where eigenvalue_id in (select id from eigenvalue where scoring_model_id = :smId)");
		query.setInteger("smId", scoringModelId);
		
		return query.list();
	}
}
