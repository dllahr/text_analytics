package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import database.DbConn;;

public class ImportEigenToOracleXe {
	private static final String delimeter = " ";
	private static final String maxEigenvalueIdQuery = "select max(id) from eigenvalue";
	private static final String eigenValueInsert = "insert into eigenvalue (id, scoring_model_id, sort_index, value) " +
			"values (?, ?, ?, ?)";
	private static final String eigenVectorInsert = "insert into eigenvector_value (eigenvalue_id, article_id, value) " +
			"values (?, ?, ?)";
	
	/**
	 * @param args
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws SQLException, IOException {
		if (args.length != 4) {
			System.out.println("usage:  importEigenToOracleXe <eigenvalue file> <eigenvector file> <scoring model ID> <first article ID>");
			System.out.println("<eigenvalue file> is single column of eigenvalues in order");
			System.out.println("<eigenvector file> is eigenvectors in columns, first column corresponds to first eigenvalue entry in <eigenvalue file>");
			System.out.println("<scoring model ID> ID of scoring model to load these eigenvalues against");
			System.out.println("<first article ID> first ID of the articles for the eigenvectors");
			System.exit(0);
		}
		
		final String eigenValueFile = args[0];
		final String eigenVectorFile = args[1];
		final int scoringModelId = Integer.valueOf(args[2]);
		final int firstArticleId = Integer.valueOf(args[3]);
		
		System.out.println("Loading eigenvalues and eigenvectors.  Parameters:");
		System.out.println("eigenvalue file:  " + eigenValueFile);
		System.out.println("eigenvector file: " + eigenVectorFile);
		System.out.println("scoring model ID:  " + scoringModelId);
		System.out.println("first article ID:  " + firstArticleId);
		
		DbConn dbConn = new DbConn();
		
		System.out.println("Loading eigenvalues");
		final int[] eigenvalueInfo = loadEigenvalues(dbConn, eigenValueFile, scoringModelId);
		final int firstEigenvalueId = eigenvalueInfo[0];
		
		System.out.println("Loading eigenvectors");
		loadEigenvectors(dbConn, eigenVectorFile, firstEigenvalueId, firstArticleId);
		
		dbConn.getConnection().commit();
		dbConn.closeConnection();
	}

	
	/**
	 * upload eigenvector values to OracleXE database
	 * @param dbConn
	 * @param eigenVectorFile
	 * @param firstEigenvalueId
	 * @param firstArticleId
	 * @throws IOException
	 * @throws SQLException
	 */
	private static void loadEigenvectors(DbConn dbConn, String eigenVectorFile, final int firstEigenvalueId, 
			final int firstArticleId) throws IOException, SQLException {
		PreparedStatement preparedStatement = dbConn.getConnection().prepareStatement(eigenVectorInsert);
		
		BufferedReader reader = new BufferedReader(new FileReader(eigenVectorFile));
		
		int eigIndex = 0;
		String curLine;
		while ((curLine = reader.readLine()) != null) {
			String[] valueArray = curLine.trim().split(delimeter);
			
			int articleIndex = 0;
			for (String value : valueArray) {
				preparedStatement.setInt(1, firstEigenvalueId + eigIndex);
				preparedStatement.setInt(2, firstArticleId + articleIndex);
				preparedStatement.setDouble(3, Double.valueOf(value));
				
				preparedStatement.addBatch();
				
				articleIndex++;
			}

			preparedStatement.executeBatch();

			eigIndex++;
		}
		
		reader.close();		
	}


	/**
	 * reads eigenvalues from a single line of a text file, loads it into OracleXE database. Returns array
	 * containing the ID used for the first eigenvalue loaded and the number of eigenvalues loaded
	 * 
	 * @param dbConn
	 * @param eigenValueFile
	 * @param scoringModelId
	 * @return number of eigenvalues loaded
	 * @throws IOException
	 * @throws SQLException
	 */
	private static int[] loadEigenvalues(DbConn dbConn, String eigenValueFile, int scoringModelId) throws IOException, SQLException {
		ResultSet rs = dbConn.getConnection().createStatement().executeQuery(maxEigenvalueIdQuery);
		rs.next();
		final int firstId = rs.getInt(1) + 1;
		
		BufferedReader reader = new BufferedReader(new FileReader(eigenValueFile));
		String curLine = reader.readLine().trim();
		reader.close();
		String[] valueArray = curLine.split(delimeter);

		PreparedStatement preparedStatement = dbConn.getConnection().prepareStatement(eigenValueInsert);
		
		int eigIndex = 0;
		for (String value : valueArray) {
			preparedStatement.setInt(1, eigIndex + firstId);
			preparedStatement.setInt(2, scoringModelId);
			preparedStatement.setInt(3, eigIndex);
			preparedStatement.setDouble(4, Double.valueOf(value));

			preparedStatement.addBatch();
			
			eigIndex++;
		}
		
		preparedStatement.executeBatch();
		
		return new int[]{firstId, eigIndex};
	}
}
