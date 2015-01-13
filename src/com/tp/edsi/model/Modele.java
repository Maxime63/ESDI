package com.tp.edsi.model;

import java.io.IOException;
import java.util.Observable;

import ilog.concert.IloException;

import com.tp.edsi.constantes.ConstantesVues;
import com.tp.edsi.metier.Data;
import com.tp.edsi.solver.Solver;
import com.tp.edsi.solver.SolverStochastique;

public class Modele extends Observable implements ConstantesVues{
	private Solver solver;
	private SolverStochastique solverStochastique;
	private String dataFilename;
	private Data data;
	private boolean isSolved;
	private boolean isAlgoSelected;
	private boolean isStochasticSolved;
	private boolean isStockIntialized;
	private String algorithme;
	
	public Modele(){
		isSolved = false;
		isAlgoSelected = false;
		isStochasticSolved = false;
		isStockIntialized = false;
	}
	
	public Solver getSolver(){
		return solver;
	}
	
	public SolverStochastique getSolverStochastique(){
		return solverStochastique;
	}
	
	public void setDataFilename(String dataFilename){
		this.dataFilename = dataFilename;
	}
	
	public void openFileChooser(){
		sendMessage(OPEN_FILE_CHOOSER);
	}
	
	public void solve(){
		try {
			data = new Data();
			data.loadData(dataFilename);
			
			if(isStochasticSolved){
				solverStochastique = new SolverStochastique(data);
				solverStochastique.solveProblem();
			}
			else{
				solver = new Solver(data);
				solver.solveProblem();				
			}
			isSolved = true;
			sendMessage(PROBLEM_SOLVE);
		} catch (IloException e) {
			sendMessage(MSG_ERREUR + SPLIT + "Erreur lors de la résolution du programme LP");
		} catch (IOException e) {
			sendMessage(MSG_ERREUR + SPLIT + "Erreur lors de la création du fichier .lp");
		}
	}

	public void sendMessage(String message){
		setChanged();
		notifyObservers(message);		
	}


	public void setAlgorithme(String algorithme) {
		this.algorithme = algorithme;
		isAlgoSelected = true;
		sendMessage(ALGO_SELECTED);
	}
	
	public boolean isSolved(){
		return isSolved;
	}

	public boolean isAlgoSelected(){
		return isAlgoSelected;
	}
	
	public void setStockInitialized(boolean isStockInitialized){
		this.isStockIntialized = isStockInitialized;
	}
	
	public void setStochasticSolved(boolean isStochasticSolved){
		this.isStochasticSolved = isStochasticSolved;
		sendMessage(STOCHASTIC_SOLVED_CHANGED);
	}
	
	public boolean isStockInitialized(){
		return isStockIntialized;
	}
	
	public boolean isStochasticSolved(){
		return isStochasticSolved;
	}

	public void applicateAglorithm() {
		switch (algorithme) {
			case ALGO_MAX_MIN_ABSOLU:
				solver.maxMinAbsolu();
				break;
			case ALGO_MAX_MIN_REGRET:
				solver.maxMinRegret();
				break;
			case ALGO_MOYENNE:
				solver.moyenne();
				break;
		}
		sendMessage(ALGO_APPLICATED);
	}

	public String getAlgorithme() {
		return algorithme;
	}
}
