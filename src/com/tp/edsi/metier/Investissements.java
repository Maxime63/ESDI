package com.tp.edsi.metier;

public class Investissements {
	
	private int nb_Machines;
	private int capa_unitaire;
	private int cout;
	private int cout_prod;

	public Investissements(int nbmac,int capau, int pcout, int cout_p)
	{
		this.nb_Machines=nbmac;
		this.capa_unitaire=capau;
		this.cout=pcout;
		this.cout_prod=cout_p;
		
	}
	public int getNb_Machines() {
		return nb_Machines;
	}

	public void setNb_Machines(int nb_Machines) {
		this.nb_Machines = nb_Machines;
	}

	public int getCapa_unitaire() {
		return capa_unitaire;
	}

	public void setCapa_unitaire(int capa_unitaire) {
		this.capa_unitaire = capa_unitaire;
	}

	public int getCout() {
		return cout;
	}

	public void setCout(int cout) {
		this.cout = cout;
	}

	public int getCout_prod() {
		return cout_prod;
	}

	public void setCout_prod(int cout_prod) {
		this.cout_prod = cout_prod;
	}
	

}
