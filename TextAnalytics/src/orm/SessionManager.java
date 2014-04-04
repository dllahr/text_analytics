package orm;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;

import controller.util.PropertiesLookup;


public class SessionManager {
	
	private static boolean useForTest = false;
	
	private static boolean displaySql = false;
	
	private static SessionFactory sessionFactory = null;
	
	private static Session session = null;
	
	private static Session getSession() {
		if (null == sessionFactory) {
			sessionFactory = buildSessionFactory();
		}
		
		if (null == session) {
			session = sessionFactory.getCurrentSession();
			session.beginTransaction();
		}

		
		return session;
	}
	
	public static Query createQuery(String queryStr) {
		return getSession().createQuery(queryStr);
	}
	
	public static SQLQuery createSqlQuery(String sqlQueryStr) {
		return getSession().createSQLQuery(sqlQueryStr);
	}
	
	public static ResultSet resultSetQuery(final String sqlQueryStr) throws HibernateException, SQLException {
		class MyWork implements Work {
			private ResultSet resultSet;
			private final String sqlQueryStr;
			
			public MyWork(String sqlQuery) {
				this.sqlQueryStr = sqlQuery;
			}
			
			@Override
			public void execute(Connection conn) throws SQLException {
				resultSet = conn.createStatement().executeQuery(sqlQueryStr);
			}
			
			public ResultSet getResultSet() {
				return resultSet;
			}
		}

		MyWork myWork = new MyWork(sqlQueryStr);
		getSession().doWork(myWork);
		return myWork.getResultSet();
	}
	
	public static void persist(Object obj) {
		getSession().persist(obj);
	}
	
	public static Object merge(Object obj) {
		return getSession().merge(obj);
	}
	
	public static void commit() {
		getSession().getTransaction().commit();
		if (session.isOpen()) {
			session.close();
		}
		session = null;
	}
	
	public static void closeAll() {
		if (session != null) {
			session.close();
			session = null;
		}
		if (sessionFactory != null) {
			sessionFactory.close();
			sessionFactory = null;
		}
	}
	
	protected static SessionFactory buildSessionFactory() {
		File cfgFile = null;
		try {
			cfgFile = new File(PropertiesLookup.getHibernateConfigFile());
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
		
		Configuration config = new Configuration();
		config.configure(cfgFile);
		if (useForTest) {
			config.setProperty("hibernate.connection.username", "tester");
			config.setProperty("hibernate.connection.password", "u3pSVrMs79xG7mnJ");
		}
		
		config.setProperty("hibernate.show_sql", String.valueOf(displaySql));
		
		return config.buildSessionFactory();
	}
	
	public static void setUseForTest(boolean useForTest) {
		SessionManager.useForTest = useForTest;
	}
	public static void setDisplaySql(boolean displaySql) {
		SessionManager.displaySql = displaySql;
	}
	
	public static void doWork(Work work) {
		getSession().doWork(work);
	}
	
	public static void displaySql() {
		
	}
}
