package com.tp.edsi.vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.Border;

import com.tp.edsi.constantes.ConstantesVues;
import com.tp.edsi.controleurs.Controleur;
import com.tp.edsi.model.Modele;
import com.tp.edsi.solver.Solver;

public class Fenetre extends JFrame implements ConstantesVues, Observer{

	/***/
	private static final long serialVersionUID = 1L;

	private Controleur ctrl;
	private Modele mdl;
	
	private JFileChooser fileChooser;
	
	private JTextField fileChoose;
	private JTextPane console;
	private ButtonGroup buttonAlgos;
	private JRadioButton buttonMaxMinAbsolu;
	private JRadioButton buttonMaxMinRegret;
	private JRadioButton buttonMoyenne;
	private Bouton solve;
	
	public Fenetre() {
		mdl = new Modele();
		mdl.addObserver(this);
		ctrl = new Controleur(mdl);
		
		fileChooser = new JFileChooser("./");
		
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
		panneau.add(new Bouton(PARCOURIR, ctrl, PARCOURIR, true), gbc);
		
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
		solve = new Bouton(SOLVE, ctrl, SOLVE, false);
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
		panneau.add(new Bouton(APPLIQUER_ALGO, ctrl, APPLIQUER_ALGO, false), gbc);
		
		
		return panneau;
	}
	
	private void openFileChooser() {
		fileChooser.showOpenDialog(this);
		String dataFilename = fileChooser.getSelectedFile().getAbsoluteFile().toString();
		fileChoose.setText(dataFilename);
		mdl.setDataFilename(dataFilename);
		solve.setEnabled(true);
	}
	
	private void afficherResultatLp(){
		int nbInvestissements = mdl.getSolver().getData().getNbInvestissements();
		int nbScenarios = mdl.getSolver().getData().getNbScenarios();
		
		StringBuilder resultatLp = new StringBuilder();
		resultatLp.append("--------------------------RESOLUTION---------------------------\n\t");
		
		for(int i = 0; i < nbScenarios; i++){
			resultatLp.append("Scenario ").append(i).append("\t");
		}
		resultatLp.append("\n");
		for(int i = 0; i < nbInvestissements; i++){
			resultatLp.append("Inv ").append(i).append("\t");
			for(int j = 0; j < nbScenarios; j++){
				if(mdl.getSolver().getSolution(i, j) == Solver.UNSOLVABLE){
					resultatLp.append("Imp.").append("\t");					
				}
				else{
					resultatLp.append(mdl.getSolver().getSolution(i, j)).append("\t");
				}
			}
			resultatLp.append("\n");
		}
		console.setText(resultatLp.toString());
	}

	@Override
	public void update(Observable o, Object arg) {
		if(o instanceof Modele){
			if(arg instanceof String){
				String msg = (String) arg;
				String[] tab = msg.split(SPLIT);
				
				switch (tab[0]){
					case OPEN_FILE_CHOOSER:
						openFileChooser();
						break;
					case PROBLEM_SOLVE:
						afficherResultatLp();						
						break;
					case MSG_ERREUR:
						console.setText("/!\\ " + tab[1] + "/!\\");
						console.setCaretColor(Color.RED);
						break;
				}
			}
		}
	}
}
