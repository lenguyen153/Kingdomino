package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.GameplayStatemachine;

import static ca.mcgill.ecse223.kingdomino.controller.Utils.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * In charge of revealing the next draft of dominos
 * @author Annabelle Dion
 */
public class DraftController
{

    public DraftController() { }

    /**
     * Set the status of the next draft to face up so it is revealed to the players.
     * @author Annabelle Dion
     */
    public static void revealNextDraft()
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        if (game.getNextDraft().getDraftStatus() != Draft.DraftStatus.Sorted)
            throw new IllegalStateException("Cannot reveal a draft which is not sorted");
        game.getNextDraft().setDraftStatus(Draft.DraftStatus.FaceUp);
    }

    public static void revealFirstDraft()
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        if (game.getCurrentDraft().getDraftStatus() != Draft.DraftStatus.Sorted)
            throw new IllegalStateException("Cannot reveal a draft which is not sorted");
        game.getCurrentDraft().setDraftStatus(Draft.DraftStatus.FaceUp);
    }
    /**
     * Create the first draft in the game.
     * @author Annabelle Dion
     */
    public static void createFirstDraft(Game game)
    {
        Draft firstDraft = new Draft(Draft.DraftStatus.FaceDown, game);

        int draftNumber = 4;
        if (game.getNumberOfPlayers() == 3)
            draftNumber = 3;

        for (int i = 0; i < draftNumber; ++i)
        {
            Domino top = game.getTopDominoInPile();

            firstDraft.addIdSortedDomino(top);

            game.setTopDominoInPile(top.getNextDomino());
        }

        game.setCurrentDraft(firstDraft);
    }

    /**
     * Create a new nextDraft.
     * @author Annabelle Dion
     */
    public static void createNextDraft(Game game)
    {
        Draft newDraft = new Draft(Draft.DraftStatus.FaceDown, game);

        int numDomsInDraft = 4;
        if (game.getNumberOfPlayers() == 3)
            numDomsInDraft = 3;

        for (int i = 0; i < numDomsInDraft; ++i)
        {
            Domino topD = game.getTopDominoInPile();

            // If at any point we run out of dominos in the pile, next draft is invalid
            if (topD == null || topD.getStatus() == Domino.DominoStatus.Discarded)
            {
                game.setTopDominoInPile(null);
                newDraft = null;
                break;
            }
            newDraft.addIdSortedDomino(topD);

            game.setTopDominoInPile(topD.getNextDomino());
            topD.setNextDomino(null);
            topD.setPrevDomino(null);
        }

        if (game.getCurrentDraft() != null)
            game.removeAllDraft(game.getCurrentDraft());
        game.setCurrentDraft(game.getNextDraft());
        game.setNextDraft(newDraft);
    }
    
    public static void createNextDraftDummy(Game game)
    {
        Draft newDraft = new Draft(Draft.DraftStatus.FaceDown, game);

        int numDomsInDraft = 4;
        if (game.getNumberOfPlayers() == 3)
            numDomsInDraft = 3;

        for (int i = 0; i < numDomsInDraft; ++i)
        {
            Domino topD = game.getTopDominoInPile();

            // If at any point we run out of dominos in the pile, next draft is invalid
            if (topD == null || topD.getStatus() == Domino.DominoStatus.Discarded)
            {
                game.setTopDominoInPile(null);
                newDraft = null;
                break;
            }
            newDraft.addIdSortedDomino(topD);

            game.setTopDominoInPile(topD.getNextDomino());
            topD.setNextDomino(null);
            topD.setPrevDomino(null);
        }

        if (game.getCurrentDraft() != null)
            game.removeAllDraft(game.getCurrentDraft());
        game.setCurrentDraft(game.getNextDraft());
        game.setNextDraft(newDraft);
        GameplayStatemachine sm = KingdominoApplication.getStatemachine();

        if(sm.isStateActive(GameplayStatemachine.State.main_region_Running_r1_InitializeTurn)) {
    		sm.getSCIDraft().raiseReady();
    		}
    	}

    
    /**
     * raises ready on SCIDraft for creatingFirstDraft state
     */
    public static void readyFirstDraft() {
    	GameplayStatemachine sm = KingdominoApplication.getStatemachine();
    	
    	if(sm.isStateActive(GameplayStatemachine.State.main_region_Initializing_r1_CreatingFirstDraft)) {
    		sm.getSCIDraft().raiseReady();
    		}
    	}
    
    /**
     * Raises ready on SCIDraft for selectingDomino state
     */
    public static void readyLastDraft() {
    	GameplayStatemachine sm = KingdominoApplication.getStatemachine();
    	
    	if(sm.isStateActive(GameplayStatemachine.State.main_region_Running_r1_InitializeTurn)) {
    		sm.getSCIDraft().raiseReady();
    		}
    	}

    /**
     * Order a given draft in increasing domino order
     * @param draft draft to order
     * @author Annabelle Dion
     */
    public static void orderDraft(Draft draft)
    {
        List<Domino> dominoList = new ArrayList<>(draft.getIdSortedDominos());

        dominoList.sort(Comparator.comparingInt(Domino::getId));

        // Remove all dominoes from the list to rebuild it
        draft.setIdSortedDominos();

        for (Domino domino : dominoList)
        {
            draft.addIdSortedDomino(domino);
        }

        draft.setDraftStatus(Draft.DraftStatus.Sorted);
    }

    public static void chooseDomino(int dominoId)
    {
        GameplayStatemachine sm = KingdominoApplication.getStatemachine();

        boolean res = false;
        // If in the initializing step, select from current draft since there is no next draft
        if (sm.isStateActive(GameplayStatemachine.State.main_region_Initializing_r1_SelectingFirstDomino))
            res = chooseFirstDomino(dominoId);
        else if(sm.isStateActive(GameplayStatemachine.State.main_region_Running_r1_PlayersTurn_r1_SelectingDomino))
            // otherwise select from the next draft
            res = chooseNextDomino(dominoId);

        if (res) sm.getSCIDraft().raiseValidSelection();
    }
    
    public static void chooseDominoDummy(int dominoId)
    {
        GameplayStatemachine sm = KingdominoApplication.getStatemachine();

        boolean res = false;
        // If in the initializing step, select from current draft since there is no next draft
        if (sm.isStateActive(GameplayStatemachine.State.main_region_Initializing_r1_SelectingFirstDomino))
            res = chooseFirstDomino(dominoId);
        else if(sm.isStateActive(GameplayStatemachine.State.main_region_Running_r1_PlayersTurn_r1_SelectingDomino))
            // otherwise select from the next draft
            res = chooseFirstDomino(dominoId);
        
        if (res) sm.getSCIDraft().raiseValidSelection();
    }

    /**
     * The current player selects domino with id dominoId.
     * @author Annabelle Dion
     */
    public static boolean chooseNextDomino(int dominoId)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        return chooseDominoFromDraft(dominoId, game.getNextDraft());
    }

    public static boolean chooseFirstDomino(int dominoId)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        return chooseDominoFromDraft(dominoId, game.getCurrentDraft());
    }

    private static boolean chooseDominoFromDraft(int dominoId, Draft draft)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        if (draft != null &&
                !game.getNextPlayer().hasDominoSelection() &&
                !getDominoByID(dominoId).hasDominoSelection())
        {
            new DominoSelection(game.getNextPlayer(), getDominoByID(dominoId), draft);
            return true;
        }

        return false;
    }


}
