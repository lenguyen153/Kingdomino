package ca.mcgill.ecse223.kingdomino.view;


import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.Game;

public class EndGameTable {

	private JFrame frame;
	private JPanel panel;
	private JTable table;
	private JLabel winnerLabel;

	public EndGameTable(){
		
		Game game = KingdominoApplication.getKingdomino().getAllGame(KingdominoApplication.getKingdomino().getAllGames().size()-1);
		//get the last game
		frame = new JFrame();
		panel = new JPanel(new BorderLayout());
		winnerLabel = new JLabel();
		frame.setTitle("End Game");
		
		
		String winner = " ";
		String data[][] = new String[game.getNumberOfPlayers()][5];
		
		for (int i = 0; i < game.getNumberOfPlayers(); i++) {
			
			data[i][0] = game.getPlayer(i).getColor().toString();
			data[i][1] = Integer.toString(game.getPlayer(i).getBonusScore());
			data[i][2] = Integer.toString(game.getPlayer(i).getPropertyScore());
			data[i][3] = Integer.toString(game.getPlayer(i).getTotalScore());
			data[i][4] = Integer.toString(game.getPlayer(i).getCurrentRanking());
			
			if(game.getPlayer(i).getCurrentRanking() == 1) {
				winner = data[i][0];
			}
		}

		String[] columns = {"Player", "Bonus", "Property Score", "Total Score", "Ranking"};

		table = new JTable(data, columns);
		table.setBounds(30, 40, 200, 300);
		JScrollPane sp = new JScrollPane(table); 
        
		
		
		winnerLabel.setText("Winner: " + winner);
        winnerLabel.setPreferredSize(new Dimension(100,100));
        winnerLabel.setHorizontalAlignment(JLabel.CENTER);
        
        panel.add(sp, BorderLayout.NORTH);
        panel.add(winnerLabel, BorderLayout.SOUTH);

        frame.add(panel);
		frame.setSize(600, 600);
		frame.setVisible(true);
	}
}


