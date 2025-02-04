package fuzz.Robot;

import Domain.Tile.MovingTile.Chap;

/**
 * A record that wraps around a `Chap` object, representing the player character in the game.
 * <p>
 * This provides utility methods, such as retrieving the current position of the `Chap`.
 *
 * @param robot The `Chap` object, which represents the player character being controlled by the bot.
 */
record RobotChap(Chap robot){

    /**
     * Retrieves the current position of the `Chap` on the game board.
     *
     * @return A `Point` object representing the `Chap`'s current position (row and column) on the grid.
     */
    public Point getPosition(){
        return new Point(robot.getRow(), robot.getCol());
    }
}