/*This code was generated using the UMPLE 1.29.1.4811.445d1d99b modeling language!*/

package ca.mcgill.ecse223.kingdomino.model;

// line 35 "../../../../../../../../ump/tmptqt54wol/model.ump"
// line 141 "../../../../../../../../ump/tmptqt54wol/model.ump"
public class Player
{

  //------------------------
  // ENUMERATIONS
  //------------------------

  public enum PlayerColor { Blue, Green, Yellow, Pink }

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Player Attributes
  private PlayerColor color;
  private int currentRanking;
  private int bonusScore;
  private int propertyScore;

  //Player Associations
  private Player nextPlayer;
  private Kingdom kingdom;
  private Game game;
  private User user;
  private Player prevPlayer;
  private DominoSelection dominoSelection;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Player(Game aGame)
  {
    currentRanking = 1;
    bonusScore = 0;
    propertyScore = 0;
    boolean didAddGame = setGame(aGame);
    if (!didAddGame)
    {
      throw new RuntimeException("Unable to create player due to game. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setColor(PlayerColor aColor)
  {
    boolean wasSet = false;
    color = aColor;
    wasSet = true;
    return wasSet;
  }

  public boolean setCurrentRanking(int aCurrentRanking)
  {
    boolean wasSet = false;
    currentRanking = aCurrentRanking;
    wasSet = true;
    return wasSet;
  }

  public boolean setBonusScore(int aBonusScore)
  {
    boolean wasSet = false;
    bonusScore = aBonusScore;
    wasSet = true;
    return wasSet;
  }

  public boolean setPropertyScore(int aPropertyScore)
  {
    boolean wasSet = false;
    propertyScore = aPropertyScore;
    wasSet = true;
    return wasSet;
  }

  public PlayerColor getColor()
  {
    return color;
  }

  public int getCurrentRanking()
  {
    return currentRanking;
  }

  public int getBonusScore()
  {
    return bonusScore;
  }

  public int getPropertyScore()
  {
    return propertyScore;
  }

  public int getTotalScore()
  {
    return bonusScore + propertyScore;
  }
  /* Code from template association_GetOne */
  public Player getNextPlayer()
  {
    return nextPlayer;
  }

  public boolean hasNextPlayer()
  {
    boolean has = nextPlayer != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Kingdom getKingdom()
  {
    return kingdom;
  }

  public boolean hasKingdom()
  {
    boolean has = kingdom != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Game getGame()
  {
    return game;
  }
  /* Code from template association_GetOne */
  public User getUser()
  {
    return user;
  }

  public boolean hasUser()
  {
    boolean has = user != null;
    return has;
  }
  /* Code from template association_GetOne */
  public Player getPrevPlayer()
  {
    return prevPlayer;
  }

  public boolean hasPrevPlayer()
  {
    boolean has = prevPlayer != null;
    return has;
  }
  /* Code from template association_GetOne */
  public DominoSelection getDominoSelection()
  {
    return dominoSelection;
  }

  public boolean hasDominoSelection()
  {
    boolean has = dominoSelection != null;
    return has;
  }
  /* Code from template association_SetOptionalOneToOptionalOne */
  public boolean setNextPlayer(Player aNewNextPlayer)
  {
    boolean wasSet = false;
    if (aNewNextPlayer == null)
    {
      Player existingNextPlayer = nextPlayer;
      nextPlayer = null;

      if (existingNextPlayer != null && existingNextPlayer.getPrevPlayer() != null)
      {
        existingNextPlayer.setPrevPlayer(null);
      }
      wasSet = true;
      return wasSet;
    }

    Player currentNextPlayer = getNextPlayer();
    if (currentNextPlayer != null && !currentNextPlayer.equals(aNewNextPlayer))
    {
      currentNextPlayer.setPrevPlayer(null);
    }

    nextPlayer = aNewNextPlayer;
    Player existingPrevPlayer = aNewNextPlayer.getPrevPlayer();

    if (!equals(existingPrevPlayer))
    {
      aNewNextPlayer.setPrevPlayer(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setKingdom(Kingdom aNewKingdom)
  {
    boolean wasSet = false;
    if (kingdom != null && !kingdom.equals(aNewKingdom) && equals(kingdom.getPlayer()))
    {
      //Unable to setKingdom, as existing kingdom would become an orphan
      return wasSet;
    }

    kingdom = aNewKingdom;
    Player anOldPlayer = aNewKingdom != null ? aNewKingdom.getPlayer() : null;

    if (!this.equals(anOldPlayer))
    {
      if (anOldPlayer != null)
      {
        anOldPlayer.kingdom = null;
      }
      if (kingdom != null)
      {
        kingdom.setPlayer(this);
      }
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToAtMostN */
  public boolean setGame(Game aGame)
  {
    boolean wasSet = false;
    //Must provide game to player
    if (aGame == null)
    {
      return wasSet;
    }

    //game already at maximum (4)
    if (aGame.numberOfPlayers() >= Game.maximumNumberOfPlayers())
    {
      return wasSet;
    }

    Game existingGame = game;
    game = aGame;
    if (existingGame != null && !existingGame.equals(aGame))
    {
      boolean didRemove = existingGame.removePlayer(this);
      if (!didRemove)
      {
        game = existingGame;
        return wasSet;
      }
    }
    game.addPlayer(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToMany */
  public boolean setUser(User aUser)
  {
    boolean wasSet = false;
    User existingUser = user;
    user = aUser;
    if (existingUser != null && !existingUser.equals(aUser))
    {
      existingUser.removePlayerInGame(this);
    }
    if (aUser != null)
    {
      aUser.addPlayerInGame(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOptionalOne */
  public boolean setPrevPlayer(Player aNewPrevPlayer)
  {
    boolean wasSet = false;
    if (aNewPrevPlayer == null)
    {
      Player existingPrevPlayer = prevPlayer;
      prevPlayer = null;

      if (existingPrevPlayer != null && existingPrevPlayer.getNextPlayer() != null)
      {
        existingPrevPlayer.setNextPlayer(null);
      }
      wasSet = true;
      return wasSet;
    }

    Player currentPrevPlayer = getPrevPlayer();
    if (currentPrevPlayer != null && !currentPrevPlayer.equals(aNewPrevPlayer))
    {
      currentPrevPlayer.setNextPlayer(null);
    }

    prevPlayer = aNewPrevPlayer;
    Player existingNextPlayer = aNewPrevPlayer.getNextPlayer();

    if (!equals(existingNextPlayer))
    {
      aNewPrevPlayer.setNextPlayer(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOptionalOneToOne */
  public boolean setDominoSelection(DominoSelection aNewDominoSelection)
  {
    boolean wasSet = false;
    if (dominoSelection != null && !dominoSelection.equals(aNewDominoSelection) && equals(dominoSelection.getPlayer()))
    {
      //Unable to setDominoSelection, as existing dominoSelection would become an orphan
      return wasSet;
    }

    dominoSelection = aNewDominoSelection;
    Player anOldPlayer = aNewDominoSelection != null ? aNewDominoSelection.getPlayer() : null;

    if (!this.equals(anOldPlayer))
    {
      if (anOldPlayer != null)
      {
        anOldPlayer.dominoSelection = null;
      }
      if (dominoSelection != null)
      {
        dominoSelection.setPlayer(this);
      }
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    if (nextPlayer != null)
    {
      nextPlayer.setPrevPlayer(null);
    }
    Kingdom existingKingdom = kingdom;
    kingdom = null;
    if (existingKingdom != null)
    {
      existingKingdom.delete();
      existingKingdom.setPlayer(null);
    }
    Game placeholderGame = game;
    this.game = null;
    if(placeholderGame != null)
    {
      placeholderGame.removePlayer(this);
    }
    if (user != null)
    {
      User placeholderUser = user;
      this.user = null;
      placeholderUser.removePlayerInGame(this);
    }
    if (prevPlayer != null)
    {
      prevPlayer.setNextPlayer(null);
    }
    DominoSelection existingDominoSelection = dominoSelection;
    dominoSelection = null;
    if (existingDominoSelection != null)
    {
      existingDominoSelection.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "currentRanking" + ":" + getCurrentRanking()+ "," +
            "bonusScore" + ":" + getBonusScore()+ "," +
            "propertyScore" + ":" + getPropertyScore()+ "," +
            "totalScore" + ":" + getTotalScore()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "color" + "=" + (getColor() != null ? !getColor().equals(this)  ? getColor().toString().replaceAll("  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "kingdom = "+(getKingdom()!=null?Integer.toHexString(System.identityHashCode(getKingdom())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "game = "+(getGame()!=null?Integer.toHexString(System.identityHashCode(getGame())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "user = "+(getUser()!=null?Integer.toHexString(System.identityHashCode(getUser())):"null") + System.getProperties().getProperty("line.separator") +
            "  " + "dominoSelection = "+(getDominoSelection()!=null?Integer.toHexString(System.identityHashCode(getDominoSelection())):"null");
  }
}
