package controller.predictFromArticles;

import static org.junit.Assert.*;

import gate.util.GateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import orm.Article;
import orm.Company;
import orm.SessionManager;

import controller.dateExtractionConversion.ArticleFileDatePair;

public class StemFrequencyDistributionAndCalcPcValsTest {

	@Test
	public void test() throws IOException, GateException {
		final int numFiles = 2001;
		final String basePath = "test/inputFiles/";
		
		List<ArticleFileDatePair> articleFileDatePairs = new ArrayList<>(numFiles);
		Date date = new Date();
		
		for (int i = 0; i < numFiles; i++) {
			String filename = basePath + i + ".txt";
			File file = new File(filename);
			FileWriter writer = new FileWriter(file);
			writer.write("a");
			writer.close();
		
			articleFileDatePairs.add(new ArticleFileDatePair(file, date));
		}
		
		Company company = (Company) SessionManager.createQuery("from Company").list().get(0);
		List<Article> articleList = StemFrequencyDistribution.calculateAndSaveArticleStems(articleFileDatePairs, company);
		
		CalcPrincipalComponentValues.calcPrincipalComponentValuesForArticles(articleList);
		
		SessionManager.commit();
		SessionManager.closeAll();
	}

}
