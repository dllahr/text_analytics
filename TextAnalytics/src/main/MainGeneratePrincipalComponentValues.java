package main;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import controller.prediction.principalComponent.ArticlePcValueSaver;
import controller.prediction.principalComponent.ArticlePrincipalComponentValueCalculator;
import controller.prediction.principalComponent.ArticlePrincipalComponentValues;


public class MainGeneratePrincipalComponentValues {

	private static final String dateFormatString = "yyyy-MM-dd";

	
	public static void main(String[] args) throws ParseException {
		final int scoringModelId = Integer.valueOf(args[0]);
		final Date minDate = (new SimpleDateFormat(dateFormatString)).parse(args[1]);
		
		List<ArticlePrincipalComponentValues> list = 
				(new ArticlePrincipalComponentValueCalculator()).calculate(scoringModelId, minDate);
		
		(new ArticlePcValueSaver()).save(list);
	}
}
