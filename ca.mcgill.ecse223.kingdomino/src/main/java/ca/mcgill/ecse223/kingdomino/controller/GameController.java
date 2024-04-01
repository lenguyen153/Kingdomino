package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import ca.mcgill.ecse223.kingdomino.view.UserProfile;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ca.mcgill.ecse223.kingdomino.controller.Utils.getPlayerByColor;
import static ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor.*;

/**
 * @author Maxens Destine
  * This controller handles the main loop of the game (we may put the loop inside the view during later stages of the project); 
  * As such, the methods which implement feature 8 to 24 will be called in this class (they may reside in other class)
  * 	
  */
public class GameController {

	public GameController() {}
	
	/**
	 * This method is part of F#1:Set game options
	 * 
	 * F#1: Set game options: As a player, I want to configure the designated options of the Kingdomino
	 * game including the number of players(2,3 or 4) and the bonus scoring options
	 *  
	 *  This method will set the number of players of the current game to the parameter int.
	 *  It will only work if the game has no players in it
	 *  
	 * @author Maxens Destine
	 * @param numPlayer The number of player that should be the particular game
	 * @return true if the operation was successful, false otherwise
	 */
	public static boolean setNumberPlayer(int numPlayer) {

		if(KingdominoApplication.getKingdomino().getCurrentGame().getPlayers().isEmpty()) {
		if(!KingdominoApplication.getKingdomino().getCurrentGame().setNumberOfPlayers(numPlayer)) {
			System.out.println("Could not set the number of players to desired int");
			return false;
		}
		else {
			return true;
			}
		}
		
		return false;

	}
	
	public static int getNumberPlayer() {
		return KingdominoApplication.getKingdomino().getCurrentGame().getNumberOfPlayers();
	}
	
	
	/**
	 * This method is part of F#1:Set game options
	 * F#1: Set game options:
	 * As a player, I want to configure the designated options of the Kingdomino
	 * game including the number of players(2,3 or 4) and the bonus scoring options
	 *
	 *This method will add the bonus option to the current game
	 *
	 * @author Maxens Destine
	 * @param selectedBonusOption The bonus option that should be added to the game
	 */
	public static boolean addBonusOption(BonusOption selectedBonusOption) {	
			return KingdominoApplication.getKingdomino().getCurrentGame().addSelectedBonusOption(selectedBonusOption);
	}
	
	/**
	 * This method is part of F#3:Start a new game
	 * 
	 * F#3: Start a new game: As a Kingdomino player, I want to start a new game of Kingdomino against some
	 * opponents with my castle placed on my territory with the current settings of the game. The initial order
	 * of player should be randomly determined.
	 * 
	 * This method is here to create a game without creating users (only players)
	 * 
	 * 
	 * @author Maxens Destine
	 * @param numPlayer The number of player that should be in the game
	 * @param selectedBonusOptionList The bonus options that should be activated in the game
	 */
	public static boolean createGame(int numPlayer, List<BonusOption> selectedBonusOptionList) {
	
		//start new game with corresponding options
		int maxPileSize=12*numPlayer;	
		Game game = new Game(maxPileSize,KingdominoApplication.getKingdomino());
		KingdominoApplication.getKingdomino().setCurrentGame(game);
		for(BonusOption b:selectedBonusOptionList){
				addBonusOption(b);
			}
		
		//If the game cannot be setup properly, it will be deleted.
		if(!setNumberPlayer(numPlayer)) {
			game.delete();
			KingdominoApplication.getKingdomino().setCurrentGame(null);
			return false;
		}

		//to make the initial order random we shuffle the list
		createPlayers(game);
		createKingdoms();
		
		return true;
	}
	
	public static boolean createGameFromViewInput(int numPlayer, List<BonusOption> selectedBonusOptionList) {
		
		//start new game with corresponding options
		int maxPileSize=12*numPlayer;	
		Game game = new Game(maxPileSize,KingdominoApplication.getKingdomino());
		KingdominoApplication.getKingdomino().setCurrentGame(game);
		for(BonusOption b:selectedBonusOptionList){
				addBonusOption(b);
			}
		
		//If the game cannot be setup properly, it will be deleted.
		if(!setNumberPlayer(numPlayer)) {
			game.delete();
			KingdominoApplication.getKingdomino().setCurrentGame(null);
			return false;
		}

		//to make the initial order random we shuffle the list
		createPlayersFromViewInput(game);
		createKingdoms();
		
		return true;
	}
	
	private static void createPlayersFromViewInput(Game game) {
		for(int i=0;i<4;i++) {
			Player p=new Player(game);
		}
		giveNameToPlayer(UserProfile.nameList, game);
		giveColorToPlayer(UserProfile.colorList, game);
		randomPlayerOrder(game);
	}
	
	/**
	 * Used for F#1:Set game options
	 * This method checks if the number of players is between 1 and 5 (excluded limits)
	 * 
	 * @author Maxens Destine
	 * @param numPlayer the number of desired players in a game
	 * @return true if the number of player is valid, false otherwise
	 */
	public static boolean checkNumPlayer(int numPlayer) {
		return numPlayer > 1 && numPlayer < 5;
	}
	
	
	
	/**
	 * This method is used for F#2:Provide user profile
	 * This method updates the user profile when the current game is done and also removes the game as the current game
	 * 
	 * @author Maxens Destine
	 */
	public static void endGame() {
		CalculateScoreController.CalculateRanking(KingdominoApplication.getKingdomino().getCurrentGame());
		PlayerProfileController.endGameStatistic();
		//removes the game as the current game
		KingdominoApplication.getKingdomino().setCurrentGame(null);			
	}

	/**
	 * This method is part of F#2:Provide user profile
	 * 
	 * F#2: Provide user profile: As a player, I wish to use my unique user name in when a game starts...
	 * 
	 * This method creates players for the current game.
	 * This method does not check for duplicates itself, noDuplicateName and noDuplicateColor have to be called prior to this 
	 * method to ensure all names and colors are distinct. This is done so that it is easy to know which part of a game creation
	 * failed when creating a game.
	 * 
	 * @author Maxens Destine
	 * @param nameList The list of all players that will play the game
	 */
	public static void createUsers( List<String>nameList, List<PlayerColor>colorList)
	{
		int index = 0;
		for (PlayerColor color : colorList)
		{
			Player p = getPlayerByColor(color);
			String name = nameList.get(index++);
			User u = new User(name, KingdominoApplication.getKingdomino());
			u.addPlayerInGame(p);
		}
	}
	
	public static void createNewUsers(List<String>nameList) {
		if(nameList==null)return;
		User u=null;
		for(int i=0;i<nameList.size();i++) {
			if(nameList.get(i)!=null) {
			 u=createUser(nameList.get(i));
			 }
			if(u!=null) {//will be null if already exists or name is invalid
				KingdominoApplication.getKingdomino().addUser(u);
			}
		}
	}
	
	/**
	 * Used for F#3:Start a new game
	 * This method will create a kingdom for every present in the current game
	 * 
	 * @author Maxens Destine
	 */
	public static void createKingdoms() {
		for(int i=0;i<KingdominoApplication.getKingdomino().getCurrentGame().getNumberOfPlayers();i++) {
			Kingdom kingdom = new Kingdom(KingdominoApplication.getKingdomino().getCurrentGame().getPlayers().get(i));
			new Castle(0, 0, kingdom, KingdominoApplication.getKingdomino().getCurrentGame().getPlayers().get(i));
		}
	}
	/**
	 * To be used in the view for F#3:Start a new game
	 * This method checks a list to see if there are duplicate values or null values
	 * It is useful to check quickly if the user inputs are valid
	 * 
	 * @author Maxens Destine
	 * @param nameList The list that should be checked for duplicates or null values
	 * @return The presence(false) or absence(true) of null or duplicate names
	 */
	public static boolean noDuplicateName(List<String>nameList) {
		
		for(int i=0;i<nameList.size();i++) {		
			//checks if the name is unique
			for(int j=0;j<nameList.size();j++) {
				
				if(nameList.get(i)==null||(nameList.get(i).equals(nameList.get(j))&&i!=j)){						
					return false;
					}
				
				}
			}
		return true;	
	}
	
	/**
	 * To be used in the view for F#3:Start a new game
	 * This method checks a list to see if there are duplicate values or null values
	 * It is useful to check quickly if the user inputs are valid
	 * 
	 * @author Maxens Destine
	 * @param colorList The list that should be checked for duplicates or null values
	 * @return The presence(false) or absence(true) of null or duplicate colors
	 */
	public static boolean noDuplicateColor(List<PlayerColor>colorList) {
		//checks if the color is unique
		for(int i=0;i<colorList.size();i++) {
			
			for(int j=0;j<colorList.size();j++) {
				
				if(colorList.get(i)==null||(colorList.get(i).equals(colorList.get(j))&&i!=j)){						
				return false;
				}
			}
		}
		return true;
	}		

	
	/**
	 * Used for F#2
	 * This method gives a name to the user of players in the list of a game. This method assume that the index 
	 * of the name in the nameList corresponds to the index of the player in the list of players that should take that name.
	 * 
	 * @author Maxens Destine
	 * @param nameList The list of names to be given to the user of the players
	 * @param game The game in which are the players that need a name for their user
	 */
	public static void giveNameToPlayer(List<String>nameList,Game game){
		/* If the name corresponds to a user, the user of the player will be set to that user,
		 * otherwise a user will be created with the corresponding name*/
		for(int i=0;i<nameList.size();i++) {
		String name = nameList.get(i);
		if(User.hasWithName(name)) {
			game.getPlayers().get(i).setUser(User.getWithName(name));
		}
		else {			
			
			game.getPlayers().get(i).setUser(createUser(name));			
			}
		}
	}
	
	/**
	 * Used for F#3:Start a new game
	 * This method creates a user using a string. The string must not be empty or contain special characters
	 * 
	 * @author Maxens Destine
	 * @param name the name of the user to be created
	 * @return the user that was created. Will be null if a user already has that name or if the name is invalid
	 */
	public static User createUser(String name) {
		User user=null;
		
		Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
	    Matcher matcher = pattern.matcher(name);
	 
	      if(!matcher.matches()) {

	      } else {
	    	  if(!name.equals("")&&!User.hasWithName(name.toLowerCase())) {					
	 			 user = new User(name.toLowerCase(),KingdominoApplication.getKingdomino());					
	 			}        
	      }
	
		return user;
	}
		
	
	/**
	 * Used for F#3:Start a new game
	 * This method gives a color to players in the list of a game. This method assume that the index of the color in the nameList
	 * corresponds to the index of the player in the list of players that should take that name.
	 * 
	 * @author Maxens Destine
	 * @param colorList List of color to be given to the players of a game
	 * @param game The game in which are the players that need the colors
	 */
	public static void giveColorToPlayer(List<PlayerColor>colorList,Game game) {
		for(int i=0;i<colorList.size();i++) {
		game.getPlayers().get(i).setColor(colorList.get(i));
		}
	}
	
	/**
	 * Used for F#3:Start a new game
	 * Creates players for a game 
	 * 
	 * @author Maxens Destine
	 * @param game The game in which the players should be created
	 */
	public static void createPlayers(Game game)
	{
		List<PlayerColor> colors = new ArrayList<PlayerColor>(Arrays.asList(Blue, Green, Pink, Yellow));
		for (int i = 0; i < game.getNumberOfPlayers(); ++i)
		{
			Player p = new Player(game);
			p.setColor(colors.get(i));
		}
		randomPlayerOrder(game);
	}

	public static void randomPlayerOrder(Game game)
	{
		List<PlayerColor> colorOrder = new ArrayList<PlayerColor>(Arrays.asList(Blue, Green, Pink, Yellow));
		if (game.getNumberOfPlayers() == 3)
			colorOrder = colorOrder.subList(0, 2);
		Collections.shuffle(colorOrder);
		setPlayerOrder(game, colorOrder);
	}

	/**
	 * Create a player order based on player's current domino selections
	 * @author Roey Borsteinas
	 */
	public static void nextPlayerOrder(Game game)
	{
		Map<Integer, PlayerColor> colorOrder = new TreeMap<>();

		for (Player player : game.getPlayers())
		{
			colorOrder.put(player.getDominoSelection().getDomino().getId(), player.getColor());
		}

		setPlayerOrder(game, new ArrayList<PlayerColor>(colorOrder.values()));
	}

	// Create the linked list in the order of the given list
	public static void setPlayerOrder(Game game, List<PlayerColor> colorOrder)
	{
		Player previous = null;
		game.setNextPlayer(getPlayerByColor(colorOrder.get(0)));
		for (PlayerColor color : colorOrder)
		{
			Player current = getPlayerByColor(color);
			// Umple guarantees this to also set the nextPlayer of the previous player to this player
			current.setPrevPlayer(previous);
			current.setNextPlayer(null);
			previous = current;
		}
	}
		
	/**
	 * Used for F#3:Start a new game
	 * Creates players for a game without giving colors
	 *
	 * @author Maxens Destine
	 * @param game The game in which the players should be created
	 */
	public static void createPlayerNoColor(Game game){
		for(int i=0;i<game.getNumberOfPlayers();i++) {
		game.getPlayers().add(game.addPlayer());
		}
	}
		
}
