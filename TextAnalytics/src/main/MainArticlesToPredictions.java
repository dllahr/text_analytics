package main;

import gate.util.GateException;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import controller.util.CommandLineParserUnrecognizedTokenException;

public class MainArticlesToPredictions {

	/**
	 * @param args
	 * @throws GateException 
	 * @throws IOException 
	 * @throws CommandLineParserUnrecognizedTokenException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, GateException, ParseException,
	CommandLineParserUnrecognizedTokenException {
		final int scoringModelId = Integer.valueOf(args[0]);
		final String articleDir = args[1];
		
		List<String[]> argsList = getArgsList(scoringModelId, articleDir);
		
		System.out.println("Load articles");
		MainLoadArticles.main(argsList.get(0));
		System.out.println("generate principal component values");
		MainGeneratePrincipalComponentValues.main(argsList.get(1));
		System.out.println("generate predictions");
		MainGeneratePredictions.main(argsList.get(2));

	}
	
	static List<String[]> getArgsList(int scoringModelId, String articleDir) {
		List<String[]> result = new LinkedList<>();
		
		switch (scoringModelId) {
		case 2:
			result.add(new String[] {"2", articleDir});
			result.add(new String[] {"2", "2"});
			result.add(new String[] {"8", "10,11", "2013-09-01", "2"});
		case 4:
			result.add(new String[] {"1", articleDir});
			result.add(new String[] {"4", "1"});
			result.add(new String[] {"7", "8,9", "2013-09-01", "1"});
		case 7:
			result.add(new String[] {"6", articleDir});
			result.add(new String[] {"7", "6"});
			result.add(new String[] {"48", "14,15", "2013-09-01", "6"});
		default:

		}
		
		return result;
	}

}
