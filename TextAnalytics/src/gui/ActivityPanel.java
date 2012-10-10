package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import gui.autoComplete.file.FileAutoCompleteTextArea;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import controller.Controller;

public class ActivityPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final Controller controller;

	public ActivityPanel(Controller cntrllr) {
		this.controller = cntrllr;
		
		JPanel loadPanel = new JPanel();
		loadPanel.setLayout(new BoxLayout(loadPanel, BoxLayout.PAGE_AXIS));
		loadPanel.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		loadPanel.add(new JLabel("csv file containing activity:"));
		
		final FileAutoCompleteTextArea activityFileTextArea = new FileAutoCompleteTextArea();
		loadPanel.add(activityFileTextArea);
		
		final JButton button = new JButton("load activity");
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				final File activityFile = activityFileTextArea.getCurrentFile();
				
				if (! activityFile.exists()) {
					System.out.println("file does not exist");
				} else if (! activityFile.canRead()) {
					System.out.println("cannot read file");
				} else {
					controller.loadActivity(activityFile);
				}
			}
		});
		loadPanel.add(button);
		
		add(loadPanel);
	}
}
