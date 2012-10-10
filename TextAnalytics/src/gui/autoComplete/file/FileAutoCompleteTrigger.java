package gui.autoComplete.file;

import gui.autoComplete.AutoCompleteTrigger;

import java.awt.event.KeyEvent;

public class FileAutoCompleteTrigger implements AutoCompleteTrigger {
	private final String triggerStr;

	public FileAutoCompleteTrigger(String triggerStr) {
		this.triggerStr = triggerStr;
	}

	@Override
	public boolean attemptAutoComplete(KeyEvent e) {
		if (String.valueOf(e.getKeyChar()).equals(triggerStr)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String modifyInputText(String entered) {
		return entered.endsWith(triggerStr) ? entered.substring(0, entered.length()-1) : entered;
	}
}