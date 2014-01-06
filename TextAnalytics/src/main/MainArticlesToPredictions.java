package main;

import gate.util.GateException;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import controller.util.CommandLineParser;
import controller.util.CommandLineParserUnrecognizedTokenException;

public class MainArticlesToPredictions {
	
	private static final String skipLoadArticles = "-skipLoadArticles";
	
	private static final String skipGenPrinCompVals = "-skipGenPrinCompVals";

	private static final String skipGenPred = "-skipGenPred";
	
	/**
	 * @param args
	 * @throws GateException 
	 * @throws IOException 
	 * @throws CommandLineParserUnrecognizedTokenException 
	 * @throws ParseException 
	 */
	public static void main(String[] args) throws IOException, GateException, ParseException,
	CommandLineParserUnrecognizedTokenException {
		if (args.length < 2) {
			System.out.println("usage:  <scoringModelId> <articleDir>");
			System.out.println("options:  ");
			for (String option : new String[]{skipLoadArticles, skipGenPrinCompVals, skipGenPred}) {
				System.out.println(option);
			}
			return;
		}
		
		final int scoringModelId = Integer.valueOf(args[0]);
		final String articleDir = args[1];
		
		Map<String, String> argsMap = CommandLineParser.parse(args, 2);
		
		List<String[]> argsList = getArgsList(scoringModelId, articleDir);
		
		if (! argsMap.containsKey(skipLoadArticles)) {
			System.out.println("Load articles");
			MainLoadArticles.main(argsList.get(0));
		} else {
			System.out.println(skipLoadArticles + " option present");
		}

		if (! argsMap.containsKey(skipGenPrinCompVals)) {
			System.out.println("generate principal component values");
			MainGeneratePrincipalComponentValues.main(argsList.get(1));
		} else {
			System.out.println(skipGenPrinCompVals + " option present");
		}
	
		if (! argsMap.containsKey(skipGenPred)) {
			System.out.println("generate predictions");
			MainGeneratePredictions.main(argsList.get(2));
		} else {
			System.out.println(skipGenPred + " option present");
		}
	}
	
	static List<String[]> getArgsList(int scoringModelId, String articleDir) {
		List<String[]> result = new LinkedList<>();
		
		switch (scoringModelId) {
		case 2: //GE
			//load article parameters
			result.add(new String[] {"2", articleDir});
			//generate principal component values parameters
			result.add(new String[] {"2", "2"});
			//generate predictions parameters
			result.add(new String[] {"8", "10,11", "2013-09-01", "2"});
		case 4: //MDLZ
			result.add(new String[] {"1", articleDir});
			result.add(new String[] {"4", "1"});
			result.add(new String[] {"7", "8,9", "2013-09-01", "1"});
		case 6:  //CAT
			result.add(new String[] {"5", articleDir});
			result.add(new String[] {"6", "5"});
			result.add(new String[] {"71", "16,17", "2013-09-05", "5"});
		case 7: //MCD
			result.add(new String[] {"6", articleDir});
			result.add(new String[] {"7", "6"});
			result.add(new String[] {"48", "14,15", "2013-09-01", "6"});
		default:

		}
		
		return result;
	}

}
