package com.tp.edsi.metier;

import java.util.ArrayList;
import java.util.List;

public class Data {
	private int nbPeriodes;
	private int nbProduits;
	private int nbScenarios;
	private int nbInvestissements;
	private List<Investissements> invests;
	private List<Integer> prixVente;
	private int amort;
	private int stockage;
	private List<Periode> periodes;
	
	public Data()
	{
		this.invests=new ArrayList<Investissements>();
		this.periodes=new ArrayList<Periode>();
		this.prixVente=new ArrayList<Integer>();
	}
	
	public Data(int periodes, int produits, int scenarios, int investissements){
		this.nbPeriodes = periodes;
		this.nbProduits = produits;
		this.nbScenarios = scenarios;
		this.nbInvestissements = investissements;
		this.invests=new ArrayList<Investissements>();
	}
	
	public int getNbPeriodes() {
		return nbPeriodes;
	}
	
	public Periode getPeriode(int index){
		return periodes.get(index);
	}
	
	public Investissements getInvests(int index)
	{
		return invests.get(index);
	}
	
	public int getNbProduits() {
		return nbProduits;
	}
	public int getScenarios() {
		return nbScenarios;
	}
	public int getNbInvestissements() {
		return nbInvestissements;
	}
	
	public void setNbPeriodes(int nbPeriodes) {
		this.nbPeriodes=nbPeriodes;
	}
	
	public void setNbProduits(int nbProduits) {
		this.nbProduits=nbProduits;
	}
	
	public void setNbScenarios(int nbScenarios) {
		this.nbScenarios=nbScenarios;
	}
	
	public void setNbInvestissements(int nbInvestisements) {
		this.nbInvestissements=nbInvestisements;
	}
	
	public int getPrixVente(int index)
	{
		return prixVente.get(index);
	}

	public int getAmort() {
		return amort;
	}

	public void setAmort(int amort) {
		this.amort = amort;
	}

	public int getStockage() {
		return stockage;
	}

	public void setStockage(int stockage) {
		this.stockage = stockage;
	}

	public void setInvests(int nbmac,int capau, int pcout, int cout_p)
	{
		Investissements i=new Investissements(nbmac, capau, pcout, cout_p);
		invests.add(i);
	}
	
	public void setPeriode(int a,int b ,int c,int d,int e,int f)
	{
		Periode p=new Periode();
		p.setDemande(0, 0, a);
		p.setDemande(0, 1, b);
		p.setDemande(0, 2, c);
		p.setDemande(1, 0, d);
		p.setDemande(1, 1, e);
		p.setDemande(1, 2, f);
		periodes.add(p);
	}

	public void setPrixVente(int prix) {
		// TODO Auto-generated method stub
		prixVente.add(prix);
	}
	
}
