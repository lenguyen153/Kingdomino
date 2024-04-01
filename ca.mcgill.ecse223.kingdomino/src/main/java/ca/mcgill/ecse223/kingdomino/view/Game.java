package ca.mcgill.ecse223.kingdomino.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DiscardController;
import ca.mcgill.ecse223.kingdomino.controller.DominoController;
import ca.mcgill.ecse223.kingdomino.controller.DraftController;
import ca.mcgill.ecse223.kingdomino.controller.SaveController;
import ca.mcgill.ecse223.kingdomino.controller.Utils;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.GameplayStatemachine;
import ca.mcgill.ecse223.kingdomino.view.DominoPanel.DirectionKind;
/**
 * 
 * @author Maxens Destine
 *
 */
public class Game extends KingdominoFrame{
	private JPanel mainPanel=new JPanel(new GridBagLayout());
	private JPanel infoPanel=new JPanel();
	private JPanel tablePanel=new JPanel();
	private JPanel kingdomPanel=new JPanel();
	private JPanel dragPanel=new JPanel(null);
	private JPanel []playerPanels=new JPanel[4];
	private JPanel gridPanel=new JPanel(new GridLayout(10,10));//panel to display the grid
	private DominoPanel selectedDomino=null;
	private PlayerPiece selectedPlayerPiece=null;
	private JTabbedPane mainTab=new JTabbedPane();
	private JPanel []panelTab=new JPanel[8];//panel that contains all the table grid (to help for display)
	private PlayerPiece []playerPieceTab=new PlayerPiece[4];//array that contain the 4 pieces (or kings or pawns)
	private PlayerPiece currentPiece;
	private ArrayList<DominoPanel>dominoPanelList=new ArrayList<DominoPanel>();//list of the DominoPanel
	private JPanel currentPlayerLogo=new JPanel();
	private JPanel gameStatPanel=new JPanel(new BorderLayout());
	private JLabel lblCurPlayer=new JLabel();
	private JPanel placePanel=new JPanel(new GridLayout(2,1));
	private JButton placeBtn=new JButton("Place");
	private JLabel gameInfoLabel=new JLabel("<html>Welcome..<html>");//to inform the player of the next action (place domino/pick domino), or game data (turns left, etc.)
	private JLabel whatToDoLabel=new JLabel("<html>Click on your piece and select a domino <html>");
	private JPanel scoreBoard=new JPanel(new GridLayout(5,1));
	private JLabel lblDominoLeft=new JLabel();
	private int turn=0;//to handle the playerPieceTab
	private int unitWidth,unitHeight;
	private int lastX=0;
	private int lastY=0;//to put gui elements back to their last place if needed
	private boolean initialStage=true;//to differentiate the first 2 draft from the rest
	private boolean upperRow=true;//to know to which row to send the dominos of the next draft
	private boolean pickDomino=true;//you either pick a domino or place a domino, used to enable/disable interaction with gui
	private DominoInKingdom domInKingdom;
	private JButton saveBtn = new JButton("Save and Exit");
	
	public Game() {
		super();
		this.getContentPane().removeAll();
		this.setTitle("Display Helper");
		this.setSize(800,800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	//	imageCreation();
		createMouseAdapter();
		mainTabCreation();
		this.setVisible(true);
	//	testPanel();
		createListeners();
		createPlayerPieces();
		//revalidate and repaint constantly because the size of some gui depend on that of other gui
		this.revalidate();
		this.validate();
		this.repaint();
		infoPanel();
		this.revalidate();
		this.validate();
		this.repaint();
		createPlayerPanels();
		this.revalidate();
		this.validate();
		this.repaint();
		createPlaceOrDiscardButtons();
		this.revalidate();
		this.validate();
		this.repaint();
		createPlayerCastle();
		this.revalidate();
		this.validate();
		this.repaint();
		DraftController.readyFirstDraft();//this should not be in the view, we should find another place to put it
		prepareFirstDraft();
		this.revalidate();
		this.validate();
		this.repaint();
	}
	/**
	 * Creates layout for the panel that contains everything
	 */
	private void mainPanelCreation() {
		infoPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		tablePanel.setBorder(BorderFactory.createLineBorder(Color.black));
	//	kingdomPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill= GridBagConstraints.BOTH;
		c.gridheight=3;
		c.gridwidth=1;
		c.gridx=0;
		c.gridy=0;
		c.ipadx=0;
		c.ipady=200;
		dragPanel.setBackground(new Color(0,0,0,0));
	//	mainPanel.add(invisPanel,c);
		mainPanel.add(dragPanel,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx=0.8;
		c.weighty=0.4;
		c.gridheight=1;
		c.gridwidth=1;
		c.ipady=0;
		c.ipadx=0;
		c.gridx = 0;
		c.gridy = 0;
		createDraftBoxes();
		tablePanel.setBackground(new Color(0,0,0,0));
		mainPanel.add(tablePanel, c);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.2;
		c.weighty=0.5;
		c.gridheight=3;
	//	c.ipady=400;
		c.gridx = 1;
		c.gridy = 0;
		mainPanel.add(infoPanel, c);
		
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.8;
		c.weighty=0.8;
		c.gridheight=2;
		c.ipady=200;
		c.gridx = 0;
		c.gridy = 1;
		kingdomPanel.setBackground(new Color(0,0,0,0));
		mainPanel.add(kingdomPanel,c);
		gridPanel.setBackground(new Color(0,0,0,0));
		mainPanel.add(gridPanel,c);
		
	}
	/**
	 * Creates the panel with information
	 */
	private void infoPanel() {
		infoPanel.setLayout(new GridLayout(4,1));
		infoPanel.add(scoreBoard);
		lblDominoLeft.setPreferredSize(new Dimension(15,15));
		scoreBoard.add(lblDominoLeft);
		gameInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		gameInfoLabel.setPreferredSize(new Dimension(15,40));
		lblCurPlayer.setPreferredSize(new Dimension(15,40));
		gameStatPanel.add(lblCurPlayer);
		infoPanel.add(gameStatPanel);
		infoPanel.add(currentPlayerLogo);
		currentPlayerLogo.setLayout(new BorderLayout());
		currentPlayerLogo.setBorder(new LineBorder(currentPiece.getColor(), 8));
		JLabel lbl=new JLabel("<html>Press enter to confirm actions <html>");
		lbl.setPreferredSize(new Dimension(15,40));
		currentPlayerLogo.add(lbl,BorderLayout.NORTH);
		whatToDoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		whatToDoLabel.setPreferredSize(new Dimension(15,40));
		currentPlayerLogo.add(gameInfoLabel,BorderLayout.CENTER);
		currentPlayerLogo.add(whatToDoLabel,BorderLayout.SOUTH);
	}

	/**
	 * Creates the game tab
	 */
	private void mainTabCreation() {
		mainPanelCreation();
		mainTab.add("gg",mainPanel);
		this.add(mainTab);
		createKingdomGrid();
	}
	
	/**
	 * Creates the mouse adapter for the game to handle clicks on pieces and dominos
	 */
	private void createMouseAdapter() {
		 final MouseAdapter ma = new MouseAdapter() {

	            //Selection of label occurs upon pressing on the panel:
	            @Override
	            public void mousePressed(final MouseEvent e) {

	            	if(selectedDomino==null&&selectedPlayerPiece==null) {//if it's the first click (or last one was invalid)
	                //Find which domino panel is at the press point:
	                Component pressedComp = dragPanel.findComponentAt(e.getX(), e.getY());
	                	if(pressedComp instanceof JLabel&&!pressedComp.getParent().equals(e.getSource())) {
	                		pressedComp=pressedComp.getParent();
	                	}
	                	
	                	if (pressedComp != null &&pressedComp.isEnabled()&&(pressedComp instanceof DominoPanel||pressedComp instanceof PlayerPiece)) {
	                		if(pressedComp instanceof DominoPanel) {//If a domino panel is pressed and is not selected yet, store it as selected
	                		selectedDomino = (DominoPanel) pressedComp;
	                		if(selectedDomino.hasPiece()||pickDomino) {
	                			selectedPlayerPiece=null;
		                		selectedDomino = null;
		                		return;
	                		}
	                		lastX=selectedDomino.getX();
	                		lastY=selectedDomino.getY();
	                		
	                		if(!selectedDomino.isGridSize()) {
	                		selectedDomino.setBounds(selectedDomino.getX()+(unitWidth*2-panelTab[0].getWidth())/2, 
	                			selectedDomino.getY()+(unitHeight-panelTab[0].getHeight())/2,
	                			unitWidth*2, unitHeight);
	                		selectedDomino.scaleImageHorizontal();
	                		selectedDomino.setIsGridSize(true);
	                		}
	                		selectedDomino.setBorder(new LineBorder(Color.CYAN, unitWidth/10));
	                		selectedPlayerPiece=null;
	            			}
	                		else {//if it is a playerPiece
	                			selectedPlayerPiece = (PlayerPiece) pressedComp;
	                			if(!selectedPlayerPiece.isEnabled()||!pickDomino) {
	                				selectedPlayerPiece=null;
	                				selectedDomino=null;
	                				return;
	                			}
	                			lastX=selectedPlayerPiece.getX();
		                		lastY=selectedPlayerPiece.getY();
		                		selectedPlayerPiece.setBorder(new LineBorder(Color.CYAN, selectedPlayerPiece.getWidth()/10));
		                		selectedDomino=null;
	                		}
	                		mainPanel.getParent().revalidate();
	            			mainPanel.getParent().validate();
	            			mainPanel.getParent().repaint();
	                	}
	                	else {
	                		selectedPlayerPiece=null;
	                		selectedDomino = null;
	                	}
	                
	                }
	            	else {//if it's the second click
	            		if(selectedPlayerPiece==null) {//if it is a dominoPanel
	            		if(e.getX()<kingdomPanel.getWidth()&&e.getY()>kingdomPanel.getY()) {//if the click is in the kingdom zone
	            			Point p=null;
	            			double width=0,height=0;
	            			
	            			if(selectedDomino.direction==DirectionKind.Down||selectedDomino.direction==DirectionKind.Up) {
	            				width=unitWidth;
	            				height=unitHeight*2;
	            				p=clampToKingdomVertical(e.getX(),e.getY(),(int)width,(int)height);
	            				selectedDomino.setBounds((int)(p.x*width)+gridPanel.getComponent(0).getX(),
	            						(int)(p.y*height/2+kingdomPanel.getY()+gridPanel.getComponent(0).getY()),(int)width,(int)height);
	            				checkPlacementVertical();
	            			}
	            			else {
	            				width=unitWidth*2;
	            				height=unitHeight;
	            				p=clampToKingdomHorizontal(e.getX(),e.getY(),(int)width,(int)height);
	            				selectedDomino.setBounds((int)(p.x*width/2)+gridPanel.getComponent(0).getX(),
	            						(int)(p.y*height+kingdomPanel.getY()+gridPanel.getComponent(0).getY()),(int)width,(int)height);
	            				checkPlacementHorizontal();
	            			}		
		        			
	            		}
	            		else if(selectedDomino.getY()<kingdomPanel.getY()){
	            			selectedDomino.setBounds(lastX, lastY, panelTab[0].getWidth(), panelTab[0].getHeight());
	            			selectedDomino.setIsGridSize(false);
	            		}
	            
	            	//	selectedDomino.setBorder(null);
	            		selectedDomino=null;
	            		}
	            		
	            		else {//if it is a player piece
	            			Component c=dragPanel.getComponentAt(e.getPoint());
	
	            			if(e.getY()>kingdomPanel.getY()||c==null||!(c instanceof DominoPanel)||((DominoPanel)c).hasPiece()||!((DominoPanel)c).isEnabled()) {
	            				selectedPlayerPiece.createBorder();
	            				selectedPlayerPiece=null;
	            			}
	            			else {
	            				DominoPanel d=(DominoPanel)c;
	            				selectedPlayerPiece.setBounds(d.getX()+d.getWidth()/2-selectedPlayerPiece.getWidth()/2, 
	            						d.getY()+d.getHeight()/2-selectedPlayerPiece.getHeight()/2, selectedPlayerPiece.getWidth(), selectedPlayerPiece.getHeight());
	            				d.setHasPiece(true);
	            				selectedPlayerPiece.setHasDomino(true);
	            				selectedPlayerPiece.setSelectedDomino(d);
	            				gameInfoLabel.setText("<html>Domino "+d.getID()+"<html>");
	            				if(dragPanel.getComponentAt(lastX, lastY) instanceof DominoPanel) {
	            					DominoPanel lastDomino=(DominoPanel)dragPanel.getComponentAt(lastX, lastY);
	            					lastDomino.setHasPiece(false);
	            				}
	       
	            				lastX=selectedPlayerPiece.getX();
	            				lastY=selectedPlayerPiece.getY();
	            				
	            			}
	            		}
	            		
	            		mainPanel.getParent().revalidate();
            			mainPanel.getParent().validate();
            			mainPanel.getParent().repaint();
	            	}
	            	
	            }
	            
	            @Override
	            public void mouseReleased(final MouseEvent e) {
	            	if(selectedDomino!=null) {
	            //	selectedDomino.setLocation(clampToKingdomVertical(selectedDomino.getX(),selectedDomino.getY(),selectedDomino.getWidth(),selectedDomino.getHeight()));
	            	}
	            	
	            //	dragPanel.setVisible(true);
	            }
	        };
	        
	        dragPanel.addMouseMotionListener(ma); //For mouseDragged().
	        dragPanel.addMouseListener(ma); //For mousePressed().
	}
	
	/**
	 * Changes the current player and the displayed kingdom for the next one
	 */
	private void refresh() {
		changeCurrentPiece();		
		currentPlayerLogo.setBorder(new LineBorder(currentPiece.getColor(),8));
	}
	
	/**
	 * Prepares the first draft (also puts the second draft face down)
	 */
	private void prepareFirstDraft() {
		int f=0,j=4;
		if(!upperRow) {
			f=4;
			j=0;
		}

		for(int i=0;i<KingdominoApplication.getKingdomino().getCurrentGame().getCurrentDraft().getIdSortedDominos().size();i++) {
			DominoPanel p=new DominoPanel(KingdominoApplication.getKingdomino().getCurrentGame().getCurrentDraft().getIdSortedDominos().get(i).getId());
				p.setBounds(panelTab[i+f].getBounds());//place them in the panels left empty by last round
				dragPanel.add(p);
				p.loadImage();
				p.createID();
				dominoPanelList.add(p);
		}

		for(int i=0;i<4;i++) {
				DominoPanel p=new DominoPanel();//fake dominos for the face down effect
				p.setBounds(panelTab[i+j].getBounds());//place them in the panels left empty
				dragPanel.add(p);
				p.putBackIcon();
				dominoPanelList.add(p);
				p.setEnabled(false);
				p.setHasPiece(true);//to prevent interactions with pieces
		}
	}
	
	/**
	 * Turns the second draft face up
	 */
	private void prepareSecondDraft() {
		for(int i=0;i<4;i++) {				
			dominoPanelList.get(i).setHasPiece(false);//allow the first draft to be placed
		}
		for(int i=0;i<4;i++) {
			dragPanel.remove(dominoPanelList.get(dominoPanelList.size()-1));
			dominoPanelList.remove(dominoPanelList.size()-1);//remove the fakes
		}
		for(int i=0;i<KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft().getIdSortedDominos().size();i++) {	
			DominoPanel p=new DominoPanel(KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft().getIdSortedDominos().get(i).getId());
			p.setBounds(panelTab[i+4].getBounds());//place them in the panels left empty by last round
			dragPanel.add(p);
			p.loadImage();
			p.createID();
			dominoPanelList.add(p);
		}
	}
	
	/**
	 * Places the 4 new domino in the correct place on the table
	 */
	private void prepareNextDraft() {
		if(KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft()!=null&&
				KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft().getIdSortedDominos().size()>0) {
		for(DominoPanel p:dominoPanelList) {
			p.setHasPiece(false);//allow interaction with the dominos already in the list
		}
		int offset=4;//put the new dominos on the first or second row
		if(upperRow) {
			offset=0;
		}
		upperRow=!upperRow;

		for(int i=0;i<KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft().getIdSortedDominos().size();i++) {
			DominoPanel p=new DominoPanel(KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft().getIdSortedDomino(i).getId());
			dominoPanelList.add(p);
			p.setBounds(panelTab[i+offset].getBounds());//place them in the panels left empty by last round
			dragPanel.add(p);
			p.loadImage();
			p.createID();
			p.setHasPiece(true);//Add new domino and prevent interaction with the user
			}
		
		}
		else {//if there is no new draft, allow to place remaining dominos
			enableMovingOfPieceDomino();
			pickDomino=false;
		}
	}
	
	/**
	 * Reorders the piece array and the kingdom array to match the player order
	 */
	private void reOrderPlayers() {
		int index=0;
		PlayerPiece tabPiece[]=new PlayerPiece[4];
		JPanel tabPanel[]=new JPanel[4];
		Player p=KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		while(p!=null){
		for(int i=0;i<4;i++) {
			if(p.getColor().equals(playerPieceTab[i].getPlayerColor())) {
				tabPiece[index]=playerPieceTab[i];
				tabPanel[index++]=playerPanels[i];
				break;
				}
			}
			p=p.getNextPlayer();
		}
		playerPieceTab=tabPiece;
		playerPanels=tabPanel;
	}
	/**
	 * Allows dominos who don`t have a piece to be selected
	 */
	private void enableSelectionOfFreeDominos() {
		boolean isSelected=false;
		for(DominoPanel d:dominoPanelList) {
			isSelected=false;
			for(PlayerPiece p: playerPieceTab) {
			if(p.getSelectedDomino().equals(d)) {
				isSelected=true;
				break;
			}
			}
			if(!isSelected) {
				d.setHasPiece(false);
				d.setEnabled(true);
			}
		}
	}
	/**
	 * Prevents any domino from being selected (as if they had a piece on them)
	 */
	private void disableSelectionOfAllDominos() {
		for(DominoPanel d:dominoPanelList) {
			d.setHasPiece(true);
		}
	}
	
	/**
	 * Allows the movement of the domino that was chosen by the current piece
	 */
	private void enableMovingOfPieceDomino() {
		for(DominoPanel p:dominoPanelList) {
			if(currentPiece.getSelectedDomino().equals(p)) {
			p.setHasPiece(false);//allow player to move his domino
			}
		}
	}
	/**
	 * Changes the current piece in play 
	 */
	private void changeCurrentPiece() {
		
		playerPanels[turn].setVisible(false);
		currentPiece.setHasDomino(false);
		currentPiece.setEnabled(false);
		
		turn++;
		if(turn>3) {
			turn=0;
		}
		playerPanels[turn].setVisible(true);
		currentPiece=playerPieceTab[turn];
		currentPiece.setEnabled(true);
		Player.PlayerColor c=currentPiece.getPlayerColor();
		lblCurPlayer.setText("<html> Player "+Utils.getPlayerByColor(currentPiece.getPlayerColor()).getUser().getName()+" ("+c+")<html>");
		}
	
	/**
	 * Creates each players panel with respective color (the kingdom is represented by a panel)
	 */
	private void createPlayerPanels() {
		
		for(int i=0;i<4;i++) {
			JPanel p=new JPanel(null);
			GridBagConstraints c = new GridBagConstraints();		
			c.fill= GridBagConstraints.BOTH;
			c.gridheight=3;
			c.gridwidth=2;
			c.gridx=0;
			c.gridy=0;
			c.ipadx=300;
			c.ipady=300;
			p.setVisible(false);
			p.setBorder(new LineBorder(playerPieceTab[i].getColor(),3));
			playerPanels[i]=p;
			mainPanel.add(p,c);
		}
		playerPanels[0].setVisible(true);
	}
	/**
	 * Creates the castles for each player kingdom panel
	 */
	private void createPlayerCastle() {
		unitWidth=gridPanel.getComponent(0).getWidth();
		unitHeight=gridPanel.getComponent(0).getHeight();
		for(int i=0;i<4;i++) {
			JPanel castle=new JPanel();
			castle.setBackground(playerPieceTab[i].getColor());//place a colored square to represent the castle
			playerPanels[i].add(castle);
			castle.setBounds(gridPanel.getX()+unitWidth*4+gridPanel.getComponent(0).getX(),
					4*unitHeight+gridPanel.getY()+gridPanel.getComponent(0).getY(), 
					unitWidth, unitHeight);
		}
	}
	/**
	 * Creates the pieces that the players move to select a domino
	 */
	private void createPlayerPieces() {
		int width=50;
		int height=width;
		int y=panelTab[4].getY()-height-10;
		Player player = Utils.getFirstPlayer();
		for(int i=0;i<4;i++) {
			PlayerPiece p=new PlayerPiece(player.getColor());
			p.setBounds(panelTab[i].getX()+width/2, y, width, height);
			dragPanel.add(p);
			p.createAppearance();
			playerPieceTab[i]=p;
			p.setEnabled(false);
			scoreBoard.add(p.getScoreBoard());
			player=player.getNextPlayer();
		}
		currentPiece=playerPieceTab[0];
		currentPiece.setEnabled(true);
		Player.PlayerColor c=currentPiece.getPlayerColor();
		lblCurPlayer.setText("<html> Player "+Utils.getPlayerByColor(currentPiece.getPlayerColor()).getUser().getName()+ " ("+c+")<html>");
	}
	
	/**
	 * Creates the grid for the kingdoms (purely a design choice)
	 */
	private void createKingdomGrid() {
		for(int i=0;i<9;i++) {
			for(int j=0;j<9;j++) {
				JLabel lbl=new JLabel();
				lbl.setBorder(BorderFactory.createLineBorder(Color.black));
				gridPanel.add(lbl);
			}
		}
	}
	
	
	/**
	 * Creates the listeners for the place and discard buttons
	 */
	private void createPlaceOrDiscardButtons() {

		saveBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				int selectedOption = fc.showOpenDialog(self);
				if (selectedOption == JFileChooser.APPROVE_OPTION) {
					try {
						SaveController sc = new SaveController();
						String path = fc.getSelectedFile().getAbsolutePath();
						sc.SaveGame(path, true);
						System.exit(0);
					} catch (Exception exception) {
						JOptionPane.showMessageDialog(self, "Error saving file - " + exception.getMessage());
					}
				}
			}
		});
		placeBtn.setPreferredSize(new Dimension(15,30));
		saveBtn.setPreferredSize(new Dimension(15,30));
		placePanel.add(placeBtn);
		placePanel.add(saveBtn);
		infoPanel.add(placePanel);
	}

	/**
	 * Creates the boxes that represent the place on the table where dominos are placed (purely a design choice)
	 */
	private void createDraftBoxes() {
		tablePanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		JLabel topLbl=new JLabel();
		topLbl.setBorder(BorderFactory.createLineBorder(Color.black));
	//	c.insets=new Insets(16,8,32,8);
		c.gridwidth=4;
		c.gridx=0;
		c.gridy=0;
		c.ipady=8;
		c.weightx = 0.5;
		c.weighty = 0.3;
	//	tablePanel.add(topLbl,c);
		
		c.fill = GridBagConstraints.BOTH;
		c.insets=new Insets(36,0,36,0);
		c.gridwidth=1;
		c.ipady=16;
		c.ipadx=16;
		c.weightx = 0.5;
		c.weighty = 0.3;
		for(int i=0;i<2;i++) {
			//normally i<GameController.getNumberPlayer() instead of 4
			for(int j=0;j<4;j++) {
				JPanel p = new JPanel();
				p.setBorder(BorderFactory.createLineBorder(Color.black));
				c.gridx = j*2+1;
				c.gridy = i+1;
				panelTab[j+i*4]=p;//the 4 would normally be the number of player
				tablePanel.add(p, c);
			}
		}
		
		c.gridheight=2;
		c.gridwidth=1;
		c.ipady=0;
		c.ipadx=0;
		c.weightx = 0.1;
		c.weighty = 0.3;
		c.gridy=1;
		for(int i=0;i<5;i++) {
			JLabel lbl = new JLabel();
		//	lbl.setBorder(BorderFactory.createLineBorder(Color.black));
			c.gridx=i*2;
			tablePanel.add(lbl, c);
		}
		JLabel midLbl=new JLabel();
		midLbl.setBorder(BorderFactory.createLineBorder(Color.black));
		c.fill = GridBagConstraints.BOTH;
		c.insets=new Insets(8,16,24,8);
		c.gridwidth=1;
		c.weightx = 0.5;
		c.weighty = 0.3;
		c.gridy = 2;
	//	tablePanel.add(midLbl,c);
	}
	
	
	/**
	 * Creates the different listeners for user input (WASD, enter, R)
	 */
	private void createListeners() {
		Action rotate=new AbstractAction() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if(selectedDomino!=null&&selectedDomino.getY()>=kingdomPanel.getY()) {
					double width=0,height=0;
					Point p=null;
					selectedDomino.rotate();
					if(selectedDomino.direction==DirectionKind.Down||selectedDomino.direction==DirectionKind.Up) {
        				width=unitWidth;
        				height=unitHeight*2;
        				p=clampToKingdomVertical(selectedDomino.getX(),selectedDomino.getY(),(int)width,(int)height);
        				selectedDomino.setBounds((int)(p.x*width)+gridPanel.getComponent(0).getX(),
        						(int)(p.y*height/2+kingdomPanel.getY()+gridPanel.getComponent(0).getY()),(int)width,(int)height);
        				checkPlacementVertical();
        			}
        			else {
        				width=unitWidth*2;
        				height=unitHeight;
        				p=clampToKingdomHorizontal(selectedDomino.getX(),selectedDomino.getY(),(int)width,(int)height);
        				selectedDomino.setBounds((int)(p.x*width/2)+gridPanel.getComponent(0).getX(),
        						(int)(p.y*height+kingdomPanel.getY()+gridPanel.getComponent(0).getY()),(int)width,(int)height);
        				checkPlacementHorizontal();
        			}
        			
        			mainPanel.getParent().revalidate();
        			mainPanel.getParent().validate();
        			mainPanel.getParent().repaint();
				}						
			}
		};					
		
		Action goDown=new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(selectedDomino!=null&&selectedDomino.isGridSize()&&!pickDomino) {
					double height=unitHeight;
					int realY=selectedDomino.getY()-(dragPanel.getHeight()-kingdomPanel.getHeight());
					if(!selectedDomino.canGoDown(realY)) {
						return;
					}				
        				selectedDomino.setBounds(selectedDomino.getX(),(int)(selectedDomino.getY()+height), 
        				selectedDomino.getWidth(), selectedDomino.getHeight());
        				
        				if(selectedDomino.direction==DirectionKind.Up||selectedDomino.direction==DirectionKind.Down) {
        					checkPlacementVertical();
        					}
        					else {
        						checkPlacementHorizontal();
        					}
        			mainPanel.getParent().revalidate();
        			mainPanel.getParent().validate();
        			mainPanel.getParent().repaint();
        			}
			}
		};
		
		Action goUp=new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(selectedDomino!=null&&selectedDomino.isGridSize()&&!pickDomino) {
					int height=unitHeight;
					int realY=selectedDomino.getY()-(dragPanel.getHeight()-kingdomPanel.getHeight());
					if(!selectedDomino.canGoUp(realY)) {
						return;
					}				
					selectedDomino.setBounds(selectedDomino.getX(),selectedDomino.getY()-height, 
        			selectedDomino.getWidth(), selectedDomino.getHeight());
					
					if(selectedDomino.direction==DirectionKind.Up||selectedDomino.direction==DirectionKind.Down) {
						checkPlacementVertical();
						}
						else {
							checkPlacementHorizontal();
						}
        			mainPanel.getParent().revalidate();
        			mainPanel.getParent().validate();
        			mainPanel.getParent().repaint();
        			}
			}
		};
		
		Action goLeft=new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(selectedDomino!=null&&selectedDomino.isGridSize()&&selectedDomino.canGoLeft()&&!pickDomino) {
				int width=unitWidth;
    			selectedDomino.setBounds(selectedDomino.getX()-width,selectedDomino.getY(), 
    			selectedDomino.getWidth(), selectedDomino.getHeight());
    			
    			if(selectedDomino.direction==DirectionKind.Up||selectedDomino.direction==DirectionKind.Down) {
					checkPlacementVertical();
					}
					else {
						checkPlacementHorizontal();
					}
				
    			mainPanel.getParent().revalidate();
    			mainPanel.getParent().validate();
    			mainPanel.getParent().repaint();
    			}
    			}
		};
		
		Action goRight=new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(selectedDomino!=null&&selectedDomino.isGridSize()&&selectedDomino.canGoRight()&&!pickDomino) {
					int width=unitWidth;
	    			selectedDomino.setBounds(selectedDomino.getX()+width,selectedDomino.getY(), 
	    			selectedDomino.getWidth(), selectedDomino.getHeight());
	    			
					if(selectedDomino.direction==DirectionKind.Up||selectedDomino.direction==DirectionKind.Down) {
					checkPlacementVertical();
					}
					else {
						checkPlacementHorizontal();
					}
	    			mainPanel.getParent().revalidate();
	    			mainPanel.getParent().validate();
	    			mainPanel.getParent().repaint();
	    			}
				}
		};
		
		Action confirm=new AbstractAction() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(initialStage) {
				//this part is only here for the first 2 draft since they are different (this is why the boolean initialStage exists)
				if(selectedPlayerPiece!=null&&selectedPlayerPiece.hasDomino()&&pickDomino){//if we are trying to place a piece and there is a selection
					DominoPanel d=(DominoPanel)dragPanel.getComponentAt(selectedPlayerPiece.getX()-5,selectedPlayerPiece.getY());//-5 in x to get the domino
					DraftController.chooseDomino(d.getID());
					selectedPlayerPiece.createBorder();
					selectedPlayerPiece=null;
							  //the gui is such that you cannot have a 'bad' selection when picking (if you are able to select a domino, it is a good selection)
					refresh();//thus we can immediately refresh
					if(KingdominoApplication.getStatemachine().isStateActive
							(GameplayStatemachine.State.main_region_Running_r1_InitializeTurn)) {
					DraftController.createNextDraftDummy(KingdominoApplication.getKingdomino().getCurrentGame());
					KingdominoApplication.getStatemachine().runCycle();
					}
					if(KingdominoApplication.getStatemachine().isStateActive
							(GameplayStatemachine.State.main_region_Running_r1_PlayersTurn_r1_InitialPlacementOfDominoForCurrentPlayer)) {
					//if we enter the 'main' game for the first time
						prepareSecondDraft();
						disableSelectionOfAllDominos();
						enableMovingOfPieceDomino();
						whatToDoLabel.setText("<html>Click on your domino and place it in your kingdom<html>");
						pickDomino=false;//players need to place domino now, not to pick
						initialStage=false;//Start to play the game normally
					}
					
					
				}

				}
				else {
					//if a domino has to be placed (not initial stage)
					if(KingdominoApplication.getStatemachine().isStateActive(GameplayStatemachine.State.main_region_Running_r1_PlayersTurn_r1_MovingPreplacedDomino)) {
					if(selectedDomino!=null&&selectedDomino.isGridSize()) {//if the selection is in the grid (dominoPanels are resized to the grid, so it can be used to know
						Point p=null;
						if(selectedDomino.direction==DirectionKind.Down||selectedDomino.direction==DirectionKind.Up) {
	        				p=checkPlacementVertical();
	        				
	        				}
						else {
							p=checkPlacementHorizontal();
						}
						if(domInKingdom==null) {//if first time that dom is placed (it was null before), initiate moving dom in kingdom by calling preplace domino
							p.x=p.x-4;//translates view coordinate to model coordinate
							p.y=-(p.y-4);
							if(selectedDomino.direction==DirectionKind.Up) {//need to adjust for the model grid (the model requires the coordinate of the left tile)
								p.y=p.y-1;
								}
							if(selectedDomino.direction==DirectionKind.Left) {//need to adjust for the model grid (the model requires the coordinate of the left tile)
								p.x=p.x+1;
								}
							domInKingdom=new DominoInKingdom(p.x,p.y,Utils.getPlayerByColor(currentPiece.getPlayerColor()).getKingdom(),
	        						Utils.getDominoByID(selectedDomino.getID()));
							}
	        				if(!DominoController.checkPlacement(Utils.getPlayerByColor(currentPiece.getPlayerColor()).getKingdom(), domInKingdom.getDomino().getId(),
	        						p.x, p.y, Utils.viewDirectionToModel(selectedDomino.direction))||
	        						!DominoController.notCastleOverlap(p.x, p.y,Utils.viewDirectionToModel(selectedDomino.direction))) {//if placement is bad, end here
	        					gameInfoLabel.setText("<html>Could not place<html>");
	        					return;
	        				}
	        				else {
	        					DominoController.placeDomino();//if well placed, place the domino in the player's panel and kingdom
	        					Rectangle b=selectedDomino.getBounds();
	        					dragPanel.remove(selectedDomino);
	        					playerPanels[turn].add(selectedDomino);
	        					if(KingdominoApplication.getKingdomino().getCurrentGame()!=null) {
	        					playerPieceTab[turn].getScoreBoard().setText(Utils.getPlayerByColor(playerPieceTab[turn].getPlayerColor()).getUser().getName()+
	        							" has "+Utils.getPlayerByColor(playerPieceTab[turn].getPlayerColor()).getTotalScore()+" points");
	        					}
	        					selectedDomino.setBounds(b);
	        					dominoPanelList.remove(selectedDomino);//remove from the list to prevent interactions
	        					selectedDomino.setEnabled(false);
	        					selectedDomino.setHasPiece(true);
	        					selectedDomino.setBorder(null);
	        					selectedDomino=null;
	        					if(KingdominoApplication.getStatemachine().isStateActive
	        							(GameplayStatemachine.State.main_region_Running_r1_PlayersTurn_r1_SelectingDomino)) {
	        						//once the player is done placing, prepare for selection
	        						pickDomino=true;
	        						whatToDoLabel.setText("<html>Click on your piece and place it<html>");
	        						enableSelectionOfFreeDominos();//allow the free dominos to be chosen
	        						KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getDominoSelection().delete();	
	        				}
	        					else if(KingdominoApplication.getStatemachine().isStateActive
	        							(GameplayStatemachine.State.main_region_Running_r1_PlayersTurn_r1_InitialPlacementOfDominoForCurrentPlayer)) {
	        						//if we are still in placement, it is the last draft, so only place dominos
	        						pickDomino=false;
									whatToDoLabel.setText("<html>Click on your domino and place it in your kingdom<html>");									
									refresh();
									disableSelectionOfAllDominos();
									enableMovingOfPieceDomino();//enable the domino that the player chose to be moved
									selectedDomino=null;
									domInKingdom=null;
									
	        					}
	        					else if(KingdominoApplication.getStatemachine().isStateActive(GameplayStatemachine.State.main_region_Running_r1_PlayersTurn_r1_Discarding)) {
	        						if(KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft()!=null) {
									pickDomino=false;

		        						Utils.getPlayerByColor(currentPiece.getPlayerColor()).getDominoSelection().delete();
		        						refresh();
		        						
									whatToDoLabel.setText("<html>Click on your domino and place it in your kingdom<html>");	
	        						JOptionPane.showMessageDialog((Component)(e.getSource()),"Your domino was automatically discarded","Discarded",0);
	        						dominoPanelList.remove(currentPiece.getSelectedDomino());
	        						dragPanel.remove(currentPiece.getSelectedDomino());
	        						currentPiece.setSelectedDomino(null);
	        						KingdominoApplication.getStatemachine().getSCIPlayer().raiseDiscard();
	        						KingdominoApplication.getStatemachine().runCycle();
									disableSelectionOfAllDominos();
									enableMovingOfPieceDomino();//enable the domino that the player chose to be moved
									selectedDomino=null;
									domInKingdom=null;
									}
	        						else {//if it is the last turn and we are trying to discard
	        							if(KingdominoApplication.getKingdomino().getCurrentGame()!=null) {
	            						pickDomino=false;
	    								whatToDoLabel.setText("<html>Click on your domino and place it in your kingdom<html>");		
	    								KingdominoApplication.getStatemachine().getSCIPlayer().raiseDiscard();
		        						KingdominoApplication.getStatemachine().runCycle();
		        						
		        						if(KingdominoApplication.getStatemachine().isStateActive
			        							(GameplayStatemachine.State.main_region_EndGame)) {
		        							JOptionPane.showMessageDialog((Component)(e.getSource()),"Last domino was placed or could not be placed. Game is done","Discarded",0);
			        						mainPanel.removeAll();
			        						mainTab.revalidate();
			        						mainTab.validate();
			        						mainTab.repaint();
			        						dispose();
			        						EndGameTable end=new EndGameTable();
			        						return;
			        					}
	    								refresh();
	    								disableSelectionOfAllDominos();
	    								enableMovingOfPieceDomino();//enable the domino that the player chose to be moved
	    								selectedDomino=null;
	    								domInKingdom=null;
	    								}
	        						}
								}
	        					 if(KingdominoApplication.getStatemachine().isStateActive
	        							(GameplayStatemachine.State.main_region_EndGame)) {
	        						mainPanel.removeAll();
	        						mainTab.revalidate();
	        						mainTab.validate();
	        						mainTab.repaint();
	        						dispose();
	        						EndGameTable end=new EndGameTable();
	        						return;
	        					}
						}
					}
				}	
					else if(KingdominoApplication.getStatemachine().isStateActive(GameplayStatemachine.State.main_region_Running_r1_PlayersTurn_r1_SelectingDomino)) {
						//if instead we want to select a domino
						if(selectedPlayerPiece!=null&&selectedPlayerPiece.hasDomino()&&pickDomino){//if we are trying to place a piece and there is a selection
						
							DominoPanel d=(DominoPanel)dragPanel.getComponentAt(selectedPlayerPiece.getX()-5,selectedPlayerPiece.getY());//-5 in x to get the domino
							DraftController.chooseDominoDummy(d.getID());
							if(KingdominoApplication.getKingdomino().getCurrentGame().getTopDominoInPile()==null&&turn==3) {
								DraftController.readyLastDraft();//since a draft is not "raised" when there are no domino, raise ready here
								KingdominoApplication.getStatemachine().runCycle();
							}
							selectedPlayerPiece.createBorder();
							selectedPlayerPiece=null;
			
							if(KingdominoApplication.getStatemachine().isStateActive(GameplayStatemachine.State.main_region_Running_r1_PlayersTurn_r1_InitialPlacementOfDominoForCurrentPlayer)
							 ||KingdominoApplication.getStatemachine().isStateActive(GameplayStatemachine.State.main_region_Running_r1_PlayersTurn_r1_Discarding)) {
								pickDomino=false;
								whatToDoLabel.setText("<html>Click on your domino and place it in your kingdom<html>");
								if(turn==3) {
									prepareNextDraft();
									Domino domino=KingdominoApplication.getKingdomino().getCurrentGame().getTopDominoInPile();
									int nb=0;
									while(domino!=null) {
										nb++;
										domino=domino.getNextDomino();
									}
									lblDominoLeft.setText(nb+" dominos left in pile");
									playerPanels[turn].setVisible(false);
									currentPiece.setHasDomino(false);
									currentPiece.setEnabled(false);
									reOrderPlayers();
								}
								
								refresh();
								disableSelectionOfAllDominos();
								enableMovingOfPieceDomino();//enable the domino that the player chose to be moved
								selectedDomino=null;
								domInKingdom=null;
								}

							if(KingdominoApplication.getStatemachine().isStateActive(GameplayStatemachine.State.main_region_Running_r1_PlayersTurn_r1_Discarding)) {
								pickDomino=true;
		            			whatToDoLabel.setText("<html>Click on your piece and place it<html>");
        						enableSelectionOfFreeDominos();//allow the free dominos to be chosen
        						JOptionPane.showMessageDialog((Component)(e.getSource()),"Your domino was automatically discarded","Discarded",0);
        						dominoPanelList.remove(currentPiece.getSelectedDomino());
        						dragPanel.remove(currentPiece.getSelectedDomino());
        						currentPiece.setSelectedDomino(null);
        						Utils.getPlayerByColor(currentPiece.getPlayerColor()).getDominoSelection().delete();
        						KingdominoApplication.getStatemachine().getSCIPlayer().raiseDiscard();
        						KingdominoApplication.getStatemachine().runCycle();
        						
        					if(KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft()==null&&
        							KingdominoApplication.getKingdomino().getCurrentGame().getTopDominoInPile()==null) {
        						//if its the end game (first domino of the last 4 got discarded), raise valid selection to go back initialTurn
        						pickDomino=false;
								whatToDoLabel.setText("<html>Click on your domino and place it in your kingdom<html>");							
								refresh();
								disableSelectionOfAllDominos();
								enableMovingOfPieceDomino();//enable the domino that the player chose to be moved
								selectedDomino=null;
								domInKingdom=null;	
										
										while(KingdominoApplication.getStatemachine().isStateActive(GameplayStatemachine.State.main_region_Running_r1_PlayersTurn_r1_Discarding)) {
											JOptionPane.showMessageDialog((Component)(e.getSource()),"Your domino was automatically discarded","Discarded",0);
											if(KingdominoApplication.getKingdomino().getCurrentGame()!=null) {
											pickDomino=false;
											whatToDoLabel.setText("<html>Click on your domino and place it in your kingdom<html>");
											dominoPanelList.remove(currentPiece.getSelectedDomino());
			        						dragPanel.remove(currentPiece.getSelectedDomino());
			        						currentPiece.setSelectedDomino(null);
			        						Utils.getPlayerByColor(currentPiece.getPlayerColor()).getDominoSelection().delete();
			        						KingdominoApplication.getStatemachine().getSCIPlayer().raiseDiscard();
			        						KingdominoApplication.getStatemachine().runCycle();
			        						if(KingdominoApplication.getStatemachine().isStateActive
				        							(GameplayStatemachine.State.main_region_EndGame)) {
			        							JOptionPane.showMessageDialog((Component)(e.getSource()),"Last domino was placed or could not be placed. Game is done","Discarded",0);
				        						mainPanel.removeAll();
				        						mainTab.revalidate();
				        						mainTab.validate();
				        						mainTab.repaint();
				        						dispose();
				        						EndGameTable end=new EndGameTable();
				        						return;
				        					}
											refresh();
											disableSelectionOfAllDominos();
											enableMovingOfPieceDomino();//enable the domino that the player chose to be moved
											selectedDomino=null;
											domInKingdom=null;
											}
										}
										 if(KingdominoApplication.getStatemachine().isStateActive
				        							(GameplayStatemachine.State.main_region_EndGame)) {
				        						mainPanel.removeAll();
				        						mainTab.revalidate();
				        						mainTab.validate();
				        						mainTab.repaint();
				        						dispose();
				        						EndGameTable end=new EndGameTable();
				        						return;
				        					}
        							}
								}
							}
						}									
				}
				mainPanel.getParent().revalidate();
    			mainPanel.getParent().validate();
    			mainPanel.getParent().repaint();	
    			
    			//to make sure the display resets whether confirm was a success or not
    			if(selectedDomino!=null) {
    				selectedDomino.setBorder(null);
    			}
    			if(selectedPlayerPiece!=null) {
    				selectedPlayerPiece.createBorder();
    			}
    			selectedDomino=null;
    			selectedPlayerPiece=null;
			}
			
		};
		
		
		placeBtn.addActionListener(confirm);//so that clicking place does the same as pressing enter
		dragPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).clear();
		dragPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0),"rotate");
		dragPanel.getActionMap().put("rotate", rotate); 
		dragPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0),"down");
		dragPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0),"down");
		dragPanel.getActionMap().put("down", goDown);
		dragPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0),"up");
		dragPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0),"up");
		dragPanel.getActionMap().put("up", goUp);
		dragPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0),"left");
		dragPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0),"left");
		dragPanel.getActionMap().put("left", goLeft);
		dragPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0),"right");
		dragPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0),"right");
		dragPanel.getActionMap().put("right", goRight);
		dragPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),"confirm");
		dragPanel.getActionMap().put("confirm", confirm);
	}
		
	/**
	 *  Checks if the placement is valid for a domino placed horizontally
	 *  
	 * @return A point corresponding to the location of the selected domino
	 */
	private Point  checkPlacementHorizontal() {
		Point p=clampToKingdomHorizontal(selectedDomino.getX(),selectedDomino.getY(),selectedDomino.getWidth(),selectedDomino.getHeight());
		//test the placement
		p.x=p.x-4;//translates view coord to model coord
		p.y=-(p.y-4);
		if(selectedDomino.direction==DirectionKind.Left) {//need to adjust for the model grid
			p.x=p.x+1;
			}
		if(domInKingdom==null) {
			domInKingdom=new DominoInKingdom(0,0,Utils.getPlayerByColor(currentPiece.getPlayerColor()).getKingdom(),
					Utils.getDominoByID(selectedDomino.getID()));//init pos  of a domino is (0,0) (its a way to solve some bug)
			}
		
			if(!DominoController.preplaceDominoDummy(Utils.getPlayerByColor(currentPiece.getPlayerColor()), domInKingdom, p.x, p.y, 
					Utils.viewDirectionToModel(selectedDomino.direction))||!DominoController.notCastleOverlap(p.x, p.y,Utils.viewDirectionToModel(selectedDomino.direction))) {
				gameInfoLabel.setText("<html>Wrong placement<html>");
				selectedDomino.setBorder(new LineBorder(Color.RED,selectedDomino.getHeight()/10));
			}
			else {
				gameInfoLabel.setText("<html>Good placement<html>");
				selectedDomino.setBorder(new LineBorder(Color.GREEN,selectedDomino.getHeight()/10));
			}
			return p;
	}
	
	/**
	 *  Checks if the placement is valid for a domino placed vertically
	 *  
	 * @return A point corresponding to the location of the selected domino
	 */
	private Point checkPlacementVertical() {
		Point p=clampToKingdomVertical(selectedDomino.getX(),selectedDomino.getY(),selectedDomino.getWidth(),selectedDomino.getHeight());
		//test the placement
		p.x=p.x-4;//translates view coord to model coord
		p.y=-(p.y-4);
		if(selectedDomino.direction==DirectionKind.Up) {//need to adjust for the model grid (the model requires the coordinate of the left tile)
			p.y=p.y-1;
			}
		if(domInKingdom==null) {
			domInKingdom=new DominoInKingdom(0,0,Utils.getPlayerByColor(currentPiece.getPlayerColor()).getKingdom(),
					Utils.getDominoByID(selectedDomino.getID()));//init pos  of a domino is (0,0) (its a way to solve some bug)
			}
		
			if(!DominoController.preplaceDominoDummy(Utils.getPlayerByColor(currentPiece.getPlayerColor()), domInKingdom, p.x, p.y, 
					Utils.viewDirectionToModel(selectedDomino.direction))||!DominoController.notCastleOverlap(p.x, p.y,Utils.viewDirectionToModel(selectedDomino.direction))) {
				gameInfoLabel.setText("<html>Wrong placement<html>");
				selectedDomino.setBorder(new LineBorder(Color.RED,selectedDomino.getWidth()/10));
			}
			else {
				gameInfoLabel.setText("<html>Good placement<html>");
				selectedDomino.setBorder(new LineBorder(Color.GREEN,selectedDomino.getWidth()/10));
			}
			return p;
	}
	
	
	/**
	 * This method allows to get a point that is on the edge of a grid square that has similar height/width to that of the parameters
	 * To be used when the component is taller than longer
	 * @param x x coordinate of the click
	 * @param y y coordinate of the click
	 * @param width width of the object
	 * @param height height of the object
	 * @return the closest point to the click where 2 lines (horizontal and vertical) meet each other on the grid
	 */
	private Point clampToKingdomVertical(int x,int y,int width,int height) {
		int realY=y-(dragPanel.getHeight()-kingdomPanel.getHeight());//because kingdomPanel starting y position is lower, so need to adjust
		int posX=(int) Math.floor(((double)x)/width);
		int posY=(int) Math.floor(((double)realY)/(((double)height)/2));
		int gridX=0;
		int gridY=0;
		if(posX<1) {
			gridX=0;
		}
		else if(posX>8) {
			gridX=8;
		}
		else {
			gridX=posX;
		}
		
		if(posY<1) {
			gridY=0;
		}
		else if(posY>7) {
			gridY=7;
		}
		else {
			gridY=posY;
		}
		Point p=new Point();
		p.x=gridX;
		p.y=gridY;
		return p;	
	}
	
	/**
	 * This method allows to get a point that is on the edge of a grid square that has similar height/width to that of the parameters
	 * To be used when the component is longer than taller
	 * @param x x coordinate of the click
	 * @param y y coordinate of the click
	 * @param width width of the object
	 * @param height height of the object
	 * @return the closest point to the click where 2 lines (horizontal and vertical) meet each other on the grid
	 */
	private Point clampToKingdomHorizontal(int x,int y,int width,int height) {
			int realY=y-(dragPanel.getHeight()-kingdomPanel.getHeight());//because kingdomPanel starting y position is lower, so need to adjust
			int posX=(int) Math.floor(((double)x)/((width)/2));
			int posY=(int) Math.floor(((double)realY)/height);
			int gridX=0;
			int gridY=0;
			if(posX<1) {
				gridX=0;
			}
			else if(posX>7) {
				gridX=7;
			}
			else {
				gridX=posX;
			}
			
			if(posY<1) {
				gridY=0;
			}
			else if(posY>8) {
				gridY=8;
			}
			else {
				gridY=posY;
			}
			Point p=new Point();
			p.x=gridX;
			p.y=gridY;
			return p;		
	}	
}
