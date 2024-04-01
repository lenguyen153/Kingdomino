package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.*;
import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;

import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.BonusOption;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;

import ca.mcgill.ecse223.kingdomino.controller.CalculateScoreController;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * @author Vincent Trinh
 */
public class CalculateBonusScoresStepDefinition {

	@Given("the game is initialized for calculate bonus scores")
	public void the_game_is_initialized_for_calculate_bonus_scores() {
		initializeGame();
	}

	@Given("the following dominoes are present in a player's kingdom:")
	public void the_player_s_kingdom_has_the_following_dominoes(io.cucumber.datatable.DataTable dataTable) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			// Get values from cucumber table
			Integer id = Integer.decode(map.get("id"));
			DirectionKind dir = getDirection(map.get("dominodir"));
			Integer posx = Integer.decode(map.get("posx"));
			Integer posy = Integer.decode(map.get("posy"));

			// Add the domino to a player's kingdom
			Domino dominoToPlace = getdominoByID(id);
			Kingdom kingdom = game.getNextPlayer().getKingdom();
			DominoInKingdom domInKingdom = new DominoInKingdom(posx, posy, kingdom, dominoToPlace);
			domInKingdom.setDirection(dir);
			dominoToPlace.setStatus(DominoStatus.PlacedInKingdom);
		}
	}


	@Given("Middle Kingdom is selected as bonus option")
	public void middle_Kingdom_is_selected_as_bonus_option() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		BonusOption MiddleKingdom = new BonusOption("MiddleKingdom", game.getKingdomino());
		game.addSelectedBonusOption(MiddleKingdom);
	}

	@Given("the player's kingdom also includes the domino {int} at position {int}:{int} with the direction {string}")
	public void the_player_s_kingdom_also_includes_the_domino_at_position_with_the_direction(Integer id, Integer posx, Integer posy, String string) {
		// Write code here that turns the phrase above into concrete actions
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Domino dominoToPlace = getdominoByID(id);
		Kingdom kingdom = game.getPlayer(0).getKingdom();
		DominoInKingdom domInKingdom = new DominoInKingdom(posx, posy, kingdom, dominoToPlace);
		DirectionKind dir = getDirection(string);
		domInKingdom.setDirection(dir);
		dominoToPlace.setStatus(DominoStatus.PlacedInKingdom);
	}

	@When("calculate bonus score is initiated")
	public void calculate_bonus_score_is_initiated() {
		Kingdomino game = KingdominoApplication.getKingdomino();
		Player aPlayer = game.getCurrentGame().getNextPlayer();
		CalculateScoreController.CalculateBonusScore(aPlayer);
	}

	@Then("the bonus score should be {int}")
	public void the_bonus_score_should_be(int bonus) {
		assertEquals(bonus, KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getBonusScore());
	}

	@Given("Harmony is selected as bonus option")
	public void harmony_is_selected_as_bonus_option() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		BonusOption Harmony = new BonusOption("Harmony",game.getKingdomino());
		game.addSelectedBonusOption(Harmony);
	}

	@Given("the player's kingdom also includes the following dominoes:")
	public void the_player_s_kingdom_also_includes__the_following_dominoes(io.cucumber.datatable.DataTable dataTable) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			// Get values from cucumber table
			Integer id = Integer.decode(map.get("id"));
			DirectionKind dir = getDirection(map.get("dominodir"));
			Integer posx = Integer.decode(map.get("posx"));
			Integer posy = Integer.decode(map.get("posy"));

			// Add the domino to a player's kingdom
			Domino dominoToPlace = getdominoByID(id);
			Kingdom kingdom = game.getPlayer(0).getKingdom();
			DominoInKingdom domInKingdom = new DominoInKingdom(posx, posy, kingdom, dominoToPlace);
			domInKingdom.setDirection(dir);
			dominoToPlace.setStatus(DominoStatus.PlacedInKingdom);
		}
	}

	@Then("The bonus score should be {int}")
	public void the_bonus_score_should_be1(int bonus) {
		assertEquals(bonus, KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getBonusScore());
	}
}
