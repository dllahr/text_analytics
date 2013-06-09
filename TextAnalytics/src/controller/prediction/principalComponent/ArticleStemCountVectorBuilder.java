package controller.prediction.principalComponent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import math.linearAlgebra.SparseVector;

import org.hibernate.Query;

import orm.Article;
import orm.ArticleStemCount;
import orm.SessionManager;
import controller.util.MapBuilder;
import controller.util.Utilities;
import controller.util.ValueOperator;

public class ArticleStemCountVectorBuilder {

	public List<ArticleData> retrieve(int scoringModelId, Date minDate, int minStemId) {

		Map<Article, SparseVector> map = buildArticleStemCountVectorMap(scoringModelId, minDate, minStemId);
		
		List<ArticleData> result = new ArrayList<>(map.size());
		
		for (Article article : map.keySet()) {
			SparseVector vector = map.get(article);
			
			result.add(new ArticleData(article, vector));
		}
		
		return result;
	}
	
	Map<Article, SparseVector> buildArticleStemCountVectorMap(int scoringModelId, Date minDate, final int minStemId) {
		List<ArticleStemCount> articleStemCountList = retrieveArticleStemCount(scoringModelId, minDate);
		
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
	
	List<ArticleStemCount> retrieveArticleStemCount(int scoringModelId, Date minDate) {
		Query query = SessionManager.createQuery("from ArticleStemCount where article.scoringModel.id = :smId and article.publishDate >= :date");
		query.setInteger("smId", scoringModelId);
		query.setDate("date", minDate);
		
		return Utilities.convertGenericList(query.list());
	}
}
