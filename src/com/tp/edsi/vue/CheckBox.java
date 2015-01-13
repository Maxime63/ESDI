package com.tp.edsi.vue;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

public class CheckBox extends JCheckBox {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CheckBox(String titre, Color backgroundColor, ActionListener actionListner, String actionCommand){
		this.setText(titre);
		this.setBackground(backgroundColor);
		this.addActionListener(actionListner);
		this.setActionCommand(actionCommand);
	}
}
