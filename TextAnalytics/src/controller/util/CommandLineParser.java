package controller.util;

import java.util.HashMap;
import java.util.Map;

public class CommandLineParser {
	
	private static final String optionPrefix = "-";

	public static Map<String, String> parse(String[] args, int startIndex) throws CommandLineParserUnrecognizedTokenException {
		Map<String, String> result = new HashMap<String, String>();
		
		for (int i = startIndex; i < args.length; i++) {
			String key = args[i];
			
			if (! key.startsWith(optionPrefix)) {
				throw new CommandLineParserUnrecognizedTokenException("expected to find a command line option with prefix " 
						+ optionPrefix + " but found this instead (position " + i + "):  " + key);
			}
			
			String value = null;
			if ((i+1) < args.length) {
				if (! args[i+1].startsWith("-")) {
					value = args[i+1];
					i++;
				}
			}
			
			result.put(key, value);
		}
		
		return result;
	}
}
