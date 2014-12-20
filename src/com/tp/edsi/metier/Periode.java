package com.tp.edsi.metier;

public class Periode {
	int nbProduits;
	int nbScenarios;
	int [][] demandes;
	
	public Periode(int nbProduits, int nbScenarios){
		this.nbProduits = nbProduits;
		this.nbScenarios =  nbScenarios;
		
		demandes = new int[nbProduits][nbScenarios];
	}
	
	public int getDemande(int produit, int scenario){
		return demandes[produit][scenario];
	}
	
	public void setDemande(int produit, int scenario, int demande){
		demandes[produit][scenario] = demande;
	}
}
