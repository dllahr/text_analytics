package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

public class Main {
	
	private static final int batchSize = 1000;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		final File fileOrderFile = new File(args[0]);
		final File vectorFileDir = new File(args[1]);
		System.err.println("fileOrderFile: " + fileOrderFile.getAbsolutePath());
		System.err.println("fileDir: " + vectorFileDir.getAbsolutePath());
		
		Date startDate = new Date();
		
		int col = 0;
		BufferedReader fileOrderReader = new BufferedReader(new FileReader(fileOrderFile));
		String curFileOrderLine;
		while ((curFileOrderLine = fileOrderReader.readLine()) != null) {
			final File vectorFile = new File(vectorFileDir, curFileOrderLine);
			
			
			BufferedReader vectorReader = new BufferedReader(new FileReader(vectorFile));
			String curVectorLine;
			while ((curVectorLine = vectorReader.readLine()) != null) {
				System.out.println(col + "," + curVectorLine);
			}
			vectorReader.close();
			
			col++;
			
			if (col%batchSize == 0) {
				System.err.println(col);
			}
		}

		fileOrderReader.close();
		
		Date endDate = new Date();
		double durMin = ((double)(endDate.getTime() - startDate.getTime())) / 60000.0;
		System.err.println("start: " + startDate + " end: " + endDate);
		System.err.println("duration[min]: " + durMin);
	}

}
