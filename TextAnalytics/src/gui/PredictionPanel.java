package gui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import gui.autoComplete.file.FileAutoCompleteTextArea;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;

import orm.ScoringModel;

import controller.Controller;

class PredictionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private static final String information = "1. prediction models for company must be in database\n" +
			"  a.  (requires stock price change calculations complete, analyzed)\n" +
			"2.  principal components for eigenvalues referenced by prediction model must calculated in database";
	
	private static final String defaultFileName = "predictions.csv";
	
	PredictionPanel(final Controller controller) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		JTextArea infoTextArea = new JTextArea(information);
		infoTextArea.setEditable(false);
		add(infoTextArea);
		
		add(new JLabel("Individual article directory:"));
		
		final FileAutoCompleteTextArea fileArea = new FileAutoCompleteTextArea();
		add(fileArea);
		
		JPanel bottomPanel = new JPanel();
		add(bottomPanel);
		
		final ScoringModelComboBox scoringModelCombobox = new ScoringModelComboBox();
		bottomPanel.add(scoringModelCombobox);
		
		JButton button = new JButton("Load Articles (formerly Make Prediction)");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.makePredictions((ScoringModel) scoringModelCombobox.getSelectedItem(), fileArea.getCurrentFile());
			}
		});
		bottomPanel.add(button);
		
		button = new JButton("Load Articles without Date");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.loadArticlesWithoutDates((ScoringModel)scoringModelCombobox.getSelectedItem(), fileArea.getCurrentFile());
			}
		});
		bottomPanel.add(button);
		
		add(Box.createVerticalStrut(15));
		JPanel results = new JPanel();
		results.setBorder(new BevelBorder(BevelBorder.RAISED));
		add(results);
		
		JButton resultButton = new JButton("Export Prediction Results");
		resultButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.savePredictions(new File(defaultFileName));
			}
		});
		results.add(resultButton);
		results.add(new JLabel("results file:  " + defaultFileName));
	}
}
