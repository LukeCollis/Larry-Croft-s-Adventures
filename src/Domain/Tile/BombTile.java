package Domain.Tile;

import Domain.Board.Board;
import Domain.Tile.MovingTile.Chap;
import Domain.Tile.MovingTile.MovingTile;

/**
 * BombTile represents a tile that contains a bomb.
 * Bombs can interact with other game entities such as Chap or Thief.
 */
public record BombTile() implements Tile{
    /**
     * Triggers the bomb explosion or interaction logic when Chap steps on it.
     * @param movingTile The movingTile character stepping on the tile.
     * @return True if Chap can step on the tile; false otherwise.
     */
    @Override
    public boolean attemptMoveAndRun(Board board, MovingTile movingTile) {
        if (movingTile instanceof Chap) {
            if (board.shouldSendDialog()) {
                // Send dialog with delay due to SFX sounds needing to play before text box
                board.sendMessageDialog("You have walked into a BOMB! You will now be reset at your level.", true);
            }
            // Set the game state to RESET after showing the message
            board.setGameState(Board.GameState.RESET_LEVEL);
            return true;
        }
        return false;
    }

    @Override
    public TileType getTileType() {return TileType.BOMB;}
}
