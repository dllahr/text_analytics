package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import oracle.jdbc.OracleDriver;

public class DbConn {
	private Connection cbipConnection = null;
	
	private static final String defaultUsername = "experiments";
	private static final String defaultPassword = "RvgL2quN";
	private static final String defaultConnectionUrl = "jdbc:oracle:thin:@localhost:1521:XE";

	public DbConn(String username, String password,
			String connectionUrl) throws SQLException {
		
		Properties connectionProperties = new Properties();
		connectionProperties.put("user", username);
		connectionProperties.put("password", password);
		
		DriverManager.registerDriver(new OracleDriver());
		
		cbipConnection = DriverManager.getConnection(connectionUrl, connectionProperties);
	}
	
	public DbConn() throws SQLException {
		this(defaultUsername, defaultPassword, defaultConnectionUrl);
	}

	public  Connection getConnection() throws SQLException {
		return cbipConnection;
	}

	
	public void closeConnection() throws SQLException {
		cbipConnection.close();
		cbipConnection = null;
	}
}
