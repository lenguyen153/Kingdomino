package ca.mcgill.ecse223.kingdomino.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.GameController;


public class GameOptions extends KingdominoFrame {
	
	private boolean middleKingdomActive = false;
	private boolean harmonyActive = false;

	
	public GameOptions(JFrame previous) {
		super(previous);
	}

	@Override
	public void createGUI() {
		GameController.createNewUsers(UserProfile.nameList);//default list of players in case the player jumps into the game directly
		// Components
		JPanel gameOptionsPNL = new JPanel(new GridBagLayout());
		JButton startBTN = new JButton("Start Game!");
		JButton backBTN = new JButton("Back");
		JButton userBTN = new JButton("Users");
		JCheckBox middleKingdomCHK = new JCheckBox("Middle Kingdom");
		JCheckBox harmonyCHK = new JCheckBox("Harmony");

		
		// Give actions to the buttons
		startBTN.addActionListener(startAction());
		backBTN.addActionListener(backAction());
		userBTN.addActionListener(userAction());
		middleKingdomCHK.addActionListener(middleKingdomToggle());
		harmonyCHK.addActionListener(harmonyToggle());
		
		// Styles
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(20, 20, 20, 20);
		gbc.ipadx = 30;
		gbc.ipady = 30;

		// Add components to the gameOptionsPNL
		gameOptionsPNL.add(middleKingdomCHK,gbc);
		gbc.gridx=1;
		gameOptionsPNL.add(harmonyCHK,gbc);
		gbc.gridx = 0;
		gbc.gridy=3;
		gameOptionsPNL.add(startBTN, gbc);
		gbc.gridy=5;
		gameOptionsPNL.add(userBTN, gbc);
		gbc.gridy = 7;
		gameOptionsPNL.add(backBTN, gbc);

		// add the gameOptions panel in the center of the main Panel
		mainPanel.add(gameOptionsPNL, BorderLayout.WEST);
	}

	/**
	 * @return
	 */
	private ActionListener startAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {


				/*
				 * When "Start" is pressed:
				 * 
				 * Switch to "Game" Pass to Game the previousFrame (which should be home)
				 * Dispose this window Call gameController.createGame / setGameOptions
				 */


				KingdominoApplication.getStatemachine().enter();
				
				BrowseDominoWindow bdw = new BrowseDominoWindow( (JFrame) previousFrame);
				bdw.setVisible(true);
				dispose();
			}
		};
	}

	/**
	 * @return
	 */
	private ActionListener backAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			//	setVisible(false);
				previousFrame.setVisible(true);
			}
		};
	}

	private ActionListener userAction(){
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UserProfile userProfile = new UserProfile( (JFrame) self);
				setVisible(false);
				userProfile.setVisible(true);

			}
		};
	}
	
	/**
	 * @return
	 */
	private ActionListener middleKingdomToggle() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				middleKingdomActive=!middleKingdomActive;
			}
		};
	}
	
	/**
	 * @return
	 */
	private ActionListener harmonyToggle() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				harmonyActive=!harmonyActive;
			}
		};
	}

}
