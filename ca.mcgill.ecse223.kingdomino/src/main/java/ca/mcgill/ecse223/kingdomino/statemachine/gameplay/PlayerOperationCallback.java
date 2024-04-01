package ca.mcgill.ecse223.kingdomino.statemachine.gameplay;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DiscardController;
import ca.mcgill.ecse223.kingdomino.model.Game;

public class PlayerOperationCallback implements IGameplayStatemachine.SCIPlayerOperationCallback
{

    @Override
    public boolean isLast()
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        return game.getNextPlayer().getNextPlayer() == null;
    }

    @Override
    public void setToFirst()
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        while (game.getNextPlayer().hasPrevPlayer())
        {
            game.setNextPlayer(game.getNextPlayer().getPrevPlayer());
        }
    }

    @Override
    public void setNext()
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        game.setNextPlayer(game.getNextPlayer().getNextPlayer());
    }

    @Override
    public boolean checkDiscard()
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        return !(new DiscardController()).canPlace(game.getNextPlayer(), game.getNextPlayer().getDominoSelection().getDomino());
    }
}
