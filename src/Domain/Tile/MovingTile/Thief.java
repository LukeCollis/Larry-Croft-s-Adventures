package Domain.Tile.MovingTile;

import Domain.Board.Board;
import Domain.Tile.FreeTile;
import Domain.Tile.Tile;

public class Thief implements MovingTile {
    private int row, col;

    /**
     * Constructor to initialize the Thief with position params
     * @param row row position of the Thief.
     * @param col col position of the Thief.
     */
    public Thief(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Attempts to update the position of the Thief on the board.
     * It checks if the move is valid and updates the board with the Thief's new position.
     * Replaces the Thief's old position with a FreeTile.
     *
     * @param board The game board.
     * @param newRow The new row where the Thief is moving to.
     * @param newCol The new column where the Thief is moving to.
     * @param oldRow The previous row of the Thief.
     * @param oldCol The previous column of the Thief.
     * @return The tile the Thief moves to, or null if the move is not valid.
     */
    @Override
    public Tile attemptUpdatePos(Board board, int newRow, int newCol, int oldRow, int oldCol) {
        // Get the target tile the Thief is trying to move to.
        Tile targetTile = board.getTileAt(newRow, newCol);

        // Check if the move is allowed by attempting to move onto the target tile.
        if (!targetTile.attemptMoveAndRun(board, this)) {
            return null; // If move is invalid, return null.
        }

        // Update the Thief's internal position to the new location.
        this.row = newRow;
        this.col = newCol;

        // Place the Thief at the new position on the board.
        board.setTileAt(newRow, newCol, this);

        // Replace the old position of the Thief with a FreeTile.
        board.setTileAt(oldRow, oldCol, new FreeTile());

        return targetTile; // Return the tile the Thief moved to.
    }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return The current row position of the Thief.
     */
    @Override
    public int getRow() {return row;}

    /**
     * @return The current column position of the Thief.
     */
    @Override
    public int getCol() {return col;}

    /**
     * @return The tile type of the Thief, used for distinguishing this object in the game logic.
     */
    @Override
    public TileType getTileType() {return TileType.THIEF;}
}
