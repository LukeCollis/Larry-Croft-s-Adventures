package Domain.Tile;

import Domain.Board.Board;
import Domain.Tile.MovingTile.Chap;
import Domain.Tile.MovingTile.MovingTile;

/**
 * InfoTile represents a tile that displays information to Chap.
 * When Chap steps on this tile, a message or information is displayed.
 */
public record InfoTile(String text) implements Tile {

    @Override
    public TileType getTileType() {
        return TileType.INFO;
    }

    /**
     * Handles Chap's interaction with the InfoTile.
     * Displays a message to the player when Chap steps on the tile.
     * @param movingTile The movingTile character interacting with the tile.
     * @return Always true, allowing Chap to step on the tile.
     */
    @Override
    public boolean attemptMoveAndRun(Board board, MovingTile movingTile) {
        if (!(movingTile instanceof Chap)) return false;
        if (board.shouldSendDialog()) {
            board.sendMessageDialog(text, false);
        }
        return true;
    }
}
