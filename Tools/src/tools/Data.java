package tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Data implements Iterable<String[]> {
	private String[][] dataArray;
	private String[] columnHeadersArray;
	private Map<String, Integer> colNameIndexMap;
	
	public Data(String[][] dataArray, String[] columnHeadersArray) {
		super();
		this.dataArray = dataArray;
		this.columnHeadersArray = columnHeadersArray;
		
		colNameIndexMap = new HashMap<String, Integer>();
		if (columnHeadersArray != null) {
			int i = 0;

			for (String columnHeader : columnHeadersArray) {
				colNameIndexMap.put(columnHeader, i);
				i++;
			}
		}
	}

	public String[][] getDataArray() {
		return dataArray;
	}

	public String[] getColumnHeadersArray() {
		return columnHeadersArray;
	}
	
	public Integer getColumnIndex(String columnName) {
		return colNameIndexMap.get(columnName);
	}
	
	public int getFirstDimension() {return dataArray.length;}
	public int getSecondDimension() {return dataArray[0].length;}
	
	@Override
	public Iterator<String[]> iterator() {
		
		return new Iterator<String[]>() {
			private int index = 0;
			
			@Override
			public boolean hasNext() {
				return index < dataArray.length;
			}

			@Override
			public String[] next() {
				String[] result = dataArray[index];
				index++;
				return result;
			}

			@Override
			public void remove() {
				throw new RuntimeException("Nien!!! Data iterator remove() not supported");
			}
		};
	}
}
