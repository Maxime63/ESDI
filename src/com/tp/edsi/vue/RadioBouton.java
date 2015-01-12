package com.tp.edsi.vue;

import java.awt.Color;
import java.awt.event.ActionListener;

import javax.swing.JRadioButton;

public class RadioBouton extends JRadioButton {
	public RadioBouton(String titre, Color backgroundColor, ActionListener actionListner, String actionCommand){
		this.setText(titre);
		this.setBackground(backgroundColor);
		this.addActionListener(actionListner);
		this.setActionCommand(actionCommand);
	}
}
