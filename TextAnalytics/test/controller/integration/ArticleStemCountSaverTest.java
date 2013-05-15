package controller.integration;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import controller.dateExtractionConversion.ArticleFileDatePair;
import controller.stemCountArticles.ArticleStemCountSaver;
import controller.util.Utilities;

import orm.ArticleStemCount;
import orm.ScoringModel;
import orm.SessionManager;
import orm.Stem;

public class ArticleStemCountSaverTest {

	@Test
	public void testSave() {
		SessionManager.setUseForTest(true);
		
		ScoringModel sm = new ScoringModel();
		sm.setId(1);
		SessionManager.persist(sm);
		
		Stem stem = new Stem();
		stem.setId(1);
		stem.setText("one");
		stem.setScoringModel(sm);
		SessionManager.persist(stem);
		
		ArticleFileDatePair articleFileDatePair = new ArticleFileDatePair(new File("fake file"), new Date());
		
		Map<String, Integer> stemCountMap = new HashMap<String, Integer>();
		stemCountMap.put(stem.getText(), 2);
		stemCountMap.put("two", 3);
		
		ArticleStemCountSaver.saveStemCountToDatabase(stemCountMap, articleFileDatePair, sm);
		
//		SessionManager.commit();
		
		List<ArticleStemCount> result = Utilities.convertGenericList(SessionManager.createQuery("from ArticleStemCount order by stem.text").list());
		
		assertEquals(2, result.size());
		
		ArticleStemCount first = result.get(0);
		assertEquals(stem.getId(), first.getStem().getId());
		assertEquals(stem.getText(), first.getStem().getText());
		assertEquals(2, (long)first.getCount());
		assertNotNull(first.getArticle());
		
		ArticleStemCount second = result.get(1);
		assertEquals("two", second.getStem().getText());
		assertEquals(3, (long)second.getCount());
		assertNotNull(second.getArticle());
	}

}
