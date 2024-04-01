package ca.mcgill.ecse223.kingdomino.features;

import static org.junit.Assert.*;
import static ca.mcgill.ecse223.kingdomino.features.TestUtils.*;

import java.util.List;
import java.util.Map;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Property;
import ca.mcgill.ecse223.kingdomino.model.TerrainType;
import ca.mcgill.ecse223.kingdomino.controller.PropertyAttributesController;
import ca.mcgill.ecse223.kingdomino.controller.PropertyController;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

/**
 * @author Vincent Trinh
 */
public class CalculatePropertyAttributesStepDefinition {

	@Given("the game is initialized for calculate property attributes")
	public void the_game_is_initialized_for_calculate_property_attributes() {
		initializeGame();
	}

	@When("calculate property attributes is initiated")
	public void calculate_property_attributes_is_initiated() {
		// Write code here that turns the phrase above into concrete actions
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();

		PropertyController pc = new PropertyController(game.getNextPlayer().getKingdom());
		pc.identifyProperties();

		for (Property property : game.getNextPlayer().getKingdom().getProperties())
		{
			PropertyAttributesController.CalculatePropertyCrown(property);
			PropertyAttributesController.CalculatePropertySize(property);
		}

	}

	@Then("the player shall have a total of {int} properties")
	public void the_player_shall_have_a_total_of_properties(int properties) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		assertEquals(properties, game.getNextPlayer().getKingdom().getProperties().size());
	}

	@Then("the player shall have properties with the following attributes:")
	public void the_player_shall_have_properties_with_the_following_attributes(io.cucumber.datatable.DataTable dataTable) {
		// the dataset to test against
		List<Map<String, String>> valueMaps = dataTable.asMaps();
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		List<Property> myProperties = game.getNextPlayer().getKingdom().getProperties();

		int expected = 0;
		int found = 0;
		for (Map<String, String> map : valueMaps) {
			// Get values from cucumber table
			TerrainType type = getTerrainTypeFromWord(map.get("type"));
			Integer size = Integer.decode(map.get("size"));
			Integer crown = Integer.decode(map.get("crowns"));
			expected++;

			// for a line we need to find if there is a corresponding property
			for (Property property : myProperties) {
				if (property.getPropertyType().equals(type) && property.getCrowns() == crown
						&& property.getSize() == size) {
					found++;
					break;
				}

			}
			assertEquals(expected, found);
		}
	}
}
