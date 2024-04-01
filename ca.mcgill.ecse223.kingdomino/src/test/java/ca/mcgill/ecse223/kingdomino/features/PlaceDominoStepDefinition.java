package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;

/**
 * @author Github: AdamMigliore
 */
public class PlaceDominoStepDefinition {

	@Given("the game is initialized for move current domino")
	public void initialize_game_for_move_domino() {
		initializeGame();
	}

	@Given("it is {string}'s turn")
	public void set_player_turn(String color) {
		// set the player to nextPlayer
		for (Player player : KingdominoApplication.getKingdomino().getCurrentGame().getPlayers()) {
			if (player.getColor().equals(getPlayerColor(color))) {
				KingdominoApplication.getKingdomino().getCurrentGame().setNextPlayer(player);
				break;
			}
		}
	}

	@Given("the {string}'s kingdom has the following dominoes:")
	public void add_dominoes_to_player_s_kingdom(String color, io.cucumber.datatable.DataTable dataTable) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			// Get values from cucumber table
			Integer id = Integer.decode(map.get("domino"));
			DirectionKind dir = getDirection(map.get("dominodir"));
			Integer posx = Integer.decode(map.get("posx"));
			Integer posy = Integer.decode(map.get("posy"));

			// Add the domino to a player's kingdom
			Domino dominoToPlace = getdominoByID(id);
      
			Player player = getPlayerByColor(color);
			Kingdom kingdom = player.getKingdom();

			DominoInKingdom domInKingdom = new DominoInKingdom(posx, posy, kingdom, dominoToPlace);
			domInKingdom.setDirection(dir);
			dominoToPlace.setStatus(DominoStatus.PlacedInKingdom);
		}
	}

	@Given("{string} has selected domino {int}")
	public void select_domino(String color,int id) {
		// create a domino selection of the id
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Draft myDraft = new Draft(DraftStatus.Sorted, game);

		Player player = getPlayerByColor(color);
		new DominoSelection(player, getdominoByID(id), myDraft);
	}

	@Given("domino {int} is tentatively placed at position {int}:{int} with direction {string}")
	public void preplace_domino(int id, int x, int y, String direction) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		// Place this domino tentatively
		DominoInKingdom tentativeDomino = new DominoInKingdom(x, y, player.getKingdom(), getdominoByID(id));
		tentativeDomino.getDomino().setStatus(DominoStatus.ErroneouslyPreplaced);
		tentativeDomino.setDirection(getDirection(direction));
	}

	@Given("domino {int} is in {string} status")
	public void domino_is_correctly_preplaced(int id,String status) {
		getdominoByID(id).setStatus(getDominoStatus(status));
	}

	@When("{string} requests to place the selected domino {int}")
	public void player_requests_to_place(String color, int id) {

		// It isn't necessary to use the player color here since this domino ID can only be associated with
		// one specific player kingdom.
		DominoController.placeDomino(id);
	}

	@Then("{string}'s kingdom should now have domino {int} at position {int}:{int} with direction {string}")
	public void place_domino(String color, int id, int x,int y,String direction) {
		// Find the requested player
		Player player = getPlayerByColor(color);

		// Find the requested domino in the player's territories
		boolean found = false;
		for (KingdomTerritory kingdomTerritory : player.getKingdom().getTerritories())
		{
			if (kingdomTerritory.getX() == x &&
					kingdomTerritory.getY() == y &&
					kingdomTerritory instanceof DominoInKingdom)
			{
				DominoInKingdom dominoInKingdom = (DominoInKingdom)kingdomTerritory;
				assertEquals("Domino at position: " + x + ", " + y + " does not have correct id",
								id, dominoInKingdom.getDomino().getId());
				assertEquals("Domino is not the right direction",
								getDirection(direction), dominoInKingdom.getDirection());

				found = true;
			}

		}
		assertTrue("Could not find domino in player's kingdom at position:" + x + ", " + y, found);

	}
}
