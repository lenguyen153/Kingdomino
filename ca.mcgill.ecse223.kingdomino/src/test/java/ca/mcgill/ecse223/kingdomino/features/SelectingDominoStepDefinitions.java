package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DraftController;
import ca.mcgill.ecse223.kingdomino.controller.GameController;
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;

import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;
import static ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus.FaceUp;
import static org.junit.jupiter.api.Assertions.*;

class TurnOperationCallbackDummy extends TurnOperationCallback
{
    @Override
    public boolean isLast()
    {
        return false;
    }
}

public class SelectingDominoStepDefinitions
{
    int chosenID;

    @Given("the game has been initialized for selecting domino")
    public void theGameHasBeenInitializedForSelectingDomino()
    {
        GameplayStatemachine sm = new GameplayStatemachine();
        KingdominoApplication.setStatemachine(sm);

        sm.getSCITurn().setSCITurnOperationCallback(new TurnOperationCallbackDummy());
        sm.getSCIPlayer().setSCIPlayerOperationCallback(new PlayerOperationCallbackDummy());
        sm.getSCIDraft().setSCIDraftOperationCallback(new DraftOperationCallback());
        sm.getSCIGame().setSCIGameOperationCallback(new GameOperationCallback());

        sm.init();
        sm.enter();
    }

    @Given("the order of players is {string}")
    public void theOrderOfPlayersIs(String colors)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        List<Player.PlayerColor> colorOrder = new ArrayList<>();
        for (String color : colors.split(","))
        {
            colorOrder.add(getPlayerColor(color));
        }
        GameController.setPlayerOrder(game, colorOrder);
    }

    @Given("the next draft has the dominoes with ID {string}")
    public void theNextDraftHasTheDominoesWithIDs(String ids)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        Draft draft = new Draft(FaceUp, game);
        for (String id : ids.split(","))
        {
            draft.addIdSortedDomino(getdominoByID(Integer.parseInt(id)));
        }
        game.setNextDraft(draft);
    }

    @Given("the {string} player is selecting his\\/her domino with ID {int}")
    public void thePlayerIsSelectingHisHerDominoWithIDChosendominoid(String color, int id)
    {
        GameplayStatemachine sm = KingdominoApplication.getStatemachine();
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        // Bring the statemachine into the SelectingDominoState of the player turn
        sm.getSCIDraft().raiseReady();
        for (int i = 0; i < game.getNumberOfPlayers(); ++i)
        {
            sm.getSCIDraft().raiseValidSelection();
        }
        // The statemachine is now in Running superstate,
        // transition it to the player's turn state
        sm.getSCIDraft().raiseReady();

        // Now in the InitialPlacementOfDominoForCurrentPlayer
        // if this is not the requested player, simulate a placement/discard/selection phase
        // until it is the requested player's turn.
        while (game.getNextPlayer().getColor() != getPlayerColor(color))
        {
            sm.getSCIPlayer().raiseDiscard();
            sm.getSCIDraft().raiseValidSelection();
        }
        // Simulate discarding for current player
        sm.getSCIPlayer().raiseDiscard();

        chosenID = id;
    }

    @When("the {string} player completes his\\/her domino selection")
    public void thePlayerCompletesHisHerDominoSelection(String color)
    {
        DraftController.chooseDomino(chosenID);
    }

    @Then("a new draft shall be available, face down")
    public void aNewDraftShallBeAvailableFaceDown()
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        GameplayStatemachine sm = KingdominoApplication.getStatemachine();

        assertTrue(sm.isStateActive(GameplayStatemachine.State.main_region_Running_r1_InitializeTurn));
        assertEquals(Draft.DraftStatus.FaceDown, game.getNextDraft().getDraftStatus());
    }
}
