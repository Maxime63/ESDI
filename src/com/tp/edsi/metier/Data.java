package com.tp.edsi.metier;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
	
	public Data(){
		this.investissements = new ArrayList<Investissement>();
		this.periodes = new ArrayList<Periode>();
	}
	
	public void loadData(String filename) throws IOException{
		InputStream ips=new FileInputStream(filename); 
		InputStreamReader ipsr=new InputStreamReader(ips);
		BufferedReader br=new BufferedReader(ipsr);
		
		String ligne;
		String readDatas[];
		
		//NB PERIODES
		ligne=br.readLine();
		readDatas=ligne.split(" ");
		nbPeriodes = Integer.parseInt(readDatas[0]);
		
		//NB PRODUITS
		ligne=br.readLine();
		readDatas=ligne.split(" ");
		nbProduits = Integer.parseInt(readDatas[0]);
		prix = new int[nbProduits];
		
		//NB SCENARIOS
		ligne=br.readLine();
		readDatas=ligne.split(" ");
		nbScenarios = Integer.parseInt(readDatas[0]);
		
		//NB INVESTISSEMENTS
		ligne=br.readLine();
		readDatas=ligne.split(" ");
		nbInvestissements = Integer.parseInt(readDatas[0]);
		
		ligne=br.readLine();
		
		//COUT DE STOCKAGE
		ligne=br.readLine();
		readDatas=ligne.split(" ");
		stockage = Integer.parseInt(readDatas[1]);
		
		//PRIX DE VENTE DES PRODUITS
		ligne=br.readLine();
		readDatas=ligne.split(" ");
		for(int i = 0; i< nbProduits; i++){
			prix[i] = Integer.parseInt(readDatas[i + 1]);
		}
		
		//COUT DE L'AMMORTISSEMENT
		ligne=br.readLine();
		readDatas=ligne.split(" ");
		amortissement = Integer.parseInt(readDatas[1]);
		
		ligne=br.readLine();
		ligne=br.readLine();

		//AJOUT DES INVESTISSEMENTS
		for(int i = 0; i < nbInvestissements; i++)
		{
			ligne=br.readLine();
			readDatas=ligne.split(" ");
			
			Investissement investissement = new Investissement(	Integer.parseInt(readDatas[1]), 
																Integer.parseInt(readDatas[2]), 
																Integer.parseInt(readDatas[3]), 
																Integer.parseInt(readDatas[4]));
			addInvestissement(investissement);
			
		}
		
		ligne=br.readLine();
		ligne=br.readLine();
		
		//AJOUT DES PERIODES
		for(int i = 0; i < nbPeriodes; i++)
		{
			ligne=br.readLine();
			readDatas=ligne.split("	");
			
			Periode periode = new Periode(nbProduits, nbScenarios);
			
			for(int j = 0; j < nbProduits; j++){
				for(int k = 0; k < nbScenarios; k++){
					String cout = readDatas[(j*nbInvestissements) + k + 2].split(" ")[0];
					periode.setDemande(j, k, Integer.parseInt(cout));
				}
			}
			addPeriode(periode);			
		}
		br.close();
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
