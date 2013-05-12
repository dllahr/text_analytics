package controller.readRawFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BuildMetaDataMap {

	private static final String commentPrefix = "#";
	private static final String delimeter = "	";
	
	public Map<String, Boolean> build(File inputFile) throws IOException {
		Map<String, Boolean> result = new HashMap<String, Boolean>();
		
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		
		String curLine;
		while ((curLine = reader.readLine()) != null) {
			curLine = curLine.trim();
			if (curLine.length() > 0 && ! curLine.startsWith(commentPrefix)) {
				String[] split = curLine.split(delimeter);
				
				if (split.length != 2) {
					throw new RuntimeException("BuildMetaDataMap build found line in input file (" 
							+ inputFile.getAbsolutePath() + ") that does not have 2 entries:  " 
							+ curLine);
				}
				
				result.put(split[0], Boolean.valueOf(split[1]));
			}
		}
		
		reader.close();
		
		return result;
	}
}
