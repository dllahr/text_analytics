package gui.autoComplete.simple;

import gui.autoComplete.AutoCompleteSuggester;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Simple auto complete suggester that takes a map containing the relationship between result-objects (objects
 * that result from the search) and the string used to look them up.  When suggestions are generated, compares
 * the entered text to the strings used for lookup, anything object's string that begins with the entered text
 * is returned as a suggested match
 * 
 * @author Dave Lahr
 *
 * @param <T> type of the object that is being searched for
 */
public class SimpleAutoCompleteSuggester<T> implements AutoCompleteSuggester<T> {

	Map<T, String> resultStringMap;
	
	/**
	 * @param resultsStringMap  map between all objects that result from the search and the string that is used to
	 * match them for the search / auto-complete
	 */
	public SimpleAutoCompleteSuggester(Map<T, String> resultsStringMap) {
		this.resultStringMap = resultsStringMap;
	}

	@Override
	public List<T> getSuggestion(String enteredText) {
		List<T> suggestList = new LinkedList<>();
		for (T result : resultStringMap.keySet()) {
			if (resultStringMap.get(result).startsWith(enteredText)) {
				suggestList.add(result);
			}
		}
		return suggestList;
	}

}
