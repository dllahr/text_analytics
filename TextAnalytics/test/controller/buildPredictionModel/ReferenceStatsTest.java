package controller.buildPredictionModel;

import static org.junit.Assert.*;

import org.junit.Test;

import orm.ScoringModel;
import orm.SessionManager;

public class ReferenceStatsTest {

	@Test
	public void test() {
		ScoringModel company = (ScoringModel)SessionManager.createQuery("from Company where id=2").list().get(0);
		
		ReferenceStats.calcStats(company);
		
		SessionManager.closeAll();
	}

}
