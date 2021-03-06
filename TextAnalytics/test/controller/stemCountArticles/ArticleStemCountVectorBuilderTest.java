package controller.stemCountArticles;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import math.linearAlgebra.SparseVector;

import org.junit.Test;

import controller.stemCountArticles.ArticleStemCountVectorBuilder;

import orm.Article;
import orm.ArticleStemCount;

public class ArticleStemCountVectorBuilderTest {

	@Test
	public void testRetrieveArticleStemCount() throws ParseException {
		Date minDate = (new SimpleDateFormat("yyyy-MM-dd")).parse("2013-05-09");
		List<Integer> articleIdList = Article.getArticleIdsForMinDateAndArticleSource(minDate, null, 1, false, false);
		
		List<ArticleStemCount> list = (new ArticleStemCountVectorBuilder()).retrieveArticleStemCount(articleIdList, false);
		assertTrue(list.size() > 0);
	}
	
	@Test
	public void testBuildArticleStemCountVectorMap() throws ParseException {
		Date minDate = (new SimpleDateFormat("yyyy-MM-dd")).parse("2013-05-09");
		List<Integer> articleIdList = Article.getArticleIdsForMinDateAndArticleSource(minDate, null, 1, false, false);
		
		Map<Article, SparseVector> map = (new ArticleStemCountVectorBuilder()).buildArticleStemCountVectorMap(articleIdList, 1, false);
		
		Article article = map.keySet().iterator().next();
		System.out.println(article.getId() + " " + article.getPublishDate() + " " + article.getFilename());
		
		SparseVector v = map.get(article);
		List<Integer> indexList = new ArrayList<>(v.getIndices());
		Collections.sort(indexList);
		for (Integer index : indexList) { 
			System.out.println(index + " " + v.getEntry(index));
		}
	}

}
