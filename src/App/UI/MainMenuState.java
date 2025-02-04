package App.UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Domain.Board.*;
import Persistency.Level;

/**
 * MainMenuState
 *
 * The state for the main menu. Will be the first state displayed when running the program. Displays buttons for starting a new game and loading a previous game.
 */


public class MainMenuState implements State, KeyListener {
    private UI ui;

    @Override
    public void setup(UI ui){
        this.ui = ui;
        var title = new JLabel("Larry Croft's Adventures", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 42));
        var newGame = new JButton("New Game");
        var loadGame = new JButton("Load Game");
        var credits = new JLabel("© 2024 Team 5");
        credits.setFont(new Font("Arial", Font.BOLD, 24));

        newGame.setPreferredSize(new Dimension(100, 50));
        loadGame.setPreferredSize(new Dimension(100, 50));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 200));
        buttonPanel.add(newGame);
        buttonPanel.add(loadGame);
        ui.add(BorderLayout.NORTH, title);
        ui.add(BorderLayout.CENTER, buttonPanel);
        ui.add(BorderLayout.SOUTH, credits);
        //setting the closeUI method in App.UI.UI to remove all elements.
        ui.addKeyListener(this);
        ui.setFocusable(true);

        ui.setCloseUI(() -> {
            ui.remove(title);
            ui.remove(buttonPanel);
            ui.remove(credits);
            ui.removeKeyListener(this);
        });

        /**
         * NewGame button action listener:
         * sets the gameState, then changes the UI state
         */
        newGame.addActionListener(e-> {
                    LevelState levelState = new LevelState();
                    levelState.getBoard().loadLevelGameState(Board.GameState.LEVEL1);
                    ui.changeState(levelState);
                }
        );


        /**
         * loadGame button action listener:
         * Initialises the levelState object, sets the board, then changes the state.
         */

        loadGame.addActionListener(e-> {
            LevelState levelState = new LevelState();
            levelState.load(levelState.getBoard(), ui);
            ui.changeState(levelState);
        });
    }

    /**
     * closeState method
     * closes the current MainMenuState. Called when changing states.
     * @param ui
     */
    @Override
    public void closeState(UI ui) {
        ui.closeUI.run();
    }

    @Override
    public void update(UI ui) {
        //no need for any implementation -- will not be updating anything in main menu.
    }

    @Override
    public void pauseGame(UI ui, Board board) {
        //no need for any implementation -- will not be pausing anything in main menu.
    }

    @Override
    public Board getBoard() {
        return null;
    }

    @Override
    public void load(Board board, UI ui) {
        //no need for any implementation -- will not be calling this method in main menu.
    }

    @Override
    public void save(Board board, UI ui) {
        //no need for any implementation -- will not be calling this method in main menu.
    }

    // KeyListener methods
    @Override
    public void keyTyped(KeyEvent e) {
        // Not needed
    }

    /**
     * KeyPressed method
     * Checks if the user is currently pressing CTRL and 1 / 2. If so, starts the game in level one / level two by
     * calling the respective methods.
     * @param e the event to be processed
     */

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getModifiersEx() == InputEvent.CTRL_DOWN_MASK) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_1 -> loadLevelOne();
                case KeyEvent.VK_2 -> loadLevelTwo();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed
    }

    /**
     * LoadLevelOne method
     * Changes the state to be level one. Called when pressing CTRL+1.
     */

    private void loadLevelOne(){
        LevelState levelState = new LevelState();
        levelState.getBoard().loadLevelGameState(Board.GameState.LEVEL1);
        ui.changeState(levelState);
    }

    /**
     * LoadLevelTwo method
     * Changes the state to be level two. Called when pressing CTRL+2.
     */

    private void loadLevelTwo(){
        LevelState levelState = new LevelState();
        levelState.getBoard().loadLevelGameState(Board.GameState.LEVEL2);
        ui.changeState(levelState);
    }

}