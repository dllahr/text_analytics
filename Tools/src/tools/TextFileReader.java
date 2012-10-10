package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TextFileReader {
	public static String readFile(File fle) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fle));
		
		String content = "";
		String curLine = reader.readLine();
		while (curLine != null) {
			content += curLine;
			curLine = reader.readLine();
		}
		
		return content;
	}
}
