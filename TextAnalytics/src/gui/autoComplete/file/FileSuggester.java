package gui.autoComplete.file;

import gui.autoComplete.AutoCompleteSuggester;

import java.io.File;
import java.io.FilenameFilter;
import java.util.LinkedList;
import java.util.List;

public class FileSuggester implements AutoCompleteSuggester<File> {

	@Override
	public List<File> getSuggestion(String enteredText) {
		final List<File> result = new LinkedList<>();
		final File raw = new File(enteredText.trim());

		final String matchPrefix;
		final File parent;
		if (! enteredText.endsWith(File.separator)) {
			matchPrefix = raw.getName();
			parent = raw.getParentFile();
		} else {
			matchPrefix = "";
			parent = raw;
		}

		if (parent != null && parent.exists() && parent.isDirectory() && parent.canRead()) {
			final File[] matchFileArray = parent.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File arg0, String arg1) {
					return arg1.startsWith(matchPrefix);
				}
			});

			for (File matchFile : matchFileArray) {
				result.add(matchFile);
			}
		}
		
		if (result.size() == 0 && raw.exists() && raw.isDirectory()) {
			for (File curFile : raw.listFiles()) {
				result.add(curFile);
			}
		}

		return result;
	}

}
