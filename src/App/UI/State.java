package App.UI;

import javax.swing.*;
import java.awt.*;
import Domain.Board.*;


/**
 * State interface
 *
 * Contains unimplemented methods that are to be used with the different states
 */

public interface State {
    void setup(UI ui); //Handles setting up the states (adding App.UI.UI elements, setting the timer/treasure left counters)
    void closeState(UI ui); //Removes components and closes the state
    void update(UI ui); //Update the state to handle logic (i.e. movement)
    void pauseGame(UI ui, Board board);
    Board getBoard();
    void load(Board board, UI ui);
    void save(Board board, UI ui);
}









