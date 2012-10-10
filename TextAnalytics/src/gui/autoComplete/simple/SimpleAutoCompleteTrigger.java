package gui.autoComplete.simple;

import gui.autoComplete.AutoCompleteTrigger;

import java.awt.event.KeyEvent;

/**
 * simple trigger mechanism for auto complete - triggers autocomplete suggestions after
 * every keystrokey after a certain number (threshold) of keys have been typed
 * @author Dave Lahr
 *
 */
public class SimpleAutoCompleteTrigger implements AutoCompleteTrigger {
	
	private final int countThreshold;
	
	private int typedCount;

	/**
	 * @param countThreshold number of keystrokes to wait before starting to indicate auto-complete is triggered
	 */
	public SimpleAutoCompleteTrigger(int countThreshold) {
		this.countThreshold = countThreshold;
	}

	@Override
	public boolean attemptAutoComplete(KeyEvent e) {
		return typedCount >= countThreshold;
	}

	@Override
	public String modifyInputText(String entered) {
		return entered;
	}

}
