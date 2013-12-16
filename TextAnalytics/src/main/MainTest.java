package main;

import gate.util.GateException;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import controller.articleIntegration.readAndSplitRawFile.SplitArticle;
import controller.stemCountArticles.StemCounter;
import controller.util.CommandLineParser;
import controller.util.CommandLineParserUnrecognizedTokenException;

public class MainTest {

	private static final String gateCmd = "-gate";
	
	private static final String[] allCmd = {gateCmd};
	/**
	 * @param args
	 * @throws IOException 
	 * @throws GateException 
	 */
	public static void main(String[] args) throws GateException, IOException {
		System.out.println("MainTest");
		
		try {
			Map<String, String> argsMap = CommandLineParser.parse(args, 0);
			
			if (argsMap.containsKey(gateCmd)) {
				testGate();
			} else {
				System.out.println("did not recognize any commands.  Choose one of:");
				for (String cmd : allCmd) {
					System.out.println(cmd);
				}
			}
		} catch (CommandLineParserUnrecognizedTokenException e) {
			System.out.println("garbled command line:  " + e.getMessage());
		}
		
	}

	static void testGate() throws GateException, IOException {
		SplitArticle splitArticle = new SplitArticle(new File("fake file"), 0);
		splitArticle.bodyLines.add("hello world.");
		splitArticle.bodyLines.add("");
		splitArticle.bodyLines.add("hello gate!");
		splitArticle.bodyLines.add("");
		splitArticle.bodyLines.add("good bye");
		
		List<SplitArticle> splitArticleList = new LinkedList<>();
		splitArticleList.add(splitArticle);
		
		StemCounter stemCounter = new StemCounter(1);
		
		stemCounter.count(splitArticleList);
		
		for (String stemStr : splitArticle.stemCountMap.keySet()) {
			System.out.println(stemStr + " " + splitArticle.stemCountMap.get(stemStr));
		}
	}
}
