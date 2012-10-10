package gui.autoComplete;


import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

/**
 * Swing text area that provides autocomplete suggestions as the user types.  Programmatically specify when to provide
 * auto-complete suggestions, suggestions to provide, and how to format display of autocomplete.
 * Works on the model of bash/unix autocomplete, not a drop down list.
 * 
 * @author dllahr
 *
 * @param <T> type of the object that is retrieved as autocomplete suggestion
 */
public class AutoCompleteTextArea<T> extends JPanel {

	private AutoCompleteSuggester<T> suggester;
	
	private AutoCompleteDisplayer<T> displayer;
	
	private final AutoCompleteTrigger trigger;
	
	private final JTextArea inputTextArea;
	
	private final JTextArea suggestTextArea;
	
	private static final long serialVersionUID = 1L;

	private List<T> suggestList;
	
	private AutoCompleteTextArea(AutoCompleteTrigger trigger) {
		this.trigger = trigger;
		
		inputTextArea = new JTextArea();
		inputTextArea.setRows(1);
		inputTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		inputTextArea.setColumns(120);
		inputTextArea.setRows(1);
		add(inputTextArea);

		suggestTextArea = new JTextArea(5,30);
		suggestTextArea.setLineWrap(true);
		suggestTextArea.setWrapStyleWord(true);
		suggestTextArea.setEditable(false);
		add(suggestTextArea);

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	}

	/**
	 * 
	 * @param trigger describes when autocomplete suggestions should be triggered (e.g. user has typed "tab" key)
	 * @param suggester  describes 
	 * @param displayer
	 */
	public AutoCompleteTextArea(AutoCompleteTrigger trigger, AutoCompleteSuggester<T> suggester, AutoCompleteDisplayer<T> displayer) {
		this(trigger);
		this.suggester = suggester;
		this.displayer = displayer;
		
		inputTextArea.addKeyListener(new AutoCompleteKeyAdapter(trigger));
	}

	private void suggestionTriggered() {
		final String triggerResult = trigger.modifyInputText(inputTextArea.getText());
		
		suggestList = suggester.getSuggestion(triggerResult);
		
		if (suggestList.size() > 1) {
			final List<String> displayStringList = displayer.getDisplayStringList(suggestList);
			final StringBuffer buffer = new StringBuffer();
			for (String suggestion : displayStringList) {
				buffer.append(suggestion);
				buffer.append("\t");
			}
			suggestTextArea.setText(buffer.toString());
			
			inputTextArea.setText(displayer.getPartialMatchText(triggerResult, suggestList));
		} else if (suggestList.size() == 1) {
			inputTextArea.setText(displayer.getMatchString(suggestList.get(0)));
			suggestTextArea.setText("");
		} else {
			inputTextArea.setText(triggerResult);
		}
		
		System.out.println(suggestTextArea.getText());
	}
	
	
	public List<T> getSuggestList() {
		return Collections.unmodifiableList(suggestList);
	}


	public String getText() {
		return inputTextArea.getText();
	}
	
	
	private class AutoCompleteKeyAdapter extends KeyAdapter {
		private AutoCompleteTrigger trigger;
		
		public AutoCompleteKeyAdapter(AutoCompleteTrigger trigger) {
			this.trigger = trigger;
		}

		@Override
		public void keyTyped(KeyEvent e) {
			super.keyPressed(e);
			if (trigger.attemptAutoComplete(e)) {
				suggestionTriggered();
			}
		}
	}
}
