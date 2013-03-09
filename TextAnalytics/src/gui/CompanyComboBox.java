package gui;

import java.util.Vector;

import javax.swing.JComboBox;

import controller.util.GetBasicInfo;

import orm.ScoringModel;

public class CompanyComboBox extends JComboBox<ScoringModel> {

	private static final long serialVersionUID = 1L;
	
	public CompanyComboBox() {
		super(new Vector<>(GetBasicInfo.getAllCompanies()));
	}

}
