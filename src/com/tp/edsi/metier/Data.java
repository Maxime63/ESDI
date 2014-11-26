package com.tp.edsi.metier;

public class Data {
	private int periodes;
	private int produits;
	private int scenarios;
	private int investissements;
	
	public Data()
	{
		
	}
	
	public Data(int periodes, int produits, int scenarios, int investissements){
		this.periodes = periodes;
		this.produits = produits;
		this.scenarios = scenarios;
		this.investissements = investissements;
	}
	
	public int getPeriodes() {
		return periodes;
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
		periodes=a;
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
}
