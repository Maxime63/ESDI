package com.tp.edsi.metier;

import java.util.ArrayList;
import java.util.List;

public class Data {
	private int nbperiodes;
	private int produits;
	private int scenarios;
	private int investissements;
	private List<Investissements> invests;
	private int prix_A;
	private int prix_B;
	private int amort;
	private int stockage;
	private List<Periode> periodes;
	
	public Data()
	{
		this.invests=new ArrayList<Investissements>();
		this.periodes=new ArrayList<Periode>();
	}
	
	public Data(int periodes, int produits, int scenarios, int investissements){
		this.nbperiodes = periodes;
		this.produits = produits;
		this.scenarios = scenarios;
		this.investissements = investissements;
		this.invests=new ArrayList<Investissements>();
	}
	
	public int getPeriodes() {
		return nbperiodes;
	}
	public int getProduits() {
		return produits;
	}
	public int getScenarios() {
		return scenarios;
	}
	public int getInvestissements() {
		return investissements;
	}
	
	public void setPeriodes(int a) {
		nbperiodes=a;
	}
	
	public void setProduits(int a) {
		produits=a;
	}
	
	public void setScenarios(int a) {
		scenarios=a;
	}
	
	public void setInvestissements(int a) {
		investissements=a;
	}
	
	public int getPrix_A() {
		return prix_A;
	}

	public void setPrix_A(int prix_A) {
		this.prix_A = prix_A;
	}

	public int getPrix_B() {
		return prix_B;
	}

	public void setPrix_B(int prix_B) {
		this.prix_B = prix_B;
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
		p.setProduitA(a, b, c);
		p.setProduitB(d, e, f);
		periodes.add(p);
	}
}
