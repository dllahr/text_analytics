package gui.autoComplete.file;

import java.io.File;

import gui.autoComplete.AutoCompleteTextArea;

public class FileAutoCompleteTextArea extends AutoCompleteTextArea<File> {
	private static final long serialVersionUID = 1L;
	
	private final static String triggerStr = "\t";
	public FileAutoCompleteTextArea() {
		super(new FileAutoCompleteTrigger(triggerStr), new FileSuggester(), new FileAutoCompleteDisplayer(triggerStr));
	}
	
	public File getCurrentFile() {
		File result = new File(getText());
		if (result.exists() && result.canRead()) {
			return result;
		} else {
			return null;
		}
	}
}
