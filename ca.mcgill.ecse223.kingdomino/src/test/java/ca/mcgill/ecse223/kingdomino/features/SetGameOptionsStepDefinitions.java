package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.GameController;
import ca.mcgill.ecse223.kingdomino.model.BonusOption;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * The step definition for SetGameOption
 * @author Maxens Destine
 *
 */

public class SetGameOptionsStepDefinitions {	
	BonusOption bonusOption1 =new BonusOption("harmony",KingdominoApplication.getKingdomino());
	BonusOption bonusOption2 =new BonusOption("middlekingdom",KingdominoApplication.getKingdomino());
	
	@Given("the game is initialized for set game options")
	public void the_game_is_initialized_for_set_game_options() {
		// Initialize empty game
		Kingdomino kingdomino = new Kingdomino();
		Game game = new Game(48, kingdomino);
		//game.setNumberOfPlayers(4);  in the game constructor, the number of player is 4 by default
		kingdomino.setCurrentGame(game);		
		KingdominoApplication.setKingdomino(kingdomino);
	}
	
	@When("set game options is initiated")
	public void set_game_options_is_initiated() {
		//no need to do anything
	}

	
	@When("the number of players is set to {int}")
	public void the_number_of_players_is_set_to(int numPlayer) {
		GameController.setNumberPlayer(numPlayer);
	}

	@When("Harmony {string} selected as bonus option")
	public void harmony_selected_as_bonus_option(String isUsingHarmony) {
		if(isUsingHarmony.equals("is")){
			GameController.addBonusOption(bonusOption1);
		}		
	}
		
	@When("Middle Kingdom {string} selected as bonus option")
	public void middle_Kingdom_is_selected_as_bonus_option(String isUsingMiddleKingdom) {	
		if(isUsingMiddleKingdom.equals("is")){
			GameController.addBonusOption(bonusOption2);
		}}
	
	@Then("the number of players shall be {int}")
	public void the_game__shall_have(int numPlayer) {
		int actualNumPlayers=KingdominoApplication.getKingdomino().getCurrentGame().getNumberOfPlayers();
		assertEquals(actualNumPlayers,numPlayer);		
	}

	@Then("Harmony {string} an active bonus")
	public void harmony_an_active_bonus(String isUsingHarmony) {
		Game game=KingdominoApplication.getKingdomino().getCurrentGame();
		if(isUsingHarmony.equals("is")){
			assertTrue(game.getSelectedBonusOptions().contains(bonusOption1));
		}
		else {
			assertFalse(game.getSelectedBonusOptions().contains(bonusOption1));
		}		
	}
		
	@Then("Middle Kingdom {string} an active bonus")
	public void middle_Kingdom_is_an_active_bonus(String isUsingMiddleKingdom) {
		Game game=KingdominoApplication.getKingdomino().getCurrentGame();
		if(isUsingMiddleKingdom.equals("is")){
			assertTrue(game.getSelectedBonusOptions().contains(bonusOption2));
		}
		else {
			assertFalse(game.getSelectedBonusOptions().contains(bonusOption2));
		}		
	}
		
	///////////////////////////////////////
	/// -----Private Helper Methods---- ///
	///////////////////////////////////////
	
}
