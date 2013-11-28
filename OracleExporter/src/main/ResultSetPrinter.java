package main;

import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetPrinter {
	private static final String delimeter = ",";
	private static final long lineBatch = 50000;
	
	public static void print(ResultSet rs, int numCols, PrintStream out) throws SQLException {
		
		long row = 0;
		while (rs.next()) {
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i < numCols; i++) {
				sb.append(rs.getObject(i)).append(delimeter);
			}
			sb.append(rs.getObject(numCols));

			out.println(sb.toString());
			
			if ((row%lineBatch) == 0) {
				System.err.println(row);
			}
			row++;
		}
	}
}
