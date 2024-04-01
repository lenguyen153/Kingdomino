package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.*;
import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;
import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.CalculateScoreController;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdom;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * @author Vincent Trinh
 */
public class CalculateRankingStepDefinition {
	boolean hasTie=true;
	@Given("the game is initialized for calculate ranking")
	public void the_game_is_initialized_for_calculate_ranking() {
		initializeGame();
	}

	//not finished
	@Given("the players have no tiebreak")
	public void the_players_have_no_tiebreak() {
		// Write code here that turns the phrase above into concrete actions
		//no calculation has been done so they are not in a tiebreak
	}
	
	@When("calculate ranking is initiated")
	public void calculate_ranking_is_initiated() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for(Player p:game.getPlayers()) {
			CalculateScoreController.CalculatePlayerScore(p);
		}
		if(hasTie){
			CalculateScoreController.calculateRanking(1,game.getPlayers());
		}
		else{
			CalculateScoreController.calculateRankingNoTie(1, game.getPlayers());
		}
	}

	@Then("player standings shall be the followings:")
	public void player_standings_shall_be_the_followings(io.cucumber.datatable.DataTable dataTable) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();

		List<Map<String, String>> valueMaps = dataTable.asMaps();
		for (Map<String, String> map : valueMaps) {
			//check if the standings are equal to the real game
			assertEquals( Integer.parseInt(map.get("standing")), game.getPlayer(getPlayerIndex( getPlayerColor(map.get("player")))).getCurrentRanking());
		}
	}

	private int getPlayerIndex(PlayerColor color) {
		List<Player> myPlayers = KingdominoApplication.getKingdomino().getCurrentGame().getPlayers();

		for(int i =0; i< myPlayers.size();i++) {
			if(myPlayers.get(i).getColor().equals(color)) {
				return i;
			}
		}
		return -1;
	}
}
