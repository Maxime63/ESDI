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
	
	public void resolutionStochastique(){
		
	}
	
	public void solveProblem() throws IloException, IOException{
		String lpFilename = "solveStoch.lp";
		solve(lpFilename);
	}
	
	private void solve(String lpFilename) throws IloException, IOException{
		createLpFile(lpFilename);		
		cplex.importModel(lpFilename);
		cplex.solve();
	}
	
	public double getSolution(int scenario){
		return matriceResultats[scenario];
	}
	

	
	private void createLpFile(String lpFilename) throws IOException{
		OutputStream ops = new FileOutputStream(lpFilename); 
		OutputStreamWriter opsw = new OutputStreamWriter(ops);
		BufferedWriter bw = new BufferedWriter(opsw);
		
		int nbInvestissements = data.getNbInvestissements();
		int nbScenarios = data.getNbScenarios();
		int nbPeriodes = data.getNbPeriodes();
		int nbProduits = data.getNbProduits();
		
		bw.write("Maximize\n");
		bw.write("profit: ");
		bw.write(createMaxFunction());
		
		bw.write("\n\n");
		bw.write("Subject to\n");
		bw.write(createStockConstraint());
		bw.write("\n");			
		bw.write(createCapaConstraint());
		bw.write(createOtherConstraint());
		
		bw.write("\n");
		bw.write("Bounds\n");
		bw.write("cst = 1");
		bw.write("\n");
		
		//STOCK INITIAUX
		
		for(int j = 0; j < nbProduits; j++){
			for(int k = 0; k < nbScenarios; k++){
				bw.write("Y_0_" + j + "_" + k + " = " + stockInitial);
				bw.write("\n");
			}
		}
		
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
		int nbScenarios = data.getNbScenarios();
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
					for(int l = 0; l < nbScenarios; l++){
						otherConstraint.append("W_").append(j).append("_").append(i).append("_").append(k).append("_").append(l).append("_a: ")
						   .append("W_").append(j).append("_").append(i).append("_").append(k).append("_").append(l)
						   .append(" - X_").append(j).append("_").append(i).append("_").append(l)
						   .append(" <= 0\n");
			
						otherConstraint.append("W_").append(j).append("_").append(i).append("_").append(k).append("_").append(l).append("_b: ")
									   .append("W_").append(j).append("_").append(i).append("_").append(k).append("_").append(l)
									   .append(" - ").append(data.getInvestissement(k).getCapacite()).append(" Z_").append(k)
									   .append(" <= 0\n");
						
						otherConstraint.append("W_").append(j).append("_").append(i).append("_").append(k).append("_").append(l).append("_c: ")
									   .append("W_").append(j).append("_").append(i).append("_").append(k).append("_").append(l).append(" >= 0\n");											
					}
				}
			}
		}
		
		return otherConstraint.toString();
	}

	private String createMaxFunction(){
		StringBuilder maxFunction = new StringBuilder();
		
		int vente = 0;
		
		int nbProduits = data.getNbProduits();
		int nbPeriodes = data.getNbPeriodes();
		int nbInvestissements = data.getNbInvestissements();
		int nbScenarios = data.getNbScenarios();
		
		StringBuilder production = new StringBuilder();
		StringBuilder stockage = new StringBuilder();
		StringBuilder ammortissement = new StringBuilder();
		StringBuilder achat = new StringBuilder();
		
		for(int i = 0; i <= nbPeriodes; i++){
			for(int j = 0; j < nbProduits; j++){
				if(i == nbPeriodes){
					//COUT DE STOCKAGE
					for(int k = 0; k < nbScenarios; k++){
						stockage.append("-")
						.append(data.getStockage()/3)
						.append(" Y_").append(j).append("_").append(i).append("_").append(k).append(" ");						
					}
				}
				else{
					//COUT DES VENTES (prix de vente * la demande)
					for(int k=0; k < nbScenarios; k++){
						vente += (data.getPrix(j) * data.getPeriode(i).getDemande(j, k))/3;						
					}

					//COUT DE PRODUCTION
					for(int k = 0; k < nbInvestissements; k++){
						for(int l = 0; l < nbScenarios; l++){
							production.append("-")
							  .append(data.getInvestissement(k).getCoutProduction()/3)
							  .append(" W_").append(j).append("_").append(i).append("_").append(k).append("_").append(l).append(" ");							
						}
					}

					//COUT DE STOCKAGE
					for(int k = 0; k < nbScenarios; k++){
						stockage.append("-")
						.append(data.getStockage()/3)
						.append(" Y_").append(j).append("_").append(i).append("_").append(k).append(" ");						
					}
					
					//COUT AMMORTISSEMENT
					for(int k = 0; k < nbInvestissements; k++){
						for(int l = 0; l < nbScenarios; l++){
							ammortissement.append("+")
							  .append(data.getAmortissement()/3)
							  .append(" W_").append(j).append("_").append(i).append("_").append(k).append("_").append(l).append(" ");							
						}
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
				   .append(vente).append(" cst ");
		
		
		return maxFunction.toString();
	}
	
	public String createCapaConstraint(){
		StringBuilder capaConstraint = new StringBuilder();
		
		int nbProduits = data.getNbProduits();
		int nbPerdiodes = data.getNbPeriodes();
		int nbInvestissements = data.getNbInvestissements();
		int nbScenarios = data.getNbScenarios();
		
		for(int i = 0; i < nbPerdiodes; i++){
			capaConstraint.append("Capa_").append(i).append(": ");
			for(int j = 0; j < nbProduits; j++){
				for(int k = 0; k < nbScenarios; k++){
					capaConstraint.append("X_").append(j).append("_").append(i).append("_").append(k);
					if((k < nbScenarios - 1) && (j < nbProduits - 1)){
						capaConstraint.append(" + ");
					}
				}				
			}
			
			for(int k =  0; k < nbInvestissements; k++){
				capaConstraint.append(" - ").append(data.getInvestissement(k).getCapacite()).append(" Z_").append(k);
			}
			
			capaConstraint.append(" <= 0\n");
		}
		
		return capaConstraint.toString();
	}
	
	public String createStockConstraint(){
		StringBuilder stockConstraint = new StringBuilder();
		
		int nbProduits = data.getNbProduits();
		int nbPerdiodes = data.getNbPeriodes();
		int nbScenarios = data.getNbScenarios();
		
		for(int i = 0; i < nbPerdiodes; i++){
			for(int j = 0; j < nbProduits; j++){
				for(int k = 0; k < nbScenarios; k++){					
					stockConstraint.append("stock_").append(j).append("_").append(i).append("_").append(k).append(": ")
					   .append("Y_").append(j).append("_").append(i + 1).append("_").append(k).append(" - ")
					   .append("Y_").append(j).append("_").append(i).append("_").append(k).append(" - ")
					   .append("X_").append(j).append("_").append(i).append("_").append(k).append(" = ")
					   .append(" -").append(data.getPeriode(i).getDemande(j, k))
					   .append("\n");
				}
			}
		}
		
		return stockConstraint.toString();
	}
}
