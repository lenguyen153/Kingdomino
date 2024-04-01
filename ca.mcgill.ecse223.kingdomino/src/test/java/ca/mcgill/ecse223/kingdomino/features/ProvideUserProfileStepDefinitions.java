package ca.mcgill.ecse223.kingdomino.features;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.GameController;
import ca.mcgill.ecse223.kingdomino.controller.PlayerProfileController;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.User;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * The step definition for ProvideUserProfile
 * @author Maxens Destine
 *
 */

public class ProvideUserProfileStepDefinitions {
	User user=null;
	List<User>list=null;
	@Given("the program is started and ready for providing user profile")
	public void the_program_is_started_and_ready_for_providing_user_profile() {
		// Initialize kingdomino
		Kingdomino kingdomino = new Kingdomino();	
		KingdominoApplication.setKingdomino(kingdomino);
	}
	
	@Given("there are no users exist")
	public void there_are_no_users_exist() {
		//no code needed
	}

	
	

	@Then("the user {string} shall be in the list of users")
	public void the_user_shall_be_in_the_list_of_users(String name) {		
		assertTrue(User.hasWithName(name));	
	}
	
	@Given("the following users exist:")
	public void the_following_users_exist(io.cucumber.datatable.DataTable dataTable) {
		for(int i=1;i<dataTable.height();i++) {
		createUser(dataTable.asList().get(i));
		}
	}
	
	@When("I provide my username {string} and initiate creating a new user")
	public void I_provide_my_username_and_initiate_creating_a_new_user(String name) {		
		
		user=GameController.createUser(name);//wil be null if it failed
	}
	
	@Then("the user creation shall {string}")
	public void the_user_creation_shall(String name) {
		String s=null;
		if(user==null) {
			s="fail";
		}
		else {
			s="succeed";
		}
		
		assertEquals(s,name);
		}
	
	
	
	@When("I initiate the browsing of all users")
	public void I_initiate_the_browing_of_all_users(){
		list=PlayerProfileController.listUsers();
	}
	
	@Then("the users in the list shall be in the following alphabetical order:")
	public void the_users_in_the_list_shall_be_in_the_following_alphabetical_order(io.cucumber.datatable.DataTable dataTable){
	
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for(User u:list){
			System.out.println("Name: "+u.getName());
		}
		//the users should be in order of name in the list
		for (Map<String, String> map : valueMaps) {
			User aUser = PlayerProfileController.userStatistics(map.get("name"));
			Integer placeInList = Integer.decode(map.get("placeinlist"));
					
			for(int i=0;i<list.size();i++) {
				if((i+1)==placeInList) {//+1 because list starts at 0
					assertEquals(list.get(i), aUser);
					break;
				}
			}
		}	
	}
	
	@Given("the following users exist with their game statistics:")
	public static void the_following_users_exist_with_their_game_statistics(io.cucumber.datatable.DataTable dataTable){
		User user1=createUser(dataTable.cell(1,0));
		User user2=createUser(dataTable.cell(2,0));
		User user3=createUser(dataTable.cell(3,0));
			
		user1.setPlayedGames(Integer.parseInt(dataTable.cell(1, 1)));
		user2.setPlayedGames(Integer.parseInt(dataTable.cell(2, 1)));
		user3.setPlayedGames(Integer.parseInt(dataTable.cell(3, 1)));
		
		user1.setWonGames(Integer.parseInt(dataTable.cell(1, 2)));
		user2.setWonGames(Integer.parseInt(dataTable.cell(2, 2)));
		user3.setWonGames(Integer.parseInt(dataTable.cell(3, 2)));	
	}
	
	@When("I initiate querying the game statistics for a user {string}")
	public void I_initiate_querying_the_game_statistics_for_a_user(String name){
	user=PlayerProfileController.userStatistics(name);	
	}
	
			
	
	
	@Then("the number of games played by {string} shall be {int}")
	public void the_number_of_games_played_by_shall_be(String string, Integer int1) {
		assertEquals(user.getPlayedGames(),int1.intValue());
		}

	@Then("the number of games won by {string} shall be {int}")
	public void the_number_of_games_won_by_shall_be(String string, Integer int1) {
		assertEquals(user.getWonGames(),int1.intValue());
	}
	
	
	public static User createUser(String name) {
		User user=null;
		
		Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");
	    Matcher matcher = pattern.matcher(name);
	 
	      if(!matcher.matches()) {
	           System.out.println(name + " contains special character(s)");
	      } else {
	    	  if(!name.equals("")&&!User.hasWithName(name.toLowerCase())) {					
	 			 user = new User(name.toLowerCase(),KingdominoApplication.getKingdomino());					
	 			}        
	      }
	
		return user;
	}

}
