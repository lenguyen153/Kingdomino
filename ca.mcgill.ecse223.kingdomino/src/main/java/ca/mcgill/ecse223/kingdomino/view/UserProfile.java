package ca.mcgill.ecse223.kingdomino.view;

import ca.mcgill.ecse223.kingdomino.controller.GameController;
import ca.mcgill.ecse223.kingdomino.controller.PlayerProfileController;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.User;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

/**
 * @author Annabelle
 */

public class UserProfile extends KingdominoFrame{
	public static ArrayList<String> nameList = new ArrayList<>(Arrays.asList("player1", "player2", "player3", "player4"));
    public static ArrayList<PlayerColor> colorList = new ArrayList<>(Arrays.asList(PlayerColor.values()));
    private List<User> userList;
    private JLabel savedUser;
    private JButton btnBack;
    private JButton btnSave;
    private JTextField[]txtFieldArray;
    private JComboBox []colorsArray;
    private JTextField newUser;
    private JComboBox colors;
    private JPanel userProfilePNL;
    private JPanel userInputPanel;
    private JPanel savedUserPanel;
    private GridBagConstraints gbc;
    private Player.PlayerColor[] playerColors;

    /**
     * 
     * @param previous the previous frame (the window that was open before this one)
     */
    public UserProfile(JFrame previous) {
        super(previous);
    }
    
    /**
     * Creates the display
     */
    @Override
    public void createGUI(){
       userProfilePNL = new JPanel(new GridBagLayout());
       gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.ipadx = 30;
        gbc.ipady = 30;

        
        savedUserPanel=new JPanel();
        createUserList();
        userInputPanel=new JPanel(new GridLayout(4,2));
        txtFieldArray=new JTextField[4];
        colorsArray=new JComboBox[4];
        playerColors=new Player.PlayerColor[4];
        playerColors[0]=Player.PlayerColor.Blue;
        playerColors[1]=Player.PlayerColor.Green;
        playerColors[2]=Player.PlayerColor.Pink;
        playerColors[3]=Player.PlayerColor.Yellow;
        for(int i=0;i<4;i++) {
        	newUser=new JTextField("Player"+(i+1));
        	txtFieldArray[i]=newUser;
        	userInputPanel.add(newUser);
        	colors=new JComboBox<Player.PlayerColor>(playerColors);
        	colors.setSelectedIndex(i);
        	colorsArray[i]=colors;
        	userInputPanel.add(colors);
        }
        userInputPanel.setBorder(BorderFactory.createEmptyBorder(200, 200, 200, 200));
        btnBack=new JButton("Back");
        btnBack.addActionListener(backAction());
        btnSave=new JButton("Save");
        btnSave.addActionListener(saveAction());
        btnSave.setPreferredSize(new Dimension(75,40));
        mainPanel.add(userInputPanel, BorderLayout.CENTER);
        mainPanel.add(btnBack,BorderLayout.SOUTH);
        mainPanel.add(btnSave,BorderLayout.EAST);
        mainPanel.add(BorderLayout.WEST,new JScrollPane(savedUserPanel));
    }

    /**
     * Creates a panel with the name of all registered users
     */
    private void createUserList() {
    	 userList = PlayerProfileController.listUsers();
    	 savedUserPanel.removeAll();
         savedUserPanel.setLayout(new GridLayout(userList.size()+1,1));
         savedUserPanel.add(new JLabel("List of users ("+userList.size()+" total registered users):"));
         if (userList.size() != 0){
             for (int i = 0; i< userList.size(); i++){
                savedUser = new JLabel(userList.get(i).getName());
                 savedUserPanel.add(savedUser);
             }
         }
    }
    /**
     * Creates and returns an action listener for the back action
     * 
     * @return action listener for the back action
     */
    private ActionListener backAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				previousFrame.setVisible(true);
			}
		};
	}
    /**
     * Creates and returns an action listener for saving inputs
     * 
     * @return action listener for the saving
     */
    private ActionListener saveAction() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			//	previousFrame.setVisible(true);				
					ArrayList<String>nameArr=new ArrayList<String>();
					userList = PlayerProfileController.listUsers();
					for(JTextField txt:txtFieldArray) {
						nameArr.add(txt.getText());
					}
					GameController.createNewUsers(nameArr);//saves the new users if the names are valid
					createUserList();//updates the user list on the display
					boolean exists=false;
					User user=null;
					for(int i=0;i<txtFieldArray.length;i++) {
						JTextField txt=txtFieldArray[i];
						exists=false;
						for(User u:userList) {
							if(u.getName().equalsIgnoreCase(txt.getText())){
								exists=true;
								user=u;
								break;
							}
						}
						if(exists) {//if name was valid, it is in the list
							nameList.remove(i);
							nameList.add(i,user.getName());
						}
						else {
							JOptionPane.showMessageDialog(((JButton)e.getSource()),"An unvalid name ("+txt.getText()+") was removed","Error in input",0);
						}
						//if invalid replace by last valid name, if valid name, will remove the caps
							txt.setText(nameList.get(i));	
					}

					
					ArrayList<Player.PlayerColor>colors=new ArrayList<Player.PlayerColor>();
					for(JComboBox<Player.PlayerColor> comboBox:colorsArray) {
						colors.add((PlayerColor) comboBox.getSelectedItem());
					}
					
					if(GameController.noDuplicateColor(colors)) {//if all colors are distinct, update the list
						colorList=colors;
					}
					else {
						JOptionPane.showMessageDialog(((JButton)e.getSource()),"One or more of the color input is invalid","Error in input",0);
						//reset the color display
						 for(int i=0;i<4;i++) {
					        	colorsArray[i].setSelectedIndex(i);
					        }
						 colors.clear();
						 for(JComboBox<Player.PlayerColor> comboBox:colorsArray) {
								colors.add((PlayerColor) comboBox.getSelectedItem());
							}
						 		colorList=colors;
					}
					
					revalidate();
					validate();
					repaint();		
			}
		};
	}
}