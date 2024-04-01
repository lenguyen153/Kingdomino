package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.PropertyController;
import ca.mcgill.ecse223.kingdomino.model.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.*;

import static org.junit.Assert.*;

import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;

/**
 * Step definitions for identifyproperty feature
 * @author Roey Borsteinas
 */
public class IdentifyPropertiesStepDefinitions
{
    @Given("the game is initialized for identify properties")
    public void theGameIsInitializedForIdentifyProperties()
    {
        initializeGame();
    }

    @When("the properties of the player are identified")
    public void thePropertiesOfThePlayerAreIdentified()
    {
        Player p = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();

        PropertyController pc = new PropertyController(p.getKingdom());
        pc.identifyProperties();
    }

    @Then("the player shall have the following properties:")
    public void thePlayerShallHaveTheFollowingProperties(io.cucumber.datatable.DataTable dataTable)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        Player player = game.getNextPlayer();

        List<Property> properties = player.getKingdom().getProperties();

        assertFalse("Player should not have 0 properties during testing",
                properties.isEmpty());

        List<Map<TerrainType, String>> propertyList = new LinkedList<>();

        for (Property property : properties)
        {
            List<Integer> dominoIds = new ArrayList<>();
            for (Domino d : property.getIncludedDominos())
            {
                dominoIds.add(d.getId());
            }
            Collections.sort(dominoIds);

            String dominos = "";
            for (Integer id : dominoIds)
            {
                dominos += id + ",";
            }
            dominos = dominos.substring(0, dominos.length() - 1);

            Map<TerrainType, String> terrainTypeStringMap = new HashMap<>();
            terrainTypeStringMap.put(property.getPropertyType(), dominos);
            propertyList.add(terrainTypeStringMap);
        }

        List<Map<String, String>> valueMaps = dataTable.asMaps();

        for (Map<String, String> map : valueMaps)
        {
            // Get values from cucumber table
            TerrainType type = getTerrainTypeFromWord(map.get("type"));
            String dominos = map.get("dominoes");
            boolean found = false;
            for (Map<TerrainType, String> property : propertyList)
            {
                if (!property.containsKey(type)) continue;

                List<String> givenIds = new LinkedList<>(Arrays.asList(dominos.split(",")));
                String[] actualIds = property.get(type).split(",");

                for (String actualId : actualIds)
                {
                    givenIds.removeIf(givenId -> givenId.equals(actualId));
                }
                if (givenIds.isEmpty())
                {
                    found = true;
                    break;
                }
            }
            assertTrue("Expected property not found in kingdom: " + type + " - " + dominos, found);
        }

    }
}
