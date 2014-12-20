package com.tp.edsi.tests;

import ilog.concert.IloException;

import java.io.IOException;

import com.tp.edsi.solver.Solver;

public class Main {
	public static void main(String[] args){
		try {
			Solver solver = new Solver();
			solver.loadData("data.txt");
			solver.createLpFile("solver_0_0.lp", 1, 0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IloException e) {
			e.printStackTrace();
		}	
	}
}
