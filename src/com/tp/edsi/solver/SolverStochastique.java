package com.tp.edsi.solver;

import ilog.concert.IloException;
import ilog.cplex.IloCplex;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.tp.edsi.metier.Data;

public class SolverStochastique {
	public static final double UNSOLVABLE = -999999999999.99;
	
	private IloCplex cplex;
	private Data data;
	private int stockInitial;
	
	private String [] matriceLpFilename;
	private double [] matriceResultats;
	
	public SolverStochastique(Data data) throws IloException{
		cplex = new IloCplex();
		
		this.data = data;
		
		//Initialisation des tableaux de résultats
		matriceLpFilename = new String [data.getNbScenarios()];
		matriceResultats = new double [data.getNbScenarios()];

	}
	
	public void setStockInitial(int stockInitial){
		this.stockInitial = stockInitial;
	}
	
	public int getStockInitial(){
		return stockInitial;
	}
	
	public Data getData(){
		return data;
	}
	
	
	public void solveProblem() throws IloException, IOException{
		int nbScenarios = data.getNbScenarios();
		
		for(int j = 0; j < nbScenarios; j++){
			String lpFilename = "solve_" + j + ".lp";
			matriceLpFilename[j] = lpFilename;
			solve(lpFilename, j);
		}
	}
	
	private void solve(String lpFilename, int scenario) throws IloException, IOException{
		createLpFile(lpFilename, scenario);		
		cplex.importModel(lpFilename);
		
		if(cplex.solve()){
            matriceResultats[scenario] = cplex.getObjValue();
		}
		else{
			matriceResultats[scenario] = UNSOLVABLE;
		}
		
	}
	
	public double getSolution(int scenario){
		return matriceResultats[scenario];
	}
	

	
	private void createLpFile(String lpFilename, int scenario) throws IOException{
		OutputStream ops = new FileOutputStream(lpFilename); 
		OutputStreamWriter opsw = new OutputStreamWriter(ops);
		BufferedWriter bw = new BufferedWriter(opsw);
		
		int nbInvestissements = data.getNbInvestissements();
		
		bw.write("Maximize\n");
		bw.write("profit: ");
		bw.write(createMaxFunction(scenario));
		
		bw.write("\n\n");
		bw.write("Subject to\n");
		bw.write(createStockConstraint(scenario));
		bw.write("\n");
		bw.write(createCapaConstraint());
		bw.write(createOtherConstraint());
		
		bw.write("\n");
		bw.write("Bounds\n");
		bw.write("cst = 1");
		bw.write("\n");
		//STOCK INITIAUX
		bw.write("Y_0_0 = " + stockInitial);
		bw.write("\n");
		bw.write("Y_1_0 = " + stockInitial);
		
		bw.write("\n");
		bw.write("\n");
		bw.write("Binaries\n");
		for(int i = 0; i < nbInvestissements; i++){
			bw.write("Z_" + i + " ");
		}

		bw.write("\n");
		bw.write("End");
		bw.close();
	}
	
	private String createOtherConstraint() {
		StringBuilder otherConstraint = new StringBuilder();
		int nbInvestissements = data.getNbInvestissements();
		int nbPeriodes = data.getNbPeriodes();
		int nbProduits = data.getNbProduits();
		
		otherConstraint.append("Somme_Z: ");
		for(int i = 0; i < nbInvestissements; i ++){
			otherConstraint.append("Z_").append(i);
			
			if(i < nbInvestissements - 1){
				otherConstraint.append(" + ");
			}
		}
		otherConstraint.append(" = 1\n");
		
		for(int i = 0; i < nbPeriodes; i++){
			for(int j = 0; j < nbProduits; j++){
				for(int k = 0; k < nbInvestissements; k++){
					otherConstraint.append("W_").append(i).append("_").append(j).append("_").append(k).append("_a: ")
								   .append("W_").append(i).append("_").append(j).append("_").append(k)
								   .append(" - X_").append(i).append("_").append(j)
								   .append(" <= 0\n");
					
					otherConstraint.append("W_").append(i).append("_").append(j).append("_").append(k).append("_b: ")
								   .append("W_").append(i).append("_").append(j).append("_").append(k)
								   .append(" - ").append(data.getInvestissement(k).getCapacite()).append(" Z_").append(k)
								   .append(" <= 0\n");
					
					otherConstraint.append("W_").append(i).append("_").append(j).append("_").append(k).append("_c: ")
								   .append("W_").append(i).append("_").append(j).append("_").append(k).append(" >= 0\n");
					
//					otherConstraint.append("W_").append(i).append("_").append(j).append("_").append(k).append("_d: ")
//								   .append("W_").append(i).append("_").append(j).append("_").append(k)
//								   .append(" - X_").append(i).append("_").append(j)
//								   .append(" - ").append(data.getInvestissement(k).getCapacite()).append(" Z_").append(k)
//								   .append(" + ").append(data.getInvestissement(k).getCapacite())
//								   .append(" <= 0\n");
				}
			}
		}
		
		return otherConstraint.toString();
	}

	private String createMaxFunction(int scenario){
		StringBuilder maxFunction = new StringBuilder();
		
		int vente = 0;
		
		int nbProduits = data.getNbProduits();
		int nbPeriodes = data.getNbPeriodes();
		int nbInvestissements = data.getNbInvestissements();
		
		StringBuilder production = new StringBuilder();
		StringBuilder stockage = new StringBuilder();
		StringBuilder ammortissement = new StringBuilder();
		StringBuilder achat = new StringBuilder();
		
		for(int i = 0; i <= nbPeriodes; i++){
			for(int j = 0; j < nbProduits; j++){
				if(i == nbPeriodes){
					//COUT DE STOCKAGE
					stockage.append("-")
							.append(data.getStockage())
							.append(" Y_").append(j).append("_").append(i).append(" ");
				}
				else{
					//COUT DES VENTES (prix de vente * la demande)
					vente += data.getPrix(j) * data.getPeriode(i).getDemande(j, scenario);

					//COUT DE PRODUCTION
					for(int k = 0; k < nbInvestissements; k++){
						production.append("-")
								  .append(data.getInvestissement(k).getCoutProduction())
								  .append(" W_").append(i).append("_").append(j).append("_").append(k);
					}

					//COUT DE STOCKAGE
					stockage.append("-")
							.append(data.getStockage())
							.append(" Y_").append(j).append("_").append(i).append(" ");
					
					//COUT AMMORTISSEMENT
					for(int k = 0; k < nbInvestissements; k++){
						ammortissement.append("+")
									  .append(data.getAmortissement())
	  								  .append(" W_").append(i).append("_").append(j).append("_").append(k);
					}
				}
			}
		}
		
		//COUT DES ACHATS DE MACHINES
		for(int i = 0; i < nbInvestissements; i++){
			achat.append("-")
				 .append(data.getInvestissement(i).getCout()).append(" Z_").append(i).append(" ");
		}
		
		//COUT DE L'AMMORTISSEMENT
		for(int i = 0; i < nbInvestissements; i++){
			ammortissement.append("-")
						  .append(data.getAmortissement() * nbPeriodes * data.getInvestissement(i).getCapacite())
						  .append(" Z_").append(i).append(" ");
		}
		
		//CREATION DE LA FONCTION DE MAXIMISATION
		maxFunction.append(production).append(stockage).append(ammortissement).append(achat).append(" +")
				   .append(vente).append(" cst");
		
		
		return maxFunction.toString();
	}
	
	public String createCapaConstraint(){
		StringBuilder capaConstraint = new StringBuilder();
		
		int nbProduits = data.getNbProduits();
		int nbPerdiodes = data.getNbPeriodes();
		int nbInvestissements = data.getNbInvestissements();
		
		for(int i = 0; i < nbPerdiodes; i++){
			capaConstraint.append("Capa_").append(i).append(": ");
			for(int j = 0; j < nbProduits; j++){
				capaConstraint.append("X_").append(j).append("_").append(i);
				
				if(j < nbProduits - 1){
					capaConstraint.append(" + ");
				}
			}
			
			for(int k =  0; k < nbInvestissements; k++){
				capaConstraint.append(" - ").append(data.getInvestissement(k).getCapacite()).append(" Z_").append(k);
			}
			
			capaConstraint.append(" <= 0\n");
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
