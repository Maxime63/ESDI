package com.tp.edsi.controleurs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JTextField;

import com.tp.edsi.constantes.ConstantesVues;
import com.tp.edsi.model.Modele;
import com.tp.edsi.vue.RadioBouton;

public class Controleur implements ActionListener, FocusListener, ConstantesVues{
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
					mdl.applicateAglorithm();
					break;
			}
		}
		if(e.getSource() instanceof RadioBouton){
			RadioBouton radioBouton = (RadioBouton) e.getSource();
			mdl.setAlgorithme(radioBouton.getActionCommand());
		}
	}

	@Override
	public void focusGained(FocusEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		if(e.getSource() instanceof JTextField){
			JTextField stockInitial = (JTextField) e.getSource();
			
			if(stockInitial.getName().equals(STOCK_INITIAL)){
				try{
					mdl.setStockInitial(Integer.parseInt(stockInitial.getText()));					
				}
				catch(NumberFormatException exception){
					System.out.println("ERREUR !");
					mdl.setStockInitial(0);
				}
			}
		}
	}
}
