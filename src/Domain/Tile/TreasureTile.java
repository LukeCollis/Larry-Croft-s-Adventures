package Domain.Tile;

import Domain.Board.Board;
import Domain.Tile.MovingTile.Chap;
import Domain.Tile.MovingTile.MovingTile;

/**
 * TreasureTile represents a tile that contains treasure.
 * Chap can collect treasure from this tile.
 */
public record TreasureTile() implements Tile {

    @Override
    public TileType getTileType() {
        return TileType.TREASURE;
    }

    /**
     * Handles Chap's interaction with the TreasureTile.
     * Chap collects the treasure when stepping on the tile.
     * @param movingTile The movingTile character interacting with the tile.
     * @return True if Chap successfully collects the treasure.
     */
    @Override
    public boolean attemptMoveAndRun(Board board, MovingTile movingTile) {
        if (movingTile instanceof Chap chap) {
            int treasureLeft = chap.getTreasureLeft() - 1;
            chap.setTreasureLeft(treasureLeft);
            assert chap.getTreasureLeft() == treasureLeft;
            return true;
        }
        return false;
    }
}
