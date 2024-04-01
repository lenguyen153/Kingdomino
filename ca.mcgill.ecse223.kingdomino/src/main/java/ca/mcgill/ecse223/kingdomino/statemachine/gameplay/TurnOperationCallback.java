package ca.mcgill.ecse223.kingdomino.statemachine.gameplay;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.GameController;
import ca.mcgill.ecse223.kingdomino.model.Game;

public class TurnOperationCallback implements IGameplayStatemachine.SCITurnOperationCallback
{
    @Override
    public void prepare()
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        GameController.nextPlayerOrder(game);
    }

	@Override
	public boolean isLast() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		if(game.getTopDominoInPile()==null&&game.getNextDraft()==null) {
			return true;
		}
		return false;
	}
}
