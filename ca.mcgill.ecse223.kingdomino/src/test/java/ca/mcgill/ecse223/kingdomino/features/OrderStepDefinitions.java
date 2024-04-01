package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DraftController;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;

/**
 * @author Annabelle Dion
 */
public class OrderStepDefinitions {
    @Given("the game is initialized for order next draft of dominoes")
    public void theGameIsInitializedForOrderNextDraftOfDominoes()
    {
        initializeGame();

        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        Draft draft = new Draft(DraftStatus.FaceDown, game);
        game.setNextDraft(draft);
    }

    @Given("the dominoes in next draft are facing down")
    public void theDominoesInNextDraftAreFacingDown()
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        game.getNextDraft().setDraftStatus(DraftStatus.FaceDown);
    }

    //next domino and previous
    @Given("the next draft is {string}")
    public void theNextDraftIs(String nextDraft)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        Draft draft = game.getNextDraft();
        
        String[] list = nextDraft.replaceAll(" ", "").split(",");
        for (String s : list)
        {
            draft.addIdSortedDomino(getdominoByID(Integer.decode(s)));
        }

    }

    @When("the ordering of the dominoes in the next draft is initiated")
    public void theOrderingOfTheDominoesInTheNextDraftIsInitiated()
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        Draft draft = game.getNextDraft();
        DraftController.orderDraft(draft);
    }

    @Then("the status of the next draft is sorted")
    public void theStatusOfTheNextDraftIsSorted()
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        assertEquals(Draft.DraftStatus.Sorted, game.getNextDraft().getDraftStatus());
    }

    @Then("the order of dominoes in the draft will be {string}")
    public void theOrderOfDominoesInTheDraftWillBe(String dominoes)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        List<Domino> expectedNextDraft = new ArrayList<>();

        for (String id : dominoes.replaceAll(" ", "").split(","))
        {
            expectedNextDraft.add(getdominoByID(Integer.decode(id)));
        }
        assertEquals(expectedNextDraft, game.getNextDraft().getIdSortedDominos());
    }
}
