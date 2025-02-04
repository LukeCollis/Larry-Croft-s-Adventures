package Domain.Tile;

import Domain.Board.Board;
import Domain.Tile.MovingTile.Chap;
import Domain.Tile.MovingTile.MovingTile;
import Domain.Tile.Objects.Key;

public record LockedDoorTile(Key keyRequired) implements Tile {

    /**
     * LockedDoorTile represents a door that can only be unlocked with a specific key.
     */
    @Override
    public boolean attemptMoveAndRun(Board board, MovingTile movingTile) {
        if (movingTile instanceof Chap chap) {
            if (!chap.hasKeyColour(keyRequired.colour())) {
                throw new IllegalStateException("Cannot allow movement. Chap does not have correct Key colour.");
            }
            chap.removeKey(keyRequired);
            return true;
        }
        return false;
    }


    @Override
    public TileType getTileType() {
        return switch (keyRequired.colour()) {
            case KeyColour.BLUE -> TileType.LOCKED_DOOR_BLUE;
            case KeyColour.GREEN -> TileType.LOCKED_DOOR_GREEN;
            case KeyColour.RED -> TileType.LOCKED_DOOR_RED;
        };
    }
}
