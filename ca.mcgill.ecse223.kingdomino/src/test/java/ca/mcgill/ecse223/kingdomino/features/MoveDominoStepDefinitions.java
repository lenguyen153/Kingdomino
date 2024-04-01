package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.DominoController;
import ca.mcgill.ecse223.kingdomino.controller.MoveController;
import ca.mcgill.ecse223.kingdomino.model.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;
import java.util.Map;

import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;
import static org.junit.Assert.*;

/**
 * @author Isabella Hao
 */
public class MoveDominoStepDefinitions
{
    DominoInKingdom placedDomino;
    @When("{string} removes his king from the domino {int}")
    public void playerRemovesHisKingFromTheDominoId(String player, int id)
    {
        DominoController.addTerritoryToKingdom(getPlayerByColor(player).getKingdom(), getdominoByID(id));
    }

    @Then("domino {int} should be tentative placed at position 0:0 of {string}'s kingdom with ErroneouslyPreplaced status")
    public void dominoIdShouldBeTentativePlacedAtPositionOfSKingdomWithErroneouslyPreplacedStatus(int id, String color)
    {
        // find the only preplaced domino in the player's kingdom
        Domino placed = null;
        for (KingdomTerritory territory : getPlayerByColor(color).getKingdom().getTerritories())
        {
            if (territory instanceof DominoInKingdom)
            {
                DominoInKingdom dominoInKingdom = (DominoInKingdom)territory;
                if (dominoInKingdom.getDomino().getStatus() == Domino.DominoStatus.ErroneouslyPreplaced)
                {
                    // If we get here, make sure we haven't already been here
                    // i.e. there aren't 2 erroneouslyplaced dominos in the kingdom
                    assertNull(placed);
                    placed = dominoInKingdom.getDomino();

                    assertEquals(0, dominoInKingdom.getX());
                    assertEquals(0, dominoInKingdom.getY());
                }
            }
        }
        assertNotNull(placed);
    }

    @Given("{string}'s kingdom has following dominoes:")
    public void sKingdomHasFollowingDominoes(String color, io.cucumber.datatable.DataTable dataTable)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        Player player = getPlayerByColor(color);
        List<Map<String, String>> valueMaps = dataTable.asMaps();
        for (Map<String, String> map : valueMaps)
        {
            Integer id = Integer.decode(map.get("id"));
            DominoInKingdom.DirectionKind dir = getDirection(map.get("dir"));
            Integer posx = Integer.decode(map.get("posx"));
            Integer posy = Integer.decode(map.get("posy"));

            DominoInKingdom dominoInKingdom = new DominoInKingdom(posx, posy,player.getKingdom(), getdominoByID(id));
            dominoInKingdom.setDirection(dir);
            getdominoByID(id).setStatus(Domino.DominoStatus.PlacedInKingdom);
        }
    }

    @When("{string} requests to move the domino {string}")
    public void requestsToMoveTheDomino(String color, String movement)
    {
        MoveController.moveDomino(getPlayerByColor(color), getDirection(movement));
    }

    @Then("the domino {int} should be tentatively placed at position {int}:{int} with direction {string}")
    public void theDominoIdShouldBeTentativelyPlacedAtPositionNposx(int id, int x, int y, String direction)
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

        placedDomino = dominoInKingdom;

        assertNotNull(dominoInKingdom);
        assertEquals(x, dominoInKingdom.getX());
        assertEquals(y, dominoInKingdom.getY());
        assertEquals(getDirection(direction), dominoInKingdom.getDirection());
    }

    @Then("the new status of the domino is {string}")
    public void theNewStatusOfTheDominoIs(String status)
    {
        assertEquals(getDominoStatus(status), placedDomino.getDomino().getStatus());
    }

    @Given("domino {int} has status {string}")
    public void dominoIdHasStatus(int id, String status)
    {
        getdominoByID(id).setStatus(getDominoStatus(status));
    }

    @Then("the domino {int} is still tentatively placed at position {int}:{int}")
    public void theDominoIdIsStillTentativelyPlacedAtPositionPosx(int id, int x, int y)
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
        placedDomino = dominoInKingdom;
        assertNotNull(dominoInKingdom);
        assertEquals(x, dominoInKingdom.getX());
        assertEquals(y, dominoInKingdom.getY());
    }

    @Then("the domino should still have status {string}")
    public void theDominoShouldStillHaveStatus(String status)
    {
        assertEquals(getDominoStatus(status), placedDomino.getDomino().getStatus());
    }
}
