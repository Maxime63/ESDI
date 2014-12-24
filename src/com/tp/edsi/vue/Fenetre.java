package com.tp.edsi.vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.Border;

import com.tp.edsi.constantes.ConstantesVues;
import com.tp.edsi.controleurs.Controleur;

public class Fenetre extends JFrame implements ConstantesVues{

	/***/
	private static final long serialVersionUID = 1L;

	private Controleur ctrl;
	
	private JTextField fileChoose;
	private JTextPane console;
	private ButtonGroup buttonAlgos;
	private JRadioButton buttonMaxMinAbsolu;
	private JRadioButton buttonMaxMinRegret;
	private JRadioButton buttonMoyenne;
	
	public Fenetre() {
		ctrl = new Controleur();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("TP - Optimisation et évaluation des systèmes dans l'incertain");
		setSize(800, 400);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new FlowLayout());
		getContentPane().setBackground(Color.WHITE);
		getContentPane().add(createGeneralPanel());
		setVisible(true);
	}
	
	private JPanel createGeneralPanel(){
		JPanel panneau = new JPanel();
		panneau.setBackground(Color.WHITE);
		panneau.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		
		//LABEL POUR CHARGEMENT DES DONNEES
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		panneau.add(new JLabel("Données : "), gbc);
		
		//ZONE DE SAISIE DU FICHIER
		gbc.gridx = 1;
		fileChoose = new JTextField(20);
		panneau.add(fileChoose, gbc);
		
		//BOUTON POUR PARCOURIR
		gbc.gridx = 2;
		JButton parcourir = new JButton(PARCOURIR);
		parcourir.addActionListener(ctrl);
		parcourir.setActionCommand(PARCOURIR);
		panneau.add(parcourir, gbc);
		
		//AFFICHAGE DE LA CONSOLE
		gbc.gridx = 3;
		gbc.gridwidth = 1;
		gbc.gridheight = 8;
		gbc.insets = new Insets(0, 5, 0, 0);
		console = new JTextPane();
		console.setBackground(Color.WHITE);
		console.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		console.setPreferredSize(new Dimension(400, 350));
		panneau.add(console, gbc);

		//BOUTON POUR RESOUDRE LE PL
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(10, 0, 10, 0);
		JButton solve = new JButton(SOLVE);
		solve.addActionListener(ctrl);
		solve.setActionCommand(SOLVE);
		panneau.add(solve, gbc);
		
		//AJOUT DES BOUTTONS POUR LES ALGOS
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridheight = 3;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(70, 0, 10, 0);
		
		JPanel panneauButtons = new JPanel(new GridLayout(3, 1));
		Border cadre = BorderFactory.createTitledBorder("Algorithmes");
		panneauButtons.setBorder(cadre);
		panneauButtons.setBackground(Color.WHITE);
	
		buttonAlgos = new ButtonGroup();
		buttonMaxMinAbsolu = new JRadioButton(ALGO_MAX_MIN_ABSOLU);
		buttonMaxMinAbsolu.setBackground(Color.WHITE);
		buttonMaxMinRegret = new JRadioButton(ALGO_MAX_MIN_REGRET);
		buttonMaxMinRegret.setBackground(Color.WHITE);
		buttonMoyenne = new JRadioButton(ALGO_MOYENNE);
		buttonMoyenne.setBackground(Color.WHITE);
		
		buttonAlgos.add(buttonMaxMinAbsolu);
		buttonAlgos.add(buttonMaxMinRegret);
		buttonAlgos.add(buttonMoyenne);
	
		panneauButtons.add(buttonMaxMinAbsolu);
		panneauButtons.add(buttonMaxMinRegret);
		panneauButtons.add(buttonMoyenne);
		
		panneau.add(panneauButtons, gbc);
		
		//BOUTON QUI APPLIQUE L'ALGO SELECTIONNE
		gbc.gridx = 1;
		gbc.gridy = 7;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 80, 0);
		JButton appliquerAlgo = new JButton(APPLIQUER_ALGO);
		panneau.add(appliquerAlgo, gbc);
		
		
		return panneau;
	}
	
//	JFileChooser fileChooser = new JFileChooser("./");
//	panneau.add(fileChooser);
}
