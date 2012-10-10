package gui.autoComplete.file;

import gui.autoComplete.AutoCompleteDisplayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import controller.util.CommonStringBeginningCalculator;


public class FileAutoCompleteDisplayer implements
		AutoCompleteDisplayer<File> {
	private final String triggerStr;
	private CommonStringBeginningCalculator commonCalc = new CommonStringBeginningCalculator();

	public FileAutoCompleteDisplayer(String triggerStr) {
		this.triggerStr = triggerStr;
	}

	@Override
	public List<String> getDisplayStringList(List<File> suggestList) {
		final List<String> result = new ArrayList<>(suggestList.size());
		for (File curFile : suggestList) {
			String fileStr = curFile.isDirectory() ? curFile.getName() + File.separator : curFile.getName();
			result.add(fileStr);
		}
		return result;
	}

	@Override
	public String getMatchString(File match) {
		return match.isDirectory() ? match.getPath() + File.separator : match.getPath();
	}

	@Override
	public String getPartialMatchText(String enteredText, List<File> suggestList) {
		enteredText = enteredText.endsWith(triggerStr) ? enteredText.substring(0, enteredText.length()-1) : enteredText;
		if (suggestList.size() > 1) {
			final List<String> displayList = new ArrayList<>(suggestList.size());
			for (File file : suggestList) {
				displayList.add(file.getPath());
			}
			String common = commonCalc.calculateCommonBeginning(displayList);
			return common.length() > enteredText.length() ? common : enteredText;
		} else {
			return enteredText;
		}
	}
}