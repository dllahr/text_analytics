package controller.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLookup {
	private static final String propertyFile = "resources/config.properties";
	
	private static Properties properties = null;
	
	public static final String hibernateConfigFileKey = "hibernateconfigfile";
	public static final String gateHomeKey = "gatehome";
	
	public static String getProperty(String key) throws IOException {
		if (null == properties) {
			readProperties();
		}
		
		return properties.getProperty(key);
	}
	
	private static void readProperties() throws IOException {
		properties = new Properties();
		
		properties.load(new FileInputStream(propertyFile));
	}

	public static String getHibernateConfigFile() throws IOException {
		return getProperty(hibernateConfigFileKey);
	}
	
	public static String getGateHome() throws IOException {
		return getProperty(gateHomeKey);
	}
}
