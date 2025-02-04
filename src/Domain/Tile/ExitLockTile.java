package Domain.Tile;

import Domain.Board.Board;
import Domain.Tile.MovingTile.Chap;
import Domain.Tile.MovingTile.MovingTile;

/**
 * ExitLockTile represents a locked exit tile.
 * It can only be unlocked when Chap has collected all necessary keys or treasures.
 */
public record ExitLockTile() implements Tile {
    /**
     * Handles Chap's interaction with the ExitLockTile.
     * @param movingTile The movingTile character interacting with the tile.
     * @return True if Chap can step on the tile (If there is no treasure left to find).
     */
    @Override
    public boolean attemptMoveAndRun(Board board, MovingTile movingTile) {
        if (movingTile instanceof Chap chap) {
            int treasureLeft = chap.getTreasureLeft();
            if (treasureLeft > 0) {
                throw new IllegalArgumentException("Chap still has treasure left to find!");
            }
            return true;
        }
        return false;
    }

    @Override
    public TileType getTileType() {
        return TileType.EXIT_LOCK;
    }
}
