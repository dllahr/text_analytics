package math.linearAlgebra;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class SparseMatrix implements Matrix {
	private List<Map<Integer, Double>> data;
	
	private int maxRowInd;
	
	public SparseMatrix() {
		data = new LinkedList<Map<Integer,Double>>();
		
		maxRowInd = 0;
	}
	
	public Double getEntry(int rowInd, int colInd) {
		if (colInd >= data.size()) {
			throw new RuntimeException("SparseMatrix getEntry colInd exceeds number of columns");
		}
		
		Map<Integer, Double> col = data.get(colInd);
		return col.containsKey(rowInd) ? col.get(rowInd) : 0.0;
	}
	
	public void setEntry(int rowInd, int colInd, double value) {
		while (colInd >= data.size()) {
			data.add(new HashMap<Integer, Double>());
		}
		
		data.get(colInd).put(rowInd, value);
		
		if (rowInd > maxRowInd) {
			maxRowInd = rowInd;
		}
	}
	
	public SparseMatrix rightMultiplyBy(SparseMatrix rightMatrix) {
		if (data.size() != rightMatrix.maxRowInd+1) {
			throw new MatrixSizeMismatchRuntimeException("left matrix: " + data.size() + " " + maxRowInd+1 + " right matrix: " + rightMatrix.data.size() + " " + rightMatrix.maxRowInd+1);
		}
		SparseMatrix result = new SparseMatrix();
		
		final int updateInterval = 10000;
		final int onePercent = data.size()*data.size() > updateInterval ?
				data.size()*data.size() / updateInterval : 1;
		int progress = 0;
		
		int colInd = 0;
		for (Map<Integer, Double> rightCol : rightMatrix.data) {
			for (int rowInd = 0; rowInd <= maxRowInd; rowInd++) {
				double val = 0.0;
				int ind = 0;
				for (Map<Integer, Double> leftCol : data) {
					if (rightCol.containsKey(ind) && leftCol.containsKey(rowInd)) {
						val += rightCol.get(ind) * leftCol.get(rowInd);
					}
					ind++;
				}
				
				if (val != 0.0) {
					result.setEntry(rowInd, colInd, val);
				}
				
				if (progress % onePercent == 0) {
					System.out.println("calculated " + progress/onePercent);
				}
				progress++;
			}
			colInd++;
		}
		
		return result;
	}
	
	public SparseMatrix createCopy() {
		SparseMatrix result = new SparseMatrix();
		
		int colInd = 0;
		for (Map<Integer, Double> col : data) {
			for (Integer rowInd : col.keySet()) {
				result.setEntry(rowInd, colInd, col.get(rowInd));
			}
			colInd++;
		}
		
		return result;
	}
	
	public void addColumn(Vector vect, int colInd) {
		if (data.size() > colInd) {
			data.get(colInd).clear();
		}
		
		for (Integer vectInd : vect.getIndices()) {
			setEntry(vectInd, colInd, vect.getEntry(vectInd));
		}
	}
	
	public int getNumRows() {
		return maxRowInd + 1;
	}
	public int getNumCols() {
		return data.size();
	}

	@Override
	public void takeTranspose() {
		throw new UnsupportedOperationException("SparseMatrix takeTranspose");
	}

	@Override
	public Matrix rightMultiplyBy(Matrix rightMatrix) {
		throw new UnsupportedOperationException("SparseMatrix rightMultiplyBy");
	}
}
