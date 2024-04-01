package ca.mcgill.ecse223.kingdomino.features;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.PileController;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Kingdomino;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;

import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;
import static org.junit.Assert.*;

/**
 * @author Isabella Hao
 */
public class ShuffleDominosStepDefinitions
{
    @Given("the game is initialized for shuffle dominoes")
    public void theGameIsInitializedForShuffleDominoes()
    {
        Kingdomino kingdomino = new Kingdomino();

        KingdominoApplication.setKingdomino(kingdomino);
    }

    @Given("there are {int} players playing")
    public void thereAreNplayersPlayersPlaying(int nplayers)
    {
        Kingdomino kingdomino = KingdominoApplication.getKingdomino();
        Game game = new Game(nplayers * 12, kingdomino);
        game.setNumberOfPlayers(nplayers);
        kingdomino.setCurrentGame(game);
        // Populate game
        addDefaultUsersAndPlayers(game, nplayers);
        createAllDominoes(game);
        game.setNextPlayer(game.getPlayer(0));
    }

    @When("the shuffling of dominoes is initiated")
    public void theShufflingOfDominoesIsInitiated()
    {
        PileController.shuffleDeck();
    }

    @Then("the first draft shall exist")
    public void theFirstDraftShallExist()
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        assertNotNull(game.getCurrentDraft());
    }

    @Then("the first draft should have {int} dominoes on the board face down")
    public void theFirstDraftShouldHaveDominoesonboardDominoesOnTheBoardFaceDown(int dominoesonboard)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        assertEquals(dominoesonboard, game.getCurrentDraft().getIdSortedDominos().size());
    }

    @Then("there should be {int} dominoes left in the draw pile")
    public void thereShouldBeDominoesleftDominoesLeftInTheDrawPile(int dominoesleft)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        assertEquals(dominoesleft, PileController.getCurrentDrawPile(game).size());
    }

    @When("I initiate to arrange the domino in the fixed order {string}")
    public void iInitiateToArrangeTheDominoInTheFixedOrderDominoarrangement(String dominoarrangement)
    {
        List<Integer> order = new ArrayList<>();
        for (String id : dominoarrangement.replaceAll(" ", "").split(","))
            order.add(Integer.decode(id));

        PileController.fixedArrangeDeck(order);
    }

    @Then("the draw pile should consist of everything in {string} except the first {int} dominoes with their order preserved")
    public void theDrawPileShouldConsistOfEverythingInDominoarrangementExceptTheFirstDominoesonboardDominoesWithTheirOrderPreserved(String dominoarrangement, int dominoesonboard)
    {
        Game game = KingdominoApplication.getKingdomino().getCurrentGame();
        List<Domino> result = PileController.getCurrentDrawPile(game);
        List<Integer> actualOrder = new ArrayList<>();
        for (Domino d : result)
            actualOrder.add(d.getId());

        List<Integer> expectedOrder = new ArrayList<>();
        int i = 0;
        for (String id : dominoarrangement.replaceAll(" ", "").split(","))
        {
            if (i++ >= dominoesonboard)
            {
                expectedOrder.add(Integer.decode(id));
            }
        }

        assertEquals(expectedOrder, actualOrder);
    }
}
