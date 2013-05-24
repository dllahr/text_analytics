package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;

import controller.util.Utilities;

import orm.Article;
import orm.Eigenvalue;
import orm.EigenvectorValue;
import orm.PrincipalComponent;
import orm.ScoringModel;
import orm.SessionManager;
import orm.Stem;

public class MainLoadEigPrincComp {
	private static final String delimeter = ",";

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		final int modelId = Integer.valueOf(args[0]);
		final File eigvalFile = new File(args[1]);
		final File eigvectFile = new File(args[2]);
		final File prinCompFile = new File(args[3]);
		
		ScoringModel sm = findScoringModel(modelId);
		
		List<Eigenvalue> eigvalList = loadEigenvalues(eigvalFile, sm);
		System.out.println("Loaded eigenvalues:  " + eigvalList.size());
		
		loadEigenvectors(eigvectFile, eigvalList);
		
		loadPrinComps(prinCompFile, eigvalList);
		
		System.out.println("Committing to database");
		SessionManager.commit();
		System.out.println("Done!");
	}
	
	private static ScoringModel findScoringModel(int scoringModelId) {
		Query query = SessionManager.createQuery("from ScoringModel where id = :id");
		query.setInteger("id", scoringModelId);
		
		return (ScoringModel)query.list().get(0);
	}
	
	private static List<Eigenvalue> loadEigenvalues(File eigvalFile, ScoringModel sm) throws IOException {
		List<Eigenvalue> result = new LinkedList<>();

		BufferedReader reader = new BufferedReader(new FileReader(eigvalFile));
		
		int sortIndex = 0;
		
		String curLine;
		while ((curLine = reader.readLine()) != null) {
			final double value = Double.valueOf(curLine);

			Eigenvalue e = new Eigenvalue(sm, sortIndex, value);
			SessionManager.persist(e);

			result.add(e);

			sortIndex++;
		}
		
		return result;
	}
	
	private static void loadEigenvectors(File eigvectFile, List<Eigenvalue> eigvalList) throws IOException {
		System.out.println("Loading eigenvectors:");
		
		List<Article> articleList = getArticles(eigvalList.get(0).getScoringModel());
		
		BufferedReader reader = new BufferedReader(new FileReader(eigvectFile));
		
		int lineNum = 0;
		for (Article article : articleList) {

			String curLine = reader.readLine();
			if (null == curLine) {
				throw new RuntimeException("eigenvector load:  file ended but there are still articles present");
			}
			
			String[] split = curLine.split(delimeter);
			if (split.length != eigvalList.size()) {
				throw new RuntimeException("eigenvector load:  number of entries in current row of file is different than number of eigenvalues. lineNum: " + lineNum);
			}

			int index = 0;
			for (Eigenvalue e : eigvalList) {
				final double value = Double.valueOf(split[index]);

				SessionManager.persist(new EigenvectorValue(e, article, value));

				index++;
			}

			lineNum++;
			
			if (lineNum%1000 == 0) {
				System.out.println("load eigenvector current lineNum:  " + lineNum);
			}
		}
		
		String endLine;
		while ((endLine = reader.readLine()) != null) {
			if (endLine.trim() != "") {
				throw new RuntimeException("eigenvector load:  there were more rows in the file than there were articles for the scoring model");
			}
		}
		
	}
	
	static List<Article> getArticles(ScoringModel sm) {
		Query query = SessionManager.createQuery("from Article where scoringModel = :sm order by id");
		query.setParameter("sm", sm);
		
		return Utilities.convertGenericList(query.list());
	}

	private static void loadPrinComps(File prinCompFile, List<Eigenvalue> eigvalList) throws IOException {
		System.out.println("loading principal components:");
		
		System.out.println("getting stems from database:");
		List<Stem> stemList = getStems(eigvalList.get(0).getScoringModel());
		
		BufferedReader reader = new BufferedReader(new FileReader(prinCompFile));

		System.out.println("loading values:");
		int lineNum = 0;
		for (Stem stem : stemList) {
			
			String curLine = reader.readLine();
			if (null == curLine) {
				throw new RuntimeException("eigenvector load:  file ended but there are still articles present");
			}
			
			String[] split = curLine.split(delimeter);
			if (split.length != eigvalList.size()) {
				throw new RuntimeException("eigenvector load:  number of entries in current row of file is different than number of eigenvalues. lineNum: " + lineNum);
			}
			
			int index = 0;
			for (Eigenvalue e : eigvalList) {
				final double value = Double.valueOf(split[index]);

				SessionManager.persist(new PrincipalComponent(e, stem, value));

				index++;
			}
			
			
			lineNum++;
			
			if (lineNum%1000 == 0) {
				System.out.println("load principal component current lineNum:  " + lineNum);
			}
		}
		
		String endLine;
		while ((endLine = reader.readLine()) != null) {
			if (endLine.trim() != "") {
				throw new RuntimeException("eigenvector load:  there were more rows in the file than there were articles for the scoring model");
			}
		}
	}
	
	static List<Stem> getStems(ScoringModel sm) {
		Query query = SessionManager.createQuery("from Stem where scoringModel = :sm order by id");
		query.setParameter("sm", sm);
		
		return Utilities.convertGenericList(query.list());
	}
}
