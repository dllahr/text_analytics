package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hibernate.Query;

import controller.util.Utilities;

import orm.Article;
import orm.Eigenvalue;
import orm.EigenvectorValue;
import orm.MeanStemCount;
import orm.PrincipalComponent;
import orm.ScoringModel;
import orm.SessionManager;
import orm.Stem;

public class MainLoadEigPrincComp {
	private static final String delimeter = ",";

	private static final long batchSize = 1000000;
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		final int modelId = Integer.valueOf(args[0]);
		final File eigvalFile = new File(args[1]);
		final File eigvectFile = new File(args[2]);
		final File prinCompFile = new File(args[3]);
		final File meanStemVectFile = new File(args[4]);
		
		System.out.println("eigenvalue file:  " + eigvalFile.getAbsolutePath());
		System.out.println("eigenvector file:  " + eigvectFile.getAbsolutePath());
		System.out.println("principal component file:  " + prinCompFile.getAbsolutePath());
		System.out.println("mean stem vector file:  " + meanStemVectFile.getAbsolutePath());
		if (! haveSameParent(new File[]{})) {
			System.out.println("WARNING:  these files are not all in the same directory");
			System.out.println("press any key to continue anyway");
			System.in.read();
		}
		
		ScoringModel scoringModel = findScoringModel(modelId);
		
		List<Eigenvalue> eigvalList = loadEigenvalues(eigvalFile, scoringModel);
		System.out.println("Loaded eigenvalues:  " + eigvalList.size());
		
		loadEigenvectors(eigvectFile, eigvalList);
		
		List<Stem> stemList = getStems(eigvalList.get(0).getScoringModel());
		loadPrinComps(prinCompFile, eigvalList, stemList);
		
		loadMeanStemVect(meanStemVectFile, scoringModel, stemList);

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
		
		doCommit("eigenvalues");
		
		for (Eigenvalue e : result) {
			SessionManager.merge(e);
		}
		
		reader.close();
		
		return result;
	}
	
	private static void loadEigenvectors(File eigvectFile, List<Eigenvalue> eigvalList) throws IOException {
		System.out.println("Loading eigenvectors:");
		
		final int numEigVal = eigvalList.size();
		
		List<Article> articleList = getArticles(eigvalList.get(0).getScoringModel());
		
		BufferedReader reader = new BufferedReader(new FileReader(eigvectFile));
		
		int lineNum = 0;
		Iterator<Article> articleIterator = articleList.iterator();
		String curLine;
		while (articleIterator.hasNext() && (curLine = reader.readLine()) != null) {
			Article article = articleIterator.next();
			
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
				System.out.println("read eigenvector current lineNum:  " + lineNum);
			}
			
			if ((lineNum*numEigVal)%batchSize == 0) {
				doCommit("eigenvectors");
			}
		}
		
		if (! articleIterator.hasNext()) {
			String endLine;

			while ((endLine = reader.readLine()) != null) {
				if (endLine.trim() != "") {
					throw new RuntimeException("eigenvector load:  there were more rows in the eigenvector file than there were articles for the scoring model");
				}
			}
		}
		
		if ((lineNum*numEigVal)%batchSize != 0) {
			doCommit("eigenvectors");
		}

		reader.close();
	}
	
	static List<Article> getArticles(ScoringModel sm) {
		Query query = SessionManager.createQuery("from Article where scoringModel = :sm order by id");
		query.setParameter("sm", sm);
		
		return Utilities.convertGenericList(query.list());
	}

	private static void loadPrinComps(File prinCompFile, List<Eigenvalue> eigvalList, List<Stem> stemList) throws IOException {
		System.out.println("loading principal components:");
		final int numEigVal = eigvalList.size();
		
		System.out.println("\tgetting stems from database:");
		
		BufferedReader reader = new BufferedReader(new FileReader(prinCompFile));

		System.out.println("\treading:");
		int lineNum = 0;
		
		String curLine;
		Iterator<Stem> stemIterator = stemList.iterator();
		
		while (stemIterator.hasNext() && (curLine = reader.readLine()) != null) {
			Stem stem = stemIterator.next();
			
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
				System.out.println("read principal component current lineNum:  " + lineNum);
			}
			
			if ((lineNum*numEigVal)%batchSize == 0) {
				doCommit("principal components");
			}
		}


		if (! stemIterator.hasNext()) {
			String endLine;

			while ((endLine = reader.readLine()) != null) {
				if (endLine.trim() != "") {
					throw new RuntimeException("principal component load:  there were more rows in the file than there were stems for the scoring model");
				}
			}
		}
		
		if ((lineNum*numEigVal)%batchSize != 0) {
			doCommit("principal components");
		}
		
		reader.close();
	}
	
	static List<Stem> getStems(ScoringModel sm) {
		Query query = SessionManager.createQuery("from Stem where scoringModel = :sm order by id");
		query.setParameter("sm", sm);
		
		return Utilities.convertGenericList(query.list());
	}
	
	
	static void loadMeanStemVect(File meanStemVectFile, ScoringModel scoringModel, List<Stem> stemList) throws IOException {
		System.out.println("reading mean stem vector");

		BufferedReader reader = new BufferedReader(new FileReader(meanStemVectFile));

		Iterator<Stem> stemIter = stemList.iterator();
		String curLine;
		while ((curLine = reader.readLine()) != null && stemIter.hasNext()) {
			Stem stem = stemIter.next();

			final double value = Double.valueOf(curLine);
			
			SessionManager.persist(new MeanStemCount(scoringModel, stem, value));
			
		}
		
		if (! stemIter.hasNext()) {
			String endLine;
			while ((endLine = reader.readLine()) != null) {
				if (endLine.trim() != "") {
					throw new RuntimeException("mean stem vector load:  there were more rows in the file than there were stems for the scoring model");
				}
			}
		}
		reader.close();
		
		doCommit("mean stem vector");
	}
	
	private static void doCommit(String loadEntityName) {
		System.out.print("saving " + loadEntityName + " ... ");
		SessionManager.commit();
		System.out.println("done");
	}
	
	static boolean haveSameParent(File[] fileArray) {
		String directory = fileArray[0].getParentFile().getAbsolutePath();
		
		for (int i = 0; i < fileArray.length; i++) {
			if (! fileArray[i].getParentFile().getAbsolutePath().equals(directory)) {
				return false;
			}
		}
		
		return true;
	}
}
