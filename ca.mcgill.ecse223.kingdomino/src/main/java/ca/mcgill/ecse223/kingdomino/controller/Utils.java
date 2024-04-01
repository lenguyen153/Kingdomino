package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Static class with utility methods used among multiple controllers.
 * 
 * @author Roey Borsteinas
 */
public class Utils {
	/**
	 * Find all empty spaces in a player's kingdom.
	 * 
	 * @return a boolean map where true means an empty space, false means filled.
	 * @author Roey Borsteinas
	 */
	public static boolean[][] findEmpty(Kingdom kingdom) {
		// Strategy to find all empty tiles will be to:
		// 1. initialize a grid for all coordinates
		// 2. iterate over all territories in the player's kingdom
		// 3. if there is a territory at this spot, set it to false in the grid
		// 4. return the resulting grid

		// Cells in this grid false represent used spaces
		boolean[][] result = new boolean[9][9];

		// 1. initialize with all possible coordinates in the grid
		// must add + 4 to all indices so that it starts at 0 (can't have negative array
		// index)
		for (int i = -4; i < 5; ++i) {
			for (int j = -4; j < 5; ++j) {
				result[i + 4][j + 4] = true;
			}
		}

		for (KingdomTerritory territory : kingdom.getTerritories()) {
			int adjustedX = territory.getX() + 4;
			int adjustedY = territory.getY() + 4;
			result[adjustedX][adjustedY] = false;

			int leftX = adjustedX;
			int leftY = adjustedY;

			// If it's a domino, make sure to set the second tile to false as well
			if (territory instanceof DominoInKingdom) {
				DominoInKingdom dominoInKingdom = (DominoInKingdom) territory;
				switch (dominoInKingdom.getDirection()) {
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
				if (leftX >= 9 || leftX < 0)
					continue;
				if (leftY >= 9 || leftY < 0)
					continue;
				result[leftX][leftY] = false;
			}
		}
		return result;
	}

	/**
	 * This function will return a 2d map of all the terrain types
	 * 
	 * @param kingdom kingdom to map
	 * @return 2d map of terrain types
	 * @author Roey Borsteinas
	 */
	public static TerrainType[][] getTerrainMap(Kingdom kingdom) {
		TerrainType[][] result = new TerrainType[9][9];

		// 1. initialize with all possible coordinates in the grid
		// must add + 4 to all indices so that it starts at 0 (can't have negative array
		// index)
		for (int i = -4; i < 5; ++i) {
			for (int j = -4; j < 5; ++j) {
				result[i + 4][j + 4] = null;
			}
		}

		for (KingdomTerritory territory : kingdom.getTerritories()) {
			if (territory instanceof Castle)
				continue;
			DominoInKingdom dominoInKingdom = (DominoInKingdom) territory;
			int adjustedX = territory.getX() + 4;
			int adjustedY = territory.getY() + 4;
			result[adjustedX][adjustedY] = dominoInKingdom.getDomino().getLeftTile();

			switch (dominoInKingdom.getDirection()) {
			case Up:
				result[adjustedX][adjustedY + 1] = dominoInKingdom.getDomino().getRightTile();
				break;
			case Down:
				result[adjustedX][adjustedY - 1] = dominoInKingdom.getDomino().getRightTile();
				break;
			case Left:
				result[adjustedX - 1][adjustedY] = dominoInKingdom.getDomino().getRightTile();
				break;
			case Right:
				result[adjustedX + 1][adjustedY] = dominoInKingdom.getDomino().getRightTile();
				break;
			default:
				break;
			}
		}
		return result;
	}

	/**
	 * This function will return a 2d map of all the dominos in the kingdom at their
	 * respective positions on the world grid.
	 * 
	 * @author Roey Borsteinas
	 */
	public static DominoInKingdom[][] getDominoMap(Kingdom kingdom) {
		DominoInKingdom[][] result = new DominoInKingdom[9][9];

		// 1. initialize with all possible coordinates in the grid
		// must add + 4 to all indices so that it starts at 0 (can't have negative array
		// index)
		for (int i = -4; i < 5; ++i) {
			for (int j = -4; j < 5; ++j) {
				result[i + 4][j + 4] = null;
			}
		}

		for (KingdomTerritory territory : kingdom.getTerritories()) {
			if (territory instanceof Castle)
				continue;
			DominoInKingdom dominoInKingdom = (DominoInKingdom) territory;
			if (dominoInKingdom.getDomino().getStatus() != Domino.DominoStatus.PlacedInKingdom)
				continue;
			int adjustedX = territory.getX() + 4;
			int adjustedY = territory.getY() + 4;
			result[adjustedX][adjustedY] = dominoInKingdom;

			switch (dominoInKingdom.getDirection()) {
			case Up:
				result[adjustedX][adjustedY + 1] = dominoInKingdom;
				break;
			case Down:
				result[adjustedX][adjustedY - 1] = dominoInKingdom;
				break;
			case Left:
				result[adjustedX - 1][adjustedY] = dominoInKingdom;
				break;
			case Right:
				result[adjustedX + 1][adjustedY] = dominoInKingdom;
				break;
			default:
				break;
			}
		}
		return result;
	}

	/**
	 * Returns a player color from a given string.
	 */
	public static Player.PlayerColor getPlayerColor(String color) {
		switch (color) {
		case "Blue":
			return Player.PlayerColor.Blue;
		case "Green":
			return Player.PlayerColor.Green;
		case "Pink":
			return Player.PlayerColor.Pink;
		case "Yellow":
			return Player.PlayerColor.Yellow;
		default:
			throw new java.lang.IllegalArgumentException("Invalid player color: " + color);
		}
	}

	public static Domino getDominoByID(Game game, int id) {
		for (Domino domino : game.getAllDominos()) {
			if (domino.getId() == id) {
				return domino;
			}
		}
		throw new java.lang.IllegalArgumentException("Domino with ID " + id + " not found.");
	}

	public static Player getPlayerByColor(Player.PlayerColor color) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for (Player player : game.getPlayers()) {
			if (player.getColor() == color)
				return player;
		}
		throw new IllegalArgumentException("No player with the given color exists: " + color);
	}

	// this code has been taken directly from DiscardDominosStepDefinition example;
	// written by either the prof or TA
	/**
	 * @author Teaching staff of ECSE223 from McGill University Winter 2020
	 * @param id: the
	 * @return domino: the corresponding domino to the id
	 */
	public static Domino getDominoByID(int id) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for (Domino domino : game.getAllDominos()) {
			if (domino.getId() == id) {
				return domino;
			}
		}
		throw new java.lang.IllegalArgumentException("Domino with ID " + id + " not found.");
	}

	// this code has been taken directly from DiscardDominosStepDefinition example;
	// written by either the prof or TA
	public static void createAllDominoes(Game game) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("src/main/resources/alldominoes.dat"));
			String line = "";
			String delimiters = "[:\\+()]";
			while ((line = br.readLine()) != null) {
				String[] dominoString = line.split(delimiters); // {id, leftTerrain, rightTerrain, crowns}
				int dominoId = Integer.decode(dominoString[0]);
				TerrainType leftTerrain = getTerrainType(dominoString[1]);
				TerrainType rightTerrain = getTerrainType(dominoString[2]);
				int numCrown = 0;
				if (dominoString.length > 3) {
					numCrown = Integer.decode(dominoString[3]);
				}
				new Domino(dominoId, leftTerrain, rightTerrain, numCrown, game);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new java.lang.IllegalArgumentException(
					"Error occured while trying to read alldominoes.dat: " + e.getMessage());
		}
	}

	// this code has been taken directly from DiscardDominosStepDefinition example;
	// written by either the prof or TA
	public static TerrainType getTerrainType(String terrain) {
		switch (terrain) {
		case "W":
			return TerrainType.WheatField;
		case "F":
			return TerrainType.Forest;
		case "M":
			return TerrainType.Mountain;
		case "G":
			return TerrainType.Grass;
		case "S":
			return TerrainType.Swamp;
		case "L":
			return TerrainType.Lake;
		default:
			throw new java.lang.IllegalArgumentException("Invalid terrain type: " + terrain);
		}
	}

	// this code has been taken directly from DiscardDominosStepDefinition example;
	// written by either the prof or TA
	public static Domino.DominoStatus getDominoStatus(String status) {
		switch (status.toLowerCase()) {
		case "inpile":
			return Domino.DominoStatus.InPile;
		case "excluded":
			return Domino.DominoStatus.Excluded;
		case "incurrentdraft":
			return Domino.DominoStatus.InCurrentDraft;
		case "innextdraft":
			return Domino.DominoStatus.InNextDraft;
		case "erroneouslypreplaced":
			return Domino.DominoStatus.ErroneouslyPreplaced;
		case "correctlypreplaced":
			return Domino.DominoStatus.CorrectlyPreplaced;
		case "placedinkingdom":
			return Domino.DominoStatus.PlacedInKingdom;
		case "discarded":
			return Domino.DominoStatus.Discarded;
		default:
			throw new java.lang.IllegalArgumentException("Invalid domino status: " + status);
		}
	}

	// this code has been taken directly from DiscardDominosStepDefinition example;
	// written by either the prof or TA
	public static DominoInKingdom.DirectionKind getDirection(String dir) {
		switch (dir.toLowerCase()) {
		case "up":
			return DominoInKingdom.DirectionKind.Up;
		case "down":
			return DominoInKingdom.DirectionKind.Down;
		case "left":
			return DominoInKingdom.DirectionKind.Left;
		case "right":
			return DominoInKingdom.DirectionKind.Right;
		default:
			throw new java.lang.IllegalArgumentException("Invalid direction: " + dir);
		}
	}

	/**
	 * translates view directionKind to player directionKind
	 * @param d the view DirectionKind
	 * @return the Player DirectionKind equivalent
	 */
	public static DirectionKind viewDirectionToModel(ca.mcgill.ecse223.kingdomino.view.DominoPanel.DirectionKind d) {
		switch (d) {
		case Up:
			return DirectionKind.Up;
		case Down:
			return DirectionKind.Down;
		case Left:
			return DirectionKind.Left;
		case Right:
			return DirectionKind.Right;
		default:
			return DirectionKind.Up;
		}
	}

	/**
	 * @author AdamMigliore
	 * @return the first player in of the game
	 */
	public static Player getFirstPlayer() {
		
		for (Player player : KingdominoApplication.getKingdomino().getCurrentGame().getPlayers()) {
			if (player.getPrevPlayer() == null) {
				System.out.println();
				return player;
			}
		}

		return null;

	}
	
	/**
	 * @author AdamMigliore
	 * set next Player links
	 */
	public static void setNextPlayerLinks() {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		
		game.getPlayer(0).setNextPlayer(game.getPlayer(1));
		game.getPlayer(game.getNumberOfPlayers()-1).setNextPlayer(game.getPlayer(game.getNumberOfPlayers()-2));
		
		for (int i = 1; i<game.getPlayers().size() - 1;i++) {
			game.getPlayer(i).setNextPlayer(game.getPlayer(i+1));
		}
	}
}
