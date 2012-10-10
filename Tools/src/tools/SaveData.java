package tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveData {
	private static final String delimeter = LoadData.csvDelimeter;
	
	private static final String quoteChar = "\"";
	
	public void saveCsvWithoutHeaderRow(Data dataToSave, File dataFile) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
		
		saveCsvBulk(writer, dataToSave);
		
		writer.close();
	}
	
	private String buildLine(String[] lineArray) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < lineArray.length; i++) {
			final boolean containsDelim = lineArray[i].contains(delimeter);
			if (containsDelim) {
				builder.append(quoteChar);
			}
			builder.append(lineArray[i]);
			if (containsDelim) {
				builder.append(quoteChar);
			}
			builder.append(delimeter);
		}
		return builder.length() > 1 ? builder.substring(0, builder.length() - 1) : "";
	}

	private void saveCsvBulk(BufferedWriter writer, Data dataToSave) throws IOException {
		for (String[] row : dataToSave) {
			writer.write(buildLine(row));
			writer.newLine();
		}
	}
	
	public void saveCsvWithHeaderRow(Data dataToSave, File file) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		
		writer.write(buildLine(dataToSave.getColumnHeadersArray()));
		writer.newLine();
		
		saveCsvBulk(writer, dataToSave);
		
		writer.close();
	}
}
