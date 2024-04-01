package ca.mcgill.ecse223.kingdomino.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import ca.mcgill.ecse223.kingdomino.controller.Utils;
import ca.mcgill.ecse223.kingdomino.model.Player;


/**
 * @author Maxens Destine
 */

public class PlayerPiece extends JPanel{
	private static ImageIcon icon=new ImageIcon("images\\piece.jpg");
	private JLabel lbl=new JLabel();
	private JLabel playerScoreBoard=new JLabel();
	private Player.PlayerColor colorP;
	private Color color;
	private DominoPanel selected;
	private boolean hasDomino=false;
	
	/**
	 * 
	 * @param colorP the player color of the piece
	 */
	public PlayerPiece(Player.PlayerColor colorP) {
		this.setLayout(new GridLayout(1,1));
		this.colorP=colorP;
		playerScoreBoard.setPreferredSize(new Dimension(15,15));
		playerScoreBoard.setText(Utils.getPlayerByColor(colorP).getUser().getName()+
				" has "+Utils.getPlayerByColor(colorP).getTotalScore()+" points");
	}
	
	public JLabel getScoreBoard() {
		return playerScoreBoard;
	}
	
	public boolean hasDomino() {
		return hasDomino;
	}
	
	public void setHasDomino(boolean hasDomino) {
		this.hasDomino=hasDomino;
	}
	public void createAppearance() {
		putImage();
		findColor();
		createBorder();
	}
	
	public void setSelectedDomino(DominoPanel p) {
		selected=p;
	}
	public DominoPanel getSelectedDomino() {
		return selected;
	}
	
	/**
	 * Puts the default image of a piece on this object
	 */
	private void putImage() {
		Image image=icon.getImage().getScaledInstance(this.getWidth(),this.getHeight(),Image.SCALE_SMOOTH);
		ImageIcon temp=new ImageIcon(image);
		lbl.setIcon(temp);
		this.add(lbl);
	}
	
	/**
	 * Finds the real color associated with this piece (not Player.color but rather a swing type color)
	 */
	private void findColor() {
		Color c=Color.ORANGE;
		switch(colorP) {
		case Blue:c=Color.BLUE;break;
		case Yellow:c=Color.YELLOW;break;
		case Green:c=Color.GREEN;break;
		case Pink:c=Color.PINK;break;
		}
		this.color=c;
	}
	
	/**
	 * Creates a border for this object (with the color of the player)
	 */
	public void createBorder() {		
		this.setBorder(new LineBorder(color, this.getWidth()/10));
	}
	
	public Player.PlayerColor getPlayerColor() {
		return colorP;
	}
	
	public Color getColor() {
		return color;
	}
	
	public static ImageIcon getIcon() {
		return icon;
	}
}
