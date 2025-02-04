package Domain.Tile;

import Domain.Board.Board;
import Domain.Tile.MovingTile.Chap;
import Domain.Tile.MovingTile.MovingTile;

/**
 * WallTile represents an immovable wall that blocks Chap or other entities from passing through.
 */
public record WallTile() implements Tile {
    /**
     * Prevents Chap or any other entity from stepping on this tile.
     * @param movingTile The MovingTile character interacting with the tile.
     * @return Always false, as WallTile is not passable.
     */
    @Override
    public boolean attemptMoveAndRun(Board board, MovingTile movingTile) {
        if (movingTile instanceof Chap) {
            throw new IllegalArgumentException("Chap cannot be moved into a wall tile");
        }
        //I DO NOT want to throw an exception here as if the Thief moves into this tile it will break its randomMove implementation
        return false;
    }

    @Override
    public TileType getTileType() {
        return TileType.WALL;
    }
}
