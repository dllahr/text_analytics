package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import controller.util.FileReaderWhereLineIsEntityId;
import controller.util.FileReaderWhereLineIsEntityId.Pair;

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
		final int scoringModelId = Integer.valueOf(args[0]);
		final int articleSourceId = Integer.valueOf(args[1]);
		final File eigvalFile = new File(args[2]);
		final File eigvectFile = new File(args[3]);
		final File prinCompFile = new File(args[4]);
		final File meanStemVectFile = new File(args[5]);
		
		System.out.println("eigenvalue file:  " + eigvalFile.getAbsolutePath());
		System.out.println("eigenvector file:  " + eigvectFile.getAbsolutePath());
		System.out.println("principal component file:  " + prinCompFile.getAbsolutePath());
		System.out.println("mean stem vector file:  " + meanStemVectFile.getAbsolutePath());
		if (! haveSameParent(new File[]{eigvalFile, eigvectFile, prinCompFile, meanStemVectFile})) {
			System.out.println("WARNING:  these files are not all in the same directory");
			System.out.println("press any key to continue anyway");
			System.in.read();
		}
		
		ScoringModel scoringModel = ScoringModel.getScoringModel(scoringModelId);
		
		List<Eigenvalue> eigvalList = loadEigenvalues(eigvalFile, scoringModel);
		System.out.println("Loaded eigenvalues:  " + eigvalList.size());
		
		loadEigenvectors(articleSourceId, eigvectFile, eigvalList);
		
		System.out.println("getting stems from database:");
		List<Stem> stemList = Stem.getStemsOrderedById();

		loadPrinComps(prinCompFile, eigvalList, stemList);
		
		loadMeanStemVect(meanStemVectFile, scoringModel, stemList);

		System.out.println("Done!");
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
		reader.close();
		
		doCommit("eigenvalues");
		
		for (Eigenvalue e : result) {
			SessionManager.merge(e);
		}
		
		return result;
	}
	
	private static void loadEigenvectors(int articleSourceId, File eigvectFile, List<Eigenvalue> eigvalList) 
			throws IOException {

		System.out.println("Loading eigenvectors:");
		
		final int numEigVal = eigvalList.size();
		
		List<Article> articleList = Article.getArticlesOrderById(articleSourceId);
		
		FileReaderWhereLineIsEntityId<Article> reader = new FileReaderWhereLineIsEntityId<>(articleList, 
				 new BufferedReader(new FileReader(eigvectFile)));
		
		int lineNum = 0;
		Pair<Article> pair;
		while ((pair = reader.readNext()) != null) {
			Article article = pair.entity;
			
			String[] split = pair.line.split(delimeter);
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
		
		if (reader.wereThereMoreLinesThanEntities()) {
			throw new RuntimeException("eigenvector load:  there were more rows in the eigenvector file than there were articles for the article source");
		}
		
		if ((lineNum*numEigVal)%batchSize != 0) {
			doCommit("eigenvectors");
		}

		reader.close();
	}


	private static void loadPrinComps(File prinCompFile, List<Eigenvalue> eigvalList, List<Stem> stemList) 
			throws IOException {

		System.out.println("loading principal components:");
		final int numEigVal = eigvalList.size();
		
		FileReaderWhereLineIsEntityId<Stem> reader = new FileReaderWhereLineIsEntityId<>(stemList, 
				new BufferedReader(new FileReader(prinCompFile))); 

		int lineNum = 0;
		
		Pair<Stem> pair;
		
		while ((pair = reader.readNext()) != null) {
			Stem stem = pair.entity;
						
			String[] split = pair.line.split(delimeter);
			if (split.length != eigvalList.size()) {
				throw new RuntimeException("eigenvector load:  number of entries in current row of file is different than number of eigenvalues. lineNum: " + lineNum);
			}
			
			
			//convert strings to doubles and check if every entry in row is a zero
			double[] values = new double[split.length];
			boolean allZeros = true;
			for (int i = 0; i < split.length; i++) {
				double val = Double.valueOf(split[i]);
				
				allZeros = allZeros && (val == 0.0);
				
				values[i] = val;
			}
			
			//if every entry in the row is a zero it means that stem entry did not occur in any articles used in the model
			//therefore do not store the data for any of them
			if (! allZeros) {
				int index = 0;
				for (Eigenvalue e : eigvalList) {
					SessionManager.persist(new PrincipalComponent(e, stem, values[index]));

					index++;
				}
			}

			lineNum++;
			
			if (lineNum%1000 == 0) {
				System.out.println("read principal component current lineNum:  " + lineNum);
			}
			
			if ((lineNum*numEigVal)%batchSize == 0) {
				doCommit("principal components");
			}
		}


		if (reader.wereThereMoreLinesThanEntities()) {
			throw new RuntimeException("principal component load:  there were more rows in the file than there were stems for the scoring model");
		}
		
		if ((lineNum*numEigVal)%batchSize != 0) {
			doCommit("principal components");
		}
		
		reader.close();
	}
	
	
	static void loadMeanStemVect(File meanStemVectFile, ScoringModel scoringModel, List<Stem> stemList) throws IOException {
		System.out.println("reading mean stem vector");

		FileReaderWhereLineIsEntityId<Stem> reader = new FileReaderWhereLineIsEntityId<>(stemList, 
				new BufferedReader(new FileReader(meanStemVectFile)));

		Pair<Stem> pair;
		while ((pair = reader.readNext()) != null) {
			Stem stem = pair.entity;

			final double value = Double.valueOf(pair.line);
			
			SessionManager.persist(new MeanStemCount(scoringModel, stem, value));
		}
		
		if (reader.wereThereMoreLinesThanEntities()) {
			throw new RuntimeException("mean stem vector load:  there were more rows in the file than there were stems for the scoring model");
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
