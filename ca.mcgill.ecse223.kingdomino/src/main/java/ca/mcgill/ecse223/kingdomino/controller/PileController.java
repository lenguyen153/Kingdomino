package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;

import static ca.mcgill.ecse223.kingdomino.controller.Utils.*;
import java.util.*;

/**
 * Feature #4 & #5
 *
 * Browse & Shuffle Domino
 * @author Isabella Hao
 */
public class PileController
{

	public PileController()
	{
	}

	/**
	 * This method is part of Feature 4: Browse Domino Pile
	 * <p>
	 * Browse the set of all dominos in increasing order of numbers
	 * prior to playing the game
	 * so that the players can adjust their strategies
	 *
	 * @author Isabella Hao
	 */
	public static List<Domino> browseAllDominos()
	{
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Domino> result = new ArrayList<>(game.getAllDominos());

		result.sort(Comparator.comparingInt(Domino::getId));
		return result;
	}

	/**
	 * This method is part of Feature 4: Browse Domino Pile
	 * <p>
	 * Filter dominoes by terrain type
	 *
	 * @author Isabella Hao
	 */
	public static List<Domino> browseAllDominos(TerrainType terrain)
	{
		List<Domino> result = browseAllDominos();

		result.sort(Comparator.comparingInt(Domino::getId));
		result.removeIf(domino -> domino.getRightTile() != terrain && domino.getLeftTile() != terrain);

		return result;
	}

	/**
	 * This method is part of Feature 4: Browse Domino Pile
	 * <p>
	 * observe an individual domino
	 *
	 * @param id the id of the desired domino
	 * @author Isabella Hao
	 */
	public static Domino browseIndividualDomino(int id)
	{
		return getDominoByID(id);
	}

	/**
	 * Shuffle the deck of dominos randomly.
	 *
	 * This function will randomly arrange the domino pile linked list.
	 * It will also ensure that there are the correct number of dominos
	 * in the pile given a certain number of players.
	 * @author Isabella Hao
	 */
	public static void shuffleDeck()
	{
		shuffleDeck(true);
	}

	/**
	 * Adds the option to not create a first draft after arranging the deck
	 * @author Isabella Hao
	 */
	public static void shuffleDeck(boolean createDraft)
	{
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();

		List<Integer> dominoIdList = new ArrayList<>();

		for (Domino d : game.getAllDominos())
			dominoIdList.add(d.getId());

		Collections.shuffle(dominoIdList);

		fixedArrangeDeck(dominoIdList, createDraft);
	}

	/**
	 * Fixed arrange a deck of dominos and create the first draft.
	 * @param ids predefined order of dominos.
	 * @author Isabella Hao
	 */
	public static void fixedArrangeDeck(List<Integer> ids)
	{
		fixedArrangeDeck(ids, true);
	}

	/**
	 * Adds the option to not create a first draft after arranging the deck
	 * @author Roey Borsteinas
	 */
	public static void fixedArrangeDeck(List<Integer> ids, boolean createDraft)
	{
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();

		// Only use the first n dominos where n is the number of dominos to use for this many players.
		if (ids.size() > game.getMaxPileSize())
			ids = ids.subList(0, game.getMaxPileSize());

		Domino prev = null;
		for (Integer id : ids)
		{
			Domino d = getDominoByID(id);

			if (d.getStatus() != Domino.DominoStatus.InPile)
				continue;

			if (prev != null)
				prev.setNextDomino(d);
			d.setPrevDomino(prev);

			prev = d;
		}

		game.setTopDominoInPile(getDominoByID(ids.get(0)));

		if (createDraft)
			DraftController.createFirstDraft(game);
	}

	/**
	 * Return a list of dominoes which are ordered as they are in the pile.
	 * @author Isabella Hao
	 */
	public static List<Domino> getCurrentDrawPile(Game game)
	{
		List<Domino> drawPile = new ArrayList<>();

		Domino current = game.getTopDominoInPile();
		while (current != null)
		{
			drawPile.add(current);
			current = current.getNextDomino();
		}
		return drawPile;
	}
}
