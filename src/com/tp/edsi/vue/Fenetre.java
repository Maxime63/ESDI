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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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
	private JTextField stockInital;
	private JScrollPane consolePan;
	private JTextArea console;
	private ButtonGroup buttonAlgos;
	private RadioBouton buttonMaxMinAbsolu;
	private RadioBouton buttonMaxMinRegret;
	private RadioBouton buttonMoyenne;
	private Bouton solve;
	private Bouton applicateAlgo;
	
	public Fenetre() {
		mdl = new Modele();
		mdl.addObserver(this);
		ctrl = new Controleur(mdl);
		
		fileChooser = new JFileChooser("./");
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("TP - Optimisation et évaluation des systèmes dans l'incertain");
		setSize(850, 450);
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
		gbc.gridheight = 9;
		gbc.insets = new Insets(0, 5, 0, 0);
		console = new JTextArea();
		console.setBackground(Color.WHITE);
		console.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		consolePan = new JScrollPane(console);
		consolePan.setPreferredSize(new Dimension(400, 350));
		panneau.add(consolePan, gbc);

		//LABEL STOCK INITIAL
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		panneau.add(new JLabel("Stock initial : "), gbc);
		
		//ZONE DE SAISIE STOCK INITIAL
		gbc.gridx = 1;
		stockInital = new JTextField(19);
		stockInital.setName(STOCK_INITIAL);
		stockInital.addFocusListener(ctrl);
		panneau.add(stockInital, gbc);

		//BOUTON POUR RESOUDRE LE PL
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(10, 0, 10, 0);
		solve = new Bouton(SOLVE, ctrl, SOLVE, false);
		panneau.add(solve, gbc);
		
		//AJOUT DES BOUTTONS POUR LES ALGOS
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridheight = 3;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(70, 0, 10, 0);
		
		JPanel panneauButtons = new JPanel(new GridLayout(3, 1));
		Border cadre = BorderFactory.createTitledBorder("Algorithmes");
		panneauButtons.setBorder(cadre);
		panneauButtons.setBackground(Color.WHITE);
	
		buttonAlgos = new ButtonGroup();
		buttonMaxMinAbsolu = new RadioBouton(ALGO_MAX_MIN_ABSOLU, Color.WHITE, ctrl, ALGO_MAX_MIN_ABSOLU);
		buttonMaxMinRegret = new RadioBouton(ALGO_MAX_MIN_REGRET, Color.WHITE, ctrl, ALGO_MAX_MIN_REGRET);
		buttonMoyenne = new RadioBouton(ALGO_MOYENNE, Color.WHITE, ctrl, ALGO_MOYENNE);
		
		buttonAlgos.add(buttonMaxMinAbsolu);
		buttonAlgos.add(buttonMaxMinRegret);
		buttonAlgos.add(buttonMoyenne);
	
		panneauButtons.add(buttonMaxMinAbsolu);
		panneauButtons.add(buttonMaxMinRegret);
		panneauButtons.add(buttonMoyenne);
		
		panneau.add(panneauButtons, gbc);
		
		//BOUTON QUI APPLIQUE L'ALGO SELECTIONNE
		gbc.gridx = 1;
		gbc.gridy = 8;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0, 0, 80, 0);
		applicateAlgo = new Bouton(APPLIQUER_ALGO, ctrl, APPLIQUER_ALGO, false);
		panneau.add(applicateAlgo, gbc);
		
		
		return panneau;
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
						if(mdl.isAlgoSelected()){
							applicateAlgo.setEnabled(true);														
						}
						break;
					case MSG_ERREUR:
						console.setText("/!\\ " + tab[1] + "/!\\");
						console.setCaretColor(Color.RED);
						break;
					case ALGO_SELECTED:
						if(mdl.isSolved()){
							applicateAlgo.setEnabled(true);							
						}
						break;
					case ALGO_APPLICATED:
						switch(mdl.getAlgorithme()){
							case ALGO_MAX_MIN_ABSOLU:
								afficherResultatMaxMinAbsolu();
								break;
							case ALGO_MAX_MIN_REGRET:
								afficherResultatMaxMinRegret();
								break;
							case ALGO_MOYENNE:
								afficherResultatMoyenne();
								break;
						}
						break;
				}
			}
		}
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
		resultatLp.append("--------------------------RESOLUTION---------------------------\n");
		
		resultatLp.append("Stock initial : ").append(mdl.getSolver().getStockInitial()).append("\n\n\t");
		
		
		for(int i = 0; i < nbScenarios; i++){
			resultatLp.append("Scenario ").append(i + 1).append("\t");
		}
		resultatLp.append("\n");
		for(int i = 0; i < nbInvestissements; i++){
			resultatLp.append("Inv ").append(i + 1).append("\t");
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

	private void afficherResultatMoyenne() {
		int nbInvestissements = mdl.getSolver().getData().getNbInvestissements();
		double maxMoyenne = 0.0;
		double valeur = 0.0;
		int bestInvestissement = 0;
		
		StringBuilder resultatMoyenne = new StringBuilder();
		resultatMoyenne.append("--------------------------ALGO MOYENNE---------------------------\n");
		
		for(int i = 0; i < nbInvestissements; i++){
			resultatMoyenne.append("Inv ").append(i + 1).append("\t");
		}
		
		resultatMoyenne.append("\n");
		
		for(int i = 0; i < nbInvestissements; i++){
			valeur = mdl.getSolver().getMoyenne(i);
			if(valeur > maxMoyenne){
				maxMoyenne = valeur;
				bestInvestissement = i;
			}
			resultatMoyenne.append(valeur).append("\t");
		}
		
		resultatMoyenne.append("\n\n");
		resultatMoyenne.append("Meilleur investissement : ").append(++bestInvestissement);
		
		console.setText(console.getText() + "\n\n\n" + resultatMoyenne.toString());
	}

	private void afficherResultatMaxMinRegret() {
		int nbInvestissements = mdl.getSolver().getData().getNbInvestissements();
		double maxRegret;
		int bestInvestissement = 0;
		
		StringBuilder resultatMaxMinRegret = new StringBuilder();
		resultatMaxMinRegret.append("--------------------------ALGO MAX MIN REGRET---------------------------\n\t");
		
		for(int i = 0; i < nbInvestissements; i++){
			resultatMaxMinRegret.append("Inv ").append(i + 1).append("\t");
		}
		resultatMaxMinRegret.append("\n\t");
		maxRegret = mdl.getSolver().getMaxMinRegret(0);
		for(int i = 0; i < nbInvestissements; i++){
			if(mdl.getSolver().getMaxMinRegret(i) == Solver.UNSOLVABLE){				
				resultatMaxMinRegret.append("Imp.").append("\t");
			}
			else{
				if(mdl.getSolver().getMaxMinRegret(i) > maxRegret){
					maxRegret = mdl.getSolver().getMaxMinRegret(i);
					bestInvestissement = i;
				}
				resultatMaxMinRegret.append(mdl.getSolver().getMaxMinRegret(i)).append("\t");
			}
		}
		resultatMaxMinRegret.append("\n\n");
		resultatMaxMinRegret.append("Meilleur investissement : ").append(++bestInvestissement);

		console.setText(console.getText() + "\n\n\n" + resultatMaxMinRegret.toString());
	}

	private void afficherResultatMaxMinAbsolu() {
		int nbScenarios = mdl.getSolver().getData().getNbScenarios();
		int nbInvestissements = mdl.getSolver().getData().getNbInvestissements();
		double valeur = 0.0;
		int[] inv = new int[nbInvestissements];
		int nb = 0;
		int bestInvestissement = 0;
		
		StringBuilder resultatAbsolu = new StringBuilder();
		resultatAbsolu.append("--------------------------ALGO MAX MIN ABSOLU---------------------------\n");
		
		for(int i = 0; i < nbScenarios; i++){
			resultatAbsolu.append("Scen ").append(i + 1).append("\t");
		}
		
		resultatAbsolu.append("\n");
		
		for(int i = 0; i < nbInvestissements; i ++){
			inv[i] = 0;
		}
		
		for(int i = 0; i < nbScenarios; i++){
			valeur = mdl.getSolver().getMaxMinAbsolu(i);
			
			for(int j = 0; j < nbInvestissements; j++){
				if(valeur == mdl.getSolver().getSolution(j, i)){
					inv[j]++;
				}
			}
			
			resultatAbsolu.append(valeur).append("\t");
		}
		
		for(int i = 0; i < nbInvestissements; i++){
			if(inv[i] > nb){
				bestInvestissement = i + 1;
				nb = inv[i];
			}
		}
		resultatAbsolu.append("\n\n");
		resultatAbsolu.append("Meilleur investissement : ").append(bestInvestissement);
		
		console.setText(console.getText() + "\n\n\n" + resultatAbsolu.toString());
	}
}
