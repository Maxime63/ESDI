package com.tp.edsi.vue;

import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FileChooser extends JFrame{

	/***/
	private static final long serialVersionUID = 1L;
	
	private JFileChooser fileChooser;
	
	public FileChooser() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("TP - Optimisation et évaluation des systèmes dans l'incertain");
		setSize(550, 375);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new FlowLayout());
		getContentPane().setBackground(Color.WHITE);
		fileChooser = new JFileChooser("./");
		getContentPane().add(fileChooser);
		setVisible(false);
		
	}

}
