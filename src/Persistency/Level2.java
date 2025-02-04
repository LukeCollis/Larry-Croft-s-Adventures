package Persistency;

import Domain.Tile.MovingTile.Chap;
import Domain.Tile.MovingTile.Thief;
import Domain.Tile.Tile;

/**
 * Represents level 2 of the game.
 *
 * @param tiles a 2D array representing the game tiles on a board
 * @param chap the main character in level 2
 * @param thief the enemy in level 2
 */
public record Level2(Tile[][] tiles, Chap chap, Thief thief) implements Level{
    public Level2{
        if(tiles == null){throw new IllegalArgumentException("The tiles are null");}
        if(chap == null){throw new IllegalArgumentException("The chap is null");}
        if(thief == null){throw new IllegalArgumentException("The thief is null");}
    }

    /**
     * Returns the thief object only for level 2
     *
     * @return thief object
     */
    @Override
    public Thief thief() {
        return thief;
    }
}

