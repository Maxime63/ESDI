package com.tp.edsi.tests;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.tp.edsi.metier.Data;

public class Main {

	public static void main(String[] args) throws IOException {
		Data donnee = new Data();
		readData("data.txt",donnee);
		writedata("LP.txt", donnee);
		
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
		donnee.setNbPeriodes(Integer.parseInt(mot[0]));
		ligne=br.readLine();
		mot=ligne.split(" ");
		donnee.setNbProduits(Integer.parseInt(mot[0]));
		ligne=br.readLine();
		mot=ligne.split(" ");
		donnee.setNbScenarios(Integer.parseInt(mot[0]));
		ligne=br.readLine();
		mot=ligne.split(" ");
		donnee.setNbInvestissements(Integer.parseInt(mot[0]));
		ligne=br.readLine();
		ligne=br.readLine();
		mot=ligne.split(" ");
		donnee.setStockage(Integer.parseInt(mot[1]));
		ligne=br.readLine();
		mot=ligne.split(" ");
		donnee.setPrixVente(Integer.parseInt(mot[1]));
		donnee.setPrixVente(Integer.parseInt(mot[2]));
		ligne=br.readLine();
		mot=ligne.split(" ");
		donnee.setAmort(Integer.parseInt(mot[1]));
		ligne=br.readLine();
		ligne=br.readLine();
		
		while(i<=donnee.getNbInvestissements())
		{
			i++;
			ligne=br.readLine();
			mot=ligne.split(" ");
			donnee.setInvests(Integer.parseInt(mot[1]), Integer.parseInt(mot[2]), Integer.parseInt(mot[3]), Integer.parseInt(mot[4]));
			
		}
		i=1;
		ligne=br.readLine();
		ligne=br.readLine();
		while(i<=donnee.getNbPeriodes())
		{
			i++;
			ligne=br.readLine();
			
			mot=ligne.split("	");
			a=Integer.parseInt(mot[4].split(" ")[0]);
			donnee.setPeriode(Integer.parseInt(mot[2]), Integer.parseInt(mot[3]), a, Integer.parseInt(mot[5]), Integer.parseInt(mot[6]), Integer.parseInt(mot[7]));
		}
		br.close();
		
	}
	
	private static void writedata(String filename,Data donnee) throws IOException
	{
		OutputStream ops=new FileOutputStream(filename); 
		OutputStreamWriter opsr=new OutputStreamWriter(ops);
		BufferedWriter bw=new BufferedWriter(opsr);
		int vente=0;
		
		for(int i=0;i<donnee.getNbPeriodes();i++)
		{
			for(int j=0;j<donnee.getNbProduits();j++)
			{
				vente+=donnee.getPrixVente(j)*donnee.getPeriode(i).getDemande(j, 0);
			}
		}
		System.out.println("vente : " + vente);
		
		int prod=0;
		//for(int k=0;)
		
		bw.write("Maximize");
		bw.close();
		
	}

}
