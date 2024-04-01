package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DominoController;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Map;

import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;

/**
 * @author Github: AdamMigliore
 */
public class VerifyNoOverlappingStepDefinition {
	private boolean actualAnswer, expectedAnswer;
	
	@Given("the game is initialized to check domino overlapping")
	public void initialized_for_domino_overlapping() {
		initializeGame();
	}
	
	@Given("the current player preplaced the domino with ID {int} at position {int}:{int} and direction {string}")
	public void preplace_domino(int id, int x, int y, String direction) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		DominoInKingdom dominoInKingdom = new DominoInKingdom(x, y,
				player.getKingdom(),
				getdominoByID(id));
		dominoInKingdom.setDirection(getDirection(direction));
		dominoInKingdom.getDomino().setStatus(DominoStatus.ErroneouslyPreplaced);
	}
	
	@When("check current preplaced domino overlapping is initiated")
	public void check_no_overlapping() {
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
		DirectionKind direction = preplacedDomino.getDirection();

		actualAnswer = DominoController.verifyNoOverlapping(player.getKingdom(), x, y, direction);
	}

	@Then("the current-domino\\/existing-domino overlapping is {string}")
	public void theCurrentDominoExistingDominoOverlappingIs(String result)
	{
		expectedAnswer = toResult(result);
		assertEquals(expectedAnswer, actualAnswer);
	}

	private boolean toResult(String result) {
		if (result.equals("valid"))
			return true;
		return false;
	}
}
