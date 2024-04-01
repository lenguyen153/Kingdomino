package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.*;
import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.BonusOption;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;

import ca.mcgill.ecse223.kingdomino.controller.CalculateScoreController;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * @author Vincent Trinh
 */
public class CalculatePlayerScoreStepDefinition {

	@Given("the game is initialized for calculate player score")
	public void the_game_is_initialized_for_calculate_player_score() {
		initializeGame();
	}

	@Given("the game has {string} bonus option")
	public void the_game_has_bonus_option(String string) {
		// Write code here that turns the phrase above into concrete actions
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		String bonusType = toBonusOptionName(string);
		
		//if there are no bonuses then dont add any option
		if(!bonusType.equals("none")) {
			BonusOption bonus = new BonusOption(bonusType, game.getKingdomino());
			game.addSelectedBonusOption(bonus);
		}
	}

	@When("calculate player score is initiated")
	public void calculate_player_score_is_initiated() {
		// Write code here that turns the phrase above into concrete actions
		Kingdomino game = KingdominoApplication.getKingdomino();
		Player aPlayer = game.getCurrentGame().getNextPlayer();
		CalculateScoreController.CalculatePlayerScore(aPlayer);
	}

	@Then("the total score should be {int}")
	public void the_total_score_should_be(int totalScore) {
		// Write code here that turns the phrase above into concrete actions
		assertEquals(totalScore, KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getTotalScore());
	}

	private String toBonusOptionName(String string) {
		switch (string.toLowerCase()) {
		case ("middle kingdom"):
			return "MiddleKingdom";
		case ("harmony"):
			return "Harmony";
		}
		return "none";
	}
}
