package Domain.Tile;

import Domain.Board.Board;
import Domain.Board.Board.GameState;
import Domain.Tile.MovingTile.MovingTile;

/**
 * ExitTile represents the final exit of the level.
 * Chap can step on this tile to complete the level.
 */
public record ExitTile() implements Tile {
    /**
     * Handles Chap's interaction with the ExitTile, allowing them to exit the level.
     * @param movingTile The movingTile character interacting with the tile.
     * @return Always true, as the exit is always accessible to Chap.
     */
    @Override
    public boolean attemptMoveAndRun(Board board, MovingTile movingTile) {
        GameState state = board.getGameState();
        switch (state) {
            case LEVEL1 -> board.setGameState(GameState.LEVEL2);
            case LEVEL2 -> board.setGameState(GameState.ENDED);
        }
        return true;
    }
    @Override
    public TileType getTileType() {
        return TileType.EXIT;
    }

}
