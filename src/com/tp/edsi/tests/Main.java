package com.tp.edsi.tests;

import ilog.concert.IloException;

import java.io.IOException;

import com.tp.edsi.metier.Data;
import com.tp.edsi.metier.Periode;
import com.tp.edsi.solver.Solver;

public class Main {
	public static void main(String[] args){
			try {
				Data data = new Data();
				data.loadData("data.txt");
				Solver solver = new Solver(data);
//				Data data = solver.getData();
//				int nbPeriodes = data.getNbPeriodes();
//				int nbScenarios = data.getNbScenarios();
//				
//				for(int i = 0; i < nbPeriodes; i++){
//					Periode p = data.getPeriode(i);
//					
//					for(int j = 0; j < nbScenarios; j++){
//						System.out.print(p.getDemande(0, j) + " ");
//					}
//					
//					System.out.print(" | ");
//
//					for(int j = 0; j < nbScenarios; j++){
//						System.out.print(p.getDemande(1, j) + " ");
//					}
//					
//					System.out.print("\n");
//				}
				
				solver.solveProblem();
			} catch (IloException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
}
