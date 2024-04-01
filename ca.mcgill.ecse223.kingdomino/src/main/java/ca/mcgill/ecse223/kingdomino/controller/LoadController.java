package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static ca.mcgill.ecse223.kingdomino.controller.Utils.*;
import static ca.mcgill.ecse223.kingdomino.controller.CalculateScoreController.*;

/**
 * This controller is responsible for deserializing game state and setting
 * the model from a save file.
 *
 * Linked with the LoadGame Gherkin feature.
 * @author: Roy Borsteinas
 */
public class LoadController
{
    public LoadController() {}

    /**
     * Set the current kingdomino model to be the one loaded from the given file.
     * @param filename file to read gamestate from.
     * @author Roey Borsteinas
     */
    public void LoadGame(String filename)
    {
        Kingdomino model = new Kingdomino();
        KingdominoApplication.setKingdomino(model);

        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(filename))
        {
            JSONObject jKingdomino = (JSONObject) parser.parse(reader);

            // Load all bonus options
            JSONArray jBonusOptions = (JSONArray)jKingdomino.get("bonusOptions");
            for (String jBonusOption : (Iterable<String>) jBonusOptions)
            {
                model.addBonusOption(jBonusOption);
            }

            // Load game data
            JSONArray jGames = (JSONArray)jKingdomino.get("games");
            int currentGameIndex = (int)(long)jKingdomino.get("currentGame");
            for (JSONObject jGame : (Iterable<JSONObject>)jGames)
            {
                int maxPileSize = (int)(long)jGame.get("maxPileSize");
                Game game = new Game(maxPileSize, model);
                // Make each game the current game while initializing, will set the proper
                // current game at the end
                model.setCurrentGame(game);

                // Use the utility function to initialize all dominoes into the game
                createAllDominoes(game);

                // Set up the players
                int numberOfPlayers = (int)(long)jGame.get("numberOfPlayers");
                game.setNumberOfPlayers(numberOfPlayers);
                Player.PlayerColor nextPlayer = getPlayerColor((String)jGame.get("nextPlayer"));

                JSONArray jPlayers = (JSONArray)jGame.get("players");
                for (JSONObject jPlayer : (Iterable<JSONObject>)jPlayers)
                {
                    Player player = new Player(game);
                    Player.PlayerColor color = getPlayerColor((String)jPlayer.get("color"));
                    player.setColor(color);
                    if (color == nextPlayer) game.setNextPlayer(player);

                    // Kingdom stuff
                    Kingdom kingdom = new Kingdom(player);
                    // Add a castle to 0,0
                    new Castle(0, 0, kingdom, player);

                    JSONObject jKingdom = (JSONObject)jPlayer.get("kingdom");
                    JSONArray jTerritories = (JSONArray)jKingdom.get("territories");
                    for (JSONObject jDominoInTerritory : (Iterable<JSONObject>)jTerritories)
                    {
                        int x = (int)(long)jDominoInTerritory.get("x");
                        int y = (int)(long)jDominoInTerritory.get("y");
                        if (x >= 5 || x <= -5) throw new IllegalStateException("Invalid x coordinate in save file");
                        if (y >= 5 || y <= -5) throw new IllegalStateException("Invalid y coordinate in save file");

                        int id = (int)(long)jDominoInTerritory.get("id");
                        DominoInKingdom.DirectionKind direction = getDirection((String)jDominoInTerritory.get("direction"));

                        // Find the corresponding domino and place it in the kingdom if possible
                        Domino domino = getDominoByID(game, id);

                        // Create the domino in kingdom object
                        DominoInKingdom dominoInKingdom = new DominoInKingdom(x, y, kingdom, domino);
                        if (dominoInKingdom.getDomino().getStatus() == Domino.DominoStatus.PlacedInKingdom)
                            throw new IllegalStateException("A domino can't be placed in multiple kingdoms. Invalid save file!");
                        dominoInKingdom.getDomino().setStatus(Domino.DominoStatus.ErroneouslyPreplaced);
                        dominoInKingdom.setDirection(direction);
                        if (!DominoController.preplaceDomino(player, dominoInKingdom, x, y, direction))
                        {
                            throw new IllegalStateException("This domino cannot be placed here. Invalid save file!");
                        }
                        DominoController.placeDomino(id);
                    }
                    (new PropertyController(kingdom)).identifyProperties();
                }

                for (Player p : game.getPlayers())
                    CalculatePlayerScore(p);

                CalculateRanking(game);

                // Load drafts
                JSONObject jCurrentDraft = (JSONObject)jGame.get("currentDraft");
                Draft currentDraft = createDraft(game, jCurrentDraft);
                game.setCurrentDraft(currentDraft);
                currentDraft.setDraftStatus(Draft.DraftStatus.FaceUp);
                for (Domino domino : currentDraft.getIdSortedDominos())
                {
                    if (domino.getStatus() == Domino.DominoStatus.PlacedInKingdom)
                        throw new IllegalStateException("Game file invalid, domino can't be in draft and placed in kingdom");
                    domino.setStatus(Domino.DominoStatus.InCurrentDraft);
                }

                JSONObject jNextDraft = (JSONObject)jGame.get("nextDraft");
                Draft nextDraft = createDraft(game, jNextDraft);
                game.setNextDraft(nextDraft);
                nextDraft.setDraftStatus(Draft.DraftStatus.FaceUp);
                for (Domino domino : nextDraft.getIdSortedDominos())
                {
                    if (domino.getStatus() == Domino.DominoStatus.PlacedInKingdom)
                        throw new IllegalStateException("Game file invalid, domino can't be in draft and placed in kingdom");
                    domino.setStatus(Domino.DominoStatus.InNextDraft);
                }

                // Randomly set the deck again since specifications do not require that
                // we save the exact order of the pile. (since it is random and players cannot have an effect on this,
                // then it doesn't matter if it is randomly generated once again).
                // passing false here since the draft was saved and don't need to recreate it.
                PileController.shuffleDeck(false);
            }

            model.setCurrentGame(model.getAllGame(currentGameIndex));


            // Initialize all the user profiles from the save file
            JSONArray jUsers = (JSONArray)jKingdomino.get("users");
            for (JSONObject jUser : (Iterable<JSONObject>) jUsers)
            {
                int gameIndex = 0;
                String name = (String)jUser.get("name");
                // JSON parser will load numbers as Long type so must be cast down twice
                // (once for boxed type Long and then from long to int).
                int wonGames = (int)(long) jUser.get("wonGames");
                int playedGames = (int)(long)jUser.get("playedGames");
                List<Integer> inGames = new ArrayList<>();

                JSONArray jUserInGames = (JSONArray)jUser.get("inGames");
                for (Long gameNumber : (Iterable<Long>) jUserInGames)
                {
                    inGames.add((int)(long)gameNumber);
                }

                User user = new User(name, model);
                user.setWonGames(wonGames);
                user.setPlayedGames(playedGames);

                int usergameIndex= 0;
                for (int playerIndex : inGames)
                {
                    user.addPlayerInGame(KingdominoApplication.getKingdomino().getAllGame(usergameIndex).getPlayer(playerIndex - 1));
                    usergameIndex++;
                }
            }
        }
        catch (IOException | ParseException e)
        {
            e.printStackTrace();
        }
		Utils.setNextPlayerLinks();
    }

    /**
     * Helper function to create a draft
     * @author Roey Borsteins
     */
    private Draft createDraft(Game game, JSONObject jDraft)
    {
        Draft draft = new Draft(Draft.DraftStatus.FaceDown, game);

        JSONArray jDominoIDs = (JSONArray)jDraft.get("ids");
        for (Long id : (Iterable<Long>)jDominoIDs)
        {
            draft.addIdSortedDomino(getDominoByID((int)(long)id));
        }

        JSONArray jSelections = (JSONArray)jDraft.get("selections");
        for (JSONObject jSelection : (Iterable<JSONObject>)jSelections)
        {
            int id = (int)(long)jSelection.get("id");
            Player.PlayerColor color = getPlayerColor((String)jSelection.get("player"));
            new DominoSelection(getPlayerByColor(color), getDominoByID(id), draft);
        }

        return draft;
    }
}
