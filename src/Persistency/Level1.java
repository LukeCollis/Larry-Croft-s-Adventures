package Persistency;

import Domain.Tile.MovingTile.Chap;
import Domain.Tile.Tile;

/**
 * Represents level 1 for the game.
 *
 * @param tiles a 2D array representing the game tiles on a board
 * @param chap the main character in level 1
 */
public record Level1(Tile[][] tiles, Chap chap) implements Level{
    public Level1{
        if(tiles == null){throw new IllegalArgumentException("The tiles are null");}
        if(chap == null){throw new IllegalArgumentException("The chap is null");}
    }
}

