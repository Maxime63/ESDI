package com.tp.edsi.controleurs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.tp.edsi.constantes.ConstantesVues;
import com.tp.edsi.model.Modele;

public class Controleur implements ActionListener, ConstantesVues{
	private Modele mdl;
	
	public Controleur(Modele mdl){
		this.mdl = mdl;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() instanceof JButton){
			JButton button = (JButton) e.getSource();
			
			switch(button.getActionCommand()){
				case PARCOURIR:
					mdl.openFileChooser();
					break;
				case SOLVE:
					mdl.solve();
					break;
				case APPLIQUER_ALGO:
					break;
			}
		}
	}
}
