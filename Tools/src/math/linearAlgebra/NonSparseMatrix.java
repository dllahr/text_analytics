package math.linearAlgebra;

import java.util.LinkedList;
import java.util.List;

public class NonSparseMatrix implements Matrix {
	private List<List<Double>> data;
	
	private int maxColInd;
	
	public NonSparseMatrix() {
		data = new LinkedList<List<Double>>();
		maxColInd = 0;
	}
	
	public Double getEntry(int rowInd, int colInd) {
		if(rowInd > data.size()) {
			throw new RuntimeException("NonSparseMatrix getEntry rowInd exceeds number of rows");
		}
		if (colInd > maxColInd) {
			throw new RuntimeException("NonSparseMatrix getEntry colInd exceeds number of columns");
		}
		
		List<Double> row = data.get(rowInd);
		return colInd < row.size() ? row.get(colInd) : 0.0;
	}
	
	public void setEntry(int rowInd, int colInd, double value) {
		while (rowInd >= data.size()) {
			data.add(new LinkedList<Double>());
		}
		
		List<Double> row = data.get(rowInd);
		while (colInd >= row.size()) {
			row.add(0.0);
		}
		
		data.get(rowInd).set(colInd, value);
		
		if (colInd > maxColInd) {
			maxColInd = colInd;
		}
	}
	
	public void takeTranspose() {
		NonSparseMatrix newMat = new NonSparseMatrix();
		
		int origRowInd = 0;
		for (List<Double> origRow : data) {
			int origColInd = 0;
			for (Double value : origRow) {
				newMat.setEntry(origColInd, origRowInd, value);
				origColInd++;
			}
			origRowInd++;
		}
		
		data = newMat.data;
		maxColInd = newMat.maxColInd;
	}
	
	public int getNumRows() {
		return data.size();
	}
	
	public int getNumCols() {
		return maxColInd + 1;
	}

	@Override
	public void addColumn(Vector vect, int colInd) {
		throw new UnsupportedOperationException("NonSparseMatrix addColumn");
	}

	@Override
	public Matrix rightMultiplyBy(Matrix rightMatrix) {
		throw new UnsupportedOperationException("NonSparseMatrix rightMultiplyBy");
	}

	@Override
	public Matrix createCopy() {
		throw new UnsupportedOperationException("NonSparseMatrix createCopy");
	}
}
