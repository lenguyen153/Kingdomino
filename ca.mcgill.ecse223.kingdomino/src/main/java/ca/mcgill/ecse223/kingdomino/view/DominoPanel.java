package ca.mcgill.ecse223.kingdomino.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import ca.mcgill.ecse223.kingdomino.controller.PileController;
import ca.mcgill.ecse223.kingdomino.controller.Utils;
/** 
 * @author Maxens Destine
 */
public class DominoPanel extends JPanel {
	
	private static ImageIcon backIcon=new ImageIcon("images\\cardBack.jpg");
	private static ImageIcon wheatIcon=new ImageIcon("images\\wheat.jpg");
	private static ImageIcon forestIcon=new ImageIcon("images\\forest.jpg");
	private static ImageIcon swampIcon=new ImageIcon("images\\swamp.jpg");
	private static ImageIcon lakeIcon=new ImageIcon("images\\lake.jpg");
	private static ImageIcon mountainIcon=new ImageIcon("images\\mine.jpg");
	private static ImageIcon grassIcon=new ImageIcon("images\\grass.jpg");
	public enum DirectionKind { Up, Down, Left, Right }
	public DirectionKind direction=DirectionKind.Right;
	private int id;
	private JLabel leftLabel=new JLabel();
	private JLabel rightLabel=new JLabel();
	private ImageIcon leftIcon=new ImageIcon();
	private ImageIcon rightIcon=new ImageIcon();
	private JLabel idLabel=new JLabel();
	private boolean isGridSize=false;//to know when to scale down or scale up
	private boolean hasPiece=false;
	
	/**
	 * 
	 * @param id the id of the domino that this dominoPanel will represent
	 */
	public DominoPanel(int id) {
		this.id=id;
		this.setBorder(BorderFactory.createLineBorder(Color.black));
		this.setLayout(new GridLayout(1,2));
		this.add(leftLabel);
		this.add(rightLabel);
	}
	
	public DominoPanel() {
		this.setBorder(BorderFactory.createLineBorder(Color.black));
	}
	/**
	 * Puts the icon for a "face down" effect
	 */
	public void putBackIcon() {
		this.removeAll();
		this.setLayout(new GridLayout(1,1));
		JLabel backLabel=new JLabel();
		ImageIcon bIcon=new ImageIcon();
		Image backImage=backIcon.getImage().getScaledInstance(this.getWidth(),this.getHeight(),Image.SCALE_SMOOTH);
		bIcon=new ImageIcon(backImage);
		backLabel.setIcon(bIcon);
		this.add(backLabel);
	}
	
	/**
	 * Creates the display for the id and the crowns
	 */
	public void createID() {
		String leftCrownstxt="",rightCrownstxt="";
		for(int i=0;i<Utils.getDominoByID(id).getLeftCrown();i++) {
			leftCrownstxt+=String.valueOf("\u2655");
		}
		for(int i=0;i<Utils.getDominoByID(id).getRightCrown();i++) {
			rightCrownstxt+=String.valueOf("\u2655");
		}
		JLabel rightCrowns=new JLabel();
		leftLabel.setLayout(new BorderLayout());
		rightLabel.setLayout(new BorderLayout());
		rightCrowns.setFont(new Font("Verdana", Font.PLAIN, 14));
		rightCrowns.setText("<html><font color='black'><span bgcolor=\"yellow\">"+rightCrownstxt+"</font></span></html>");
		rightLabel.add(rightCrowns,BorderLayout.NORTH);
		//display the id on the left part
		idLabel.setFont(new Font("Verdana", Font.PLAIN, 14));
		idLabel.setText("<html><font color='red'><span bgcolor=\"yellow\">"+"<strong>"+id+"</strong></font><font color='black'> "+leftCrownstxt+"</font></span></html>");
		leftLabel.add(idLabel,BorderLayout.NORTH);
	}
	
	/**
	 * creates the initial image of the domino depending on the type of each side
	 */
	public void loadImage() {
		this.removeAll();
		this.add(leftLabel);
		this.add(rightLabel);
		switch(PileController.browseIndividualDomino(id).getLeftTile()) {
		case Forest:leftIcon=new ImageIcon(forestIcon.getImage());break;
		case Grass:leftIcon=new ImageIcon(grassIcon.getImage());break;
		case Lake:leftIcon=new ImageIcon(lakeIcon.getImage());break;
		case Mountain:leftIcon=new ImageIcon(mountainIcon.getImage());break;
		case Swamp:leftIcon=new ImageIcon(swampIcon.getImage());break;
		case WheatField:leftIcon=new ImageIcon(wheatIcon.getImage());break;
		}
		Image leftImage=leftIcon.getImage().getScaledInstance(this.getWidth(),this.getHeight(),Image.SCALE_SMOOTH);
		leftIcon=new ImageIcon(leftImage);
		leftLabel.setIcon(leftIcon);
		
		switch(PileController.browseIndividualDomino(id).getRightTile()) {
		case Forest:rightIcon=new ImageIcon(forestIcon.getImage());break;
		case Grass:rightIcon=new ImageIcon(grassIcon.getImage());break;
		case Lake:rightIcon=new ImageIcon(lakeIcon.getImage());break;
		case Mountain:rightIcon=new ImageIcon(mountainIcon.getImage());break;
		case Swamp:rightIcon=new ImageIcon(swampIcon.getImage());break;
		case WheatField:rightIcon=new ImageIcon(wheatIcon.getImage());break;
		}
		Image rightImage=rightIcon.getImage().getScaledInstance(this.getWidth(),this.getHeight(),Image.SCALE_SMOOTH);
		rightIcon=new ImageIcon(rightImage);
		rightLabel.setIcon(rightIcon);
	}
	
	public int getID() {
		return id;
	}
	
	public boolean isGridSize() {
		return isGridSize;
	}
	
	public void setIsGridSize(boolean isGridSize) {
		this.isGridSize=isGridSize;
	}
	
	public boolean hasPiece() {
		return hasPiece;
	}
	
	public void setHasPiece(boolean hasPiece) {
		this.hasPiece=hasPiece;
	}
	
	/**
	 * Rotates the dominoPanel counter clockwise
	 */
	public void rotate() {
		switch(direction) {
		case Down:rotateToRightSide();break;
		case Right:rotateToUpSide();break;
		case Up:rotateToLeftSide();break;
		case Left:rotateToDownSide();break;
		}
		arrangeIcon();
	}
	/**
	 * Scales the images with the size of this dominoPanel (when it is horizontal)
	 */
	public void scaleImageHorizontal() {
		Image leftImage=leftIcon.getImage().getScaledInstance(this.getWidth()/2,this.getHeight(),Image.SCALE_SMOOTH);
		leftIcon=new ImageIcon(leftImage);
		leftLabel.setIcon(leftIcon);
		Image rightImage=rightIcon.getImage().getScaledInstance(this.getWidth()/2,this.getHeight(),Image.SCALE_SMOOTH);
		rightIcon=new ImageIcon(rightImage);
		rightLabel.setIcon(rightIcon);
	}
	
	/**
	 * Scales the images with the size of this dominoPanel (when it is vertical)
	 */
	public void scaleImageVertical() {
		Image leftImage=leftIcon.getImage().getScaledInstance(this.getWidth(),this.getHeight()/2,Image.SCALE_SMOOTH);
		leftIcon=new ImageIcon(leftImage);
		leftLabel.setIcon(leftIcon);
		Image rightImage=rightIcon.getImage().getScaledInstance(this.getWidth(),this.getHeight()/2,Image.SCALE_SMOOTH);
		rightIcon=new ImageIcon(rightImage);
		rightLabel.setIcon(rightIcon);
	}
	
	/**
	 * Arranges the image display depending on the direction
	 */
	private void arrangeIcon() {
		switch(direction) {
		case Down:this.removeAll();
		this.setLayout(new GridLayout(2,1));
		this.add(leftLabel);
		this.add(rightLabel);
		scaleImageVertical();
		break;
		
		case Right:this.removeAll();
		this.setLayout(new GridLayout(1,2));
		this.add(leftLabel);
		this.add(rightLabel);
		scaleImageHorizontal();
		break;
		
		case Up:this.removeAll();
		this.setLayout(new GridLayout(2,1));
		this.add(rightLabel);
		this.add(leftLabel);
		scaleImageVertical();
		break;
		
		case Left:this.removeAll();
		this.setLayout(new GridLayout(1,2));
		this.add(rightLabel);
		this.add(leftLabel);
		scaleImageHorizontal();
		break;
		}
	}
	
	/**
	 * Rotates from down side to right side
	 */
	private void rotateToRightSide() {
		int lastX=this.getX();
		int lastY=this.getY();
		this.setBounds(lastX, lastY, this.getWidth()*2,this.getHeight()/2);
		direction=DirectionKind.Right;
	}
	
	/**
	 * Rotates from right side to up side
	 */
	private void rotateToUpSide() {
		int lastX=this.getX();
		int lastY=this.getY();
		this.setBounds(lastX, lastY-this.getHeight(), this.getWidth()/2,this.getHeight()*2);
		direction=DirectionKind.Up;
	}
	
	/**
	 * Rotates from up side to left side
	 */
	private void rotateToLeftSide() {
		int lastX=this.getX();
		int lastY=this.getY();
		this.setBounds(lastX-this.getWidth(), lastY+this.getHeight()/2, this.getWidth()*2,this.getHeight()/2);
		direction=DirectionKind.Left;
	}
	
	/**
	 * Rotates from left side to down side
	 */
	private void rotateToDownSide() {
		int lastX=this.getX();
		int lastY=this.getY();
		this.setBounds(lastX+this.getWidth()/2, lastY, this.getWidth()/2,this.getHeight()*2);
		direction=DirectionKind.Down;
	}
	
	/**
	 * 
	 * @return true if it can go left, false otherwise
	 */
	public boolean canGoLeft() {
		int p=0;
		if(direction==DirectionKind.Down||direction==DirectionKind.Up) {
			p=(int)Math.floor(((double)this.getX())/this.getWidth());
			if(p<1) {
				return false;
			}
		}
		else {
			p=(int)Math.floor(((double)this.getX())/((double)this.getWidth()/2));
			if(p<1) {
				return false;
			}			
		}
		return true;
	}
	
	/**
	 * 
	 * @return true if it can go right, false otherwise
	 */
	public boolean canGoRight() {
		int p=0;
		if(direction==DirectionKind.Down||direction==DirectionKind.Up) {
			p=(int)Math.floor(((double)this.getX())/this.getWidth());
			if(p>=8) {
				return false;
			}
		}
		else {
			p=(int)Math.floor(((double)this.getX())/((double)this.getWidth()/2));
			if(p>=7) {
				return false;
			}			
		}
		return true;
	}
	
	/**
	 * 
	 * @param y y coordinate of the domino
	 * @return true if it can go up, false otherwise
	 */
	public boolean canGoUp(int y) {
		int p=0;
		if(direction==DirectionKind.Down||direction==DirectionKind.Up) {
			p=(int) Math.floor(y/(((double)this.getHeight())/2));
			if(p<1) {
				return false;
			}
		}
		else {
			p=(int) Math.floor(((double)y)/this.getHeight());
			if(p<1) {
				return false;
			}			
		}
		return true;
	}
	/**
	 * 
	 * @param y y coordinate of the domino
	 * @return true if it can go down false otherwise
	 */
	public boolean canGoDown(int y) {
		int p=0;
		if(direction==DirectionKind.Down||direction==DirectionKind.Up) {
			p=(int) Math.floor(y/(((double)this.getHeight())/2));
			if(p>=7) {
				return false;
			}
		}
		else {
			p=(int) Math.floor(((double)y)/this.getHeight());
			if(p>=8) {
				return false;
			}			
		}
		return true;
	}
	

}
