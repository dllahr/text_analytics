package controller.legacy.loadVectors;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


abstract class VectorFileReader {
	protected BufferedReader reader;
	
	public void setFile(File vectorFile) throws IOException {
		if (reader != null) {
			reader.close();
		}

		reader = new BufferedReader(new FileReader(vectorFile));
	}
	
	public abstract int[] nextVector() throws IOException;

	public void close() throws IOException {
		reader.close();
		reader = null;
	}
}
