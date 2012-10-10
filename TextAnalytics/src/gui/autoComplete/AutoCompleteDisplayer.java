package gui.autoComplete;

import java.util.List;

/**
 * functionality for displaying results of suggestions, and if there is a match found
 * 
 * @author Dave Lahr
 *
 * @param <T> type of the object that is being searched / suggested
 */
public interface AutoCompleteDisplayer<T> {
	/**
	 * get a list of strings for display based on the list of provided suggestions
	 * @param suggestList
	 * @return
	 */
	public List<String> getDisplayStringList(List<T> suggestList);
	
	/**
	 * get the string of text to be used when replacing the users currently typed text
	 * with a found match
	 * @param match
	 * @return
	 */
	public String getMatchString(T match);
	
	/**
	 * get the string of text to be used when replacing the users currently typed text
	 * with a partial match
	 * @param enteredText
	 * @param suggestList
	 * @return
	 */
	public String getPartialMatchText(String enteredText, List<T> suggestList);
}
