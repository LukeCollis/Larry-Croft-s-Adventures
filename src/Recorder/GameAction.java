package Recorder;

import App.Controller.Controller;
import App.Controller.Direction;
import Domain.Board.Board;
import Domain.Tile.MovingTile.Chap;
import Domain.Tile.MovingTile.Thief;
import org.json.JSONObject;

/**
 * The GameAction class represents an action that occurs in the game, such as
 * a movement or key press. This class includes methods for executing the action
 * in the game and converting it to and from JSON for recording and replay purposes.
 */
public class GameAction {


    public enum Actor {
        CHAP,
        THIEF
    }



    private String type;
    private String details;
    private int row;
    private int col;
    private Direction direction;
    private Chap chap;
    private Actor actor;


    /**
     * Constructs a new GameAction with the specified type and details.
     *
     * @param type    the type of action (e.g., "MOVE", "PICKUP", etc.)
     * @param details the details of the action (e.g., direction of movement, item picked up)
     * @param row     the row position where the action took place
     * @param col     the column position where the action took place
     * @param direction the direction of the movement, if applicable
     */


    public GameAction(String type, String details, int row, int col, Direction direction, Actor actor) {
        this.type = type;
        this.details = details;
        this.row = row;
        this.col = col;
        this.direction = direction;
        this.actor = actor;
    }


    /**
     * Redoes the action. This reapplies the effect of the action in the game.
     */

    public void redo(Controller controller) {
        if ("MOVE".equalsIgnoreCase(type)) {
            controller.setUndoing(true);
            if (actor == Actor.CHAP) {
                controller.moveChap(direction);
            } else if (actor == Actor.THIEF) {
                Board board = controller.getBoard();
                Thief thief = board.getThief();
                moveThief(thief, board, direction);
            }
            controller.setUndoing(false);
        } else {
            System.out.println("Cannot redo action type: " + type);
        }
    }

    /**
     * Undoes the action. This reverses the effect of the action in the game.
     */


    public void undo(Controller controller) {
        if ("MOVE".equalsIgnoreCase(type)) {
            Direction oppositeDirection = direction.getOpposite();
            controller.setUndoing(true);
            if (actor == Actor.CHAP) {
                controller.moveChap(oppositeDirection);
            } else if (actor == Actor.THIEF) {
                // Get the Board from the Controller
                Board board = controller.getBoard();
                Thief thief = board.getThief();
                moveThief(thief, board, oppositeDirection);
            }
            controller.setUndoing(false);
            System.out.println("Undoing MOVE action: " + direction + " for " + actor);
        } else {
            System.out.println("Cannot undo action type: " + type);
        }
    }



    /**
     * Converts this GameAction into a JSON object for saving or transmission.
     *
     * @return a JSONObject representing this action with its type, details, row, and column.
     */


    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("type", type);
        json.put("details", details);
        json.put("row", row);
        json.put("col", col);
        return json;
    }

    /**
     * Creates a GameAction from a JSON object. This method is used when loading
     * recorded actions from a file.
     *
     * @param json the JSONObject representing a recorded action
     * @return a GameAction object with the type, details, row, and column extracted from the JSON
     */


    public static GameAction fromJSON(JSONObject json) {
        Direction direction = Direction.valueOf(json.getString("details").toUpperCase());
        Actor actor = Actor.valueOf(json.getString("actor").toUpperCase());
        return new GameAction(
                json.getString("type"),
                json.getString("details"),
                json.getInt("row"),
                json.getInt("col"),
                direction,
                actor
        );
    }

    // Getters for row and col
    public int getRow() { return row; }
    public int getCol() { return col; }
    public String getType() { return type; }
    public String getDetails() { return details; }


    private void moveThief(Thief thief, Board board, Direction direction) {
        int oldRow = thief.getRow();
        int oldCol = thief.getCol();
        int newRow = oldRow + direction.getRow();
        int newCol = oldCol + direction.getCol();

        // Perform the movement
        thief.attemptUpdatePos(board, newRow, newCol, oldRow, oldCol);
    }

}


