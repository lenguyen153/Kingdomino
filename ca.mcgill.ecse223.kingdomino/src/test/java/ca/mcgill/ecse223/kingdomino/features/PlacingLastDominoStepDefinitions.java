package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.DraftOperationCallback;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.GameOperationCallback;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.GameplayStatemachine;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.PlayerOperationCallback;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.TurnOperationCallback;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

public class PlacingLastDominoStepDefinitions {
	/*
	 * Used to get the game up to state wanted
	 */
	class TurnOperationCallbackDummy extends TurnOperationCallback {

		@Override
		public boolean isLast() {
			// TODO Auto-generated method stub
			return true;
		}

	}

	class PlayerOperationCallbackDummy extends PlayerOperationCallback {
		@Override
		public boolean checkDiscard() {
			// TODO Auto-generated method stub
			return false;
		}
	}

	@Given("the game has been initialized for placing last domino")
	public void gameHasBeenInitalizedForPlacingLastDomino() {

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

	@Given("it is the last turn of the game")
	public void itIsTheLastTurnOfTheGame() {
		// Do not need to do anything since we set isNextLast to true

	}

	@Then("the next player shall be placing his\\/her domino")
	public void nextPlayerShallBePlacingDomino() {
		// The player can either be placing or discarding their domino, since these are two distinct states
		// in our model and not in the professors version.
		GameplayStatemachine sm = KingdominoApplication.getStatemachine();
		assertTrue(sm.isStateActive(
		GameplayStatemachine.State.main_region_Running_r1_PlayersTurn_r1_InitialPlacementOfDominoForCurrentPlayer) ||
				sm.isStateActive(GameplayStatemachine.State.main_region_Running_r1_PlayersTurn_r1_Discarding));
	}

	@Then("the game shall be finished")
	public void gameShallBeFinished() {
		GameplayStatemachine sm = KingdominoApplication.getStatemachine();
		assertTrue(sm.isStateActive(GameplayStatemachine.State.main_region_EndGame));
		assertNull(KingdominoApplication.getKingdomino().getCurrentGame());
	}

	@Then("the final results after successful placement shall be computed")
	public void finalResultsSuccessfulPlacementComputed() {
		Game game = KingdominoApplication.getKingdomino().getAllGame(0);
		// only player index 0 should have a score different than 0
		assertTrue(game.getNextPlayer().getTotalScore() != 0);
	}
}
