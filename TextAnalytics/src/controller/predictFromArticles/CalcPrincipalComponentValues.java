package controller.predictFromArticles;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;

import orm.Article;
import orm.SessionManager;

public class CalcPrincipalComponentValues {
	protected static final String articleParamName = "articleIdList";
	
	protected static final String queryStr = "INSERT INTO article_pc_value (article_id, eigenvalue_id, value) "
			+ "SELECT artsc.article_id, pc.eigenvalue_id, SUM(artsc.count * pc.value) product "  
			+ "FROM article_stem_count artsc "
			+ "JOIN principal_component pc ON pc.stem_id=artsc.stem_id "
			+ "WHERE artsc.article_id IN (:" + articleParamName + ") "
			+ "GROUP BY artsc.article_id, pc.eigenvalue_id "; 
	
	private static final int batchSize = 1000;
	
	public static void calcPrincipalComponentValuesForArticles(List<Article> articleList) {
		List<Integer> articleIdList = new ArrayList<>(articleList.size());
		for (Article article : articleList) {
			articleIdList.add(article.getId());
		}
		
		Query query = SessionManager.createSqlQuery(queryStr);
		
		final int numBatches = articleList.size() / batchSize;
		final boolean hasRemaining = (articleList.size() % batchSize) > 0;
		for (int i = 0; i < numBatches; i++) {
			final int startIndex = i*batchSize;
			final int endIndex = (i+1)*batchSize;
			List<Integer> curIdList = articleIdList.subList(startIndex, endIndex);

			query.setParameterList(articleParamName, curIdList);
			query.executeUpdate();
		}
		
		if (hasRemaining) {
			final int startIndex = batchSize*numBatches;
			List<Integer> curIdList = articleIdList.subList(startIndex, articleIdList.size());
			query.setParameterList(articleParamName, curIdList);
			query.executeUpdate();
		}
	}
	
}
