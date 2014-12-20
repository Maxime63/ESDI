package com.tp.edsi.metier;

import java.util.ArrayList;
import java.util.List;

public class Data {
	private int nbPeriodes;
	private int nbProduits;
	private int nbScenarios;
	private int nbInvestissements;

	private List<Periode> periodes;
	private List<Investissement> investissements;

	private int[] prix;
	
	private int amortissement;
	private int stockage;
	
	public Data(int nbPeriodes, int nbProduits, int nbScenarios, int nbInvestissements){
		this.investissements = new ArrayList<Investissement>();
		this.periodes = new ArrayList<Periode>();
		this.prix = new int[nbProduits];

		this.nbPeriodes = nbPeriodes;
		this.nbProduits = nbProduits;
		this.nbScenarios = nbScenarios;
		this.nbInvestissements = nbInvestissements;		
	}
	
	public int getNbPeriodes() {
		return nbPeriodes;
	}
	
	public int getNbProduits() {
		return nbProduits;
	}
	
	public int getNbScenarios() {
		return nbScenarios;
	}
	
	public int getNbInvestissements() {
		return nbInvestissements;
	}
	
	public int getPrix(int produit) {
		return prix[produit];
	}

	public void setPrix(int produit, int prix) {
		this.prix[produit] = prix;
	}

	public int getAmortissement() {
		return amortissement;
	}

	public void setAmortissement(int amortissement) {
		this.amortissement = amortissement;
	}

	public int getStockage() {
		return stockage;
	}

	public void setStockage(int stockage) {
		this.stockage = stockage;
	}

	public void addInvestissement(Investissement investissement)
	{
		investissements.add(investissement);
	}
	
	public void addPeriode(Periode periode)
	{
		periodes.add(periode);
	}
	
	public Periode getPeriode(int index){
		return periodes.get(index);
	}
	
	public Investissement getInvestissement(int index){
		return investissements.get(index);
	}
}
