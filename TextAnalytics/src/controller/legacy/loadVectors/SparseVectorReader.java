package controller.legacy.loadVectors;

import java.io.IOException;

public class SparseVectorReader extends VectorFileReader {
	
	private static final String delimeter = ",";

	@Override
	public int[] nextVector() throws IOException {
		int[] result = null;
		
		final String curLine = super.reader.readLine();
		if (curLine != null) {
			String[] curLineArray = curLine.split(delimeter);
			
			result = new int[2];
			
			result[0] = Integer.valueOf(curLineArray[0]);
			result[1] = Integer.valueOf(curLineArray[1]);
		}
		
		return result;
	}



}
