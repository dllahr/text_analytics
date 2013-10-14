package math.linearAlgebra;

public interface Matrix {
	public Double getEntry(int rowInd, int colInd);
	
	public void setEntry(int rowInd, int colInd, double value);
	
	public void takeTranspose();
	
	public int getNumRows();
	
	public int getNumCols();

	public void addColumn(Vector vect, int colInd);
	
	public Matrix rightMultiplyBy(Matrix rightMatrix);
	
	public Matrix createCopy();
}
