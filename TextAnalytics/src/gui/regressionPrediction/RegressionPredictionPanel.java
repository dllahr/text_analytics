package gui.regressionPrediction;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

import controller.Controller;
import controller.util.Utilities;

import orm.RegressionModel;
import orm.SessionManager;

public class RegressionPredictionPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public RegressionPredictionPanel(final Controller controller) {
		super.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		final JComboBox<RegressionModel> rmComboBox = buildRegressionModelComboBox();
		rmComboBox.setMaximumSize(new Dimension(200, 20));
		super.add(rmComboBox);
		
		final RegressionPredictionTableModel tableModel = new RegressionPredictionTableModel();
		JTable table = new JTable(tableModel);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.HORIZONTAL);
		Enumeration<TableColumn> tableColumnEnumeration = table.getColumnModel().getColumns();
		while (tableColumnEnumeration.hasMoreElements()) {
			tableColumnEnumeration.nextElement().setCellRenderer(centerRenderer);
		}
				
		JButton button = new JButton();
		button.setText("Generate Regression Model Prediction");
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				RegressionModel rm = (RegressionModel)rmComboBox.getSelectedItem();
				tableModel.setRegressionPredictionDataList(controller.makeRegressionPrediction(rm));
			}
		});
		super.add(button);
		
		JScrollPane tableScrollPane = new JScrollPane(table);
		super.add(tableScrollPane);
		
	}

	private JComboBox<RegressionModel> buildRegressionModelComboBox() {
		List<RegressionModel> rmList = Utilities.convertGenericList(SessionManager.createQuery("from RegressionModel").list());
		
		Collections.sort(rmList, new Comparator<RegressionModel>() {

			@Override
			public int compare(RegressionModel o1, RegressionModel o2) {
				if (o1.getScoringModel().getId().equals(o2.getScoringModel().getId())) {
					return o1.getId() - o2.getId();
				} else {
					return o1.getScoringModel().getNotes().compareTo(o2.getScoringModel().getNotes());
				}
			}
			
		});
		
		return new JComboBox<>(new Vector<>(rmList));
	}

	
}
