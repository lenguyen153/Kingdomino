package ca.mcgill.ecse223.kingdomino.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class About extends JFrame {

	private final int FRAME_WIDTH = 1024;
	private final int FRAME_HEIGHT = 640;
	private JPanel mainPanel = new JPanel();
	private Home home;
	
	public About(Home home) {
		this.home = home;
		
		// Set the frame title
		setTitle("Kingdomino");
		// set to default width and height
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		// centers frame
		setLocationRelativeTo(null);
		// set default operation when closing frame
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// creates the main content panel and adds it to home
		createAbout();
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		// set visible
		setVisible(true);

	}

	private void createAbout() {
		String aboutText = "Kingdomino for ECSE223 @ McGill University in Winter 2020 <br> by Roey Borsteinas, Annabelle Dion, Vincent Trinh, Isabella Hao, Maxens Destine, and Adam Di Re.";
		String html = "<html><div style='text-align: center;'>" + aboutText+"<div><html>";
		JLabel aboutLBL = new JLabel(html);
		JButton backBTN = new JButton("Back");
		
		aboutLBL.setForeground(new Color(156,73,229));
		backBTN.addActionListener(backAction());
		
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(aboutLBL, BorderLayout.CENTER);
		mainPanel.add(backBTN, BorderLayout.SOUTH);
		
	}
	
	private ActionListener backAction() {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				home.setVisible(true);
				dispose();
			}
		};
	}
}
