package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.MoveController;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.KingdomTerritory;
import ca.mcgill.ecse223.kingdomino.model.Player;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Isabella Hao
 */
public class RotateDominoStepDefinitions
{
    @Given("the game is initialized for rotate current domino")
    public void gameInitializedForRotateDomino()
    {
        initializeGame();
    }

    @When("{string} requests to rotate the domino with {string}")
    public void requestsToRotateTheDomino(String color, String rotation)
    {
        MoveController.rotateDomino(getPlayerByColor(color), getRotation(rotation));
    }

    @Then("the domino {int} is still tentatively placed at {int}:{int} but with new direction {string}")
    public void theDominoIsStillTentativelyPlacedAtButWIthNewDirection(int id, int x, int y, String direction)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        // Find this domino in some player's kingdom
        DominoInKingdom dominoInKingdom = null;
        for (Player p : game.getPlayers())
        {
            for (KingdomTerritory territory : p.getKingdom().getTerritories())
            {
                if (territory instanceof DominoInKingdom)
                {
                    if (((DominoInKingdom) territory).getDomino().getId() == id)
                    {
                        dominoInKingdom = (DominoInKingdom)territory;
                    }
                }
            }
        }

        assertNotNull(dominoInKingdom);
        assertEquals(x, dominoInKingdom.getX());
        assertEquals(y, dominoInKingdom.getY());
        assertEquals(getDirection(direction), dominoInKingdom.getDirection());
    }


    @Then("domino {int} is tentatively placed at the same position {int}:{int} with the same direction {string}")
    public void theDominoIdIsStillTentativelyPlacedAtPositionPosxWithNewDirection(int id, int x, int y, String direction)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        // Find this domino in some player's kingdom
        DominoInKingdom dominoInKingdom = null;
        for (Player p : game.getPlayers())
        {
            for (KingdomTerritory territory : p.getKingdom().getTerritories())
            {
                if (territory instanceof DominoInKingdom)
                {
                    if (((DominoInKingdom) territory).getDomino().getId() == id)
                    {
                        dominoInKingdom = (DominoInKingdom)territory;
                    }
                }
            }
        }

        assertNotNull(dominoInKingdom);
        assertEquals(x, dominoInKingdom.getX());
        assertEquals(y, dominoInKingdom.getY());
        assertEquals(getDirection(direction), dominoInKingdom.getDirection());
    }

    @Then("domino {int} is tentatively placed at position {int}:{int} with the same direction {string}")
    public void theDominoIdIsTentativelyPlacedAtPositionPosxWithSameDirection(int id, int x, int y, String direction)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        // Find this domino in some player's kingdom
        DominoInKingdom dominoInKingdom = null;
        for (Player p : game.getPlayers())
        {
            for (KingdomTerritory territory : p.getKingdom().getTerritories())
            {
                if (territory instanceof DominoInKingdom)
                {
                    if (((DominoInKingdom) territory).getDomino().getId() == id)
                    {
                        dominoInKingdom = (DominoInKingdom)territory;
                    }
                }
            }
        }

        assertNotNull(dominoInKingdom);
        assertEquals(x, dominoInKingdom.getX());
        assertEquals(y, dominoInKingdom.getY());
        assertEquals(getDirection(direction), dominoInKingdom.getDirection());
    }

    @Then("domino {int} should still have status {string}")
    public void dominoShouldStillHaveStatus(int id, String status)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        // Find this domino in some player's kingdom
        DominoInKingdom dominoInKingdom = null;
        for (Player p : game.getPlayers())
        {
            for (KingdomTerritory territory : p.getKingdom().getTerritories())
            {
                if (territory instanceof DominoInKingdom)
                {
                    if (((DominoInKingdom) territory).getDomino().getId() == id)
                    {
                        dominoInKingdom = (DominoInKingdom)territory;
                    }
                }
            }
        }

        assertNotNull(dominoInKingdom);
        assertEquals(getDominoStatus(status), dominoInKingdom.getDomino().getStatus());
    }

    @Then("the domino {int} should have status {string}")
    public void dominoShouldHaveStatus(int id, String status)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();

        // Find this domino in some player's kingdom
        DominoInKingdom dominoInKingdom = null;
        for (Player p : game.getPlayers())
        {
            for (KingdomTerritory territory : p.getKingdom().getTerritories())
            {
                if (territory instanceof DominoInKingdom)
                {
                    if (((DominoInKingdom) territory).getDomino().getId() == id)
                    {
                        dominoInKingdom = (DominoInKingdom)territory;
                    }
                }
            }
        }

        assertNotNull(dominoInKingdom);
        assertEquals(getDominoStatus(status), dominoInKingdom.getDomino().getStatus());
    }

    // Helper method only used here
    public static MoveController.Rotation getRotation(String rotation)
    {
        switch (rotation.toLowerCase())
        {
            case "clockwise":
                return MoveController.Rotation.Clockwise;
            case "counterclockwise":
                return MoveController.Rotation.Counterclockwise;
            default:
                throw new IllegalArgumentException("no direction " + rotation + " exists");
        }
    }
}
