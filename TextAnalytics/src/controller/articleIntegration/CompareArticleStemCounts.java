package controller.articleIntegration;

import java.util.ArrayList;
import java.util.List;

import math.linearAlgebra.Matrix;
import math.linearAlgebra.Operations;
import math.linearAlgebra.Vector;

import orm.Article;

import controller.stemCountArticles.ArticleStemCountVector;
import controller.stemCountArticles.ArticleStemCountVectorBuilder;
import controller.util.Triple;



public class CompareArticleStemCounts {
	
	
	public List<Triple<Article, Article, Double>> compare(List<Integer> articleIdList) {
		System.out.println("retrieve article stem count vectors");
		List<ArticleStemCountVector> vectorList = (new ArticleStemCountVectorBuilder()).retrieve(articleIdList, 1, false);
		
		System.out.println("calculate distances");
		return compareVectors(vectorList);
	}
	
	
	static List<Triple<Article, Article, Double>> compareVectors(List<ArticleStemCountVector> artScVectorList) {
		List<Vector> vectList = new ArrayList<>(artScVectorList.size());
		
		for (ArticleStemCountVector artScVect : artScVectorList) {
			vectList.add(artScVect.stemCountVector);
		}
		
		Matrix distMat = Operations.calculateUpperTriangleDistanceMatrix(vectList);

		final int size = (artScVectorList.size() + 1) * (artScVectorList.size()) / 2;
		List<Triple<Article, Article, Double>> result = new ArrayList<>(size);
		
		for (int rowInd = 0; rowInd < distMat.getNumRows(); rowInd++) {
			Article rowArticle = artScVectorList.get(rowInd).article;
			
			for (int colInd = rowInd + 1; colInd < distMat.getNumCols(); colInd++) {
				Article colArticle = artScVectorList.get(colInd).article;
				
				Triple<Article, Article, Double> entry = new Triple<>();
				entry.t = rowArticle;
				entry.u = colArticle;
				entry.v = distMat.getEntry(rowInd, colInd);
				
				result.add(entry);
			}
		}
		
		return result;
	}

}
