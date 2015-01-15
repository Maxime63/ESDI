package com.tp.edsi.solver;

import ilog.concert.IloException;
import ilog.concert.IloLPMatrix;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.tp.edsi.metier.Data;

/**Solver stochastique utilisé pour résoudre le programme linéaire et prennant en compte
 * tous les investissements et tous les scénarios possibles.
 * Il nous donnera la solution optimal et le meilleur investissement retenu.
 * @author Maxime
 */
public class SolverStochastique {
	public static final double UNSOLVABLE = -999999999999.99;
	
	private IloCplex cplex;
	private Data data;
	private int stockInitial;
	private boolean isStockInitial;
	private double solution;
	private double[] bestInvestissement;
	
	public SolverStochastique(Data data) throws IloException{
		cplex = new IloCplex();
		
		this.data = data;
		bestInvestissement = new double[data.getNbScenarios()];
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
	
	public double getSolution(){
		return solution;
	}
	
	public void setIsStockInitial(boolean isStockInitial){
		this.isStockInitial = isStockInitial;
	}
	
	public boolean isStockInitial(){
		return isStockInitial;
	}
	
	public double getBestInvestissement(int investissement){
		return bestInvestissement[investissement];
	}
	
	public void solveProblem() throws IloException, IOException{
		String lpFilename = "solveStoch.lp";
		solve(lpFilename);
	}
	
	private void solve(String lpFilename) throws IloException, IOException{
		createLpFile(lpFilename);		
		cplex.importModel(lpFilename);
		cplex.solve();
		solution = cplex.getObjValue();

		IloLPMatrix lp = (IloLPMatrix)cplex.LPMatrixIterator().next();
        IloNumVar[] vars = lp.getNumVars();
        double[]    vals = cplex.getValues(vars);
        
        for (int i = 0; i < vals.length; i++) {
        	String var = vars[i].getName().substring(0, 2);
        	if(var.equals("Z_")){
        		int investissement = Integer.valueOf(vars[i].getName().substring(2, 3));
        		bestInvestissement[investissement] = vals[i];
        	}
         }
	}
	
	private void createLpFile(String lpFilename) throws IOException{
		OutputStream ops = new FileOutputStream(lpFilename); 
		OutputStreamWriter opsw = new OutputStreamWriter(ops);
		BufferedWriter bw = new BufferedWriter(opsw);
		
		int nbInvestissements = data.getNbInvestissements();
		int nbScenarios = data.getNbScenarios();
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
				bw.write("Y_" + j + "_0_" + k + " = " + stockInitial);
				bw.write("\n");
			}
		}
		
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
		otherConstraint.append(" = 1 \n");
		
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
						.append(data.getStockage()/3.0)
						.append(" Y_").append(j).append("_").append(i).append("_").append(k).append(" ");						
					}
					stockage.append("\n");
				}
				else{
					//COUT DES VENTES (prix de vente * la demande)
					for(int k=0; k < nbScenarios; k++){
						vente += (data.getPrix(j) * data.getPeriode(i).getDemande(j, k))/3.0;						
					}

					//COUT DE PRODUCTION
					for(int k = 0; k < nbInvestissements; k++){
						for(int l = 0; l < nbScenarios; l++){
							production.append("-")
							  .append(data.getInvestissement(k).getCoutProduction()/3.0)
							  .append(" W_").append(j).append("_").append(i).append("_").append(k).append("_").append(l).append(" ");							
						}
					}
					production.append("\n");

					//COUT DE STOCKAGE
					for(int k = 0; k < nbScenarios; k++){
						stockage.append("-")
						.append(data.getStockage()/3.0)
						.append(" Y_").append(j).append("_").append(i).append("_").append(k).append(" ");						
					}
					stockage.append("\n");
					
					//COUT AMMORTISSEMENT
					for(int k = 0; k < nbInvestissements; k++){
						for(int l = 0; l < nbScenarios; l++){
							ammortissement.append("+")
							  .append(data.getAmortissement()/3.0)
							  .append(" W_").append(j).append("_").append(i).append("_").append(k).append("_").append(l).append(" ");							
						}
					}
					ammortissement.append("\n");
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
	
	private String createCapaConstraint(){
		StringBuilder capaConstraint = new StringBuilder();
		
		int nbProduits = data.getNbProduits();
		int nbPerdiodes = data.getNbPeriodes();
		int nbInvestissements = data.getNbInvestissements();
		int nbScenarios = data.getNbScenarios();
		
		for(int i = 0; i < nbPerdiodes; i++){
			for(int k = 0; k < nbScenarios; k++){
				capaConstraint.append("Capa_").append(i).append("_").append(k).append(": ");
				for(int j = 0; j < nbProduits; j++){
					capaConstraint.append("X_").append(j).append("_").append(i).append("_").append(k);
					if(j < nbProduits - 1){
						capaConstraint.append(" + ");
					}									
				}
				for(int l =  0; l < nbInvestissements; l++){
					capaConstraint.append(" - ").append(data.getInvestissement(l).getCapacite()).append(" Z_").append(l);
				}			

				capaConstraint.append(" <= 0\n");
			}
		}
		
		return capaConstraint.toString();
	}
	
	private String createStockConstraint(){
		StringBuilder stockConstraint = new StringBuilder();
		
		int nbProduits = data.getNbProduits();
		int nbPerdiodes = data.getNbPeriodes();
		int nbScenarios = data.getNbScenarios();
		
		for(int i = 0; i < nbPerdiodes; i++){
			for(int j = 0; j < nbProduits; j++){
				for(int k = 0; k < nbScenarios; k++){
					stockConstraint.append("stock_").append(j).append("_").append(i).append("_").append(k).append(": ")
					   .append("Y_").append(j).append("_").append(i).append("_").append(k).append(" + ")
					   .append("X_").append(j).append("_").append(i).append("_").append(k).append(" - ")
					   .append("Y_").append(j).append("_").append(i + 1).append("_").append(k).append(" = ")
					   .append(data.getPeriode(i).getDemande(j, k))
					   .append("\n");					
				}
			}
		}
		
		return stockConstraint.toString();
	}
}
