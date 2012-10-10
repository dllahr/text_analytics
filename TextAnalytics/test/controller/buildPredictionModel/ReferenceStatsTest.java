package controller.buildPredictionModel;

import static org.junit.Assert.*;

import org.junit.Test;

import orm.Company;
import orm.SessionManager;

public class ReferenceStatsTest {

	@Test
	public void test() {
		Company company = (Company)SessionManager.createQuery("from Company where id=2").list().get(0);
		
		ReferenceStats.calcStats(company);
		
		SessionManager.closeAll();
	}

}
