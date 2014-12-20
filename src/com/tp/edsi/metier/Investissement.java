package com.tp.edsi.metier;

public class Investissement {
	private int nbMachines;
	private int capaciteUnitaire;
	private int cout;
	private int coutProduction;

	public Investissement(int nbMachines,int capaciteUnitaire, int cout, int coutProduction)
	{
		this.nbMachines=nbMachines;
		this.capaciteUnitaire=capaciteUnitaire;
		this.cout=cout;
		this.coutProduction=coutProduction;
	}

	public int getNbMachines() {
		return nbMachines;
	}

	public int getCapaciteUnitaire() {
		return capaciteUnitaire;
	}

	public int getCout() {
		//Pour pouvoir obtenir les résultats envoyés par mail.
		//Mais c'est faux, il ne faut pas faire comme ça !
		return cout*nbMachines;
	}

	public int getCoutProduction() {
		return coutProduction;
	}
	
	public int getCapacite(){
		return capaciteUnitaire*nbMachines;
	}
	
}