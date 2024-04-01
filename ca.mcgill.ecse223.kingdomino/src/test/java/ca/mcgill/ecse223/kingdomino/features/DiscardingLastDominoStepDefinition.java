package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DiscardController;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.DraftOperationCallback;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.GameOperationCallback;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.GameplayStatemachine;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.PlayerOperationCallback;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.TurnOperationCallback;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import static org.junit.Assert.*;

public class DiscardingLastDominoStepDefinition {
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
		 	//Because of our design decision, a domino is discarded without preplacing it
	        return true;
	    }

	}
	@Given("the game is initialized for discarding last domino")
	public void the_game_is_initialized_for_discarding_last_domino() {
		GameplayStatemachine sm = new GameplayStatemachine();

		sm.getSCITurn().setSCITurnOperationCallback(new TurnOperationCallbackDummy());
		sm.getSCIPlayer().setSCIPlayerOperationCallback(new PlayerOperationCallbackDummy());
		sm.getSCIDraft().setSCIDraftOperationCallback(new DraftOperationCallback());
		sm.getSCIGame().setSCIGameOperationCallback(new GameOperationCallback());

		KingdominoApplication.setStatemachine(sm);
		
		// initialize the statemachine and enter it
		sm.init();
		sm.enter();
		
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		
		sm.getSCIDraft().raiseReady();

		for (int i = 0; i < game.getNumberOfPlayers(); i++)
		{
			sm.getSCIDraft().raiseValidSelection();
		}
		// Hold here to set player's domino selection before initiating their turn.
	}

	@Then("the final results after discard shall be computed")
	public void the_final_results_after_discard_shall_be_computed() {
		GameplayStatemachine sm=KingdominoApplication.getStatemachine();
		sm.getSCIPlayer().raiseDiscard();
		sm.runCycle();
		Game game = KingdominoApplication.getKingdomino().getAllGame(0);
		assertTrue(game.getNextPlayer().getTotalScore() != 0);
	}
}
