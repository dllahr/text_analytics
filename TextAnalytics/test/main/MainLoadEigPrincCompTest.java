package main;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import orm.Article;
import orm.ScoringModel;
import orm.SessionManager;
import orm.Stem;

public class MainLoadEigPrincCompTest {

	@Test
	public void testGetArticles() {
		List<Article> list = MainLoadEigPrincComp.getArticles(getScoringModel());
		assertTrue(list.size() > 0);
		
		int prevId = list.get(0).getId() - 1;
		for (Article article : list) {
			assertTrue(article.getId() > prevId);
			prevId = article.getId();
		}
	}
	
	@Test
	public void testGetStems() {
		List<Stem> list = MainLoadEigPrincComp.getStems(getScoringModel());
		assertTrue(list.size() > 0);
		
		int prevId = list.get(0).getId() - 1;
		for (Stem stem : list) {
			assertTrue(stem.getId() > prevId);
			prevId = stem.getId();
		}
	}
	
	private ScoringModel getScoringModel() {
		ScoringModel sm = (ScoringModel)SessionManager.createQuery("from ScoringModel where id = 1").list().get(0);
		assertNotNull(sm);
		
		return sm;
	}

}
