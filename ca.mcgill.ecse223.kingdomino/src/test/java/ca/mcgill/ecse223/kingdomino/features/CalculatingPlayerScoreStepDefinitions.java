package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DraftController;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import static ca.mcgill.ecse223.kingdomino.controller.Utils.getPlayerByColor;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatingPlayerScoreStepDefinitions
{
    Player.PlayerColor player;
    @Given("the game is initialized for calculating player score")
    public void theGameIsInitializedForCalculatingPlayerScore()
    {
        GameplayStatemachine sm = new GameplayStatemachine();

        sm.getSCITurn().setSCITurnOperationCallback(new TurnOperationCallback());
        sm.getSCIPlayer().setSCIPlayerOperationCallback(new PlayerOperationCallback());
        sm.getSCIDraft().setSCIDraftOperationCallback(new DraftOperationCallback());
        sm.getSCIGame().setSCIGameOperationCallback(new GameOperationCallback());

        KingdominoApplication.setStatemachine(sm);

        // initialize the statemachine and enter it
        sm.init();
        sm.enter();

        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        // Bring the state machine into the running state
        sm.getSCIDraft().raiseReady();
        for (int i = 0; i < game.getNumberOfPlayers(); ++i)
        {
            // Instead of just raising valid selection, since discard may be in play here,
            // must actually create domino selections
            DraftController.chooseDomino(game.getCurrentDraft().getIdSortedDomino(i).getId());
        }
        // The statemachine is now in Running superstate,
        // transition it to the player's turn state
        sm.getSCIDraft().raiseReady();
    }

    @Given("the current player has no dominoes in his\\/her kingdom yet")
    public void theCurrentPlayerHasNoDominoesInHisHerKingdomYet()
    {
        // True by default
    }

    @Given("the score of the current player is {int}")
    public void theScoreOfTheCurrentPlayerIs(int score)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        game.getNextPlayer().setPropertyScore(score);
        // Since the state machine will automatically move on to the next player's turn
        // when the domino is placed, we need to remember who actually placed the domino.
        player = game.getNextPlayer().getColor();
    }

    @Given("the game has no bonus options selected")
    public void theGameHasNoBonusOptionsSelected()
    {
        // By default this is true
    }

    @Then("the score of the current player shall be {int}")
    public void theScoreOfTheCurrentPlayerShallBe(int id)
    {
        assertEquals(id, getPlayerByColor(player).getTotalScore());
    }
}
