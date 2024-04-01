package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.KingdomTerritory;
import ca.mcgill.ecse223.kingdomino.model.Player;

import java.time.Clock;

/**
 * Manages movement of dominos.
 * @author Isabella Hao
 */
public class MoveController
{
    public enum Rotation
    {
        Clockwise,
        Counterclockwise
    }

    public MoveController(){}

    /**
     * Move the player's currently preplaced domino.
     * @author Isabella Hao
     */
    public static void moveDomino(Player player, DominoInKingdom.DirectionKind moveDirection)
    {
        // Find the tentatively placed domino in the kingdom
        DominoInKingdom dominoInKingdom = null;
        for (KingdomTerritory territory : player.getKingdom().getTerritories())
        {
            if (territory instanceof DominoInKingdom)
            {
                if (((DominoInKingdom) territory).getDomino().getStatus() == Domino.DominoStatus.ErroneouslyPreplaced ||
                        ((DominoInKingdom) territory).getDomino().getStatus() == Domino.DominoStatus.CorrectlyPreplaced)
                {
                    dominoInKingdom = (DominoInKingdom)territory;
                }
            }
        }

        assert dominoInKingdom != null;

        int x = dominoInKingdom.getX();
        int y = dominoInKingdom.getY();
        int newX = x;
        int newY = y;

        // Move in the direction
        switch (moveDirection)
        {
            case Up:
                newY++;
                break;
            case Down:
                newY--;
                break;
            case Right:
                newX++;
                break;
            case Left:
                newX--;
                break;
            default:
                break;
        }

        int secondX = newX;
        int secondY = newY;
        switch (dominoInKingdom.getDirection())
        {
            case Up:
                secondY++;
                break;
            case Down:
                secondY--;
                break;
            case Right:
                secondX++;
                break;
            case Left:
                secondX--;
                break;
            default:
                break;
        }

        // If the domino would not break the grid size, move it anyways
        if ((newX < 5 && newX > -5) && (newY < 5 && newY > -5) &&
            (secondX < 5 && secondX > -5) && (secondY < 5 && secondY > -5))
        {
            dominoInKingdom.setX(newX);
            dominoInKingdom.setY(newY);
            dominoInKingdom.getDomino().setStatus(Domino.DominoStatus.ErroneouslyPreplaced);
        }
        // If domino can be placed, place it, otherwise don't do anything
        DominoController.preplaceDomino(player, dominoInKingdom, newX, newY, dominoInKingdom.getDirection());
    }

    /**
     * Rotate the player's currently preplaced domino
     * @author Isabella Hao
     */
    public static void rotateDomino(Player player, Rotation rotation)
    {
        // Find the tentatively placed domino in the kingdom
        DominoInKingdom dominoInKingdom = null;
        for (KingdomTerritory territory : player.getKingdom().getTerritories())
        {
            if (territory instanceof DominoInKingdom)
            {
                if (((DominoInKingdom) territory).getDomino().getStatus() == Domino.DominoStatus.ErroneouslyPreplaced ||
                        ((DominoInKingdom) territory).getDomino().getStatus() == Domino.DominoStatus.CorrectlyPreplaced)
                {
                    dominoInKingdom = (DominoInKingdom)territory;
                }
            }
        }

        assert dominoInKingdom != null;

        DominoInKingdom.DirectionKind newDirection = null;

        if (rotation == Rotation.Clockwise)
        {
            switch (dominoInKingdom.getDirection())
            {
                case Up:
                    newDirection = DominoInKingdom.DirectionKind.Right;
                    break;
                case Down:
                    newDirection = DominoInKingdom.DirectionKind.Left;
                    break;
                case Right:
                    newDirection = DominoInKingdom.DirectionKind.Down;
                    break;
                case Left:
                    newDirection = DominoInKingdom.DirectionKind.Up;
                    break;
                default:
                    break;
            }
        }
        else if (rotation == Rotation.Counterclockwise)
        {
            switch (dominoInKingdom.getDirection())
            {
                case Up:
                    newDirection = DominoInKingdom.DirectionKind.Left;
                    break;
                case Down:
                    newDirection = DominoInKingdom.DirectionKind.Right;
                    break;
                case Right:
                    newDirection = DominoInKingdom.DirectionKind.Up;
                    break;
                case Left:
                    newDirection = DominoInKingdom.DirectionKind.Down;
                    break;
                default:
                    break;
            }
        }

        assert newDirection != null;

        int secondX = dominoInKingdom.getX();
        int secondY = dominoInKingdom.getY();

        switch (newDirection)
        {
            case Up:
                secondY++;
                break;
            case Down:
                secondY--;
                break;
            case Right:
                secondX++;
                break;
            case Left:
                secondX--;
                break;
            default:
                break;
        }

        if ((secondX < 5 && secondX > -5) && (secondY < 5 && secondY > -5))
        {
            dominoInKingdom.setDirection(newDirection);
        }

        DominoController.preplaceDomino(player, dominoInKingdom, dominoInKingdom.getX(), dominoInKingdom.getY(), dominoInKingdom.getDirection());
    }

}
