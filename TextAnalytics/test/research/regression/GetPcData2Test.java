package research.regression;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Query;
import org.junit.Test;

import controller.util.Utilities;

import orm.Eigenvalue;
import orm.SessionManager;

public class GetPcData2Test {

	@Test
	public void test() {
		
		Query query = SessionManager.createQuery("select count(*) from EigenvectorValue where eigenvalue.company.id=1 group by eigenvalue");
		query.setMaxResults(1);
		
		double count = (double)(Long)(query.list().get(0));
		System.out.println(count);
		
		final double threshold = 0.25;
		
		int lowerThresholdInd = (int)(Math.round(count*threshold));
		int upperThresholdInd = (int)(Math.round(count*(1.0 - threshold)));
		System.out.println(lowerThresholdInd + " " + upperThresholdInd);
		
		
		query = SessionManager.createQuery("from Eigenvalue where company.id=1");
		List<Eigenvalue> eigvalList = Utilities.convertGenericList(query.list());
		
		final String eigvalParam = "eigval";
		query = SessionManager.createQuery("select value from EigenvectorValue where eigenvalue = :" + eigvalParam + " order by value asc");
		query.setMaxResults(1);
		
		for (Eigenvalue eigval : eigvalList) {
			query.setParameter(eigvalParam, eigval);

			Thresholds thresholds = new Thresholds();
			
			query.setFirstResult(lowerThresholdInd-1);
			List result = query.list();
//			System.out.println("size:  " + result.size());
			if (result.size() > 0) {
				thresholds.lowerThreshold = (double)result.get(0);
			
				query.setFirstResult(upperThresholdInd-1);
				result = query.list();
				thresholds.upperThreshold = (double)result.get(0);
				
				System.out.println(eigval.getId() + " " + thresholds.lowerThreshold + " "+ thresholds.upperThreshold);
			}
		}
		
		
		SessionManager.closeAll();
	}

}
