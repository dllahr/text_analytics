package tools;

public class DataConverterToDouble {
	public static double[][] convertToDouble(Data data) {
		double[][] result = new double[data.getFirstDimension()][data.getSecondDimension()];

		for (int rowInd = 0; rowInd < data.getFirstDimension(); rowInd++) {
			for (int colInd = 0; colInd < data.getSecondDimension(); colInd++) {
				result[rowInd][colInd] = Double.valueOf(data.getDataArray()[rowInd][colInd].trim());
			}
		}
		
		return result;
	}
}
