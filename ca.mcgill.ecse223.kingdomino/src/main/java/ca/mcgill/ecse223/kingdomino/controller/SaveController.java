package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import org.json.simple.*;

import java.io.*;
import java.util.List;

/**
 * This controller handles serialization of game state and writing it to a file.
 *
 * Linked with the SaveGame Gherkin feature.
 * 
 * @author: Roy Borsteinas
 */
public class SaveController {
	public SaveController() {
	}

	/**
	 * Default savegame method; will handle serializing and then writing the
	 * serialized json string to a file. Will not overwrite the file if there
	 * already exists a file with the given name
	 * 
	 * @param filename name of file to write to
	 */
	public void SaveGame(String filename) {
		SaveGame(filename, false);
	}

	/**
	 * Will handle serializing and then writing the serialized json string to a
	 * file.
	 * 
	 * @param filename  name of file to write to
	 * @param overwrite should overwrite existing file?
	 * @author Roey Borsteinas
	 */
	public void SaveGame(String filename, boolean overwrite) {
		File file = new File(filename);
		// If the file doesn't exist or we should overwrite, write to the file
		if (!file.exists() || overwrite) {
			try (FileWriter writer = new FileWriter(file)) {
				// Serialize the state and write it
				writer.write(SerializeGameState());
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		// Otherwise do nothing
	}

	/**
	 * Traverse the object hierarchy and serialize each object required to reproduce
	 * the state
	 * 
	 * @return serialize json string
	 * @author Roey Borsteinas
	 */
	private String SerializeGameState() {
		// Game stuff
		JSONArray jGames = new JSONArray();
		List<Game> kingdominoGames = KingdominoApplication.getKingdomino().getAllGames();
		for (Game game : kingdominoGames) {
			JSONObject jGame = new JSONObject();
			jGame.put("numberOfPlayers", game.getNumberOfPlayers());
			jGame.put("maxPileSize", game.getMaxPileSize());
			if (game.hasNextPlayer())
				jGame.put("nextPlayer", game.getNextPlayer().getColor().toString());
			else
				jGame.put("nextPlayer", "");

			JSONArray jPlayers = new JSONArray();
			List<Player> kingdominoPlayers = game.getPlayers();
			for (Player player : kingdominoPlayers) {
				JSONObject jPlayer = new JSONObject();
				jPlayer.put("color", player.getColor().toString());

				if (player.hasKingdom()) {
					JSONObject jKingdom = new JSONObject();

					// Territories
					JSONArray jTerritories = new JSONArray();
					List<KingdomTerritory> kTerritories = player.getKingdom().getTerritories();
					for (KingdomTerritory t : kTerritories) {
						// Only save non-castle territories
						if (t.getX() != 0 || t.getY() != 0) {
							JSONObject jTerritory = new JSONObject();
							jTerritory.put("x", t.getX());
							jTerritory.put("y", t.getY());
							jTerritory.put("id", ((DominoInKingdom) t).getDomino().getId());
							jTerritory.put("direction", ((DominoInKingdom) t).getDirection().toString());
							jTerritories.add(jTerritory);
						}
					}
					jKingdom.put("territories", jTerritories);
					jPlayer.put("kingdom", jKingdom);
				} else
					jPlayer.put("kingdom", new JSONObject());

				jPlayers.add(jPlayer);
			}
			jGame.put("players", jPlayers);

			JSONObject jCurrentDraft = new JSONObject();
			{
				JSONArray jDraftIDs = new JSONArray();
				for (Domino domino : game.getCurrentDraft().getIdSortedDominos()) {
					jDraftIDs.add(domino.getId());
				}

				JSONArray jSelections = new JSONArray();
				for (DominoSelection selection : game.getCurrentDraft().getSelections()) {
					JSONObject jSelection = new JSONObject();
					jSelection.put("id", selection.getDomino().getId());
					jSelection.put("player", selection.getPlayer().getColor().toString());
					jSelections.add(jSelection);
				}

				jCurrentDraft.put("ids", jDraftIDs);
				jCurrentDraft.put("selections", jSelections);
			}

			JSONObject jNextDraft = new JSONObject();
			{
				JSONArray jDraftIDs = new JSONArray();
				for (Domino domino : game.getNextDraft().getIdSortedDominos()) {
					jDraftIDs.add(domino.getId());
				}

				JSONArray jSelections = new JSONArray();
				for (DominoSelection selection : game.getNextDraft().getSelections()) {
					JSONObject jSelection = new JSONObject();
					jSelection.put("id", selection.getDomino().getId());
					jSelection.put("player", selection.getPlayer().getColor().toString());
					jSelections.add(jSelection);
				}
				jNextDraft.put("selections", jSelections);
				jNextDraft.put("ids", jDraftIDs);
			}

			jGame.put("currentDraft", jCurrentDraft);
			jGame.put("nextDraft", jNextDraft);

			jGames.add(jGame);
		}

		// For now user saves will be stored with game saves since there is no specific
		// feature for this but
		// I would still like to be able to load player profiles from a saved game.
		JSONArray jUsers = new JSONArray();
		List<User> kingdominoUsers = KingdominoApplication.getKingdomino().getUsers();
		for (User user : kingdominoUsers) {
			JSONObject jUser = new JSONObject();
			jUser.put("name", user.getName());
			jUser.put("playedGames", user.getPlayedGames());
			jUser.put("wonGames", user.getWonGames());

			if (user.hasPlayerInGames()) {
				JSONArray jGameIndices = new JSONArray();
				for (Player p : user.getPlayerInGames()) {
					int gameIndex = kingdominoGames.indexOf(p.getGame());
					jGameIndices.add(gameIndex);
				}
				jUser.put("inGames", jGameIndices);
			}
			jUsers.add(jUser);
		}

		// Bonus options
		JSONArray jbonusOptions = new JSONArray();
		for (BonusOption bOpt : KingdominoApplication.getKingdomino().getBonusOptions()) {
			jbonusOptions.add(bOpt.getOptionName());
		}

		// Putting together
		JSONObject kingdomino = new JSONObject();
		Kingdomino model = KingdominoApplication.getKingdomino();
		if (model.hasCurrentGame())
			kingdomino.put("currentGame", model.getAllGames().indexOf(model.getCurrentGame()));
		else
			kingdomino.put("currentGame", -1);
		kingdomino.put("games", jGames);
		kingdomino.put("users", jUsers);
		kingdomino.put("bonusOptions", jbonusOptions);

		return kingdomino.toJSONString();
	}
}
