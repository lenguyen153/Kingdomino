package ca.mcgill.ecse223.kingdomino.controller;

import java.util.HashMap;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.GameplayStatemachine;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.GameplayStatemachine.State;

import static ca.mcgill.ecse223.kingdomino.controller.Utils.*;
import static ca.mcgill.ecse223.kingdomino.controller.DominoControllerHelper.*;

/**
 * This controller should happen anything pertaining to the domino; - Placement
 * (F13) - Checking Placement (F14-15-16-17) - Moving (F11) - Rotation (F12) -
 * Discarding (F19)
 * 
 * @author Github: AdamMigliore
 * @author Annabelle Dion
 */
public class DominoController {

	public DominoController() {
	} // This should be empty in almost every single case.

	/**
	 * Feature 13 - PlaceDomino: As a player, I wish to place my selected domino to
	 * my kingdom. If I am satisfied with its placement, and its current position
	 * respects the adjacency rules, I wish to finalize the placement. (Actual
	 * checks of adjacency conditions are implemented as separate features)
	 * 
	 * checkPlacement: Checks if the domino can be correctly placed here
	 * 
	 * @author Github: AdamMigliore
	 * @param kingdom:   the kingdom to check the placement in
	 * @param id:        id of the domino to check
	 * @param xPos:      x position of the domino
	 * @param yPos:      y position of the domino
	 * @param direction: direction of the domino
	 * @return true: if domino is can be correctly preplaced here, false otherwise
	 */
	public static boolean checkPlacement(Kingdom kingdom, int id, int xPos, int yPos, DirectionKind direction) {
		return (verifyCastleAdjacency(xPos, yPos, direction)
				|| verifyNeighborAdjacency(kingdom, id, xPos, yPos, direction))
				&& verifyNoOverlapping(kingdom, xPos, yPos, direction)
				&& verifyGridSize(kingdom, xPos, yPos, direction);
	}

	/**
	 * Feature 13 - PlaceDomino: As a player, I wish to place my selected domino to
	 * my kingdom. If I am satisfied with its placement, and its current position
	 * respects the adjacency rules, I wish to finalize the placement. (Actual
	 * checks of conditions are implemented as separate features)
	 * 
	 * preplaceDomino: sets the domino to CORRECTLY PREPLACED if it passes all
	 * checks
	 * 
	 * @author Github: AdamMigliore
	 * @param player:          the player who is preplacing the domino
	 * @param dominoInKingdom: the domino to check if it is corrently placed
	 * @param xPos:            x position of the domino
	 * @param yPos:            y position of the domino
	 * @param direction:       direction of the domino
	 * @return true: if domino has been set to CorrectlyPreplaced; false: if domino
	 *         was set to ErroneouslyPreplaced
	 */
	public static boolean preplaceDomino(Player player, DominoInKingdom dominoInKingdom, int xPos, int yPos,
			DirectionKind direction) {
		if (checkPlacement(player.getKingdom(), dominoInKingdom.getDomino().getId(), xPos, yPos, direction)) {
			dominoInKingdom.setX(xPos);
			dominoInKingdom.setY(yPos);
			dominoInKingdom.setDirection(direction);

			dominoInKingdom.getDomino().setStatus(DominoStatus.CorrectlyPreplaced);
			
			//Raise the statemachine
			GameplayStatemachine sm = KingdominoApplication.getStatemachine();
			sm.getSCITurn().raisePreplaceDomino();
			
			return true;
		} else {
			return false;
		}
	}
	
	
	public static boolean preplaceDominoDummy(Player player, DominoInKingdom dominoInKingdom, int xPos, int yPos,
			DirectionKind direction) {
		GameplayStatemachine sm = KingdominoApplication.getStatemachine();
		if (checkPlacement(player.getKingdom(), dominoInKingdom.getDomino().getId(), xPos, yPos, direction)) {
			dominoInKingdom.setX(xPos);
			dominoInKingdom.setY(yPos);
			dominoInKingdom.setDirection(direction);

			dominoInKingdom.getDomino().setStatus(DominoStatus.CorrectlyPreplaced);
			
			//Raise the statemachine
			sm.getSCITurn().raisePreplaceDomino();
			
			return true;
		} else {//raise wheter it works or not
			sm.getSCITurn().raisePreplaceDomino();
			return false;
		}
	}
	
	/**
	 * place domino in kingdom and raise statemachine event
	 */
	public static void placeDomino() {
		GameplayStatemachine sm = KingdominoApplication.getStatemachine();
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		
		DominoInKingdom preplacedDomino = findPreplacedDomino(game.getNextPlayer().getKingdom());
		
		if(sm.isStateActive(GameplayStatemachine.State.main_region_Running_r1_PlayersTurn_r1_MovingPreplacedDomino) && placeDomino(preplacedDomino.getDomino().getId())) {
			// Update the player's score
			CalculateScoreController.CalculatePlayerScore(game.getNextPlayer());
			sm.getSCITurn().raiseValidPlacement();
		}
	}

	/**
	 * Feature 13 - PlaceDomino: As a player, I wish to place my selected domino to
	 * my kingdom. If I am satisfied with its placement, and its current position
	 * respects the adjacency rules, I wish to finalize the placement. (Actual
	 * checks of adjacency conditions are implemented as separate features)
	 * 
	 * placeDomino: if the domino is in status CORRECTLY PREPLACED then add it to
	 * the kingdom
	 * 
	 * @author Github: AdamMigliore
	 * @param id: the id of the domino to place
	 * @return true: domino was correctly added to the kindom; false: domino could
	 *         not be added to the kingdom
	 */
	public static boolean placeDomino(int id) {
		// we have the player; we can add a domino if all the conditions are met
		// there is some kind of tile adjacency and no overlapping and the domino fits
		// in the grid
		Domino domino = getDominoByID(id);

		if (domino.getStatus().equals(DominoStatus.CorrectlyPreplaced)) {
			domino.setStatus(DominoStatus.PlacedInKingdom);
			return true;
		}
		return false;
	}

	/**
	 * Verifies that a kingdom will maintain valid size after a new domino is
	 * placed. Pass x = 0, y = 0, direction = null to this to check if the kingdom
	 * is valid in it's current state
	 * 
	 * @author Github: roeyb1
	 * @param kingdom the kingdom to check
	 * @param newX    x position of the new domino
	 * @param newY    y position of the new domino
	 * @return true if the new domino can be placed in this kingdom, false if not.
	 */
	public static boolean verifyGridSize(Kingdom kingdom, int newX, int newY, DirectionKind directionKind) {
		boolean[][] map = findEmpty(kingdom);
		int adjustedX = newX + 4;
		int adjustedY = newY + 4;
		int leftX = adjustedX;
		int leftY = adjustedY;

		map[adjustedX][adjustedY] = false;

		if (directionKind != null) {
			switch (directionKind) {
			case Up:
				leftY += 1;
				break;
			case Down:
				leftY -= 1;
				break;
			case Right:
				leftX += 1;
				break;
			case Left:
				leftX -= 1;
				break;
			default:
				break;
			}
		}

		// If would overlap the sizes of the kingdom
		if (leftX >= 9 || leftX < 0)
			return false;
		if (leftY >= 9 || leftY < 0)
			return false;

		map[leftX][leftY] = false;

		int minX = 0;
		int maxX = 0;
		int minY = 0;
		int maxY = 0;

		for (int x = -4; x < 5; ++x) {
			for (int y = -4; y < 5; ++y) {
				// if it's true here, its an empty spot so ignore it
				if (map[x + 4][y + 4])
					continue;
				if (x < minX)
					minX = x;
				if (x > maxX)
					maxX = x;
				if (y < minY)
					minY = y;
				if (y > maxY)
					maxY = y;
			}
		}

		if (maxX - minX > 4)
			return false;
		if (maxY - minY > 4)
			return false;
		return true;
	}

	/**
	 * Verify if a kingdom is valid in its current state
	 * 
	 * @param kingdom kingdom to check
	 * @return valid or not
	 * @author Roey Borsteinas
	 */
	public static boolean verifyGridSize(Kingdom kingdom) {
		return verifyGridSize(kingdom, 0, 0, null);
	}

	/**
	 * Feature 14 - VerifyCastleAdjacency: As a player, I want the Kingdomino app to
	 * automatically check if my current domino is placed next to my castle.
	 * 
	 * @author Github: AdamMigliore
	 * @param xPos:      x position of the domino
	 * @param yPos:      y position of the domino
	 * @param direction: direction of the domino
	 * @return true: there is castle adjacency; false: there is no catle adjacency
	 */
	public static boolean verifyCastleAdjacency(int xPos, int yPos, DirectionKind direction) {
		final int CASTLE_X = 0;
		final int CASTLE_Y = 0;
		// We want to check that the x or y tile for each tile of the territory is next
		// to the castle --> (Left or Right tile is adjacent) and (no tile = castle)

		// Step 1: Get the castle object
		// Actually not necessary because the all dominos are relative to the castle
		// which is always @ (0,0)

		// Step 2: Get the end coordinates of the tile
		HashMap<String, Integer> startCoordinates = new HashMap<String, Integer>();
		startCoordinates.put("x", xPos);
		startCoordinates.put("y", yPos);

		HashMap<String, Integer> endCoordinates = calculateEndCoordinate(xPos, yPos, direction);

		// We need to check the interval [xPos,xPos+DirectionModifier] and [yPos,
		// yPos+DirectionModifier]
		// Step 3: check if the tile will be within 1 unit of the castle object and that
		// it is not on the castle object

		HashMap<String, Integer> castleCoordinates = new HashMap<String, Integer>();
		castleCoordinates.put("x", CASTLE_X);
		castleCoordinates.put("y", CASTLE_Y);

		if (!(startCoordinates.equals(castleCoordinates) || endCoordinates.equals(castleCoordinates))
				&& (checkAdjacency(startCoordinates, castleCoordinates)
						|| checkAdjacency(endCoordinates, castleCoordinates))) {
			return true;
		}

		// We want to prove the adjacency is true
		return false;
	}
	
	/**
	 * Verify that the domino is not placed on the castle
	 */
	public static boolean notCastleOverlap(int xPos, int yPos, DirectionKind direction) {
		final int CASTLE_X = 0;
		final int CASTLE_Y = 0;
		
		HashMap<String, Integer> startCoordinates = new HashMap<String, Integer>();
		startCoordinates.put("x", xPos);
		startCoordinates.put("y", yPos);
		
		HashMap<String, Integer> endCoordinates = calculateEndCoordinate(xPos, yPos, direction);

		HashMap<String, Integer> castleCoordinates = new HashMap<String, Integer>();
		castleCoordinates.put("x", CASTLE_X);
		castleCoordinates.put("y", CASTLE_Y);
		
		if (!(startCoordinates.equals(castleCoordinates)||endCoordinates.equals(castleCoordinates))) {
			return true;
		}

		// We want to prove the overlap is false
		return false;
	}


	/**
	 * Feature 15 - VerifyNeighborAdjacency: As a player, I want the Kingdomino app
	 * to automatically check if my current domino is placed to an adjacent
	 * territory.
	 * 
	 * @author Github: AdamMigliore
	 * @param kingdom:   the kingdom to check the placement in
	 * @param xPos:      x position of the domino
	 * @param yPos:      y position of the domino
	 * @param direction: direction of the domino
	 * @return true: there is neighbor adjacency; false: there is no neighboring
	 *         adjacency
	 */
	public static boolean verifyNeighborAdjacency(Kingdom kingdom, int id, int xPos, int yPos,
			DirectionKind direction) {
		Domino myDomino = getDominoByID(id);

		HashMap<String, Integer> startCoordinates = new HashMap<String, Integer>();
		startCoordinates.put("x", xPos);
		startCoordinates.put("y", yPos);
		HashMap<String, Integer> endCoordinates = calculateEndCoordinate(xPos, yPos, direction);

		for (KingdomTerritory territory : kingdom.getTerritories()) {
			// it makes the difference between a domino and a castle
			if (territory instanceof DominoInKingdom
					&& ((DominoInKingdom) territory).getDomino().getStatus().equals(DominoStatus.PlacedInKingdom)) {
				// check for each end if it is adjacent to a square and of same type
				// we can re use the same code from the No Overlapping but just change the
				// condition for the return

				// Make sure not to check against preplaced dominos
				DominoInKingdom dominoInKingdom = (DominoInKingdom) territory;

				HashMap<String, Integer> terrStartCoords = new HashMap<String, Integer>();
				terrStartCoords.put("x", dominoInKingdom.getX());
				terrStartCoords.put("y", dominoInKingdom.getY());
				HashMap<String, Integer> terrEndCoords = calculateEndCoordinate(dominoInKingdom.getX(),
						dominoInKingdom.getY(), dominoInKingdom.getDirection());

				if ((checkAdjacency(startCoordinates, terrStartCoords)
						&& (myDomino.getLeftTile() == dominoInKingdom.getDomino().getLeftTile()))
						|| (checkAdjacency(startCoordinates, terrEndCoords)
								&& (myDomino.getLeftTile() == dominoInKingdom.getDomino().getRightTile()))
						|| (checkAdjacency(endCoordinates, terrStartCoords)
								&& (myDomino.getRightTile() == dominoInKingdom.getDomino().getLeftTile()))
						|| (checkAdjacency(endCoordinates, terrEndCoords)
								&& (myDomino.getRightTile() == dominoInKingdom.getDomino().getRightTile()))) {
					return true;
				}
			}
		}
		// we want to prove that there is an adjacent one
		return false;

	}

	/**
	 * Feature 16 - VerifyNoOverlapping: As a player, I want the Kingdomino app to
	 * automatically check that my current domino is not overlapping with existing
	 * dominos.
	 * 
	 * @author Github: AdamMigliore
	 * @param kingdom:   the kingdom to check the placement in
	 * @param xPos:      x position of the domino
	 * @param yPos:      y position of the domino
	 * @param direction: direction of the domino
	 * @return true: no overlapping; false: there is overlapping
	 */
	public static boolean verifyNoOverlapping(Kingdom kingdom, int xPos, int yPos, DirectionKind direction) {
		/*
		 * We want to check if the endCoordinates or startingCoordinates are inside of
		 * any tile Solution: We calculate endCoordinates and startingCoordinates and
		 * check equality for all the territories in the kingdom
		 */

		// We put the coordinates in a hashmap to facilitate checking the equality
		HashMap<String, Integer> startCoords = new HashMap<String, Integer>();
		startCoords.put("x", xPos);
		startCoords.put("y", yPos);
		HashMap<String, Integer> endCoords = calculateEndCoordinate(xPos, yPos, direction);

		// check if getting correctly
		// for each territory in our kingdom we want to check if any of the tiles are
		// overlapping
		for (KingdomTerritory territory : kingdom.getTerritories()) {
			if (territory instanceof DominoInKingdom
					&& ((DominoInKingdom) territory).getDomino().getStatus().equals(DominoStatus.PlacedInKingdom)) {
				HashMap<String, Integer> terrStartCoords = new HashMap<String, Integer>();
				terrStartCoords.put("x", territory.getX());
				terrStartCoords.put("y", territory.getY());
				HashMap<String, Integer> terrEndCoords = calculateEndCoordinate(territory.getX(), territory.getY(),
						((DominoInKingdom) territory).getDirection());

				// There are 4 situations:
				// [p1.1 overlaps (p2.1 or p2.2)] or [p1.2 overlaps (p2.1 or p2.2)]
				// p1: to place territory; p2: placed territory; .1: starting tile; .2: ending
				// tile
				if ((startCoords.equals(terrStartCoords)) || (startCoords.equals(terrEndCoords))
						|| (endCoords.equals(terrStartCoords)) || (endCoords.equals(terrEndCoords))) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Adds a new domino the given kingdom if it's status is not set to discard,
	 * otherwise do nothing. This method will set it to the default position and
	 * direction which are guaranteed to be erroneous.
	 * 
	 * @author Roey Borsteinas
	 * @param kingdom kingdom to place the domino as a territory
	 * @param domino  domino to create territory with
	 */
	public static void addTerritoryToKingdom(Kingdom kingdom, Domino domino) {
		if (domino.getStatus() != DominoStatus.Discarded) {
			DominoInKingdom dominoInKingdom = new DominoInKingdom(0, 0, kingdom, domino);
			dominoInKingdom.setDirection(DirectionKind.Up);
			dominoInKingdom.getDomino().setStatus(DominoStatus.ErroneouslyPreplaced);
		}
	}

	/**
	 * @author AdamMigliore
	 * @param kingdom kingdom to search into
	 * @return DominoInKingdom that is currently being preplaced, i.e.
	 *         CorrectlyPreplaced or ErroneouslyPreplaced
	 */
	public static DominoInKingdom findPreplacedDomino(Kingdom kingdom) {
		for (KingdomTerritory kingdomTerritory : kingdom.getTerritories()) {
			if (kingdomTerritory instanceof DominoInKingdom) {
				DominoInKingdom dominoInKingdom = (DominoInKingdom) kingdomTerritory;
				if ((dominoInKingdom.getDomino().getStatus() == DominoStatus.ErroneouslyPreplaced)
						|| (dominoInKingdom.getDomino().getStatus() == DominoStatus.CorrectlyPreplaced))
					return dominoInKingdom;
			}
		}
		return null;
	}
}
