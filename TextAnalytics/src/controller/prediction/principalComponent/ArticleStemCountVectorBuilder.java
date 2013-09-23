package controller.prediction.principalComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import math.linearAlgebra.SparseVector;

import org.hibernate.Query;

import orm.Article;
import orm.ArticleStemCount;
import orm.SessionManager;
import controller.util.BatchRetriever;
import controller.util.GenericRetriever;
import controller.util.MapBuilder;
import controller.util.Utilities;
import controller.util.ValueOperator;

public class ArticleStemCountVectorBuilder {

	public List<ArticleStemCountVector> retrieve(List<Integer> articleIdList, int minStemId, boolean noStopWords) {

		Map<Article, SparseVector> map = buildArticleStemCountVectorMap(articleIdList, minStemId, noStopWords);
		
		List<ArticleStemCountVector> result = new ArrayList<>(map.size());
		
		for (Article article : map.keySet()) {
			SparseVector vector = map.get(article);
			
			result.add(new ArticleStemCountVector(article, vector));
		}
		
		return result;
	}
	
	Map<Article, SparseVector> buildArticleStemCountVectorMap(List<Integer> articleIdList, final int minStemId, 
			boolean noStopWords) {
		
		List<ArticleStemCount> articleStemCountList = retrieveArticleStemCount(articleIdList, noStopWords);
		
		ValueOperator<ArticleStemCount, Article, SparseVector> vo = new ValueOperator<ArticleStemCount, Article, SparseVector>() {
			@Override
			public Article getKey(ArticleStemCount t) {
				return t.getArticle();
			}
			@Override
			public SparseVector buildNew() {
				return new SparseVector();
			}
			@Override
			public void addValue(ArticleStemCount t, SparseVector aggregation) {
				aggregation.setEntry(t.getStem().getId() - minStemId, (double)t.getCount());
			}
		}; 

		return (new MapBuilder()).build(vo, articleStemCountList);
	}
	
	List<ArticleStemCount> retrieveArticleStemCount(List<Integer> articleIdList, boolean noStopWords) {
		String queryString = "from ArticleStemCount where article.id in (:articleIdList)";
		if (noStopWords) {
			queryString += " and stem.isStop = false";
		}
		final Query query = SessionManager.createQuery(queryString);
		
		GenericRetriever<Integer> gr = new GenericRetriever<Integer>() {
			@SuppressWarnings("rawtypes")
			@Override
			public List retrieve(Collection<Integer> coll) {
				query.setParameterList("articleIdList", coll);
				return query.list();
			}
		};
		
		BatchRetriever<Integer> br = new BatchRetriever<>(gr);

		return Utilities.convertGenericList(br.retrieveAll(articleIdList));
	}
}
