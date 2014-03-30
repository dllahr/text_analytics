package orm.update;

import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.jdbc.Work;

import controller.util.Utilities;
import orm.SessionManager;

public class Update0001 {

	public static void main(String[] args) {
		Query query = SessionManager.createSqlQuery("select scoring_model_id, min(id) from eigenvalue group by scoring_model_id");
		
		InsertWork insertWork = new InsertWork();
		
		for (Object rowObj : query.list()) {
			Object[] row = (Object[])rowObj;
			
			insertWork.scoringModelId = Integer.valueOf(row[0].toString());
			int eigId = Integer.valueOf(row[1].toString());
			
			System.out.println("scoringModelId:" + insertWork.scoringModelId + "  eidId:" + eigId);
			
			System.out.println("retrieve article ID's");
			insertWork.articleIdArray = buildArticleIdArrayString(eigId);
			
			System.out.println("inserting vector...  ");
			SessionManager.doWork(insertWork);
			System.out.println("  rowCount:" + insertWork.rowCount);
		}

		SessionManager.commit();
		SessionManager.closeAll();
	}
	
	static Integer[] buildArticleIdArrayString(int eigId) {
		Query articleIdQuery = SessionManager.createSqlQuery("select article_id from eigenvector_value where eigenvalue_id = :eigId order by article_id");
		articleIdQuery.setParameter("eigId", eigId);
		
		List<Integer> articleIdList = Utilities.convertGenericList(articleIdQuery.list());
		
		Integer[] result = articleIdList.toArray(new Integer[articleIdList.size()]);

		return result;
	}

	static class InsertWork implements Work {
		private static final String insertString = "insert into scoring_model_article (scoring_model_id, article_id_array) "
				+ "values (?, ?)";
		
		public Integer scoringModelId;
		
		public Integer[] articleIdArray;
		
		public int rowCount;
		
		public InsertWork() {
			scoringModelId = null;
			articleIdArray = null;
			
			rowCount = -1;
		}

		@Override
		public void execute(Connection conn) throws SQLException {
			Array sqlArray = conn.createArrayOf("integer", articleIdArray);
			PreparedStatement ps = conn.prepareStatement(insertString);
			ps.setInt(1, scoringModelId);
			ps.setArray(2, sqlArray);

			rowCount = ps.executeUpdate();
		}
	}
}
