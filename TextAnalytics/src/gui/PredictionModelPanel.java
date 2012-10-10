package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import orm.Company;

import controller.Controller;

public class PredictionModelPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private final CompanyComboBox companyComboBox;
	
	private static final String refStatsExplanation = "1. Articles for the company that referenced by eigenvectors are loaded into database with day_index set.\n" +
			"2. Stock data is loaded for the company for the date range specified by the dates of the articles.";

	private static final String calcStatsExplanation = "1.  Eigenvectors are loaded (eigenvector_value)\n" +
			"2.  Articles associated with eigenvectors are loaded and have day index";

	private static final String createPredModelExplanation = "1. eigenvectors loaded (eigenvector_value)\n" +
			"2. stock price changes calculated, ones to be used tagged (use_for_prediction='Y')";
	
	public PredictionModelPanel(final Controller controller) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		companyComboBox = new CompanyComboBox();
		add(companyComboBox);
		
		JPanel calcRefStatsPanel = new JPanel();
		JTextArea calcRefStatsTextArea = new JTextArea(refStatsExplanation);
		calcRefStatsTextArea.setEditable(false);
		calcRefStatsPanel.add(calcRefStatsTextArea);
		
		JButton calcRefStatsButton = new JButton("Calculate Reference Statistics");
		calcRefStatsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.calculateReferenceStatistics((Company)companyComboBox.getSelectedItem());
			}
		});
		calcRefStatsPanel.add(calcRefStatsButton);
		add(calcRefStatsPanel);
		
		JPanel calcStatsPanel = new JPanel();
		JTextArea calcStatsTextArea = new JTextArea(calcStatsExplanation);
		calcStatsTextArea.setEditable(false);
		calcStatsPanel.add(calcStatsTextArea);
		
		JButton calcStatsButton = new JButton("Calculate article-stock stats");
		calcStatsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.calculateArticleStockStatistics((Company)companyComboBox.getSelectedItem());
			}
		});
		calcStatsPanel.add(calcStatsButton);
		add(calcStatsPanel);
		
		JPanel createPredictionModelPanel = new JPanel();
		JTextArea createPredModelTextArea = new JTextArea(createPredModelExplanation);
		createPredModelTextArea.setEditable(false);
		createPredictionModelPanel.add(createPredModelTextArea);
		
		JButton createPredModelButton = new JButton("Create Prediction Model");
		createPredModelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controller.createPredictionModel((Company)companyComboBox.getSelectedItem());
			}
		});
		createPredictionModelPanel.add(createPredModelButton);
		add(createPredictionModelPanel);
	}
}
