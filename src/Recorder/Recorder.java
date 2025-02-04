package Recorder;

import App.Controller.Controller;
import App.UI.LevelState;
import App.UI.UI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;


public class Recorder {

    private Stack<GameAction> actionHistory;
    private Stack<GameAction> redoStack;
    private Controller controller;
    private LevelState levelState;
    private UI ui;

    public Recorder() {
        redoStack = new Stack<>();
        actionHistory = new Stack<>();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }


    /**
     * Records an action and adds it to the performed stack.
     * Clears the undone actions stack when a new action is recorded.
     *
     * @param action The game action to record
     */
    public void recordAction(GameAction action) {
        if (controller.isReplaying()) {
            // Do not record actions during replay
            return;
        }
        redoStack.clear(); // Clear the redo stack on a new action
        actionHistory.push(action);
    }

    /**
     * Undo the last action by stepping back. Move the last action from the performed stack
     * to the undone stack and reverse its effect.
     */
    public void stepBackward() {
        if (controller == null) {
            System.out.println("Controller is null");
            return; // Early exit if controller is not set
        }

        if (!actionHistory.isEmpty()) {
            GameAction actionToUndo = actionHistory.pop();
            actionToUndo.undo(controller); // Undo the action
            redoStack.push(actionToUndo);  // Push the undone action onto the redoStack
            System.out.println("Undoing action: " + actionToUndo.getType());
        } else {
            System.out.println("No actions to undo.");
        }
    }



    /**
     * Redo the last undone action by stepping forward.
     * Move the last action from the undone stack back to the performed stack and reapply it.
     */
    public void stepForward() {
        if (!redoStack.isEmpty()) {
            GameAction actionToRedo = redoStack.pop();
            actionToRedo.redo(controller); // Redo the action
            actionHistory.push(actionToRedo); // Push it back to the history
            //System.out.println("Redoing action: " + actionToRedo.getType());
        } else {
            System.out.println("No actions to redo.");
        }
    }


    public void autoReplay() {
        autoReplay(500); // Default to 500ms delay
    }

    public void autoReplay(int replaySpeed) {
        if (controller == null || levelState == null) {
            System.out.println("Controller or LevelState is null");
            return;
        }

        List<GameAction> actionsToReplay = new ArrayList<>(actionHistory);
        Collections.reverse(actionsToReplay);



        // Update controller reference if necessary
        this.controller = levelState.getController();

        // Set isReplaying to true
        controller.setReplaying(true);

        // Use SwingWorker to avoid blocking the UI thread
        SwingWorker<Void, GameAction> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (GameAction action : actionsToReplay) {
                    publish(action); // Send action to process() method
                    Thread.sleep(replaySpeed); // Use the replay speed
                }
                return null;
            }

            @Override
            protected void process(List<GameAction> chunks) {
                // Execute action on the Event Dispatch Thread
                GameAction action = chunks.get(chunks.size() - 1);
                action.redo(controller);
            }

            @Override
            protected void done() {
                controller.setReplaying(false);
            }
        };

        worker.execute();
    }



    // Method to check if there are any actions to undo
    public boolean isEmpty() {
        return actionHistory.isEmpty();
    }

    // Method to check if there are any actions to redo
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    public void setLevelState(LevelState levelState) {
        this.levelState = levelState;
    }

    public void setUI(UI ui) {
        this.ui = ui;
    }
}
