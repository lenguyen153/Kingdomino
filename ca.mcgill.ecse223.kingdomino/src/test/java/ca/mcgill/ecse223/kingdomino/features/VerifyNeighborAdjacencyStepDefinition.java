package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;

/**
 * @author Github: AdamMigliore
 */
public class VerifyNeighborAdjacencyStepDefinition {
	private boolean actualAnswer;

	@Given("the game is initialized for neighbor adjacency")
	public void initialized_for_neighbor_adjacency() {
		initializeGame();
	}
	
	@When("check current preplaced domino adjacency is initiated")
	public void check_neighbor_adjacency() {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();

		// Get the current preplaced domino
		DominoInKingdom preplacedDomino = null;

		for (KingdomTerritory kingdomTerritory : player.getKingdom().getTerritories())
		{
			if (kingdomTerritory instanceof DominoInKingdom)
			{
				DominoInKingdom dominoInKingdom = (DominoInKingdom)kingdomTerritory;
				if (dominoInKingdom.getDomino().getStatus() == DominoStatus.ErroneouslyPreplaced)
					preplacedDomino = dominoInKingdom;
			}
		}
		int x = preplacedDomino.getX();
		int y = preplacedDomino.getY();
		int id = preplacedDomino.getDomino().getId();
		DirectionKind direction = preplacedDomino.getDirection();
		actualAnswer = DominoController.verifyNeighborAdjacency(player.getKingdom(), id, x, y, direction);
	}
	
	@Then("the current-domino\\/existing-domino adjacency is {string}")
	public void answer_to_neighbor_adjacency(String result) {
		boolean expectedAnswer = isValid(result);

		assertEquals(expectedAnswer,actualAnswer);
	}
}
