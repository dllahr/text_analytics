package gui.autoComplete;

import java.util.List;

/**
 * generate suggestions to display to user based on entered text
 * 
 * @author Dave Lahr
 *
 * @param <T> type of object that is a suggestion for the auto-complete
 */
public interface AutoCompleteSuggester<T> {
	/**
	 * 
	 * @param enteredText text currently entered by user
	 * @return list of objects that are possible matches to what the user has typed
	 */
	public List<T> getSuggestion(String enteredText);
}
