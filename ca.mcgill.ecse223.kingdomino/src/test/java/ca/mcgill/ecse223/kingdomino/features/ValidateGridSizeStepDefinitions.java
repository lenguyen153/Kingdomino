package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DominoController;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.Player;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;
import static org.junit.Assert.*;

/**
 * @author Annabelle Dion
 */
public class ValidateGridSizeStepDefinitions
{
    private boolean actual = false;
    @Given("the game is initialized for verify grid size")
    public void theGameIsInitializedForVerifyGridSize()
    {
        initializeGame();
    }

    @When("validation of the grid size is initiated")
    public void validationOfTheGridSizeIsInitiated()
    {
        Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
        actual = DominoController.verifyGridSize(player.getKingdom());
    }

    @Then("the grid size of the player's kingdom shall be {string}")
    public void theGridSizeOfThePlayerSKingdomShallBe(String result)
    {
        if (result.equals("valid"))
            assertTrue(actual);
        else if (result.equals("invalid"))
            assertFalse(actual);
        else // this would be a problem if this happens..
            assertTrue(false);
    }

    @Given("the  player preplaces domino {int} to their kingdom at position {int}:{int} with direction {string}")
    public void thePlayerPreplacesDominoIdToTheirKingdomAtPositionPosx(int id, int x, int y, String direction)
    {
        Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
        (new DominoInKingdom(x, y, player.getKingdom(), getdominoByID(id))).getDomino().setStatus(Domino.DominoStatus.ErroneouslyPreplaced);
    }
}
