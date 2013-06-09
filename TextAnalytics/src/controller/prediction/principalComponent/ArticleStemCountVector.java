package controller.prediction.principalComponent;

import math.linearAlgebra.Vector;
import orm.Article;

public class ArticleStemCountVector {
	final public Article article;
	
	public Vector stemCountVector;

	public ArticleStemCountVector(Article article, Vector stemCountVector) {
		this.article = article;
		this.stemCountVector = stemCountVector;
	}
}
