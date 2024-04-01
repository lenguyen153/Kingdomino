package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DominoController;
import ca.mcgill.ecse223.kingdomino.model.*;

import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.assertEquals;

import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;
/**
 * @author Github: AdamMigliore
 *
 */
public class VerifyCastleAdjacencyStepDefinition {
	private boolean actualAnswer, expectedAnswer;
	
	@Given("the game is initialized for castle adjacency")
	public void initialized_for_castle_adjacency() {
		initializeGame();
	}


	@When("check castle adjacency is initiated")
	public void check_castle_adjacency_initiated() {
		int x = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getTerritory(KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getTerritories().size()-1).getX();
		int y = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getTerritory(KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getTerritories().size()-1).getY();
		DirectionKind direction = ((DominoInKingdom) KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getTerritory(KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getKingdom().getTerritories().size()-1)).getDirection();
		actualAnswer = DominoController.verifyCastleAdjacency(x, y, direction);
	}

	@Then("the castle\\/domino adjacency is {string}")
	public void answer_to_castle_adjacency(String result) {
		expectedAnswer = isValid(result);
		assertEquals(expectedAnswer, actualAnswer);
	}

}