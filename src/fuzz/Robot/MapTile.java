package fuzz.Robot;

import Domain.Tile.Tile;

/**
 * Represents a tile on the game map. Each tile has a type (e.g., WALL, FLOOR) and
 * tracks the number of times it has been visited by the bot.
 */
class MapTile {

    private int numVisit = 0;
    private final Tile.TileType type;

    /**
     * Initializes a new MapTile with a specific tile type.
     *
     * @param type The type of tile, which determines its properties (e.g., if it's a wall).
     */
    MapTile(Tile.TileType type) {
        this.type = type;

    }

    /**
     * Retrieves the number of times this tile has been visited by the bot.
     *
     * @return The number of visits to this tile.
     */
    protected int getNumVisit() {
        return numVisit;
    }

    /**
     * Increments the visit counter for this tile.
     * <p>
     * This is called whenever the bot moves onto the tile, allowing the bot to track
     * which tiles have been visited more frequently.
     */
    protected void incrementNumVisit() {
        numVisit++;
    }

    /**
     * Determines whether this tile is a wall.
     *
     * @return {@code true} if this tile is a wall, {@code false} otherwise.
     */
    public boolean isWall() {
        return type == Tile.TileType.WALL;
    }
}

