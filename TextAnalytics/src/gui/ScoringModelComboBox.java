package gui;

import java.util.Vector;

import javax.swing.JComboBox;

import controller.util.GetBasicInfo;

import orm.ScoringModel;

public class ScoringModelComboBox extends JComboBox<ScoringModel> {

	private static final long serialVersionUID = 1L;
	
	public ScoringModelComboBox() {
		super(new Vector<>(GetBasicInfo.getAllScoringModels()));
	}

}
