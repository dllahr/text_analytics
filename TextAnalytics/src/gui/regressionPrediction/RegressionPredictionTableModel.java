package gui.regressionPrediction;

import java.sql.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import controller.regressionPrediction.RegressionPredictionData;

public class RegressionPredictionTableModel implements TableModel {
	
	private List<RegressionPredictionData> list;

	private static final String[] columnNameArray = {"Date", "Day Index", "Prediction Value"};
	private static final Class<?>[] columnClassArray = {Date.class, int.class, double.class};
	
	private final Set<TableModelListener> tableModelListenerSet;
	
	public RegressionPredictionTableModel() {
		this.list = new LinkedList<>();
		tableModelListenerSet = new HashSet<>();
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
		tableModelListenerSet.add(l);
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return columnClassArray[columnIndex];
	}

	@Override
	public int getColumnCount() {
		return columnNameArray.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNameArray[columnIndex];
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		RegressionPredictionData rpd = list.get(rowIndex);
		if (0 == columnIndex) {
			return rpd.getDate();
		} else if (1 == columnIndex) {
			return rpd.getDayIndex();
		} else if (2 == columnIndex) {
			return rpd.getValue();
		} else {
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		tableModelListenerSet.remove(l);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}
	
	public void setRegressionPredictionDataList(List<RegressionPredictionData> list) {
		this.list = list;
		
		for (TableModelListener l : tableModelListenerSet) {
			l.tableChanged(new TableModelEvent(this));
		}
	}
}
