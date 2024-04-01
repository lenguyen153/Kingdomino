package ca.mcgill.ecse223.kingdomino.statemachine.gameplay;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DraftController;
import ca.mcgill.ecse223.kingdomino.controller.GameController;
import ca.mcgill.ecse223.kingdomino.controller.PileController;
import ca.mcgill.ecse223.kingdomino.controller.Utils;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;

import java.util.ArrayList;
import java.util.Arrays;

public class GameOperationCallback implements IGameplayStatemachine.SCIGameOperationCallback
{
    @Override
    public void create()
    {	//leave this as the tests require a different method from the real game
    	//Create game then set Users
        GameController.createGame(4, new ArrayList<>());
    	
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
    	
    	//list of temp userInfo
        // TODO: once the view is ready, remove this from here
    	ArrayList<String> nameList = new ArrayList<>(Arrays.asList("u1", "u2", "u3", "u4"));
        ArrayList<PlayerColor> colorList = new ArrayList<>(Arrays.asList(PlayerColor.values()));
        
        GameController.createUsers(nameList, colorList);
        Utils.createAllDominoes(game);
        PileController.shuffleDeck(true);
        game.setNextDraft(game.getCurrentDraft());
        DraftController.createNextDraft(game);
    }

	@Override
	public void end() {
		GameController.endGame();	
	}
	
	public class GameOperationCallbackDummy extends GameOperationCallback{
		 @Override
		    public void create()
		    {	
		    	//Create game with view input
		        GameController.createGameFromViewInput(4, new ArrayList<>());	    	
		        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		        Utils.createAllDominoes(game);
		        PileController.shuffleDeck(true);
		        game.setNextDraft(game.getCurrentDraft());
	//	        DraftController.createNextDraft(game);
		    }
	}
}
