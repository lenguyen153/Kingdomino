package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.GameplayStatemachine;

import static ca.mcgill.ecse223.kingdomino.controller.Utils.*;

/**
 * Manages discard feature.
 * @author Roy Borsteinas
 */
public class DiscardController
{
    public DiscardController() {}

    /**
     * Will set the players domino selection to discard if there is no valid placement for this domino,
     * otherwise it will be set to erroneously preplaced.
     * This should be called before attempting to allow the player to select placement.
     * @param player player to check for discard.
     * @param domino the domino to check for valid placement.
     * @author Roy Borsteinas
     */
    public void doDiscard(Player player, Domino domino)
    {
        if (canPlace(player, domino))
            domino.setStatus(Domino.DominoStatus.ErroneouslyPreplaced);
        else
        {
            GameplayStatemachine sm = KingdominoApplication.getStatemachine();
            domino.setStatus(Domino.DominoStatus.Discarded);
            // This should wait for player input to confirm the discard once the view is created
            sm.getSCIPlayer().raiseDiscard();
        }
    }

    /**
     * Check if there exists a valid placement in the player's kingdom for the given domino.
     * @param player the player who is being checked.
     * @param domino the domino which is being checked.
     * @return returns true if the player should discard their currently selected domino. i.e. there
     * is no valid placement for it.
     * @author Roy Borsteinas
     */
    public boolean canPlace(Player player, Domino domino)
    {
        boolean[][] map = findEmpty(player.getKingdom());

        DominoInKingdom.DirectionKind[] directionKinds = {
                DominoInKingdom.DirectionKind.Up,
                DominoInKingdom.DirectionKind.Down,
                DominoInKingdom.DirectionKind.Left,
                DominoInKingdom.DirectionKind.Right };

        for (int x = -4; x < 5; ++x)
        {
            for (int y = -4; y < 5; ++y)
            {
                // Must adjust coordinates here to properly map to negative x,y pos
                if (map[x + 4][y + 4])
                {
                    for (DominoInKingdom.DirectionKind direction : directionKinds)
                    {
                        boolean canPlace = DominoController.checkPlacement(player.getKingdom(), domino.getId(), x, y, direction);
                        if (canPlace)
                        {
                            // If there is even a single position which works, can stop and return true
                            return true;
                        }
                    }
                }
            }
        }
        // There must be no position which works
        return false;
    }
}
