package ca.mcgill.ecse223.kingdomino.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.PileController;
import ca.mcgill.ecse223.kingdomino.model.Domino;

/**
 * @author AdamMigliore Create the BrowseDominoWindow
 */
public class BrowseDominoWindow extends KingdominoFrame {

	public Domino topDom = null;

	public BrowseDominoWindow(JFrame previous) {
		super(previous);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(windowEvent());
	}

	/**
	 * @author AdamMigliore
	 * @return windowListener
	 */
	private WindowListener windowEvent() {
		return new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void windowClosing(WindowEvent e) {
				previousFrame.setVisible(true);
				dispose();

			}

			@Override
			public void windowClosed(WindowEvent e) {

			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub

			}
		};
	}

	/**
	 * Creates 4 columns of 12 pieces to be filled up with domino
	 */
	public void createGUI() {
		// each domino will take 3 spots: LT RT SPACE ...
		// row: 12
		// colums: 4
		int numColumns = 4;
		int numRows = 12;
		int boundsX = getWidth() / (numColumns * 2);
		int boundsY = getHeight() / (numRows);
		topDom = KingdominoApplication.getKingdomino().getCurrentGame().getTopDominoInPile();
		JButton nextBtn = new JButton("Next");
		
		JPanel column1Panel = new JPanel(new GridLayout(numRows, 1, 30, 5));
		JPanel column2Panel = new JPanel(new GridLayout(numRows, 1, 30, 5));
		JPanel column3Panel = new JPanel(new GridLayout(numRows, 1, 30, 5));
		JPanel column4Panel = new JPanel(new GridLayout(numRows, 1, 30, 5));

		int i = 0;
		int j = 0;
		while (topDom.hasNextDomino()) {
			if (j == 12) {
				j = 0;
				i++;
			}
			DominoPanel dominoPanel = new DominoPanel(topDom.getId());
			dominoPanel.setBounds(0, 10, boundsX, boundsY);
			dominoPanel.loadImage();
			dominoPanel.createID();
			topDom = topDom.getNextDomino();
			j++;
			switch (i) {
			case 0:
				column1Panel.add(dominoPanel);
				break;
			case 1:
				column2Panel.add(dominoPanel);
				break;
			case 2:
				column3Panel.add(dominoPanel);
				break;
			case 3:
				column4Panel.add(dominoPanel);
				break;
			}
		}
		//add the last domino
		DominoPanel dominoPanel = new DominoPanel(topDom.getId());
		dominoPanel.setBounds(0, 10, boundsX, boundsY);
		dominoPanel.loadImage();
		dominoPanel.createID();
		column4Panel.add(dominoPanel);

		// this will have the 4 columns
		JPanel gridLayout = new JPanel(new GridLayout(1, numColumns, 5, 30));

		gridLayout.add(column1Panel);
		gridLayout.add(column2Panel);
		gridLayout.add(column3Panel);
		gridLayout.add(column4Panel);

		nextBtn.addActionListener(nextAction());

		mainPanel.add(nextBtn, BorderLayout.SOUTH);
		mainPanel.add(gridLayout, BorderLayout.CENTER);

	}

	/**
	 * @author AdamMigliore
	 * @return actionListener for next Button
	 */
	private ActionListener nextAction() {
		// TODO Auto-generated method stub
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Game game = new Game();
				game.setVisible(true);
				dispose();
			}
		};
	}

}
