package Domain.Tile.MovingTile;

import Domain.Board.Board;
import Domain.Tile.FreeTile;
import Domain.Tile.Objects.Key;
import Domain.Tile.Tile;
import Domain.Tile.VentTile;

import java.util.ArrayList;
import java.util.List;

public class Chap implements MovingTile {
    private int row, col, treasureLeft;
    private List<Key> keys;
    private VentTile ventStandingOn = null;

    public Chap(int row, int col, int treasureLeft) {
        this(row, col, treasureLeft, new ArrayList<>());
    }

    public Chap(int row, int col, int treasureLeft, List<Key> keys) {
        this.row = row;
        this.col = col;
        this.treasureLeft = treasureLeft;
        this.keys = keys;
    }

    /**
     * Updates Chap's position on the board.
     * @param board The board where Chap is moved.
     * @param newRow The new row for Chap.
     * @param newCol The new column for Chap.
     * @param oldRow The previous row for Chap.
     * @param oldCol The previous column for Chap.
     * @return The tile Chap moves to.
     */
    @Override
    public Tile attemptUpdatePos(Board board, int newRow, int newCol, int oldRow, int oldCol) {
        Tile targetTile = board.getTileAt(newRow, newCol);

        // Check if the move is allowed.
        if (!targetTile.attemptMoveAndRun(board, this)) return null;

        // Handle vent logic separately.
        if (targetTile instanceof VentTile) {
            board.setTileAt(oldRow, oldCol, new FreeTile());
            return targetTile;
        }

        // Place Chap on the new tile and update its position.
        board.setTileAt(newRow, newCol, this);
        this.row = newRow;
        this.col = newCol;

        // Handle moving off a vent tile.
        if (ventStandingOn != null) {
            board.setTileAt(oldRow, oldCol, ventStandingOn);
            ventStandingOn = null;
        } else {
            board.setTileAt(oldRow, oldCol, new FreeTile());
        }

        return targetTile;
    }

    /**
     * Executes Chap's movement between vent tiles.
     * @param board The game board.
     * @param ventFrom The vent tile Chap is moving from.
     * @param ventTo The vent tile Chap is moving to.
     */
    public void runVentMove(Board board, VentTile ventFrom, VentTile ventTo) {
        this.ventStandingOn = ventTo;
        board.setTileAt(ventTo.getRow(), ventTo.getCol(), this);
        board.setTileAt(ventFrom.getRow(), ventFrom.getCol(), ventFrom);

        this.row = ventTo.getRow();
        this.col = ventTo.getCol();
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return Row position of Chap
     */
    @Override
    public int getRow() {return row;}

    /**
     * @return col position of Chap
     */
    @Override
    public int getCol() {return col;}

    /**
     * Sets the treasure left for Chap to get
     * @param treasureLeft The amount of treasure left
     */
    public void setTreasureLeft(int treasureLeft) {this.treasureLeft = treasureLeft;}

    /**
     * Gets the amount of treasure left for Chap
     * @return returns the amount of treasure left
     */
    public int getTreasureLeft() {return treasureLeft;}

    /**
     * Checks if Chap has a specific key
     * @param colour Key Colour to check if Chap has
     * @return true or false depending on if Chap has the key or not
     */
    public boolean hasKeyColour(KeyColour colour){ return keys.stream().anyMatch(k -> k.colour().equals(colour)); }

    /**
     * Adds a key to Chaps keys inventory
     * @param key Key to add to Chaps inventory
     */
    public void addKey(Key key) { this.keys.add(key); }

    /**
     * Removes a key from Chaps keys inventory
     * @param key Key to remove from Chaps inventory
     */
    public void removeKey(Key key) { this.keys.remove(key); }

    /**
     * @return Returns the set of keys that Chap has
     */
    public List<Key> getKeys() {return keys;}

    public void setKeys(List<Key> keys) {this.keys = keys;}

    @Override
    public TileType getTileType() {return TileType.CHAP;}
}
