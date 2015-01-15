package com.tp.edsi.tests;

import ilog.concert.IloException;

import java.io.IOException;

import com.tp.edsi.metier.Data;
import com.tp.edsi.metier.Periode;
import com.tp.edsi.solver.Solver;
import com.tp.edsi.solver.SolverStochastique;

public class TestConsole {
	public static void main(String[] args){
			try {
				Data data = new Data();
				data.loadData("data.txt");
				Solver solver = new Solver(data);
				SolverStochastique solverStochastique = new SolverStochastique(data);
				
				//Résolution normale
				System.out.println("\n\nRESOLUTION NORMALE:");
				solver.solveProblem();
				
				//Résolution stochastique
				System.out.println("\n\nRESOLUTION STOCHASTIQUE:");
				solverStochastique.solveProblem();
			} catch (IloException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
}
