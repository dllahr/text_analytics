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
		ScoringModel sm = (ScoringModel)SessionManager.createQuery("from ScoringModel where id = 2").list().get(0);
		assertNotNull(sm);
		
		return sm;
	}

	@Test
	public void testLoadMeanStemCountVector() throws IOException {
		ScoringModel sm = getScoringModel();
		
		List<Stem> stemList = MainLoadEigPrincComp.getStems(sm);
		
		File meanStemVectFile = new File("E:/daves_stuff/projects/text_analytics/companies/ge/models/model_jb/cov_eig_prinComp/mean_stem_vect.csv");
//		assertTrue("do not run this test because it commits unless you really mean to", false);
		MainLoadEigPrincComp.loadMeanStemVect(meanStemVectFile, sm, stemList);
	}
	
	@Test
	public void testCheckForSameParent() {
		File[] fileArray = {new File("/a/b"), new File("/a/c"), new File("/a/d")};
		
		assertTrue(MainLoadEigPrincComp.haveSameParent(fileArray));
		
		fileArray[2] = new File("/e/f");
		
		assertFalse(MainLoadEigPrincComp.haveSameParent(fileArray));
	}
}
