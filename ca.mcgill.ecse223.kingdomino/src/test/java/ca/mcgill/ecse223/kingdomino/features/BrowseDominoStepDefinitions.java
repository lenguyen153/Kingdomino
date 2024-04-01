package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.controller.PileController;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;
import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author Isabella Hao
 */
public class BrowseDominoStepDefinitions
{
    List<Domino> collectionResult;
    Domino individualResult;

    @Given("the program is started and ready for browsing dominoes")
    public void theProgramIsStartedAndReadyForBrowsingDominos()
    {
        initializeGame();
    }

    @When("I initiate the browsing of all dominoes")
    public void iInitiateTheBrowsingOfAllDominoes()
    {
        collectionResult = PileController.browseAllDominos();
    }

    @Then("all the dominoes are listed in increasing order of identifiers")
    public void allTheDominoesAreListedInIncreasingOrderOfIdentifiers()
    {
        int lastId = 0;
        for (Domino d : collectionResult)
        {
            assertTrue(lastId < d.getId());
            lastId = d.getId();
        }
    }

    @When("I provide a domino ID {int}")
    public void iProvideADominoIDId(int id)
    {
        individualResult = PileController.browseIndividualDomino(id);
    }

    @Then("the listed domino has {string} left terrain")
    public void theListedDominoHasLeftTerrain(String terrain)
    {
        assertEquals(getTerrainTypeFromWord(terrain), individualResult.getLeftTile());
    }

    @Then("the listed domino has {string} right terrain")
    public void theListedDominoHasRightTerrain(String terrain)
    {
        assertEquals(getTerrainTypeFromWord(terrain), individualResult.getRightTile());
    }

    @Then("the listed domino has {int} crowns")
    public void theListedDominoHasCrownsCrowns(int crowns)
    {
        int actualCrows = individualResult.getLeftCrown() + individualResult.getRightCrown();
        assertEquals(crowns, actualCrows);
    }

    @When("I initiate the browsing of all dominoes of {string} terrain type")
    public void iInitiateTheBrowsingOfAllDominoesOfTerrainType(String terrainType)
    {
        collectionResult = PileController.browseAllDominos(getTerrainTypeFromWord(terrainType));
    }

    @Then("list of dominoes with IDs {string} should be shown")
    public void listOfDominoesWithIDsShouldBeShown(String dominoes)
    {
        Set<Integer> expectedIds = new HashSet<>();
        Set<Integer> actualIds = new HashSet<>();

        for (String id : dominoes.split(","))
            expectedIds.add(Integer.decode(id));

        for (Domino d : collectionResult)
            actualIds.add(d.getId());

        assertEquals(expectedIds, actualIds);
    }
}
