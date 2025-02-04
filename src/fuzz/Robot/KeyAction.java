package fuzz.Robot;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * KeyAction enum represents the movement and interaction commands for the game, allowing a `Robot`
 * to simulate key presses for moving the player character (Chap) on the board. Each action corresponds
 * to a specific keyboard event.
 * <p>
 * The enum also tracks whether the movement is possible based on the bot's surroundings and how many
 * times a tile has been visited.
 */
public enum KeyAction {
    UP(KeyEvent.VK_UP),
    DOWN(KeyEvent.VK_DOWN),
    LEFT(KeyEvent.VK_LEFT),
    RIGHT(KeyEvent.VK_RIGHT);

    private final int keyCode;
    private boolean canMove = false;
    private MapTile currentTile;

    /**
     * Initializes a KeyAction with the corresponding key code.
     * <p>
     * This constructor sets the key code for the specified action (UP, DOWN, LEFT, or RIGHT) which will
     * be used to simulate key presses for moving the player character.
     *
     * @param keyCode The keyboard key code associated with the movement action, such as KeyEvent.VK_UP for UP.
     */
    KeyAction(int keyCode){
        this.keyCode = keyCode;
    }

    /**
     * Simulates a key press for the bot to move the player character in the game.
     * <p>
     * This method sends the specified key press to the game using a `Robot`, allowing the bot to simulate
     * movement. It presses and releases the key with a brief delay to mimic real user input.
     *
     * @param robot The `Robot` instance used to simulate key presses for the movement.
     * @return The current `KeyAction` that was executed, allowing the bot to track its previous move.
     */
    public KeyAction move(Robot robot) {
        robot.keyPress(keyCode);
        robot.delay(100);//To Stop the robot from going to fast and to simulate a more realistic human interaction
        robot.keyRelease(keyCode);
        return this;
    }

    /**
     * Checks whether this movement action is currently possible.
     * <p>
     * This method returns `true` if the bot can move in the direction associated with this action,
     * based on the current state of the game.
     *
     * @return `true` if the movement is allowed, `false` otherwise.
     */

    public boolean canMove() {
        return canMove;
    }

    /**
     * Retrieves the number of times the bot has visited the current tile associated with this action.
     * <p>
     * This method tracks how many times the bot has been on the tile in the direction of this movement,
     * which can be used to prioritize less-visited tiles.
     *
     * @return The number of visits to the tile in the direction of this movement.
     */

    public int getNumVisit(){return currentTile.getNumVisit();}

    /**
     * Updates the movement action based on the properties of the surrounding tiles.
     * <p>
     * This method evaluates the tile that the bot would move onto for this action, checking whether the
     * tile is a wall and how many times it has been visited. If the tile is not a wall, the bot can move
     * there, and the number of visits is updated.
     *
     * @param mapTile The tile the bot would move to. This tile is evaluated to determine whether movement is possible.
     */
    public void updateTileValue(MapTile mapTile){
        if(!mapTile.isWall()){
            currentTile = mapTile;
            canMove = true;
            return;
        }
        canMove = false;
    }
}
