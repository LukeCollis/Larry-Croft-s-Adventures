package Domain.Tile;

import Domain.Board.Board;
import Domain.Tile.MovingTile.Chap;
import Domain.Tile.MovingTile.MovingTile;
import Domain.Tile.Objects.Key;

/**
 * KeyTile represents a tile containing a key that Chap can collect.
 */
public record KeyTile(Key key) implements Tile {

    @Override
    public TileType getTileType() {
        return switch (key.colour()) {
            case KeyColour.BLUE -> TileType.KEY_BLUE;
            case KeyColour.GREEN -> TileType.KEY_GREEN;
            case KeyColour.RED -> TileType.KEY_RED;
        };
    }

    /**
     * Handles Chap's interaction with the KeyTile.
     * Chap collects the key when stepping on the tile.
     * @param movingTile The MovingTile character interacting with the tile.
     * @return True if Chap successfully collects the key.
     */
    @Override
    public boolean attemptMoveAndRun(Board board, MovingTile movingTile) {
        if (movingTile instanceof Chap chap) {
            chap.addKey(this.key);
            return true;
        }
        return false;
    }
}
