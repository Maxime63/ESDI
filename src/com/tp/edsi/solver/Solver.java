package com.tp.edsi.solver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;

import com.tp.edsi.metier.Data;
import com.tp.edsi.metier.Investissement;
import com.tp.edsi.metier.Periode;

public class Solver {
	private IloCplex cplex;
	private Data data;
	private String lpFilename;
	
	public Solver(String filename) throws IloException{
		cplex = new IloCplex();
		lpFilename=filename;
	}
	
	public Data getData(){
		return data;
	}
	
	public void solve() throws IloException{
		cplex.importModel(lpFilename);
		
		if(cplex.solve()){
            cplex.output().println("Solution status = " + cplex.getStatus());
            cplex.output().println("Solution value  = " + cplex.getObjValue());

		}
		
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
		int nbPeriodes = Integer.parseInt(readDatas[0]);
		
		//NB PRODUITS
		ligne=br.readLine();
		readDatas=ligne.split(" ");
		int nbProduits = Integer.parseInt(readDatas[0]);
		
		//NB SCENARIOS
		ligne=br.readLine();
		readDatas=ligne.split(" ");
		int nbScenarios = Integer.parseInt(readDatas[0]);
		
		//NB INVESTISSEMENTS
		ligne=br.readLine();
		readDatas=ligne.split(" ");
		int nbInvestissements = Integer.parseInt(readDatas[0]);
		
		data = new Data(nbPeriodes, nbProduits, nbScenarios, nbInvestissements);
		
		ligne=br.readLine();
		
		//COUT DE STOCKAGE
		ligne=br.readLine();
		readDatas=ligne.split(" ");
		data.setStockage(Integer.parseInt(readDatas[1]));
		
		//PRIX DE VENTE DES PRODUITS
		ligne=br.readLine();
		readDatas=ligne.split(" ");
		for(int i = 0; i< nbProduits; i++){
			data.setPrix(i, Integer.parseInt(readDatas[i + 1]));
		}
		
		//COUT DE L'AMMORTISSEMENT
		ligne=br.readLine();
		readDatas=ligne.split(" ");
		data.setAmortissement(Integer.parseInt(readDatas[1]));
		
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
			data.addInvestissement(investissement);
			
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
			data.addPeriode(periode);			
		}
		br.close();
	}
	
	public void createLpFile(int investissement, int scenario) throws IOException{
		OutputStream ops = new FileOutputStream(lpFilename); 
		OutputStreamWriter opsw = new OutputStreamWriter(ops);
		BufferedWriter bw = new BufferedWriter(opsw);
		
		bw.write("Maximize\n");
		bw.write("profit: ");
		bw.write(createMaxFunction(investissement, scenario));
		
		bw.write("\n\n");
		bw.write("Subject to\n");
		bw.write(createStockConstraint(scenario));
		bw.write("\n");
		bw.write(createCapaConstraint(investissement));
		
		
		bw.write("\n");
		bw.write("Bounds\n");
		bw.write("cst = 1");
		bw.write("\n");
		bw.write("Y_0_0 = 0");
		bw.write("\n");
		bw.write("Y_1_0 = 0");
		
		bw.write("\n");
		bw.write("End");
		bw.close();
	}
	
	private String createMaxFunction(int investissement, int scenario){
		StringBuilder maxFunction = new StringBuilder();
		
		int vente = 0;
		int achat = 0;
		int ammort = 0;
		
		int nbProduits = data.getNbProduits();
		int nbPeriodes = data.getNbPeriodes();

		StringBuilder production = new StringBuilder();
		StringBuilder stockage = new StringBuilder();
		StringBuilder ammortissement = new StringBuilder();
		
		//COUT DES VENTES
		for(int i = 0; i < nbPeriodes; i++){
			for(int j = 0; j < nbProduits; j++){
				vente += data.getPrix(j) * data.getPeriode(i).getDemande(j, scenario);

				//COUT DE PRODUCTION
				production.append("-")
						  .append(data.getInvestissement(investissement).getCoutProduction())
						  .append(" X_").append(j).append("_").append(i).append(" ");

				//COUT DE STOCKAGE
				stockage.append("-")
						.append(data.getStockage())
						.append(" Y_").append(j).append("_").append(i).append(" ");
				
				//COUT AMMORTISSEMENT
				ammortissement.append("-")
				  .append(data.getAmortissement())
				  .append(" X_").append(j).append("_").append(i).append(" ");
			}
		}
		
		//COUT DES ACHATS DE MACHINES
		achat = data.getInvestissement(investissement).getCout();
		ammort = data.getAmortissement() * nbPeriodes * data.getInvestissement(investissement).getCapacite();
		
		//CREATION DE LA FONCTION DE MAXIMISATION
		maxFunction.append(production).append(stockage).append(ammortissement)
				   .append(vente - achat - ammort).append(" cst");
		
		
		return maxFunction.toString();
	}
	
	public String createCapaConstraint(int investissement){
		StringBuilder capaConstraint = new StringBuilder();
		
		int nbProduits = data.getNbProduits();
		int nbPerdiodes = data.getNbPeriodes();
		int capacite = data.getInvestissement(investissement).getCapacite();
		
		for(int i = 0; i < nbPerdiodes; i++){
			capaConstraint.append("Capa_").append(i).append(": ");
			for(int j = 0; j < nbProduits; j++){
				capaConstraint.append("X_").append(j).append("_").append(i);
				
				if(j < nbProduits - 1){
					capaConstraint.append(" + ");
				}
			}
			capaConstraint.append(" <= ").append(capacite).append("\n");
		}
		
		return capaConstraint.toString();
	}
	
	public String createStockConstraint(int scenario){
		StringBuilder stockConstraint = new StringBuilder();
		
		int nbProduits = data.getNbProduits();
		int nbPerdiodes = data.getNbPeriodes();
		
		for(int i = 0; i < nbPerdiodes; i++){
			for(int j = 0; j < nbProduits; j++){
				stockConstraint.append("stock_").append(j).append("_").append(i).append(": ")
							   .append("Y_").append(j).append("_").append(i + 1).append(" - ")
							   .append("Y_").append(j).append("_").append(i).append(" - ")
							   .append("X_").append(j).append("_").append(i).append(" = ")
							   .append(" -").append(data.getPeriode(i).getDemande(j, scenario))
							   .append("\n");
			}
		}
		
		return stockConstraint.toString();
	}
}
