package main;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
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

	@Test
	public void testLoadMeanStemCountVector() throws IOException {
		ScoringModel sm = getScoringModel();
		
		List<Stem> stemList = MainLoadEigPrincComp.getStems(sm);
		
		File meanStemVectFile = new File("E:/daves_stuff/projects/text_analytics/companies/kraft/model/just_text/cov_eig_prinComp/mean_stem_vect.csv");
		MainLoadEigPrincComp.loadMeanStemVect(meanStemVectFile, sm, stemList);
//		SessionManager.commit();
	}
}
