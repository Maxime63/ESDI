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
	

}
