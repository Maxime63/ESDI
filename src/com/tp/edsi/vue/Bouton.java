package com.tp.edsi.vue;

import java.awt.event.ActionListener;

import javax.swing.JButton;

public class Bouton extends JButton{
	/**	 */
	private static final long serialVersionUID = 1L;

	public Bouton(String titre, ActionListener actionListener, String actionCommand, boolean enable){
		setText(titre);
		addActionListener(actionListener);
		setActionCommand(actionCommand);
		setEnabled(enable);
	}
}
