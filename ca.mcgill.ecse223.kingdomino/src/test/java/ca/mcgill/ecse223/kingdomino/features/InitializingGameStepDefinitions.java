package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DraftController;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Draft;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * 
 * @author Maxens Destine
 *
 */
public class InitializingGameStepDefinitions {
	
	@Given("the game has not been started")
	public void the_game_has_not_been_started() {
		 GameplayStatemachine sm = new GameplayStatemachine();
	        sm.getSCITurn().setSCITurnOperationCallback(new TurnOperationCallback());
	        sm.getSCIPlayer().setSCIPlayerOperationCallback(new PlayerOperationCallback());
	        sm.getSCIDraft().setSCIDraftOperationCallback(new DraftOperationCallback());
	        sm.getSCIGame().setSCIGameOperationCallback(new GameOperationCallback());
	        KingdominoApplication.setStatemachine(sm); 
	        sm.init();
	        sm.enter();
	}

	@When("start of the game is initiated")
	public void start_of_the_game_is_initiated() {
		GameplayStatemachine sm = KingdominoApplication.getStatemachine();
		DraftController.readyFirstDraft();
	}

	@Then("the pile shall be shuffled")
	public void the_pile_shall_be_shuffled() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		boolean notInOrder=false;		
		//if one of the domino does not have his "natural" next as his next domino (the next of 1 is 2, the next of 13 is 14, etc.)
		//than the pile can be considered as shuffled		
		for(Domino d:game.getAllDominos()){
			if(d!=null&&d.hasNextDomino()&&(d.getNextDomino().getId()-d.getId()!=1)){
				notInOrder=true;
				break;
			}			
		}
		assertTrue(notInOrder);	
	}
	
	

	@Then("the first draft shall be on the table")
	public void the_first_draft_shall_be_on_the_table() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();		
		int pileSize=1;
		Domino d=game.getTopDominoInPile();
		while(d.hasNextDomino()) {
			pileSize++;
			d=d.getNextDomino();
		}
		//first and second draft are on the table, so there are 40 dominos left, and 2 drafts in total
		assertEquals(2,game.getAllDrafts().size());
		assertEquals(40,pileSize);				
	}

	@Then("the first draft shall be revealed")
	public void the_first_draft_shall_be_revealed() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		assertEquals(DraftStatus.FaceUp,game.getCurrentDraft().getDraftStatus());
	}

	@Then("the initial order of players shall be determined")
	public void the_initial_order_of_players_shall_be_determined() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Player first=game.getNextPlayer();

		int i=1;
		while(first.hasNextPlayer()){
			first=first.getNextPlayer();
			i++;			
		}
		assertEquals(game.getNumberOfPlayers(),i);	
	}

	@Then("the first player shall be selecting his\\/her first domino of the game")
	public void the_first_player_shall_be_selecting_his_her_first_domino_of_the_game() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		GameplayStatemachine sm = KingdominoApplication.getStatemachine();
		assertTrue(sm.isStateActive(GameplayStatemachine.State.main_region_Initializing_r1_SelectingFirstDomino));
	}

	@Then("the second draft shall be on the table, face down")
	public void the_second_draft_shall_be_on_the_table_face_down() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		Draft draft = game.getNextDraft();
		assertEquals(DraftStatus.FaceDown,draft.getDraftStatus());
	}
}
