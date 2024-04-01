package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DominoController;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.DraftOperationCallback;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.GameOperationCallback;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.GameplayStatemachine;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.PlayerOperationCallback;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.TurnOperationCallback;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;
import static org.junit.Assert.*;

public class PlacingDominoStepDefinition {

	/*
	 * Used to get the game up to state wanted
	 */
	class TurnOperationCallbackDummy extends TurnOperationCallback {

		@Override
		public boolean isLast() {
			// TODO Auto-generated method stub
			return false;
		}

	}
	
	class PlayerOperationCallbackDummy extends PlayerOperationCallback {
		@Override
		public boolean checkDiscard() {
			// TODO Auto-generated method stub
			return false;
		}
	}

	@Given("the game has been initialized for placing domino")
	public void gameHasBeenInitalizedForPlacingDomino() {

		GameplayStatemachine sm = new GameplayStatemachine();

		sm.getSCITurn().setSCITurnOperationCallback(new TurnOperationCallbackDummy());
		sm.getSCIPlayer().setSCIPlayerOperationCallback(new PlayerOperationCallbackDummy());
		sm.getSCIDraft().setSCIDraftOperationCallback(new DraftOperationCallback());
		sm.getSCIGame().setSCIGameOperationCallback(new GameOperationCallback());

		KingdominoApplication.setStatemachine(sm);
		
		// initialize the statemachine and enter it
		sm.init();
		sm.enter();

		// let's bring the statemachine to the first turn of the game
		int nbPlayers = KingdominoApplication.getKingdomino().getCurrentGame().getNumberOfPlayers();

		// Get to running state
		sm.getSCIDraft().raiseReady();

		for (int i = 0; i < nbPlayers; i++) {
			sm.getSCIDraft().raiseValidSelection();
		}
	}

	@Given("it is not the last turn of the game")
	public void itIsNotTheLastTurnOfTheGame() {
		// Do not need to do anything since we set isNextLast to false
	}

	@Given("the current player is not the last player in the turn")
	public void theCurrentPlayerIsNotTheLastPlayerInTheTurn() {
		// just entered state so we are not the last player in the turn
	}

	@Given("the current player is preplacing his\\/her domino with ID {int} at location {int}:{int} with direction {string}")
	public void preplace_domino(int id, int x, int y, String direction) {
		GameplayStatemachine sm = KingdominoApplication.getStatemachine();
		// Signal to the state machine that we are ready to begin the player's turn.
		sm.getSCIDraft().raiseReady();

		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player player = game.getNextPlayer();
		// Place this domino tentatively
		DominoInKingdom tentativeDomino = new DominoInKingdom(x, y, player.getKingdom(), getdominoByID(id));
		tentativeDomino.getDomino().setStatus(DominoStatus.ErroneouslyPreplaced);
		tentativeDomino.setDirection(getDirection(direction));

		// Get the stateMachine to moveDomino state
		DominoController.preplaceDomino(player, tentativeDomino, tentativeDomino.getX(), tentativeDomino.getY(), tentativeDomino.getDirection());
	}

	@And("the preplaced domino has the status {string}")
	public void preplacedDominoHasTheStatus(String status) {
		// doesn't need to check because it is a given
	}

	@When("the current player places his\\/her domino")
	public void currentPlayerPlacesDomino() {
		DominoController.placeDomino();
	}

	@Then("this player now shall be making his\\/her domino selection")
	public void playerMakingSelection() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		GameplayStatemachine sm = KingdominoApplication.getStatemachine();
		assertTrue(sm.isStateActive(GameplayStatemachine.State.main_region_Running_r1_PlayersTurn_r1_SelectingDomino));
	}

	@Given("the current player is the last player in the turn")
	public void currentPlayerIsTheLastPlayerInTurn() {
		// Bring statemachine to last player in turn
		// let's set him into a last player state
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		// Set the current player to the last player in the list
		while (game.getNextPlayer().hasNextPlayer())
		{
			game.setNextPlayer(game.getNextPlayer().getNextPlayer());
		}
	}
}
