package Domain.Tile;

import Domain.Board.Board;
import Domain.Tile.MovingTile.MovingTile;

/**
 * FreeTile represents an empty tile where Chap or Thief can freely step.
 */
public record FreeTile() implements Tile {

    /**
     * Allows any character to step on a FreeTile.
     * @return Always true, as FreeTile is not obstructed.
     */
    @Override
    public boolean attemptMoveAndRun(Board board, MovingTile movingTile) {
        return true;
    }

    @Override
    public TileType getTileType() {
        return TileType.FREE;
    }
}
 