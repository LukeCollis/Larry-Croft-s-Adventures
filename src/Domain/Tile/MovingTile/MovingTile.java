package Domain.Tile.MovingTile;

import Domain.Board.Board;
import Domain.Tile.Tile;

import javax.swing.*;

public interface MovingTile extends Tile {

    /**
     * @param b Domain.Board to set the Domain.Tile at
     * @param newRow new Row to set the Domain.Tile at
     * @param newCol new Col to set the tile at
     * @return Returns the tile that the movingTile has moved into
     */
    Tile attemptUpdatePos(Board b, int newRow, int newCol, int oldRow, int oldCol);

    /**
     * @return The row int of the moving tile
     */
    int getRow();

    /**
     * @return The Col int of the moving tile
     */
    int getCol();

    /**
     * @param movingTile the moving tile attempting to move into this instance of tile
     * @param board the board to run logic off of
     * @return Returns true if (thief or chap) moves into eachother the level ends, or else false
     */
    @Override
    default boolean attemptMoveAndRun(Board board, MovingTile movingTile) {
        if (movingTile instanceof Chap && this instanceof Thief || movingTile instanceof Thief && this instanceof Chap) {
            board.setGameState(Board.GameState.RESET_LEVEL);
            if (board.shouldSendDialog()) {
                board.sendMessageDialog("You have been ROBBED by the Thief! Your level progress will be reset!", false);
            }
            return false;
        }
        return false;
    }
}
