package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DominoController;
import ca.mcgill.ecse223.kingdomino.controller.DraftController;
import ca.mcgill.ecse223.kingdomino.controller.GameController;
import ca.mcgill.ecse223.kingdomino.controller.PileController;
import ca.mcgill.ecse223.kingdomino.model.Castle;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * 
 * @author Maxens Destine
 *
 */
public class StartANewGameStepDefinitions {

	
	@Given("the program is started and ready for starting a new game")
	public void the_program_is_started_and_ready_for_starting_a_new_game() {
		Kingdomino kingdomino = new Kingdomino();			
		KingdominoApplication.setKingdomino(kingdomino);
	}

	@Given("there are four selected players")
	public void there_are_four_selected_players() {
		//empty
	}

	@Given("bonus options Harmony and MiddleKingdom are selected")
	public void bonus_options_Harmony_and_MiddleKingdom_are_selected() {
		KingdominoApplication.getKingdomino().addBonusOption("Harmony");
	    KingdominoApplication.getKingdomino().addBonusOption("MiddleKingdom");
	 }

	@When("starting a new game is initiated")
	public void starting_a_new_game_is_initiated() {
		
		assertTrue(GameController.createGame(4, KingdominoApplication.getKingdomino().getBonusOptions()));
		TestUtils.createAllDominoes(KingdominoApplication.getKingdomino().getCurrentGame());
	   
	}

	@When("reveal first draft is initiated")
	public void reveal_first_draft_is_initiated() {
		Game game=KingdominoApplication.getKingdomino().getCurrentGame();
		game.setTopDominoInPile(game.getAllDominos().get(0));
		PileController.shuffleDeck();
	    DraftController.createNextDraft(game);
	    DraftController.orderDraft(game.getNextDraft());

	}

	@Then("all kingdoms shall be initialized with a single castle")
	public void all_kingdoms_shall_be_initialized_with_a_single_castle() {
		Game game=KingdominoApplication.getKingdomino().getCurrentGame();
		for(Player p:game.getPlayers()){
			assertTrue(p.getKingdom().getTerritories().size()==1);
		}
	}

	@Then("all castle are placed at {int}:{int} in their respective kingdoms")
	public void all_castle_are_placed_at_in_their_respective_kingdoms(Integer int1, Integer int2) {
		Game game=KingdominoApplication.getKingdomino().getCurrentGame();
		assertTrue(DominoController.verifyCastleAdjacency(1, 0, DirectionKind.Right));
		for(Player p:game.getPlayers()){
			assertTrue(DominoController.verifyCastleAdjacency(-1, 0, DirectionKind.Down));
			//we already proved there is only on territory (the castle)
			assertTrue(DominoController.verifyCastleAdjacency(-1, -1, DirectionKind.Up));
			assertTrue(p.getKingdom().getTerritory(0) instanceof Castle);
			assertEquals(0, p.getKingdom().getTerritory(0).getX());
			assertEquals(0, p.getKingdom().getTerritory(0).getY());
		}
	}

	@Then("the first draft of dominoes is revealed")
	public void the_first_draft_of_dominoes_is_revealed() {
		Game game=KingdominoApplication.getKingdomino().getCurrentGame();
		DraftController.revealNextDraft();
	
		game.setCurrentDraft(game.getNextDraft());
		assertEquals(game.getNextDraft().getDraftStatus(),DraftStatus.FaceUp);
	}

	@Then("all the dominoes form the first draft are facing up")
	public void all_the_dominoes_form_the_first_draft_are_facing_up() {
		Game game=KingdominoApplication.getKingdomino().getCurrentGame();
		assertEquals(game.getNextDraft().getDraftStatus(),DraftStatus.FaceUp);
	}

	@Then("all the players have no properties")
	public void all_the_players_have_no_properties() {
		Game game=KingdominoApplication.getKingdomino().getCurrentGame();
		for(Player p:game.getPlayers()){
			assertTrue(p.getKingdom().getProperties().isEmpty());
		}
	}

	@Then("all player scores are initialized to zero")
	public void all_player_scores_are_initialized_to_zero() {
		Game game=KingdominoApplication.getKingdomino().getCurrentGame();
		for(Player p:game.getPlayers()){
			assertTrue(p.getTotalScore()==0);
		}}
	
	
	
	public void createPlayers(){
		Game game=KingdominoApplication.getKingdomino().getCurrentGame();
		Player p1=new Player(game);
		p1.setColor(PlayerColor.Blue);
		Player p2=new Player(game);
		p2.setColor(PlayerColor.Green);
		Player p3=new Player(game);
		p3.setColor(PlayerColor.Pink);
		Player p4=new Player(game);
		p4.setColor(PlayerColor.Yellow);
		
		
	}
}
