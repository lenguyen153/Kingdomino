package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import ca.mcgill.ecse223.kingdomino.statemachine.gameplay.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Static helper methods used in multiple tests. Some methods here are taken
 * from the provided code.
 * 
 * @author Roey Borsteinas
 */
public class TestUtils {
	// Taken from prof code
	public static void addDefaultUsersAndPlayers(Game game) {
		String[] userNames = { "User1", "User2", "User3", "User4" };
		for (int i = 0; i < userNames.length; i++) {
			User user = game.getKingdomino().addUser(userNames[i]);
			Player player = new Player(game);
			player.setUser(user);
			player.setColor(Player.PlayerColor.values()[i]);
			Kingdom kingdom = new Kingdom(player);
			new Castle(0, 0, kingdom, player);
		}
	}

    public static void addDefaultUsersAndPlayers(Game game, int numPlayers) {
        String[] userNames = { "User1", "User2", "User3", "User4" };
        for (int i = 0; i < numPlayers; i++) {
            User user = game.getKingdomino().addUser(userNames[i]);
            Player player = new Player(game);
            player.setUser(user);
            player.setColor(Player.PlayerColor.values()[i]);
            Kingdom kingdom = new Kingdom(player);
            new Castle(0, 0, kingdom, player);
        }
    }

    // Taken from prof code
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

    // Taken from prof code
    public static Domino getdominoByID(int id) {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        for (Domino domino : game.getAllDominos()) {
            if (domino.getId() == id) {
                return domino;
            }
        }
        throw new java.lang.IllegalArgumentException("Domino with ID " + id + " not found.");
    }


    public static Player getPlayerByColor(String color)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        for (Player player : game.getPlayers())
        {
            if (player.getColor() == getPlayerColor(color))
                return player;
        }
        throw new IllegalArgumentException("No player with the given color exists: " + color);
    }

    // Taken from prof code
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

    public static TerrainType getTerrainTypeFromWord(String terrain)
    {
        switch (terrain.toLowerCase()) {
            case "wheat":
                return TerrainType.WheatField;
            case "forest":
                return TerrainType.Forest;
            case "mountain":
                return TerrainType.Mountain;
            case "grass":
                return TerrainType.Grass;
            case "swamp":
                return TerrainType.Swamp;
            case "lake":
                return TerrainType.Lake;
            default:
                throw new java.lang.IllegalArgumentException("Invalid terrain type: " + terrain);
        }
    }

    // Taken from prof code
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

    // Taken from prof code
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

    // Taken from prof code
    public static Player.PlayerColor getPlayerColor(String playerColor) {
        switch (playerColor.toLowerCase()) {
            case ("pink"):
                return Player.PlayerColor.Pink;
            case ("blue"):
                return Player.PlayerColor.Blue;
            case ("green"):
                return Player.PlayerColor.Green;
            case ("yellow"):
                return Player.PlayerColor.Yellow;
        }
        return null;
    }

    /**
     * Initializes a basic game.
     * Code taken from discard dominos provided code.
     */
    public static void initializeGame()
    {
        // Intialize empty game
        Kingdomino kingdomino = new Kingdomino();
        Game game = new Game(48, kingdomino);
        game.setNumberOfPlayers(4);
        kingdomino.setCurrentGame(game);
        // Populate game
        addDefaultUsersAndPlayers(game);
        createAllDominoes(game);
        game.setNextPlayer(game.getPlayer(0));
        KingdominoApplication.setKingdomino(kingdomino);
    }

    /**
     * Initialize the statemachine WITHOUT entering it
     */
    public static void initializeStatemachine()
    {
        GameplayStatemachine sm = new GameplayStatemachine();
        KingdominoApplication.setStatemachine(sm);

        sm.getSCITurn().setSCITurnOperationCallback(new TurnOperationCallback());
        sm.getSCIPlayer().setSCIPlayerOperationCallback(new PlayerOperationCallback());
        sm.getSCIDraft().setSCIDraftOperationCallback(new DraftOperationCallback());
        sm.getSCIGame().setSCIGameOperationCallback(new GameOperationCallback());

        sm.init();
    }

	/**
	 * @param result the result status from the gherkin step
	 * @return the equivalent boolean mapping
	 */
	public static boolean isValid(String result) {
		if (result.equals("valid"))
			return true;
		return false;
	}
}
