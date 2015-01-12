package com.tp.edsi.model;

import java.io.IOException;
import java.util.Observable;

import ilog.concert.IloException;

import com.tp.edsi.constantes.ConstantesVues;
import com.tp.edsi.metier.Data;
import com.tp.edsi.solver.Solver;

public class Modele extends Observable implements ConstantesVues{
	private Solver solver;
	private String dataFilename;
	private Data data;
	private boolean isSolved;
	private boolean isAlgoSelected;
	private int stockInitial;
	private String algorithme;
	
	public Modele(){
		stockInitial = 0;
		isSolved = false;
		isAlgoSelected = false;
	}
	
	public Solver getSolver(){
		return solver;
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
			solver = new Solver(data);
			solver.setStockInitial(stockInitial);
			solver.solveProblem();
			isSolved = true;
			sendMessage(PROBLEM_SOLVE);
		} catch (IloException e) {
			sendMessage(MSG_ERREUR + SPLIT + "Erreur lors de la r�solution du programme LP");
		} catch (IOException e) {
			sendMessage(MSG_ERREUR + SPLIT + "Erreur lors de la cr�ation du fichier .lp");
		}
	}

	public void sendMessage(String message){
		setChanged();
		notifyObservers(message);		
	}

	public void setStockInitial(int stockInitial) {
		this.stockInitial = stockInitial;
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
