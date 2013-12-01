package orm.postgres;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

import orm.Company;
import orm.SessionManager;

public class BasicConnectionTest {

	@Test
	public void testDirect() throws SQLException {
		//based on:
		//http://jdbc.postgresql.org/documentation/80/connect.html
		
		final String url = "jdbc:postgresql://54.204.3.133/text_analytics";
		Properties props = new Properties();
		props.setProperty("user", "postgres");
		props.setProperty("password", "VR2tommPC5AWAwHW");
		Connection conn = DriverManager.getConnection(url, props);
		
		ResultSet rs = conn.createStatement().executeQuery("select * from company");

		while (rs.next()) {
			System.out.println(rs.getObject(1) + " " + rs.getObject(2));
		}
		conn.close();
	}
	

	@Test
	public void test() {
		
		for (Object obj : SessionManager.createQuery("from Company").list()) {
			System.out.println((Company)obj);
		}
	}

}
