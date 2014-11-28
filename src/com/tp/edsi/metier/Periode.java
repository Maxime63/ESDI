package com.tp.edsi.metier;

public class Periode {
	private int[] produitA;
	private int[] produitB;
	
	public Periode()
	{
		produitA=new int[3];
		produitB=new int[3];
	}
	
	public void setProduitA(int a,int b,int c)
	{
		produitA[0]=a;
		produitA[1]=b;
		produitA[2]=c;
		
	}

	public void setProduitB(int a,int b,int c)
	{
		produitB[0]=a;
		produitB[1]=b;
		produitB[2]=c;
		
	}
}
