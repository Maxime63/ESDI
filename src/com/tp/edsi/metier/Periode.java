package com.tp.edsi.metier;

public class Periode {
	private int[][] demande;
	
	public Periode()
	{
		demande = new int[2][3];
	}
	
	
	public void setDemande(int produit,int scenario,int valeur)
	{
		demande[produit][scenario]=valeur;
		
	}

	public int getDemande(int produit,int scenario)
	{
		return demande[produit][scenario];
		
	}
}
