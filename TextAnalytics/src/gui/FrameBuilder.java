package gui;


import gui.regressionPrediction.RegressionPredictionPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import controller.Controller;
import controller.stockUpdate.StockUpdate;

public class FrameBuilder {

	public JFrame buildFrame(final Controller controller) {
		JFrame frame = new JFrame("dllahr text analytics");
		frame.setSize(1200, 800);

		JTabbedPane tabPane = new JTabbedPane();
		frame.getContentPane().add(tabPane);

		JButton button = new JButton("Update Stock Prices");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					(new StockUpdate()).updateStockPrices();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		JPanel stockPanel = new JPanel();
		stockPanel.setLayout(new BoxLayout(stockPanel, BoxLayout.PAGE_AXIS));
		stockPanel.add(button);
		tabPane.add("Basic", stockPanel);
		
		tabPane.add("Prediction", new PredictionPanel(controller));
		
		tabPane.add("Regression", new RegressionPredictionPanel(controller));
		
		tabPane.add("Legacy", new LegacyPanel());
		
		tabPane.add("Prediction Model", new PredictionModelPanel(controller));
		
		tabPane.add("Activity", new ActivityPanel(controller));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.pack();

		return frame;
	}
}
