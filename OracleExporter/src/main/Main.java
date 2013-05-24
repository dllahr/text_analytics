package main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Main {
	private static final String username = "personal";
	private static final String password = "JzRNIno7U9jCf3LK";
	private static final String connectionUrl = "jdbc:oracle:thin:@localhost:1521:XE";
//	private static final String username = "data_mig";
//	private static final String password = "H6iwlFd5QqQS";
//	private static final String connectionUrl = "jdbc:oracle:thin:@vmbarddev:1521:barddev";

	private static final String delimeter = ",";
	private static final long lineBatch = 50000;
	
	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		final int numCols = Integer.valueOf(args[0]);
		final String queryStr = args[1];
		
		System.err.println("numCols: " + numCols);
		System.err.println("query: " + queryStr);
		Date startDate = new Date();
		System.err.println("started at: " + startDate);
		
		DbConn dbConn= new DbConn(username, password, connectionUrl);
		
		ResultSet rs = dbConn.getConnection().createStatement().executeQuery(queryStr);
		
		long row = 0;
		while (rs.next()) {
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i < numCols; i++) {
				sb.append(rs.getObject(i)).append(delimeter);
			}
			sb.append(rs.getObject(numCols));

			System.out.println(sb.toString());
			
			if ((row%lineBatch) == 0) {
				System.err.println(row);
			}
			row++;
		}
		
		Date endDate = new Date();
		double durationMinutes = ((double)(endDate.getTime() - startDate.getTime())) / 60000.0;
		System.err.println("finished at: " + endDate);
		System.err.println("duraction [min]: " + durationMinutes);
		
		dbConn.closeConnection();
	}

}
