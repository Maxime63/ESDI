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
	
	public Modele(){
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
			Data data = new Data();
			data.loadData(dataFilename);
			solver = new Solver(data);
			solver.solveProblem();
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
}
