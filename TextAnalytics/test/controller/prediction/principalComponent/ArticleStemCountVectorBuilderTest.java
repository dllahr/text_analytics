package controller.prediction.principalComponent;

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

import orm.Article;
import orm.ArticleStemCount;

public class ArticleStemCountVectorBuilderTest {

	@Test
	public void testRetrieveArticleStemCount() throws ParseException {
		Date minDate = (new SimpleDateFormat("yyyy-MM-dd")).parse("2013-05-09");
		List<Integer> articleIdList = Article.retrieveArticleIdsForMinDateAndScoringModel(minDate, null, 1);
		
		List<ArticleStemCount> list = (new ArticleStemCountVectorBuilder()).retrieveArticleStemCount(articleIdList);
		assertTrue(list.size() > 0);
	}
	
	@Test
	public void testBuildArticleStemCountVectorMap() throws ParseException {
		Date minDate = (new SimpleDateFormat("yyyy-MM-dd")).parse("2013-05-09");
		List<Integer> articleIdList = Article.retrieveArticleIdsForMinDateAndScoringModel(minDate, null, 1);
		
		Map<Article, SparseVector> map = (new ArticleStemCountVectorBuilder()).buildArticleStemCountVectorMap(articleIdList, 1);
		
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
