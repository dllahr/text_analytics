package controller.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class FileReaderWhereLineIsEntityId <T extends HasId>{
	
	private final Iterator<T> entityIter;
	
	private final BufferedReader reader;
	
	private int linenum;
	
	public FileReaderWhereLineIsEntityId(List<T> entityListOrderedById, BufferedReader reader) {
		this.entityIter = entityListOrderedById.iterator();
		this.reader = reader;
		
		linenum = entityListOrderedById.get(0).getId();
	}
	
	public Pair<T> readNext() throws IOException {
		String line = reader.readLine();
		if (entityIter.hasNext() && line != null) {
			T entity = entityIter.next();
			
			final int entityId = entity.getId();
			
			while (linenum < entityId) {
				line = reader.readLine();
				linenum++;
			}
			
			linenum++;
			
			return new Pair<T>(entity, line);
		} else {
			return null;
		}
	}
	
	
	public boolean wereThereMoreLinesThanEntities() throws IOException {
		if (! entityIter.hasNext()) {
			String endLine;
			
			while ((endLine = reader.readLine()) != null) {
				if (! endLine.trim().equals("")) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	public void close() throws IOException {
		reader.close();
	}
	
	
	public static class Pair<U> {
		public final U entity;
		public final String line;
		public Pair(U entity, String line) {
			this.entity = entity;
			this.line = line;
		}
	}
}
