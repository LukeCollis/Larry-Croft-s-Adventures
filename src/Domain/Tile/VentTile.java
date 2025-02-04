package Domain.Tile;

import Domain.Board.Board;
import Domain.Tile.MovingTile.Chap;
import Domain.Tile.MovingTile.MovingTile;

/**
 * VentTile represents a vent tile that can transport Chap between two vent tiles.
 */
public class VentTile implements Tile{
    private VentTile linkedVent = null;
    private final int row, col;

    /**
     *
     * @param row Row of the vent
     * @param col Col of the vent
     */
    public VentTile(int row, int col) {
        this.row = row;
        this.col = col;
    }
    /**
     * @return The linked vent tile, allowing external access if needed.
     */
    public VentTile getLinkedVent() {
        return linkedVent;
    }

    /**
     * This method sets the ventTo VentTile
     * @param linkedVent VentTile to vent to from the instance of this
     */
    public void setLinkedVent(VentTile linkedVent) {
        this.linkedVent = linkedVent;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public TileType getTileType() {
        return TileType.VENT;
    }

    /**
     * Handles Chap's interaction with the VentTile.
     * Transports Chap to the linked vent tile.
     * @param movingTile The movingTile character interacting with the tile.
     * @return True if Chap is successfully transported to the linked vent.
     */
    @Override
    public boolean attemptMoveAndRun(Board board, MovingTile movingTile) {
        if (movingTile instanceof Chap chap) {
            assert linkedVent != null;
            chap.runVentMove(board, this, linkedVent); // Move Chap to the vent
            return true;
        }
        return false;
    }
}
