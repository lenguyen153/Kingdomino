package ca.mcgill.ecse223.kingdomino.statemachine.gameplay;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DraftController;
import ca.mcgill.ecse223.kingdomino.model.Game;

public class DraftOperationCallback implements IGameplayStatemachine.SCIDraftOperationCallback
{
    @Override
    public void prepareFirst()
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        DraftController.orderDraft(game.getCurrentDraft());
    }

    @Override
    public void prepareNext()
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        DraftController.createNextDraft(game);
    }

    @Override
    public void revealFirst()
    {
        DraftController.revealFirstDraft();
    }

    @Override
    public void revealNext()
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        if(game.getNextDraft()!=null) {
        DraftController.orderDraft(game.getNextDraft());
        DraftController.revealNextDraft();
        }
    }
    
    public class DraftOperationCallbackDummy extends DraftOperationCallback{
		@Override
		public void prepareNext() {
			Game game=KingdominoApplication.getKingdomino().getCurrentGame();
			DraftController.createNextDraftDummy(game);
		}
	}
}
