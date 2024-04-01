package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DraftController;
import ca.mcgill.ecse223.kingdomino.model.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.*;

import static org.junit.Assert.*;
import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;

/**
 * @author Annabelle Dion
 */
public class ChooseNextDominoStepDefinitions {
    @Given("the game is initialized for choose next domino")
    public void theGameIsInitializedForChooseNextDomino() {
        initializeGame();
        initializeStatemachine();
    }
    @Given("the next draft is sorted with dominoes {string}")
    public void theNextDraftIsSortedWithDominoes(String ids)
    {
        Game game  = KingdominoApplication.getKingdomino().getCurrentGame();

        Draft draft = new Draft(Draft.DraftStatus.Sorted, game);

        for (String id : ids.replaceAll(" ", "").split(","))
        {
            draft.addIdSortedDomino(getdominoByID(Integer.decode(id)));
        }

        game.setNextDraft(draft);
    }
    @Given("player's domino selection {string}")
    public void playerSDominoSelection(String selections)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        int index = 0;
        for (String color : selections.split(","))
        {
            if (color.equals("none"))
            {
                index++;
                continue;
            }
            Player player = getPlayerByColor(color);
            new DominoSelection(player, game.getNextDraft().getIdSortedDomino(index++), game.getNextDraft());
        }
    }

    @Given("the current player is {string}")
    public void theCurrentPlayerIs(String color)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        game.setNextPlayer(getPlayerByColor(color));

    }

    @When("current player chooses to place king on {int}")
    public void currentPlayerChoosesToPlaceKingOn(int id)
    {
        Game game  = KingdominoApplication.getKingdomino().getCurrentGame();
        Draft draft = game.getNextDraft();

        DraftController.chooseNextDomino(id);
    }

    @Then("current player king now is on {string}")
    public void currentPlayerKingNowIsOn(String id)
    {
        Game game  = KingdominoApplication.getKingdomino().getCurrentGame();
        Player player = game.getNextPlayer();
        assertEquals((int)Integer.decode(id), player.getDominoSelection().getDomino().getId());
    }

    @Then("the selection for next draft is now equal to {string}")
    public void theSelectionForNextDraftIsNowEqualTo(String selections)
    {
        Game game  = KingdominoApplication.getKingdomino().getCurrentGame();
        List<String> expectedIds = new ArrayList<String>(Arrays.asList(selections.split(",")));
        List<String> actualIds = new ArrayList<>();

        for (Domino d : game.getNextDraft().getIdSortedDominos())
        {
            if (d.hasDominoSelection())
                actualIds.add(d.getDominoSelection().getPlayer().getColor().toString().toLowerCase());
            else
                actualIds.add("none");
        }

        assertEquals(expectedIds, actualIds);
    }

    @Then("the selection for the next draft selection is still {string}")
    public void theSelectionForTheNextDraftSelectionIsStill(String selections)
    {
        Game game  = KingdominoApplication.getKingdomino().getCurrentGame();
        List<String> expectedIds = new ArrayList<String>(Arrays.asList(selections.split(",")));
        List<String> actualIds = new ArrayList<>();

        for (Domino d : game.getNextDraft().getIdSortedDominos())
        {
            if (d.hasDominoSelection())
                actualIds.add(d.getDominoSelection().getPlayer().getColor().toString().toLowerCase());
            else
                actualIds.add("none");
        }

        assertEquals(expectedIds, actualIds);
    }
}
