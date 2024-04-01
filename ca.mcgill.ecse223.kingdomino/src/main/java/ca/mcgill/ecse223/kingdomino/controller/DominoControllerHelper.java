package ca.mcgill.ecse223.kingdomino.controller;

import java.util.HashMap;

import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;

/**
 * @author Github: AdamMigliore
 */
public class DominoControllerHelper
{
	/**
	 * Check the adjacency between two coordinates provided in a HashMap with key "x" and "y"
	 * @author GitHub: AdamMigliore
	 * @param positionCheck:  the coordinates to check (the tile that will be placed)
	 * @param positionStatic: the coordinates to check in reference too (another tile that is static)
	 * @return true: there is adjacency between the two tiles; false: there is no adjacency between the two tiles
	 */
	public static boolean checkAdjacency(HashMap<String, Integer> positionCheck,
			HashMap<String, Integer> positionStatic) {
		/*
		 * adjacencyX_ 1 or 2: represent each part of the test condition for adjacency with a right/left situation	
		 * adjacencyY_ 1 or 2: represent each part of the test condition for adjacency with a up/down situation	
		 * testInX: merges both conditions
		 * testInY: merges both conditions
		 * 
		 * I used this set up for debugging mainly; however, I decided it makes much easy to correct as well
		 * 
		 * Note: the constant 1 is used to represent adjacency...read it as: their coordinates have a difference of one meaning they are adjacent
		 */
		boolean adjacencyX_1 = Math.abs((positionCheck.get("x")) - (positionStatic.get("x"))) == 1;
		boolean adjacencyX_2 = positionCheck.get("y").compareTo(positionStatic.get("y"))==0;
		boolean adjacencyY_1= Math.abs((positionCheck.get("y")) - (positionStatic.get("y"))) == 1;
		boolean adjacencyY_2 = positionCheck.get("x").compareTo(positionStatic.get("x"))==0;
		boolean testInX = adjacencyX_1 && adjacencyX_2;
		boolean testInY = adjacencyY_1 && adjacencyY_2;
		
		if (testInX || testInY) {
			return true;
		}
		return false;
	}

	/**
	 * Computes the endCoordinates of the given coordinates
	 * @author GitHub: AdamMigliore
	 * @param xPos:      the initial x position of the domino
	 * @param yPos:      the initial y position of the domino
	 * @param direction: the direction of the domino
	 * @return a x,y coordinate pairing of the end coordinates of the domino
	 */
	public static HashMap<String, Integer> calculateEndCoordinate(int xPos, int yPos, DirectionKind direction) {
		HashMap<String, Integer> endCoordinates = new HashMap<String, Integer>();

		switch (direction) {
		case Down:
			endCoordinates.put("x", xPos);
			endCoordinates.put("y", yPos - 1);
			break;
		case Left:
			endCoordinates.put("x", xPos - 1);
			endCoordinates.put("y", yPos);
			break;
		case Right:
			endCoordinates.put("x", xPos + 1);
			endCoordinates.put("y", yPos);
			break;
		case Up:
			endCoordinates.put("x", xPos);
			endCoordinates.put("y", yPos + 1);
			break;
		}
		return endCoordinates;
	}
}