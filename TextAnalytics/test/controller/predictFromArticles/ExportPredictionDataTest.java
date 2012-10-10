package controller.predictFromArticles;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.junit.Test;

import tools.Data;

public class ExportPredictionDataTest {

	@Test
	public void test() throws HibernateException, SQLException {
		Data data = ExportPredictionData.retrieveData();
		assertNotNull(data);
		assertTrue(data.getFirstDimension() > 0);
		assertTrue(data.getSecondDimension() > 0);
		assertTrue(data.getColumnHeadersArray().length > 0);
	}

}
