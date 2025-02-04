package App.Controller;
import App.UI.State;
import App.UI.UI;
import App.UI.*;
import Domain.Board.Board;
import Domain.Board.Board.GameState;
import Domain.Tile.*;
import Domain.Tile.MovingTile.Thief;
import Recorder.*;
import Domain.Tile.MovingTile.Chap;
import Renderer.Sound;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Controller class
 * Used for handling key presses while in the level state.
 */
public class Controller extends Keys {
    private Board board;
    private Recorder recorder;
    private UI ui;
    private GameState currentState;
    private State appState;
    private Sound sfx;
    private Direction direction;
    private boolean isUndoing = false;
    private boolean isReplaying = false;

    /**
     * Controller constructor
     * Sets action listeners for movement, pausing, loading, saving and exiting.
     */
    public Controller(Board board, Recorder recorder, UI ui, GameState currentState, State appState, Sound sfx) {
        this.board = board;
        this.recorder = recorder;
        this.ui = ui;
        this.currentState = currentState;
        this.appState = appState;
        this.sfx = sfx;
        setAction(KeyEvent.VK_UP, () -> moveChap(Direction.UP), () -> {
        }, 0);
        setAction(KeyEvent.VK_DOWN, () -> moveChap(Direction.DOWN), () -> {
        }, 0);
        setAction(KeyEvent.VK_LEFT, () -> moveChap(Direction.LEFT), () -> {
        }, 0);
        setAction(KeyEvent.VK_RIGHT, () -> moveChap(Direction.RIGHT), () -> {
        },0);
        setAction(KeyEvent.VK_SPACE, () -> appState.pauseGame(ui, board), () -> {
        },0);

        setAction(KeyEvent.VK_X, this::exitGame, () -> { }, InputEvent.CTRL_DOWN_MASK);
        setAction(KeyEvent.VK_S, () -> appState.save(board, ui), () -> { }, InputEvent.CTRL_DOWN_MASK);
        setAction(KeyEvent.VK_R, () -> appState.load(board, ui), () -> { }, InputEvent.CTRL_DOWN_MASK);

    }

    /**
     * Closes the game if called.
     */
    private void exitGame() {
        System.exit(0);
    }

    /**
     * Moves chap if called.
     * @param direction
     */
    public void moveChap(Direction direction) {
        this.direction = direction;
        int oldRow = board.getChap().getRow();
        int oldCol = board.getChap().getCol();
        int newRow = oldRow + direction.getRow();
        int newCol = oldCol + direction.getCol();


        if (!isUndoing) {
            recorder.recordAction(new GameAction("move", direction.toString(), board.getChap().getRow(), board.getChap().getCol(), direction, GameAction.Actor.CHAP));
        }
        Tile t = board.getChap().attemptUpdatePos(board, newRow, newCol, oldRow, oldCol);


        pingMove(t);

    }


    /**
     * pingMove()
     * Updates the game state and plays sound effects every time chap is moved.
     * @param t
     */
    private void pingMove(Tile t) {

        if (board.getGameState() != currentState && board.getGameState() == GameState.LEVEL2) {
            LevelState state = (LevelState) appState;
            state.resetLevel(ui);
            board.loadLevelGameState(board.getGameState());
        }

        if (board.getGameState() == GameState.RESET_LEVEL) {
            LevelState state = (LevelState) appState;
            state.resetLevel(ui);
            board.setGameState(currentState);
            board.loadLevelGameState(board.getGameState()); // Set the gameState back to the level again (Reset it)
        }



        if (t == null) return; //to prevent a NPE

        switch(t.getTileType()){
            case TREASURE -> sfx.setFile(2);
            case KEY_BLUE, KEY_GREEN, KEY_RED -> sfx.setFile(3);
            case BOMB -> sfx.setFile(4);
            case VENT -> sfx.setFile(5);
            default -> sfx.setFile(6);
        }
        sfx.play();

    }

    public void setUndoing(boolean isUndoing) {
        this.isUndoing = isUndoing;
    }

    public Board getBoard() {
        return this.board;
    }

    public boolean isReplaying() {
        return isReplaying;
    }

    public void setReplaying(boolean isReplaying) {
        this.isReplaying = isReplaying;
    }

    public void setBoard(Board board) {
        this.board = board;
    }








}
