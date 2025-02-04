package Domain.Tile;

import Domain.Board.Board;
import Domain.Tile.MovingTile.MovingTile;

public interface Tile {

    /**
     * @return Returns the tiles TileType
     */
    TileType getTileType();

    /**
     * A method which allows for easy implementation of running logic for moving tiles and moving into tiles.
     * @param movingTile The moving tile which is attempting to move into that instance of tile
     * @param board the board to run logic off of
     * @return Returns true or false if the movingTile is able to move into the tile
     */
    boolean attemptMoveAndRun(Board board, MovingTile movingTile);

    enum KeyColour {
        RED,
        GREEN,
        BLUE
    }

    enum TileType {
        FREE,
        WALL,
        THIEF,
        CHAP,
        BOMB,
        EXIT_LOCK,
        EXIT,
        INFO,
        KEY_RED,
        KEY_GREEN,
        KEY_BLUE,
        LOCKED_DOOR_RED,
        LOCKED_DOOR_GREEN,
        LOCKED_DOOR_BLUE,
        TREASURE,
        VENT
    }
}


