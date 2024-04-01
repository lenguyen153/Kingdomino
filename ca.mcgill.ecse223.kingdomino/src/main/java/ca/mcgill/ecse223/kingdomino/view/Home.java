package ca.mcgill.ecse223.kingdomino.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.LoadController;

class Home extends KingdominoFrame {

	public Home() {
		getContentPane().add(mainPanel, BorderLayout.CENTER);
	}

	/**
	 * Create Home's content pane (add to mainPanel, add actionListeners to buttons)
	 * 
	 * @author AdamMigliore
	 */
	@Override
	public void createGUI() {
		// Components in the main content pane
		JPanel buttonPNL = new JPanel(new GridBagLayout());
		JButton startBTN = new JButton("Start Game");
		JButton loadBTN = new JButton("Load Game");
		JButton aboutBTN = new JButton("About");
		JButton exitBTN = new JButton("Exit");

		// Create a mainPanel which will have a borderLayout and set another panel in
		// center with buttons

		// Styles
		exitBTN.setForeground(new Color(255, 0, 0));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 2;
		gbc.gridwidth = 4;
		gbc.ipadx = 40;
		gbc.ipady = 20;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.weighty = 1;
		gbc.weightx = 1;
		// add buttons to our button panel
		buttonPNL.add(startBTN, gbc);
		gbc.gridy = 3;
		buttonPNL.add(loadBTN, gbc);
		gbc.gridy = 5;
		buttonPNL.add(aboutBTN, gbc);
		gbc.gridy = 7;
		buttonPNL.add(exitBTN, gbc);

		// Set the actions of each button
		startBTN.addActionListener(startAction());
		loadBTN.addActionListener(loadAction());
		aboutBTN.addActionListener(aboutAction());
		exitBTN.addActionListener(exitAction());

		// add the buttonPanel to mainPanel
		mainPanel.add(buttonPNL, BorderLayout.WEST);
	}

	/**
	 * Exits application when the exit button is pressed
	 * 
	 * @return ActionListener
	 */
	private ActionListener exitAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);

			}
		};
	}

	/**
	 * @return
	 */
	private ActionListener startAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				GameOptions gameOptions = new GameOptions((JFrame) self);
				setVisible(false);
				gameOptions.setVisible(true);

			}
		};
	}

	/**
	 * @return
	 */
	private ActionListener loadAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int selectedOption = fc.showOpenDialog(self);
				if (selectedOption == JFileChooser.APPROVE_OPTION) {
					try {
						String path = fc.getSelectedFile().getAbsolutePath();
						LoadController lc = new LoadController();
						lc.LoadGame(path);
						Game game = new Game();
						self.setVisible(false);
						game.setVisible(true);
					} catch (Exception exception) {
						exception.printStackTrace();
						JOptionPane.showMessageDialog(self, "Error opening file - " + exception.getMessage());
					}

				}
			}
		};
	}

	/**
	 * Switch to about frame and minimize this frame
	 * 
	 * @return ActionListener
	 */
	private ActionListener aboutAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(self,
						"Kingdomino for ECSE223 @ McGill University in Winter 2020 by Roey Borsteinas, Annabelle Dion, Vincent Trinh, Isabella Hao, Maxens Destine, and Adam Di Re.");
			}
		};
	}

}
