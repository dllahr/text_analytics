package gui;

import gui.autoComplete.file.FileAutoCompleteTextArea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import controller.legacy.LoadArticles;
import controller.legacy.LoadDistributionCalc;
import controller.legacy.loadVectors.LoadVectors;

import orm.Company;

public class LegacyPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	final private FileAutoCompleteTextArea fileArea;
	
	final private JTextArea messageArea;
	
	final private JComboBox<Company> comboBox;
	
	public LegacyPanel() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
 
		comboBox = new CompanyComboBox();
		add(comboBox);
		
		fileArea = new FileAutoCompleteTextArea();
		add(fileArea);
		
		messageArea = new JTextArea();
		messageArea.setEditable(false);
		add(messageArea);
		
		addLoadDistCalcButton();
		
		addLoadVectorsCalcButton();
		
		add(buildSpacerPanel());
		
		addLoadArticlesPanel();
	}
	
	private static JPanel buildSpacerPanel() {
		JPanel spacer = new JPanel();
		spacer.add(new JLabel("---"));
		return spacer;
	}

	private void addLoadArticlesPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
		panel.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		JPanel articlePanel = new JPanel();
//		panel.setLayout(new BoxLayout(articlePanel, BoxLayout.LINE_AXIS));
		articlePanel.add(new JLabel("article directory & file prefix"));
		final JTextField articlePrefixField = new JTextField();
		articlePrefixField.setColumns(50);
		articlePanel.add(articlePrefixField);
		final FileAutoCompleteTextArea articleDirFileArea = new FileAutoCompleteTextArea();
		articlePanel.add(articleDirFileArea);
		panel.add(articlePanel);
		
		JPanel articleOrderPanel = new JPanel();
		articleOrderPanel.add(new JLabel("File containing order of articles"));
		final FileAutoCompleteTextArea articleOrderFileArea = new FileAutoCompleteTextArea();
		articleOrderPanel.add(articleOrderFileArea);
		panel.add(articleOrderPanel);
		
		JButton button = new JButton("Load Articles");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				File articleDir = articleDirFileArea.getCurrentFile();
				File articleOrderFile = articleOrderFileArea.getCurrentFile();
				
				if (articleDir != null && articleOrderFile != null) {
					LoadArticles.load(articleDir, articlePrefixField.getText(),
							articleOrderFile, (Company)comboBox.getSelectedItem());
				}
			}
		});
		panel.add(button);
		
		add(panel);
	}

	private void addLoadVectorsCalcButton() {
		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		final JTextField prefix = new JTextField();
		prefix.setColumns(40);
		panel.add(prefix);
		
		final JCheckBox checkBox = new JCheckBox("sparse vectors");
		checkBox.setSelected(true);
		panel.add(checkBox);
		
		JButton button = new JButton("Load vectors");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				File dataFile = fileArea.getCurrentFile();
				if (dataFile != null) {
					if (checkBox.isSelected()) {
						LoadVectors.loadSparseVectors(prefix.getText().trim(), dataFile, (Company)comboBox.getSelectedItem());
					} else {
						LoadVectors.loadRegularVectors(prefix.getText().trim(), dataFile, (Company)comboBox.getSelectedItem());
					}
				}
			}
		});
		panel.add(button);
		
		add(panel);
	}

	private void addLoadDistCalcButton() {
		JButton button = new JButton("Load distribution calculation results file (csv)");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File dataFile = fileArea.getCurrentFile();
				if (dataFile != null) {
					LoadDistributionCalc.loadDistributionCalc(dataFile, (Company)comboBox.getSelectedItem());
				}
			}
		});
		
		add(button);
	}
	
	
}
