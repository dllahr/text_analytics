package orm;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
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

	@Test
	public void testGetArticleIds() throws HibernateException, SQLException {
		List<Integer> smIds = Utilities.convertGenericList(SessionManager.createQuery("select id from ScoringModel").list());
		assertTrue(smIds.size() > 0);
		
		for (Integer smId : smIds) {
			Integer[] aIds = ScoringModel.getArticleIds(smId);
			
			System.out.print("smId:  " + smId);
			if (aIds != null) {
				assertTrue(aIds.length > 0);
				System.out.println("  aIds.length:" + aIds.length + "  aIds[0]:" + aIds[0] + "  aIds[last]:" + aIds[aIds.length-1]);
			} else {
				System.out.println(" no article Ids found");
			}
			
		}
	}
}
