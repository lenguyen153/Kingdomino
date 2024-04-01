package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.*;
import java.util.Scanner;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.SaveController;
import ca.mcgill.ecse223.kingdomino.model.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * Implementation of SaveGame Gherkin Scenario;
 * @author: Roy Borsteinas
 */
public class SaveGameStepDefinition
{
    @Given("the game is initialized for save game")
    public void the_game_is_initialized_for_save_game()
    {
        // Initialize a new model for testing and then set it
        KingdominoApplication.getKingdomino().delete();
        Kingdomino model = new Kingdomino();

        Game g = new Game(48, model);
        TestUtils.addDefaultUsersAndPlayers(g);
        TestUtils.createAllDominoes(g);
        g.setNextPlayer(g.getPlayer(0));

        for (Player p : g.getPlayers())
        {
            new DominoInKingdom(1, 0, p.getKingdom(), g.getAllDomino(0));
            (new Property(p.getKingdom())).addIncludedDomino(g.getAllDomino(0));
        }


        // Just initialize some draft stuff to populate that section of the save file
        Draft d1 = new Draft(Draft.DraftStatus.FaceUp, g);
        g.setCurrentDraft(d1);
        d1.addIdSortedDomino(g.getAllDomino(44));
        DominoSelection d1s4 = new DominoSelection(g.getPlayer(3), g.getAllDomino(44), d1);

        Draft d2 = new Draft(Draft.DraftStatus.FaceUp, g);
        g.setNextDraft(d2);
        d2.addIdSortedDomino(g.getAllDomino(10));
        d2.addIdSortedDomino(g.getAllDomino(2));
        d2.addIdSortedDomino(g.getAllDomino(9));
        d2.addIdSortedDomino(g.getAllDomino(23));
        DominoSelection d2s1 = new DominoSelection(g.getPlayer(0), g.getAllDomino(10), d2);
        DominoSelection d2s2 = new DominoSelection(g.getPlayer(1), g.getAllDomino(2), d2);
        DominoSelection d2s3 = new DominoSelection(g.getPlayer(2), g.getAllDomino(9), d2);

        model.setCurrentGame(g);
        KingdominoApplication.setKingdomino(model);
    }

    @Given("the game is still in progress")
    public void the_game_is_still_in_progress()
    {
        // This already done in the above block
    }

    @Given("no file named {string} exists in the filesystem")
    public void noFileNamedExistsInTheFilesystem(String filename)
    {
        // Ensure that no file name "filename" exists
        File file = new File(filename);
        boolean exists = file.exists();
        if (exists)
        {
            file.delete();
        }
    }

    @When("the user initiates saving the game to a file named {string}")
    public void theUserInitiatesSavingTheGameToAFileNamed(String filename)
    {
        // Save the game
        SaveController sc = new SaveController();
        sc.SaveGame(filename);
    }

    @Then("a file named {string} shall be created in the filesystem")
    public void aFileNamedShallBeCreatedInTheFilesystem(String filename)
    {
        // Check to make sure the file was then created
        File file = new File(filename);
        boolean exists = file.exists();
        assertTrue(exists);
    }

    @Given("the file named {string} exists in the filesystem")
    public void theFileNamedExistsInTheFilesystem(String filename)
    {
        // Make sure that this file does exist
        try (FileWriter file = new FileWriter(filename))
        {
            file.write("Some garbage placeholder data");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @When("the user agrees to overwrite the existing file named {string}")
    public void theUserAgreesToOverwriteTheExistingFile(String filename)
    {
        // The file is overwritten
        SaveController sc = new SaveController();
        sc.SaveGame(filename, true);
    }

    @Then("the file named {string} shall be updated in the filesystem")
    public void theFileNamedShallBeUpdatedInTheFilesystem(String filename)
    {
        // Ensure the file exists before continuing
        File file = new File(filename);
        boolean exists = file.exists();
        assertTrue(exists);

        // Make sure the file was actually overwritten
        try (Scanner s = new Scanner(file))
        {
            String val = s.nextLine();
            assertNotEquals("Some garbage placeholder data", val);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
