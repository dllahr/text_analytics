package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import oracle.jdbc.OracleDriver;

public class DbConn {
	private Connection connection = null;
	
	private static final String defaultUsername = "personal";
	private static final String defaultPassword = "JzRNIno7U9jCf3LK";
	private static final String defaultConnectionUrl = "jdbc:oracle:thin:@localhost:1521:XE";

	public DbConn(String username, String password,
			String connectionUrl) throws SQLException {
		
		Properties connectionProperties = new Properties();
		connectionProperties.put("user", username);
		connectionProperties.put("password", password);
		
		DriverManager.registerDriver(new OracleDriver());
		
		connection = DriverManager.getConnection(connectionUrl, connectionProperties);
	}
	
	public DbConn() throws SQLException {
		this(defaultUsername, defaultPassword, defaultConnectionUrl);
	}

	public  Connection getConnection() throws SQLException {
		return connection;
	}

	
	public void closeConnection() throws SQLException {
		connection.close();
		connection = null;
	}
}
