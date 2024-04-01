package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DraftController;
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.Game;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static ca.mcgill.ecse223.kingdomino.features.TestUtils.initializeGame;
import static org.junit.Assert.assertEquals;

/**
 * @author Annabelle Dion
 */
public class RevealStepDefinitions
{
    @Given("the game is initialized for reveal next draft of dominoes")
    public void theGameIsInitializedForRevealNextDraftOfDominoes()
    {
        initializeGame();

        Game game  = KingdominoApplication.getKingdomino().getCurrentGame();

        Draft nextDraft = new Draft(Draft.DraftStatus.FaceDown, game);
        game.setNextDraft(nextDraft);
    }

    @Given("the dominoes in next draft are sorted")
    public void theDominoesInNextDraftAreSorted()
    {
        Game game  = KingdominoApplication.getKingdomino().getCurrentGame();
        Draft nextDraft = game.getNextDraft();
        nextDraft.setDraftStatus(Draft.DraftStatus.Sorted);
    }

    @When("the revealing of the dominoes in the next draft is initiated")
    public void theRevealingOfTheDominoesInTheNextDraftIsInitiated()
    {
        DraftController.revealNextDraft();
    }

    @Then("the status of the next draft is face up")
    public void theStatusOfTheNextDraftIsFaceUp() {
        Game game  = KingdominoApplication.getKingdomino().getCurrentGame();
        assertEquals(Draft.DraftStatus.FaceUp, game.getNextDraft().getDraftStatus());
    }
}
