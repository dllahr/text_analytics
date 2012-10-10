package gui.autoComplete;

import java.awt.event.KeyEvent;

/**
 * Calculates whether or not a trigger has occurred, based on last key typed.  Provides how to modify
 * user entered text (e.g. to strip out the last character typed, because that character was a trigger
 * character but should not be part of actual selection, e.g. tab)
 * 
 * @author David Lahr
 *
 */
public interface AutoCompleteTrigger {
	/**
	 * calculate whether or not the user's key typed indicates autocomplete should be provided
	 * @param e generated from the key typed event
	 * @return true if auto complete should be attempted based on the typed key, otherwise false
	 */
	public boolean attemptAutoComplete(KeyEvent e);
	
	/**
	 * modify the total text entered by the user, provided as return value.  For example, if the 
	 * trigger is set to occur when the user presses the tab button, the modifiedInputText could
	 * be set to remove the last tab character from the entered text
	 * @param entered current text entered by user
	 * @return resulting modified text
	 */
	public String modifyInputText(String entered);
}
