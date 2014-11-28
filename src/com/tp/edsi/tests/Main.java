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
		int i=1;
		InputStream ips=new FileInputStream(filename); 
		InputStreamReader ipsr=new InputStreamReader(ips);
		BufferedReader br=new BufferedReader(ipsr);
		String ligne;
		String mot[];
		int a;
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
		ligne=br.readLine();
		ligne=br.readLine();
		mot=ligne.split(" ");
		donnee.setStockage(Integer.parseInt(mot[1]));
		ligne=br.readLine();
		mot=ligne.split(" ");
		donnee.setPrix_A(Integer.parseInt(mot[1]));
		donnee.setPrix_B(Integer.parseInt(mot[2]));
		ligne=br.readLine();
		mot=ligne.split(" ");
		donnee.setAmort(Integer.parseInt(mot[1]));
		ligne=br.readLine();
		ligne=br.readLine();
		
		while(i<=donnee.getInvestissements())
		{
			i++;
			ligne=br.readLine();
			mot=ligne.split(" ");
			donnee.setInvests(Integer.parseInt(mot[1]), Integer.parseInt(mot[2]), Integer.parseInt(mot[3]), Integer.parseInt(mot[4]));
			
		}
		i=1;
		ligne=br.readLine();
		ligne=br.readLine();
		while(i<=donnee.getPeriodes())
		{
			i++;
			ligne=br.readLine();
			
			mot=ligne.split("	");
			a=Integer.parseInt(mot[4].split(" ")[0]);
			donnee.setPeriode(Integer.parseInt(mot[2]), Integer.parseInt(mot[3]), a, Integer.parseInt(mot[5]), Integer.parseInt(mot[6]), Integer.parseInt(mot[7]));
		}
		br.close();
		
	}

}
