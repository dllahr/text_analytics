package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TextFileReader {
	
	public static String readFile(File fle) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fle));

		StringBuilder builder = new StringBuilder();

		int curVal;
		while ((curVal = reader.read()) != -1) {
			builder.append((char)curVal);
		}
		
		return builder.toString();
	}
}
