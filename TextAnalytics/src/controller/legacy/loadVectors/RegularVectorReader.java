package controller.legacy.loadVectors;

import java.io.File;
import java.io.IOException;

class RegularVectorReader extends VectorFileReader {
	
	private int rowIndex;

	@Override
	public void setFile(File vectorFile) throws IOException {
		super.setFile(vectorFile);

		rowIndex = 0;
	}

	@Override
	public int[] nextVector() throws IOException {
		int[] result = null;
		
		boolean isZero = true;
		String curLine;
		while (isZero && (curLine = reader.readLine()) != null) {
			Integer curVal = Integer.valueOf(curLine);
			if (curVal != 0) {
				isZero = false;
				result = new int[2];
				result[0] = rowIndex;
				result[1] = curVal;
			}
			
			rowIndex++;
		}
		
		return result;
	}
}
