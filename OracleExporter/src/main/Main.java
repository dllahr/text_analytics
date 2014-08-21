package main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import database.ResultSetPrinter;

public class Main {
	private static final String username = "personal";
	private static final String password = "JzRNIno7U9jCf3LK";
	private static final String connectionUrl = "jdbc:oracle:thin:@localhost:1521:XE";
//	private static final String username = "data_mig";
//	private static final String password = "H6iwlFd5QqQS";
//	private static final String connectionUrl = "jdbc:oracle:thin:@vmbarddev:1521:barddev";


	
	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		if (args.length == 0) {
			System.out.println("1st argument:  number of columns");
			System.out.println("2nd argument:  sql query");
			return;
		}

		final int numCols = Integer.valueOf(args[0]);
		final String queryStr = args[1];
		
		System.err.println("numCols: " + numCols);
		System.err.println("query: " + queryStr);
		Date startDate = new Date();
		System.err.println("started at: " + startDate);
		
		DbConn dbConn= new DbConn(username, password, connectionUrl);
		
		ResultSet rs = dbConn.getConnection().createStatement().executeQuery(queryStr);
		
		ResultSetPrinter.print(rs, numCols, System.out);
		
		Date endDate = new Date();
		double durationMinutes = ((double)(endDate.getTime() - startDate.getTime())) / 60000.0;
		System.err.println("finished at: " + endDate);
		System.err.println("duraction [min]: " + durationMinutes);
		
		dbConn.closeConnection();
	}

}
