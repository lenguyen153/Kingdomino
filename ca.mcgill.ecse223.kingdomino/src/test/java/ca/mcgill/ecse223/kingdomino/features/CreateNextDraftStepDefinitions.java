package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DraftController;
import ca.mcgill.ecse223.kingdomino.model.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;

/**
 * @author Annabelle Dion
 */
public class CreateNextDraftStepDefinitions
{
    public Draft formerNextDraft = null;

    @Given("the game is initialized to create next draft")
    public void theGameIsInitializedToCreateNextDraft()
    {
        initializeGame();
    }

    @Given("there has been {int} drafts created")
    public void thereHasBeenDrafts(int numCreated)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        // Build up the pile of dominoes to be only the size required for the number
        // of players
        Domino prev = null;
        int lastDomAdded = 0;
        for (int i = 0; i < game.getMaxPileSize(); ++i)
        {
            Domino d = game.getAllDomino(i);
            if (i == 0)
                game.setTopDominoInPile(d);

            d.setStatus(Domino.DominoStatus.InPile);

            d.setPrevDomino(prev);
            if (prev != null)
                prev.setNextDomino(d);

            prev = d;
            lastDomAdded = i;
        }

        // Set the status of the last 0 or 12 dominoes to discarded.
        // This will only need to happen if there are 2 or 3 players
        // This is to simulate having less than 48 dominos in the pile
        // (which is the case when playing with fewer players).
        for (int i = lastDomAdded + 1; i < game.getAllDominos().size(); ++i)
        {
            game.getAllDomino(i).setStatus(Domino.DominoStatus.Discarded);
        }

        int numDomsPerDraft = 4;
        if (game.getNumberOfPlayers() == 3)
            numDomsPerDraft = 3;

        // Simulate n drafts.
        int index = 0;

        for (int i = 0; i < numCreated; ++i)
        {
            // For each draft, remove numDomsPerDraft dominoes from the game by setting
            // them to discarded
            for (int j = 0; j < numDomsPerDraft; ++j)
            {
                game.getAllDomino(index++).setStatus(Domino.DominoStatus.Discarded);
            }
        }
    }

    @Given("there is a current draft")
    public void thereIsACurrentDraft()
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        game.setCurrentDraft(new Draft(Draft.DraftStatus.FaceDown, game));
    }

    @Given("there is a next draft")
    public void thereIsANextDraft()
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        game.setNextDraft(new Draft(Draft.DraftStatus.FaceDown, game));
    }

    @Given("the top {int} dominoes in my pile have the IDs {string}")
    public void theTopDominoesInMyPileHaveTheIDs(int n, String ids)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        String[] idArr = ids.replaceAll(" ", "").split(",");
        Domino prev = null;

        // Place the dominos in the domino pile (queue-style structure according to tests)
        for (int i = 0; i < n; ++i)
        {
            Domino d = getdominoByID(Integer.decode(idArr[i]));

            if (i == 0)
            {
                game.setTopDominoInPile(d);
            }

            d.setStatus(Domino.DominoStatus.InPile);

            d.setPrevDomino(prev);
            if (prev != null)
                prev.setNextDomino(d);

            prev = d;
        }
    }

    @When("create next draft is initiated")
    public void createNextDraftIsInitiated()
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        formerNextDraft = game.getNextDraft();
        DraftController.createNextDraft(game);
    }

    @Then("a new draft is created from dominoes {string}")
    public void aNewDraftIsCreatedFromDominoes(String draft_ids)
    {
        Game game  = KingdominoApplication.getKingdomino().getCurrentGame();
        Set<Integer> actualIds = new HashSet<>();
        Set<Integer> expectedIds = new HashSet<>();

        for (Domino d : game.getAllDraft(game.getAllDrafts().size() -1).getIdSortedDominos())
        {
            actualIds.add(d.getId());
        }

        for (String id : draft_ids.replaceAll(" ", "").split(","))
        {
            expectedIds.add(Integer.decode(id));
        }

        assertEquals(expectedIds, actualIds);
    }

    @Then("the next draft now has the dominoes {string}")
    public void theNextDraftNowHasTheDominoes(String draft_ids)
    {
        Game game  = KingdominoApplication.getKingdomino().getCurrentGame();
        Set<Integer> actualIds = new HashSet<>();
        Set<Integer> expectedIds = new HashSet<>();

        for (Domino d : game.getNextDraft().getIdSortedDominos())
        {
            actualIds.add(d.getId());
        }

        for (String id : draft_ids.replaceAll(" ", "").split(","))
        {
            expectedIds.add(Integer.decode(id));
        }

        assertEquals(expectedIds, actualIds);

    }

    @Then("the dominoes in the next draft are face down")
    public void theDominoesInTheNextDraftAreFaceDown()
    {
        Game game  = KingdominoApplication.getKingdomino().getCurrentGame();

        assertEquals(Draft.DraftStatus.FaceDown, game.getNextDraft().getDraftStatus());
    }

    @Then("the top domino of the pile is ID {int}")
    public void theTopDominoOfThePileIsID(int id)
    {
        Game game  = KingdominoApplication.getKingdomino().getCurrentGame();

        assertEquals(id, game.getTopDominoInPile().getId());
    }

    @Then("the former next draft is now the current draft")
    public void theFormerNextDraftIsNowTheCurrentDraft()
    {
        Game game  = KingdominoApplication.getKingdomino().getCurrentGame();

        assertEquals(formerNextDraft, game.getCurrentDraft());
    }

    @Given("this is a {int} player game")
    public void thisIsAGame(int numPlayers)
    {
        KingdominoApplication.getKingdomino().delete();
        Kingdomino kingdomino = new Kingdomino();

        int maxPileSize = numPlayers * 12;

        Game game = new Game(maxPileSize, kingdomino);
        game.setNumberOfPlayers(numPlayers);
        kingdomino.setCurrentGame(game);
        // Populate game
        addDefaultUsersAndPlayers(game);
        createAllDominoes(game);
        game.setNextPlayer(game.getPlayer(0));
        KingdominoApplication.setKingdomino(kingdomino);
    }

    @Then("the pile is empty")
    public void thePileIsEmpty()
    {
        Game game  = KingdominoApplication.getKingdomino().getCurrentGame();

        assertNull(game.getTopDominoInPile());
    }

    @Then("there is no next draft")
    public void thereIsNoNextDraft()
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        assertNull(game.getNextDraft());
    }

}
