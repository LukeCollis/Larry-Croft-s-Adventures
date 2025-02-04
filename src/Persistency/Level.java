package Persistency;

import Domain.Tile.MovingTile.Chap;
import Domain.Tile.MovingTile.Thief;
import Domain.Tile.Tile;

/**
 * Represents the game level, allowing access to tiles and the users character chap.
 * Also, includes a thief character for level 2.
 */

public interface Level{
    Tile[][] tiles();
    Chap chap();

    /**
     * Default constructor for thief, as different levels may or may not need it.
     *
     * @return null
     */
    default Thief thief() {
        return null;
    }
}
