package com.tp.edsi.tests;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.tp.edsi.metier.Data;

public class Main {

	public static void main(String[] args) throws IOException {
		Data donnee = new Data();
		readData("data.txt",donnee);
		
	}
	
	private static void readData(String filename,Data donnee) throws IOException{
		
		InputStream ips=new FileInputStream(filename); 
		InputStreamReader ipsr=new InputStreamReader(ips);
		BufferedReader br=new BufferedReader(ipsr);
		String ligne;
		String mot[];
		ligne=br.readLine();
		mot=ligne.split(" ");
		donnee.setPeriodes(Integer.parseInt(mot[0]));
		ligne=br.readLine();
		mot=ligne.split(" ");
		donnee.setProduits(Integer.parseInt(mot[0]));
		ligne=br.readLine();
		mot=ligne.split(" ");
		donnee.setScenarios(Integer.parseInt(mot[0]));
		ligne=br.readLine();
		mot=ligne.split(" ");
		donnee.setInvestissements(Integer.parseInt(mot[0]));
		br.close(); 
		
	}

}
