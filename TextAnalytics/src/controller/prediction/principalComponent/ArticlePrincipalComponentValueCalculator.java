package controller.prediction.principalComponent;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ArticlePrincipalComponentValueCalculator {

	public List<ArticlePrincipalComponentValues> calculate(int scoringModelId, Date minArticleDate) {
		MeanStemCountVector meanVect = (new MeanStemCountVectorBuilder()).build(scoringModelId);
		
		List<ArticleStemCountVector> artStemCountVectList = (new ArticleStemCountVectorBuilder()).retrieve(scoringModelId, 
				minArticleDate, meanVect.minStemId);
		
		List<PrincipalComponentVector> pcVectorList = 
				(new PrincipalComponentVectorBuilder()).build(scoringModelId, 
						meanVect.meanVector.getMaxIndex() + 1);
		
		//(a-m)*pc = a*pc - m*pc
		Map<PrincipalComponentVector, Double> pcVectDotMean = multiplyPrincipalComponentVectorsByMeanVector(pcVectorList, meanVect);

		List<ArticlePrincipalComponentValues> result = new ArrayList<>(artStemCountVectList.size());

		for (ArticleStemCountVector artVect : artStemCountVectList) {
			ArticlePrincipalComponentValues artPcv = new ArticlePrincipalComponentValues(artVect.article);
			result.add(artPcv);
			
			for (PrincipalComponentVector pcv : pcVectDotMean.keySet()) {
				double articleDotPcv = artVect.stemCountVector.vectorMultiply(pcv.vector);
				double value = articleDotPcv - pcVectDotMean.get(pcv);

				artPcv.prinCompValuesMap.put(pcv.eigenvalue, value);
			}
		}
		
		return result;
	}
	
	
	static Map<PrincipalComponentVector, Double> multiplyPrincipalComponentVectorsByMeanVector(List<PrincipalComponentVector> pcVectorList, 
			MeanStemCountVector meanVect) {
		System.out.println("multiply principal component vectors by mean vector");
		
		Map<PrincipalComponentVector, Double> result = new HashMap<PrincipalComponentVector, Double>();
		
		for (PrincipalComponentVector pcv : pcVectorList) {
			double dotProduct = pcv.vector.vectorMultiply(meanVect.meanVector);
			result.put(pcv, dotProduct);
			
			System.out.println("eig:  " + pcv.eigenvalue.getId() + " " + dotProduct);
		}
		
		return result;
	}
}
