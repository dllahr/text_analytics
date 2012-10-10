package gui.autoComplete.simple;

import gui.autoComplete.AutoCompleteDisplayer;

import java.util.LinkedList;
import java.util.List;

import controller.util.CommonStringBeginningCalculator;


public class SimpleAutoCompleteDisplayer<T> implements AutoCompleteDisplayer<T> {
	
	final private CommonStringBeginningCalculator commonCalc;

	public SimpleAutoCompleteDisplayer() {
		commonCalc = new CommonStringBeginningCalculator();
	}

	@Override
	public List<String> getDisplayStringList(List<T> suggestList) {
		List<String> result = new LinkedList<>();
		for (T suggest : suggestList) {
			result.add(suggest.toString());
		}
		return result;
	}

	@Override
	public String getMatchString(T match) {
		return match.toString();
	}

	@Override
	public String getPartialMatchText(String enteredText, List<T> suggestList) {
		if (suggestList.size() > 1) {
			final List<String> displayList = getDisplayStringList(suggestList);

			String common = commonCalc.calculateCommonBeginning(displayList);
			
			return common.length() > enteredText.length() ? common : enteredText;
		} 
		return enteredText;
	}

}
