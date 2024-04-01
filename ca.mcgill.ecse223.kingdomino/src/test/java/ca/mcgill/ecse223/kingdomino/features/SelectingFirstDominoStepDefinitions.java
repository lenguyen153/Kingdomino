package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DraftController;
import ca.mcgill.ecse223.kingdomino.controller.GameController;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;

import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;
import static ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus.FaceUp;
import static ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus.Sorted;
import static org.junit.Assert.assertEquals;

/**
 * Dummy version of the turn operation callback class which allows skipping of player placement
 * since discard checks always return true.
 */
class PlayerOperationCallbackDummy extends PlayerOperationCallback
{
    @Override
    public boolean checkDiscard()
    {
        return true;
    }
}

public class SelectingFirstDominoStepDefinitions
{
    private int chosenID;

    @Given("the game has been initialized for selecting first domino")
    public void gameHasBeenInitializeForSelectingDomino()
    {
        GameplayStatemachine sm = new GameplayStatemachine();
        KingdominoApplication.setStatemachine(sm);

        sm.getSCITurn().setSCITurnOperationCallback(new TurnOperationCallback());
        sm.getSCIPlayer().setSCIPlayerOperationCallback(new PlayerOperationCallbackDummy());
        sm.getSCIDraft().setSCIDraftOperationCallback(new DraftOperationCallback());
        sm.getSCIGame().setSCIGameOperationCallback(new GameOperationCallback());

        sm.init();
        sm.enter();
    }

    @Given("the initial order of players is {string}")
    public void theInitialOrderOfPlayersIs(String colors)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        List<Player.PlayerColor> colorOrder = new ArrayList<>();
        for (String color : colors.split(","))
        {
            colorOrder.add(getPlayerColor(color));
        }
        GameController.setPlayerOrder(game, colorOrder);
    }

    @Given("the current draft has the dominoes with ID {string}")
    public void theCurrentDraftHasTheDominoesWithID(String ids)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        Draft draft = new Draft(Sorted, game);
        for (String id : ids.split(","))
        {
            draft.addIdSortedDomino(getdominoByID(Integer.parseInt(id)));
        }
        game.setCurrentDraft(draft);
    }

    @Given("player's initial domino selection {string}")
    public void playersInitialDominoSelection(String selections)
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
            new DominoSelection(player, game.getCurrentDraft().getIdSortedDomino(index++), game.getCurrentDraft());
        }
    }

    @Given("the {string} player is selecting his\\/her first domino with ID {int}")
    public void thePlayerIsSelectingHisHerFirstDominoOfTheGameWithIDChosendominoid(String color, int id)
    {
        GameplayStatemachine sm = KingdominoApplication.getStatemachine();
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        sm.getSCIDraft().raiseReady();

        // Set the current player in the model
        game.setNextPlayer(getPlayerByColor(color));

        chosenID = id;
    }

    @And("the validation of domino selection returns {string}")
    public void theValidationOfDominoSelectionReturns(String arg0)
    {
        // This is done automatically by the draft controller when selecting dominos.
        // (it wont allow an invalid selection)
    }

    @Then("the {string} player shall be placing his\\/her domino")
    public void thePlayerShallBePlacingHisHerDomino(String color)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        GameplayStatemachine sm = KingdominoApplication.getStatemachine();
        assertEquals(getPlayerColor(color), game.getNextPlayer().getColor());
    }

    @When("the {string} player completes his\\/her first domino selection of the game")
    public void thePlayerCompletesHisHerFirstDominoSelectionOfTheGame(String color)
    {
        DraftController.chooseDomino(chosenID);
    }
}
