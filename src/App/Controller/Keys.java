package App.Controller;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Keys class
 * Contains methods for handling key presses/releases.
 */
class Keys implements KeyListener {
    private final Map<Integer, Runnable> actionsPressed = new HashMap<>();
    private final Map<Integer, Runnable> actionsReleased = new HashMap<>();
    private final Map<Integer, Integer> modifiers = new HashMap<>();


    /**
     * KeyTyped method
     * Unused method. Only here because I implemented keyListener
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // leaving this empty as not needed
    }

    /**
     * keyPressed method
     * runs action when key (and modifiers, if used) is pressed.
     * @param e the event to be processed
     */

    @Override
    public void keyPressed(KeyEvent e) {
        assert SwingUtilities.isEventDispatchThread();
        Runnable action = actionsPressed.get(e.getKeyCode());
        if (action != null) {
            int requiredModifiers = modifiers.getOrDefault(e.getKeyCode(), 0);
            if ((e.getModifiersEx() & requiredModifiers) == requiredModifiers) {
                action.run();
            }
        }
    }

    /**
     * Stops action when key is released.
     * @param e the event to be processed
     */

    @Override
    public void keyReleased(KeyEvent e) {
        assert SwingUtilities.isEventDispatchThread();
        actionsReleased.getOrDefault(e.getKeyCode(), () -> {}).run();
    }

    /**
     * setAction
     * Sets a runnable to be called when a key is pressed and released..
     * @param keyCode
     * @param onPressed
     * @param onReleased
     * @param modifiers
     */

    public void setAction(int keyCode, Runnable onPressed, Runnable onReleased, int modifiers) {
        actionsPressed.put(keyCode, onPressed);
        actionsReleased.put(keyCode, onReleased);
        this.modifiers.put(keyCode, modifiers);
    }
}
