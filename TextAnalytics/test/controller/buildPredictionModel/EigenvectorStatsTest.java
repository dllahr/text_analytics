package controller.buildPredictionModel;

import static org.junit.Assert.*;

import org.junit.Test;

import orm.Company;
import orm.SessionManager;
import orm.UtilitiesForTesting;

public class EigenvectorStatsTest {

	@Test
	public void test() {
		Company company = (Company)SessionManager.createQuery("from Company where id=1").list().get(0);
		
		EigenvectorStats eigenvectorStats = new EigenvectorStats();
		eigenvectorStats.doCalc(company);
	}

}
