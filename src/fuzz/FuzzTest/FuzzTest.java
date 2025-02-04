package fuzz.FuzzTest;

import App.UI.LevelState;
import App.UI.UI;
import Domain.Board.Board;
import fuzz.Robot.Bot;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

public class FuzzTest {
    /**
     * Initializes the Game UI, loads the selected level, and starts a Bot instance to perform fuzz testing on the game.
     * <p>
     * This method sets up the game's user interface and prepares the test environment by loading the specified game state.
     * It then runs the Bot class to simulate input and interactions for fuzz testing purposes, ensuring the game can handle
     * a wide range of unexpected behaviors or inputs.
     *
     * @param gameState The predefined game state used for testing. This represents the level configuration to be loaded
     *                  and tested during the fuzz test.
     */
    static void test(Board.GameState gameState) {

            assertTimeoutPreemptively(Duration.ofSeconds(60), () -> {
                final UI[] uiHolder = new UI[1];
                SwingUtilities.invokeAndWait(() -> {
                    uiHolder[0] = new UI();//Inits UI
                });
                Robot robot = new Robot();//Starts Java.awt robot to pass to Bot class

                LevelState levelState = new LevelState();//Inits LevelState

                levelState.getBoard().loadLevelGameState(gameState);//Loads in given level selected by game state

                uiHolder[0].changeState(levelState);//Sets JWindow to selected level

                Bot bot = new Bot(robot, levelState);//Inits Bot to run through game
                bot.run();//Tells bot to run through game

            });

    }
}
