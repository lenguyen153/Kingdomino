package ca.mcgill.ecse223.kingdomino.controller;

import java.util.ArrayList;
import java.util.List;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.User;

/**
 * 
 * @author Maxens Destine
 *
 */
public class PlayerProfileController {
	
	public PlayerProfileController() {}
	
	/**
	 * This method is part of F#2
	 * F#2: Provide user profile: As a player, I wish to use my unique user name in when a game starts
	 * I also want the Kingdomino app to maintain my game statistics (e.g. number of games played, won, etc.).
	 * 
	 * @author Maxens Destine
	 * This class updates the number of game played and won for all players that are part of the current Kingdomino game
	 */
	public static void endGameStatistic() {
		List<Player> playerList = KingdominoApplication.getKingdomino().getCurrentGame().getPlayers(); 
		Player winner=null;
		int maxPoints=-1;
		
		for(Player p:playerList) {

			if(p.hasUser()) {
			p.getUser().setPlayedGames(p.getUser().getPlayedGames()+1);
			/*This should instead get the winner from the method that solves tiebreak*/
			if(p.getTotalScore()>maxPoints) {			
				winner=p;
				maxPoints=p.getTotalScore();				
				}			
			}		
		}

		if(winner.hasUser()) {
			winner.getUser().setWonGames(winner.getUser().getWonGames()+1);
		}
				
	}
	
	
	/**
	 * This method is part of F#2
	 * F#2: Provide user profile...
	 * This method creates a list in alphabetical order of the name of all users
	 * 
	 * @author Maxens Destine
	 * @return an ordered list that contains the name of all users
	 */
	public static List<User> listUsers() {
		
		List<User> userList=KingdominoApplication.getKingdomino().getUsers();
		List<User>userName=new ArrayList<User>();
		List<User>userNameOrdered=new ArrayList<User>();
		
		for(User u:userList) {
			userName.add(u);
		}
		
		
		while(!userName.isEmpty()){
			User s=userName.get(0);
			for(int i=0;i<userName.size();i++){
				if(userName.get(i).getName().compareTo(s.getName())<0) {
					s=userName.get(i);
				}			
			}	
			userName.remove(s);
			userNameOrdered.add(s);
		}	
		return userNameOrdered;	
	}
	
	/**
	 * This method is part of F#2
	 * F#2: Provide user profile...
	 * This method prints (for now as there is no view yet) the name and played/won game statistics of the player
	 *
	 * @author Maxens Destine
	 * @param name the name of the user we want statistics from
	 */
	public static User userStatistics(String name) {
		if(User.hasWithName(name)) {
			User user=User.getWithName(name);
			return user;
		}
		
		else {
			
			System.out.println("Could not find user with name: "+name);
			return null;
		}
		
	}

}
