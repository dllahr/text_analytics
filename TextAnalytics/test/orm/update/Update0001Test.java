package orm.update;

import static org.junit.Assert.*;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.hibernate.jdbc.Work;
import org.junit.Test;

import orm.SessionManager;

public class Update0001Test {

	@Test
	public void testBuildArticleIdArrayString() {
		Integer[] result = Update0001.buildArticleIdArrayString(301);
		assertNotNull(result);
		assertTrue(result.length > 0);
		
		for (int i = 0; i < 10; i++) {
			System.out.print(result[i] + " ");
		}
		System.out.println();
		for (int i = result.length-10; i < result.length; i++) {
			System.out.print(result[i] + " ");
		}
		System.out.println();
	}
	
	@Test
	public void testInsertArray() {
		final Integer[] array = {2,3,5};
		
		Work work = new Work() {
			
			@Override
			public void execute(Connection conn) throws SQLException {
				Array sqlArray = conn.createArrayOf("integer", array);
				PreparedStatement ps = conn.prepareStatement("insert into scoring_model_article (scoring_model_id, article_id_array)"
						+ " values(4, ?)");
				ps.setArray(1, sqlArray);
				ps.executeUpdate();
			}
		};
		
		SessionManager.doWork(work);
//		SessionManager.commit();
	}

}
