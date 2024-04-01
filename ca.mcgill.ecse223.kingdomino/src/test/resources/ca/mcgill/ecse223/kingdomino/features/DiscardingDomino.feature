Feature: Discarding Domino

  Background:
    Given the game is initialized for discarding domino

  Scenario: Player discards domino during game
    Given it is not the last turn of the game
    Given the current player is not the last player in the turn
    Given the player's kingdom has the following dominoes:
      | id | dominodir | posx | posy |
      |  7 | right     |    0 |    1 |
      | 23 | up        |   -2 |    0 |
      | 21 | left      |    2 |    0 |
      | 48 | down      |   -1 |    1 |
      |  1 | right     |   -2 |    2 |
      | 16 | down      |    0 |   -1 |
      | 22 | left      |   -1 |   -2 |
      | 46 | left      |   -1 |   -1 |
      | 41 | right     |    1 |   -1 |
      | 12 | right     |    1 |   -2 |
    # This statement needs to be changed to fit with the design decisions we made about discarding:
    # A domino which should be discarded, will be discarded without preplacing it in the kingdom.
    Given the current player has selected the domino with id 3
    And it is impossible to place the current domino in his/her kingdom
    When the current player discards his/her domino
    Then this player now shall be making his/her domino selection

  Scenario: Fourth player discards domino during game
    Given it is not the last turn of the game
    Given the current player is the last player in the turn
    Given the player's kingdom has the following dominoes:
      | id | dominodir | posx | posy |
      |  7 | right     |    0 |    1 |
      | 23 | up        |   -2 |    0 |
      | 21 | left      |    2 |    0 |
      | 48 | down      |   -1 |    1 |
      |  1 | right     |   -2 |    2 |
      | 16 | down      |    0 |   -1 |
      | 22 | left      |   -1 |   -2 |
      | 46 | left      |   -1 |   -1 |
      | 41 | right     |    1 |   -1 |
      | 12 | right     |    1 |   -2 |
    # See above
    Given the current player has selected the domino with id 3
    And it is impossible to place the current domino in his/her kingdom
    When the current player discards his/her domino
    Then this player now shall be making his/her domino selection
