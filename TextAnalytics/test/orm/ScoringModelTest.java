package orm;

import static org.junit.Assert.*;

import java.util.List;

import org.hibernate.Query;
import org.junit.Test;

import controller.util.Utilities;

public class ScoringModelTest {

	@Test
	public void test() {
		Query query = SessionManager.createQuery("from ScoringModel order by id");
		List<ScoringModel> smList = Utilities.convertGenericList(query.list());
		for (ScoringModel sm : smList) {
			assertNotNull(sm);
			System.out.println(sm);
		}
	}

}
