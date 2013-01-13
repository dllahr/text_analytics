package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class LoadData {
	static String csvDelimeter = ",";
	private static final String textDelimeter = "\\s";
	
	private static final LoadDataLineHandler csvlineHandler = new LoadDataLineHandler() {
		@Override
		public String[] splitLine(String line) {
			return line.split(csvDelimeter);
		}
	};
	
	public Data loadCsvWithHeaderRow(File dataFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(dataFile));
		
		String[] headerArray = reader.readLine().split(csvDelimeter);
		
		int maxCols = headerArray.length;
		List<String[]> splitLinesList = new LinkedList<String[]>();
		
		int maxColsFromBulk = loadBulk(reader, csvlineHandler, splitLinesList);
		if (maxColsFromBulk > maxCols) {maxCols = maxColsFromBulk;}
		reader.close();
		
		if (splitLinesList.size() == 0) {
			return null;
		}

		return new Data(buildDataArray(splitLinesList, maxCols), headerArray);
	}
	
	public Data loadCsvWithoutHeaderRow(File dataFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(dataFile));
		
		List<String[]> splitLinesList = new LinkedList<String[]>();
		final int maxCols = loadBulk(reader, csvlineHandler, splitLinesList);
		
		reader.close();
		
		if (splitLinesList.size() == 0) {
			return null;
		}
		
		return new Data(buildDataArray(splitLinesList, maxCols), null);
	}
	
	/**
	 * uses any whitespace (regex \\s) as delimeter)
	 * @param dataFile
	 * @return
	 * @throws IOException
	 */
	public Data loadText(File dataFile) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(dataFile));
		
		LoadDataLineHandler lineHandler = new LoadDataLineHandler() {
			@Override
			public String[] splitLine(String line) {
				String[] split = line.split(textDelimeter);
				List<String> splitList = new LinkedList<String>();
				for (String entry : split) {
					if (entry != null && ! entry.trim().equals("")) {
						splitList.add(entry);
					}
				}
				if (splitList.size() > 0) {
					String[] result = new String[splitList.size()];
					return splitList.toArray(result);
				}
				else {
					return null;
				}
			}
		};
		List<String[]> splitLinesList = new LinkedList<String[]>();
		final int maxCols = loadBulk(reader, lineHandler, splitLinesList);
		reader.close();
		
		if (splitLinesList.size() == 0) {
			return null;
		}
		
		return new Data(buildDataArray(splitLinesList, maxCols), null);
	}
	
	private int loadBulk(BufferedReader reader, LoadDataLineHandler lineHandler, 
			List<String[]> splitLinesList) throws IOException {
		int maxCols = 0;
		
		String curLine = reader.readLine();
		while (curLine != null) {
			String[] curLineSplit = lineHandler.splitLine(curLine);
			if (curLineSplit.length > maxCols) {
				maxCols = curLineSplit.length;
			}
			
			splitLinesList.add(curLineSplit);
			curLine = reader.readLine();
		}
		
		return maxCols;
	}
	
	private String[][] buildDataArray(List<String[]> splitLinesList, final int maxCols) {
		String[][] dataArray = new String[splitLinesList.size()][maxCols];
		int rowInd = 0;
		for (String[] curSplitLine : splitLinesList) {
			int colInd = 0;
			for (String curEntry : curSplitLine) {
				dataArray[rowInd][colInd] = curEntry;
				colInd++;
			}
			rowInd++;
		}
		
		return dataArray;
	}
}
