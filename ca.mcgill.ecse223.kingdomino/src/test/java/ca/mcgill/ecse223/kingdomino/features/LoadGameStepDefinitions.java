package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.LoadController;
import ca.mcgill.ecse223.kingdomino.model.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.lang.reflect.Array;
import java.util.*;

import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;

import static org.junit.Assert.*;

/**
 * Step definitions for the load game gherkin scenario.
 *
 * Note: All provided savegames were translated into custom json format. Each file lives in the respective .json file
 * and maps exactly to the provided savegame.
 *
 * Note: Due to a last minute error we found in the save games (for which the pull request was only just merged into iteration-3),
 * some changes to the save games had to be made:
 * P2: 32,D -> 10,D
 * P3: 11,D -> 4,D;23,R -> 6,D
 * P4: 43,D -> 28,D
 *
 * @author Roey Borsteinas
 */
public class LoadGameStepDefinitions
{
    private boolean wasLoaded;
    @Given("the game is initialized for load game")
    public void theGameIsInitializedForLoadGame()
    {
        // There should be nothing in the current model
        KingdominoApplication.getKingdomino().delete();
    }

    @When("I initiate loading a saved game from {string}")
    public void iInitiateLoadingASavedGameFrom(String filename)
    {
        LoadController lc = new LoadController();
        try
        {
            lc.LoadGame(filename);
            wasLoaded = true;
        }
        catch (IllegalStateException e)
        {
            wasLoaded = false;
        }
    }

    @When("each tile placement is valid")
    public void eachTilePlacementIsValid()
    {
        // Each tile placement will already be guaranteed to be valid when calling the controller or
        // it will throw exception.
    }

    @When("the game result is not yet final")
    public void theGameResultIsNotYetFinal()
    {
        // do nothing here since this doesn't have an effect with the current implementation.
    }

    @Then("it shall be player {string}'s turn")
    public void itShallBePlayerSTurn(String name)
    {
        assertTrue("Game was not successfully loaded", wasLoaded);
        Kingdomino model = KingdominoApplication.getKingdomino();
        // Turn this into an integer by subtracting the ascii value by the value '0'.
        // This is some C hacks
        int playerNumber = name.charAt(0) - '0';

        // Since player number is a number from 1-4, subtract one to get the index into player list
        assertEquals(model.getCurrentGame().getNextPlayer(), model.getCurrentGame().getPlayer(playerNumber - 1));
    }

    @Then("each of the players should have the corresponding tiles on their grid:")
    public void eachOfThePlayersShouldHaveTheCorrespondingTilesOnTheirGrid(io.cucumber.datatable.DataTable dataTable)
    {
        assertTrue("Game was not successfully loaded", wasLoaded);
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        List<Map<String, String>> mapList = dataTable.asMaps();
        for (Map<String, String> map : mapList)
        {
            Integer playerNumber = Integer.decode(map.get("playerNumber"));
            String[] playerTilesStrings = map.get("playerTiles").replaceAll(" ", "").split(",");

            List<Integer> playerTiles = new ArrayList<Integer>();
            for (String tileString : playerTilesStrings)
            {
                playerTiles.add(Integer.decode(tileString));
            }

            Player player = game.getPlayer(playerNumber - 1);
            for (KingdomTerritory territory : player.getKingdom().getTerritories())
            {
                if (territory.getX() != 0 || territory.getY() != 0)
                {
                    DominoInKingdom dominoInKingdom = (DominoInKingdom)territory;
                    playerTiles.removeIf(id -> dominoInKingdom.getDomino().getId() == id);
                }
            }

            assertTrue(playerTiles.isEmpty());
        }
    }

    @Then("it shall be player {int}'s turn")
    public void itShallBePlayerPlayerSTurn(int playerNumber)
    {
        assertTrue("Game was not successfully loaded", wasLoaded);
        Player player = getPlayerByNumber(playerNumber);

        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        assertEquals(player.getColor(), game.getNextPlayer().getColor());
    }

    @Then("each of the players should have claimed the corresponding tiles:")
    public void eachOfThePlayersShouldHaveClaimedTheCorrespondingTiles(io.cucumber.datatable.DataTable dataTable)
    {
        assertTrue("Game was not successfully loaded", wasLoaded);
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        List<Map<String, String>> mapList = dataTable.asMaps();
        for (Map<String, String> map : mapList)
        {
            for (int i = 1; i <= 4; ++i)
            {
                Player player = getPlayerByNumber(Integer.decode(map.get("playerNumber")));
                int claimedId = Integer.decode(map.get("claimedTile"));
                assertEquals(claimedId, player.getDominoSelection().getDomino().getId());
            }

        }
    }
    
    @Then("tiles {string} shall be unclaimed on the board")
    public void tilesShallBeUnclaimedOnTheBoard(String unclaimed)
    {
        assertTrue(wasLoaded);
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        Set<Integer> expectedUnclaimedIds = new HashSet<>();
        String[] unclaimedArray = unclaimed.replaceAll(" ", "").split(",");
        for (String id : unclaimedArray)
        {
            expectedUnclaimedIds.add(Integer.decode(id));
        }
        Set<Integer> actualUnclaimedIds = new HashSet<>();

        // Since unclaimed dominos can only exist in the next draft
        for (Domino domino : game.getNextDraft().getIdSortedDominos())
        {
            actualUnclaimedIds.add(domino.getId());
        }
        // Set up actualUnclaimedIds
        for (DominoSelection selection : game.getNextDraft().getSelections())
        {
            actualUnclaimedIds.removeIf(id -> id == selection.getDomino().getId());
        }

        assertEquals(expectedUnclaimedIds, actualUnclaimedIds);
    }

    @Then("the game shall become ready to start")
    public void theGameShallBecomeReadyToStart()
    {
        assertTrue("Game was not successfully loaded", wasLoaded);
    }

    @Then("the game shall notify the user that the loaded game is invalid")
    public void theGameShallNotifyTheUserThatTheGameFileIsInvalid()
    {
        assertFalse(wasLoaded);
    }

    public static Player getPlayerByNumber(int number)
    {
        Player player;
        switch (number)
        {
            case 1:
                player = getPlayerByColor("blue");
                break;
            case 2:
                player = getPlayerByColor("green");
                break;
            case 3:
                player = getPlayerByColor("yellow");
                break;
            case 4:
                player = getPlayerByColor("pink");
                break;
            default:
                throw new IllegalArgumentException("No played with number: " + number);
        }
        return player;
    }
}
