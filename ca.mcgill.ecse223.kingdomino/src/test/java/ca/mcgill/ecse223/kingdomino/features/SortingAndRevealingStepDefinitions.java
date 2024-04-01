package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DraftController;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.*;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static ca.mcgill.ecse223.kingdomino.statemachine.gameplay.GameplayStatemachine.State.main_region_Running_r1_PlayersTurn_r1_InitialPlacementOfDominoForCurrentPlayer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Annabelle Dion
 */
public class SortingAndRevealingStepDefinitions {

    @Given("there is a next draft, face down")
    public void thereIsANextDraftFaceDown() {
        GameplayStatemachine sm = new GameplayStatemachine();
        KingdominoApplication.setStatemachine(sm);

        sm.getSCITurn().setSCITurnOperationCallback(new TurnOperationCallback());
        sm.getSCIPlayer().setSCIPlayerOperationCallback(new PlayerOperationCallback());
        sm.getSCIDraft().setSCIDraftOperationCallback(new DraftOperationCallback());
        sm.getSCIGame().setSCIGameOperationCallback(new GameOperationCallback());

        sm.init();
        //next draft created when entering the state machine
        sm.enter();
    }

    @And("all dominoes in current draft are selected")
    public void allDominoesInCurrentDraftAreSelected() {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        GameplayStatemachine sm = KingdominoApplication.getStatemachine();

        //the current draft is created and ordered when entering the stage machine
        Draft currentDraft = game.getCurrentDraft();

        //current draft is revealed
        sm.getSCIDraft().raiseReady();

        //change the next player after each iteration
        //enter the running state when nextPlayer is null
        for (int i = 0; i < game.getNumberOfPlayers(); i++){
            DraftController.chooseDomino(currentDraft.getIdSortedDomino(i).getId());
        }
    }

    @When("next draft is sorted")
    public void nextDraftIsSorted() {
        //done automatically by the state machine
    }

    @When("next draft is revealed")
    public void nextDraftIsRevealed() {
        GameplayStatemachine sm = KingdominoApplication.getStatemachine();
        sm.getSCIDraft().raiseReady();
    }

    @Then("the next draft shall be sorted")
    public void theNextDraftShallBeSorted() {
        Game game  = KingdominoApplication.getKingdomino().getCurrentGame();
        Draft next = game.getNextDraft();

        for (int i = 0; i < next.numberOfIdSortedDominos() - 1; i++){
            assertTrue(next.getIdSortedDomino(i+1).getId() > next.getIdSortedDomino(i).getId());
        }
    }

    @Then("the next draft shall be facing up")
    public void theNextDraftShallBeFacingUp() {
        Game game  = KingdominoApplication.getKingdomino().getCurrentGame();
        assertEquals(Draft.DraftStatus.FaceUp, game.getNextDraft().getDraftStatus());
    }

    @Then("it shall be the player's turn with the lowest domino ID selection")
    public void itShallBeThePlayerSTurnWithTheLowestDominoIDSelection() {
        Game game  = KingdominoApplication.getKingdomino().getCurrentGame();
        GameplayStatemachine sm = KingdominoApplication.getStatemachine();

        //check if the statemachine is in the start of a player's turn
        //assertTrue(sm.isStateActive(main_region_Running_r1_PlayersTurn_r1_InitialPlacementOfDominoForCurrentPlayer));

        // Assert that each player in the current player order is in the correct selection id order
        Player current = game.getNextPlayer();
        int index = 0;
        while (current != null)
        {
            assertEquals(current.getDominoSelection().getDomino().getId(), game.getCurrentDraft().getIdSortedDomino(index++).getId());
            current = current.getNextPlayer();
        }



    }
}
