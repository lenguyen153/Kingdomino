package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DiscardController;
import ca.mcgill.ecse223.kingdomino.controller.DominoController;
import ca.mcgill.ecse223.kingdomino.controller.DraftController;
import ca.mcgill.ecse223.kingdomino.features.PlacingDominoStepDefinition.PlayerOperationCallbackDummy;
import ca.mcgill.ecse223.kingdomino.features.PlacingDominoStepDefinition.TurnOperationCallbackDummy;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.DominoSelection;
import ca.mcgill.ecse223.kingdomino.model.Draft;
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
import static ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus.FaceDown;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
public class DiscardingDominoStepDefinition {
	
	class TurnOperationCallbackDummy extends TurnOperationCallback {

		@Override
		public boolean isLast() {
			// TODO Auto-generated method stub
			return false;
		}

	}

	@Given("the game is initialized for discarding domino")
	public void the_game_is_initialized_for_discarding_domino() {
		GameplayStatemachine sm = new GameplayStatemachine();

		sm.getSCITurn().setSCITurnOperationCallback(new TurnOperationCallbackDummy());
		sm.getSCIPlayer().setSCIPlayerOperationCallback(new PlayerOperationCallback());
		sm.getSCIDraft().setSCIDraftOperationCallback(new DraftOperationCallback());
		sm.getSCIGame().setSCIGameOperationCallback(new GameOperationCallback());

		KingdominoApplication.setStatemachine(sm);
		
		// initialize the statemachine and enter it
		sm.init();
		sm.enter();
		
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		
		sm.getSCIDraft().raiseReady();

		for (int i = 0; i < game.getNumberOfPlayers(); i++) {
			sm.getSCIDraft().raiseValidSelection();
		}

		// Hold the game in the initialize turn state so that we don't check discard
		// before we set the current player's domino selection
	}

	@Given("the current player has selected the domino with id {int}")
	public void currentPlayerHasSelectedDominoWithID(int id)
	{
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		// Delete this player's domino selection and give them a new one
		if (game.getNextPlayer().hasDominoSelection())
		{
			game.getNextPlayer().getDominoSelection().delete();
		}
		new DominoSelection(game.getNextPlayer(), getdominoByID(id), game.getCurrentDraft());

		// Now that the player has a domino selection, move to their turn.
		GameplayStatemachine sm = KingdominoApplication.getStatemachine();
		sm.getSCIDraft().raiseReady();
	}

	@And("it is impossible to place the current domino in his\\/her kingdom")
	public void it_is_impossible_to_place_the_current_domino_in_his_her_kingdom() {
	
	}

	@When("the current player discards his\\/her domino")
	public void the_current_player_discards_his_her_domino() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		DiscardController dc = new DiscardController();
		dc.doDiscard(game.getNextPlayer(), game.getNextPlayer().getDominoSelection().getDomino());
	}
}
