package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import database.DbConn;

public class ImportArticleScore {
	
	private static final String delimeter = ",";
	
	private static final String insertString = "insert into article_pc_value (article_id, eigenvalue_id, value) values (?, ?, ?)";

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws IOException, SQLException {
		if (args.length != 3) {
			System.out.println("usage:  importArticleScoreToOracleXe <article score file> <first article ID> <first eigenvalue ID>");
			System.out.println("<article score file> is a csv file (no header) of a matrix of article scores, each row is for an individual article, each column contains the scores for a principal component corresponding to an eigenvector");
			System.out.println("<first article ID> ID of the first article");
			System.out.println("<first eigenvalue ID> ID of the first eigenvalue");
			System.exit(0);
		}
		
		String articleScoreFilePath = args[0];
		final int firstArticleId = Integer.valueOf(args[1]);
		final int firstEigenvalueId = Integer.valueOf(args[2]);
		
		System.out.println("Loading article scores:");
		System.out.println("article score file:  " + articleScoreFilePath);
		System.out.println("first article ID:  " + firstArticleId);
		System.out.println("first eigenvalue ID:  " + firstEigenvalueId);
		
		DbConn dbConn = new DbConn();
		PreparedStatement insertStatement = dbConn.getConnection().prepareStatement(insertString);
		
		int currentArticleId = firstArticleId;
		
		BufferedReader reader = new BufferedReader(new FileReader(articleScoreFilePath));
		String curLine;
		while ((curLine = reader.readLine()) != null) {
			String[] curLineSplit = curLine.split(delimeter);
			
			int currentEigenvalueId = firstEigenvalueId;
			for (String valueString : curLineSplit) {
				double value = Double.valueOf(valueString);
				
				insertStatement.setInt(1, currentArticleId);
				insertStatement.setInt(2, currentEigenvalueId);
				insertStatement.setDouble(3, value);
				insertStatement.addBatch();
				
				currentEigenvalueId++;
			}
		
			currentArticleId++;
		}
		
		insertStatement.executeBatch();
		
		reader.close();
		dbConn.closeConnection();

	}

}
