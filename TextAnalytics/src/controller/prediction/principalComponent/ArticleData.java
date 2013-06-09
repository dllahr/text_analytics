package controller.prediction.principalComponent;

import java.util.HashMap;
import java.util.Map;

import math.linearAlgebra.Vector;
import orm.Article;
import orm.Eigenvalue;

public class ArticleData {
	final public Article article;
	
	public Vector stemCountVector;
	
	public final Map<Eigenvalue, Double> prinCompValuesMap;

	public ArticleData(Article article, Vector stemCountVector) {
		this.article = article;
		this.stemCountVector = stemCountVector;
		
		prinCompValuesMap = new HashMap<Eigenvalue, Double>();
	}
	
	
	
}
