
Feature: Selecting First Domino

  Background:
    Given the game has been initialized for selecting first domino

  Scenario Outline: Select first domino of the game
    Given the initial order of players is "<playerorder>"
    Given the current draft has the dominoes with ID "1,2,3,4"
    Given player's initial domino selection "<currentselection>"
    Given the "<currentplayer>" player is selecting his/her first domino with ID <chosendominoid>
    And the validation of domino selection returns "<result>"
    When the "<currentplayer>" player completes his/her first domino selection of the game
    Then the "<nextplayer>" player shall be placing his/her domino

    Examples:
      | playerorder            | currentplayer | currentselection     | chosendominoid | result  | nextplayer |
      | blue,green,pink,yellow | blue          | none,none,none,none  |              1 | success | green      |
      | blue,green,pink,yellow | pink          | blue,green,none,none |              3 | success | yellow     |
      | green,pink,blue,yellow | pink          | green,none,none,none |              2 | success | blue       |
      | green,pink,yellow,blue | pink          | green,none,none,none |              1 | error   | pink       |
      | blue,green,pink,yellow | yellow        | blue,green,pink,none |              3 | error   | yellow     |

  Scenario Outline: Complete first turn of domino selection
    Given the initial order of players is "<playerorder>"
    Given the current draft has the dominoes with ID "1,2,3,4"
    Given player's initial domino selection "<currentselection>"
    Given the "<currentplayer>" player is selecting his/her first domino with ID <chosendominoid>
    And the validation of domino selection returns "<result>"
    When the "<currentplayer>" player completes his/her first domino selection of the game
    Then a new draft shall be available, face down

    Examples:
      | playerorder            | currentplayer | currentselection     | chosendominoid | result  |
      | blue,green,pink,yellow | yellow        | blue,green,pink,none |              4 | success |