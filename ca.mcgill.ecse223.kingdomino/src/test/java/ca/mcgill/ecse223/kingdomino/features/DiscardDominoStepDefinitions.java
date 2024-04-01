package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DiscardController;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;

/**
 * Step definitions for discarddomino feature
 * @author Roey Borsteinas
 */
public class DiscardDominoStepDefinitions
{
	@Given("the game is initialized for discard domino")
	public void the_game_is_initialized_for_discard_domino()
	{
		initializeGame();
	}

	@Given("the player's kingdom has the following dominoes:")
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

	@Given("domino {int} is in the current draft")
	public void domino_is_in_the_current_draft(int domID) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();

		// Can simply create a new draft and add a single domino to it
		Draft draft = new Draft(Draft.DraftStatus.FaceUp, game);
		draft.addIdSortedDomino(getdominoByID(domID));

		game.setCurrentDraft(draft);
	}

	@Given("the current player has selected domino {int}")
	public void the_current_player_has_selected_domino(int domID) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();

		Draft draft = game.getCurrentDraft();
		new DominoSelection(game.getNextPlayer(), getdominoByID(domID), draft);
	}

	@Given("the player preplaces domino {int} at its initial position")
	public void the_player_preplaces_domino_at_its_initial_position(int domID) {
		// Since the current implementation works on a domino selection, there is no
		// need to _preplace_ the domino at all
	}

	@When("the player attempts to discard the selected domino")
	public void the_player_attempts_to_discard_the_selected_domino() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();

		DiscardController dc = new DiscardController();
		dc.doDiscard(game.getNextPlayer(), game.getNextPlayer().getDominoSelection().getDomino());
	}

	@Then("domino {int} shall have status {string}")
	public void domino_shall_have_status(int domID, String domStatus) {
		DominoStatus actualStatus = getdominoByID(domID).getStatus();
		DominoStatus expectedStatus = getDominoStatus(domStatus);
		assertEquals(expectedStatus, actualStatus);
	}
}
