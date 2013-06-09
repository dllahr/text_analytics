package controller.prediction.principalComponent;

import java.util.HashMap;
import java.util.Map;

import orm.Article;
import orm.Eigenvalue;

public class ArticlePrincipalComponentValues {
	
	public final Article article;
	
	public final Map<Eigenvalue, Double> prinCompValuesMap;
	
	public ArticlePrincipalComponentValues(Article article) {
		this.article = article;
		
		prinCompValuesMap = new HashMap<Eigenvalue, Double>();
	}
}
