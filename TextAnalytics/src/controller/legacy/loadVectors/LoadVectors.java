package controller.legacy.loadVectors;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import controller.util.Utilities;

import orm.Article;
import orm.ArticleStemCount;
import orm.Company;
import orm.SessionManager;
import orm.Stem;

public class LoadVectors {
	private static final String companyParam = "company";
	
	private static final String articleQuery = "from Article where company=:" + companyParam;
	
	private static final String stemQuery = "from Stem where company=:" + companyParam;
	
	private static final int progressIncrement = 100;
	
	public static void loadSparseVectors(final String filePrefix, File vectorsDir, Company company) {
		VectorFileReader vectorFileReader = new SparseVectorReader();
		loadVectors(filePrefix, vectorsDir, company, vectorFileReader);
	}
	
	public static void loadRegularVectors(final String filePrefix, File vectorsDir, Company company) {
		VectorFileReader vectorFileReader = new RegularVectorReader();
		loadVectors(filePrefix, vectorsDir, company, vectorFileReader);
	}
	
	public static void loadVectors(final String filePrefix, File vectorsDir, Company company, 
			VectorFileReader vectorFileReader) {
		
		final Map<Integer, Stem> idStemMap = getStems(company);
		
		final int startingStemId = Collections.min(idStemMap.keySet());
		
		final Map<String, Article> fileArticleMap = getArticles(company);
		
		FileFilter fileFilter = new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return (!pathname.isDirectory()) && pathname.getName().startsWith(filePrefix);
			}
		};
		
		int progress = 0;
		for (File vectorFile : vectorsDir.listFiles(fileFilter)) {
			String articleName = vectorFile.getName().substring(filePrefix.length());
			Article article = fileArticleMap.get(articleName);
			
			try {
				vectorFileReader.setFile(vectorFile);
				loadVectorFromFile(startingStemId, article, idStemMap, vectorFileReader);
				vectorFileReader.close();
			} catch (IOException e) {
				System.err.println("LoadVectors loadVectors unable to open / read / close file " + vectorFile.getAbsolutePath());
				e.printStackTrace();
			}
			
			if ((progress%progressIncrement) == 0) {
				System.out.println("word vectors loaded:  " + progress);
				SessionManager.commit();
			}
			progress++;
		}

		SessionManager.commit();
	}

	
	private static void loadVectorFromFile(final int startingStemId, Article article, Map<Integer, Stem> idStemMap, 
			VectorFileReader vectorFileReader) throws IOException {

		int[] curEntry;
		while ((curEntry = vectorFileReader.nextVector()) != null) {

			final int stemId = startingStemId + curEntry[0];
			final int count = curEntry[1];

			ArticleStemCount asc = new ArticleStemCount();
			asc.setArticle(article);
			asc.setCount(count);
			asc.setStem(idStemMap.get(stemId));

			SessionManager.persist(asc);
		}
	}


	private static Map<String, Article> getArticles(Company company) {
		Query query = SessionManager.createQuery(articleQuery);
		query.setParameter(companyParam, company);
		List<Article> articleList = Utilities.convertGenericList(query.list());
		
		Map<String, Article> fileArticleMap = new HashMap<>();
		
		for (Article article : articleList) {
			File file = new File(article.getFilename());
			fileArticleMap.put(file.getName(), article);
		}
		
		return fileArticleMap;
	}
	
	private static Map<Integer, Stem> getStems(Company company) {
		Query query = SessionManager.createQuery(stemQuery);
		query.setParameter(companyParam, company);
		List<Stem> stemList = Utilities.convertGenericList(query.list());
		
		Map<Integer, Stem> idStemMap = new HashMap<>();
		
		for (Stem stem : stemList) {
			idStemMap.put(stem.getId(), stem);
		}
		
		return idStemMap;
	}
	

}
