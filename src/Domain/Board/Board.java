package Domain.Board;

import App.Controller.Direction;
import Domain.Tile.MovingTile.Thief;
import Domain.Tile.MovingTile.Chap;
import Domain.Tile.Tile;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import Persistency.Persistency;
import Persistency.Level;

import javax.swing.*;
import javax.swing.Timer;

/**
 * Represents the game board, including tiles and characters like Chap and Thief.
 * Handles game state transitions, tile interactions, and movement of actors.
 */
public class Board {
    private Tile[][] tiles;
    private Chap chap;
    private Thief thief;
    private final Random random = new Random();
    private GameState gameState;
    private ScheduledExecutorService scheduler;
    private boolean sendDialog = true;

    private static final Direction[] directions = Direction.values();

    /**
     * @return 2D Array of Tiles
     */

    public Tile[][] getTiles() {return tiles;}

    /**
     * @return Current GameState
     */
    public GameState getGameState() {return gameState;}

    /**
     * @return Chap object
     */
    public Chap getChap() {return chap;}

    /**
     * @return Thief object
     */
    public Thief getThief() {return thief;}

    /**
     * @return Whether dialog messages should be sent or not
     */
    public boolean shouldSendDialog() {return sendDialog;}

    /**
     * Sets the flag for whether dialog messages should be sent.
     * @param sendDialog The new flag state
     */
    public void setSendDialog(boolean sendDialog) {this.sendDialog = sendDialog;}

    public enum GameState {
        START,
        RESET_LEVEL,
        LEVEL1,
        LEVEL2,
        PAUSED,
        ENDED
    }

    /**
     * Default constructor. Initializes the game with the START state.
     */
    public Board() {
        setGameState(GameState.START);
    }

    /**
     * Handles game state transitions and loads the appropriate board for the game state.
     * @param newGameState The new game state to set
     */
    public void loadLevelGameState(GameState newGameState) {
        if (newGameState != GameState.LEVEL1 && newGameState != GameState.LEVEL2) throw new IllegalStateException("Invalid game state transition. GameState needs to be LEVEL1 or LEVEL2");

        // Load the appropriate level based on the game state
        Level level = newGameState == GameState.LEVEL1 ? Persistency.getLevel1() : Persistency.getLevel2();
        Thief levelThief = newGameState == GameState.LEVEL2 ? level.thief() : null;

        loadBoard(level.tiles(), level.chap(), levelThief, newGameState);
    }

    /**
     * Handles game state transitions without loading the board.
     * Starts or stops actor movement based on the state.
     * @param newGameState The new game state to set
     */
    public void setGameState(GameState newGameState) {
        if (this.gameState == newGameState) throw new IllegalStateException("Invalid game state transition. Game State is already " + this.gameState);

        // Stop actor movement when transitioning to PAUSED or ENDED states
        if (newGameState == GameState.PAUSED || newGameState == GameState.ENDED) stopActorMovement();
        // Start actor movement when transitioning back to LEVEL2 from PAUSED
        else if (newGameState == GameState.LEVEL2 && this.gameState == GameState.PAUSED) startActorMovement();

        this.gameState = newGameState;
    }

    /**
     * Loads the board with all of the tiles, sets the game state, and places Chap and Thief if applicable.
     * @param newTiles The 32x32 tile array for the board
     * @param newChap Chap object to place on the board
     * @param newThief Thief object (optional for LEVEL2)
     * @param newGameState The new game state to apply (LEVEL1 or LEVEL2)
     */
    public void loadBoard(Tile[][] newTiles, Chap newChap, Thief newThief, GameState newGameState) {
        if (!is32x32(newTiles)) throw new IllegalArgumentException("Tiles array must be of size 32x32");

        this.tiles = new Tile[32][32];
        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 32; j++) {
                Tile tile = newTiles[i][j];
                if (this.tiles[i][j] != tile) setTileAt(i, j, tile);
            }
        }
        if (chap == null) {
            this.chap = newChap;
        } else {
            this.chap.setPosition(newChap.getRow(), newChap.getCol());
            this.chap.setTreasureLeft(newChap.getTreasureLeft());
            this.chap.setKeys(new ArrayList<>(newChap.getKeys()));
        }
        setTileAt(newChap.getRow(), newChap.getCol(), this.chap);

        if (newGameState == GameState.LEVEL2) {
            if (newThief == null) throw new IllegalArgumentException("Thief cannot be null for LEVEL2");
            this.thief = new Thief(newThief.getRow(), newThief.getCol());
            setTileAt(thief.getRow(),thief.getCol(), thief);
            startActorMovement();
        }

        this.gameState = newGameState;
    }


    /**
     *
     * @param array Array to check
     * @return returns true or false whether array is 32x32
     */
    public static boolean is32x32(Tile[][] array) {
        if (array == null || array.length != 32) return false;
        for (int i = 0; i < 32; i++) if (array[i] == null || array[i].length != 32) return false;
        return true;
    }

    /**
     * Starts the actor movement
     */
    public void startActorMovement() {
        if (scheduler == null || scheduler.isShutdown()) {
            scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(this::moveActorIncrementally, 0, 1, TimeUnit.SECONDS); // Run every 1 second
        }
    }

    /**
     * Stops Actor Movement
     */
    public void stopActorMovement() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }

    /**
     * Moves the actor in a direction it can
     */
    private void moveActorIncrementally() {
        int currentRow = thief.getRow();
        int currentCol = thief.getCol();

        // Get a random direction from the enum
        Direction randomDirection = directions[random.nextInt(directions.length)];

        int newRow = currentRow + randomDirection.getRow();
        int newCol = currentCol + randomDirection.getCol();

        if (isNotWithinBounds(newRow, newCol)) return; // Return and let it run again

        thief.attemptUpdatePos(this, newRow, newCol, currentRow, currentCol); // If position does not update correctly we will let the thief run again
    }

    private boolean isNotWithinBounds(int row, int col) {
        return row < 0 || row >= 32 || col < 0 || col >= 32;
    }

    /**
     * Sets a tile on the board at a certain position
     * @param row the row to be set at
     * @param col the column to be set at
     * @param tile The tile to set to at row,col position
     */
    public void setTileAt(int row, int col, Tile tile) {
        if (tile == null) {
            throw new IllegalArgumentException("Tile cannot be null");
        }
        if (isNotWithinBounds(row, col)) {
            throw new IllegalArgumentException("row or col out of bounds");
        }
        tiles[row][col] = tile;
    }

    /**
     * @param row the Row to get the tile from
     * @param col the column
     * @return Returns a tile at a specific row & col
     */
    public Tile getTileAt(int row, int col) {
        if (isNotWithinBounds(row, col)) {
            throw new IllegalArgumentException("row or col out of bounds at position: " + row + ", " + col);
        }
        return tiles[row][col];
    }

    /**
     * Sends a message dialog with optional delay.
     * @param text Text to send
     * @param shouldDelay should I send the message with a 1 second delay or not
     */
    public void sendMessageDialog(String text, boolean shouldDelay) {
        if (shouldDelay) {
            Timer timer = new Timer(1000, _ -> JOptionPane.showMessageDialog(null, text));
            timer.setRepeats(false);
            timer.start();
            return;
        }
        JOptionPane.showMessageDialog(null, text);
    }
}
