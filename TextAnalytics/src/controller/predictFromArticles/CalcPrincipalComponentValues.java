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
	
	public static void calcPrincipalComponentValuesForArticles(List<Article> articleList) {
		List<Integer> articleIdList = new ArrayList<>(articleList.size());
		for (Article article : articleList) {
			articleIdList.add(article.getId());
		}
		
		Query query = SessionManager.createSqlQuery(queryStr);
		query.setParameterList(articleParamName, articleIdList);
		query.executeUpdate();
	}
}
