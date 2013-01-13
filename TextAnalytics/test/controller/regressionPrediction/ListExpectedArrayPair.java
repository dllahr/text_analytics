package controller.regressionPrediction;

import java.util.List;

import orm.ArticlePcValue;

public class ListExpectedArrayPair {
	public final List<ArticlePcValue> list;
	public final double[][] expectedArray;
	
	public ListExpectedArrayPair(List<ArticlePcValue> list,
			double[][] expectedArray) {
		this.list = list;
		this.expectedArray = expectedArray;
	}
}
