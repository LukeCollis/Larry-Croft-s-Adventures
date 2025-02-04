package App.UI;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


/**
 * UI class
 * Class that stores the window that holds all the UI elements.
 * When constructed (which is when the game is started), sets the state to MainMenuState to display a main menu on startup.
 */
public class UI extends JFrame{
    private State state;
    Runnable closeUI= ()->{};
    public UI(){
        assert SwingUtilities.isEventDispatchThread();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800,600);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        changeState(new MainMenuState());

        setVisible(true);
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){closeUI.run();}
        });
    }

    /**
     * ChangeState method
     * Closes the current state (stored in state) to remove all prev swing elements, and updates the state to be equal to the new state.
     * Calls the setup method to set up the new state and display it on the screen.
     * @param newState
     */

    public void changeState(State newState){
        if(state != null){
            state.closeState(this); //exit the current state if there is one
        }
        state = newState;
        state.setup(this);
    }


    /**
     * Sets actions to be performed when closeUI is done (what elements to remove). Called within the setup method.
     * @param action
     */
    void setCloseUI(Runnable action){
        closeUI = action;
    }

    public State getCurrentState(){
        return state;
    }
}
