package App.UI;

import Domain.Board.Board;
import Domain.Board.Board.GameState;
import App.Controller.Controller;
import Domain.Tile.MovingTile.Chap;
import Persistency.Persistency;
import Recorder.Recorder;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import Renderer.Renderer;
import Renderer.Sound;
import Persistency.*;
/**
 * LevelState
 *
 * Grabs and displays the current board from domain, and displays all buttons.
 */

public class LevelState implements State {

    protected JLabel timeLabel;
    protected JLabel treasureLabel;
    protected int timeLeft = 100;
    protected static Board board = new Board();
    protected Timer gameUpdateTimer;
    protected Renderer renderer;
    protected Controller controller;
    protected Recorder recorder;
    protected int levelNumber;
    protected JTextArea inventoryArea;
    protected GameState currentGameState;
    protected Sound bgm;
    protected Sound sfx;
    protected static Persistency persistency = new Persistency(board);
    protected UI uiState;



    /**
     * Setup method
     *
     * Loads all the swing components and displays the board.
     * @param ui
     */
    @Override
    public void setup(UI ui) {
        ui.setFocusable(true);
        ui.requestFocusInWindow();
        currentGameState = board.getGameState();
        recorder = new Recorder();
        sfx = new Sound();
        controller = new Controller(board, recorder, ui, board.getGameState(), this, sfx);
        recorder.setController(controller);
        recorder.setLevelState(this);
        recorder.setUI(ui);
        bgm = new Sound();
        this.uiState = ui;

        switch(board.getGameState()){
            case LEVEL1 -> levelNumber = 1;
            case LEVEL2 -> levelNumber = 2;
        }

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        JMenuItem loadMenuItem = new JMenuItem("Load");

        fileMenu.add(saveMenuItem);
        fileMenu.add(loadMenuItem);

        menuBar.add(fileMenu);
        ui.setJMenuBar(menuBar);

        /**
         * Action listeners for loading and saving
         */

        loadMenuItem.addActionListener(e -> {
            load(board, ui);
        });


        saveMenuItem.addActionListener(e -> {
            save(board, ui);
        });




        JPanel levelPanel = new JPanel(new BorderLayout());
        levelPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        levelPanel.setPreferredSize(new Dimension(800, 600));
        levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.X_AXIS));

        renderer = new Renderer(board); //the panel that will contain the maze
        JPanel rendererContainer = new JPanel();
        rendererContainer.setLayout(new BoxLayout(rendererContainer, BoxLayout.Y_AXIS));
        rendererContainer.add(Box.createVerticalStrut(50));
        rendererContainer.add(renderer);


        JPanel levelLabelPanel = new JPanel();
        levelLabelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel levelLabel = new JLabel("Level: " + levelNumber);
        levelLabel.setFont(new Font("Dialog", Font.BOLD, 24));
        levelLabelPanel.add(levelLabel);

        levelPanel.add(levelLabelPanel);
        levelPanel.add(rendererContainer);
        levelPanel.add(Box.createHorizontalGlue());


        JPanel infoPanel = new JPanel(); //panel containing info about the game. Has 3 rows, one for time left, one for treasure left, one for the level and one for inventory
        infoPanel.setPreferredSize(new Dimension(150, 500));

        /*
            Button for pausing
         */
        JButton pauseButton = new JButton("Pause");
        pauseButton.setPreferredSize(new Dimension(150, 40));
        pauseButton.addActionListener(e -> pauseGame(ui, board));

        /*
         * JButtons for calling recorder methods for moving a step back, moving a step forward, auto replay, and setting the speed.
         */

        JButton stepBackButton = new JButton("-");
        JButton stepForwardButton = new JButton("+");


        stepBackButton.setPreferredSize(new Dimension(75, 40));
        stepForwardButton.setPreferredSize(new Dimension(75, 40));

        JTextField speedTextField = new JTextField(5);
        JLabel speedLabel = new JLabel("Replay Speed: ");


        /**
         * Action listener for step back button
         */

        stepBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (recorder != null) {
                    recorder.stepBackward();  // Undo the last action
                }
            }
        });

        /**
         * Action listener for step forward button
         */
        stepForwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                recorder.stepForward();  // Redo the last undone action
            }
        });









        JButton helpButton = getjButton();

        /*
         * JButtons for changing volume
         */
        JButton volumeUpButton = new JButton("Vol+");
        JButton volumeDownButton = new JButton("Vol-");

        volumeUpButton.setPreferredSize(new Dimension(75, 40));
        volumeDownButton.setPreferredSize(new Dimension(75, 40));

        volumeUpButton.addActionListener(e -> {
            bgm.increaseVolume(5);
            ui.requestFocusInWindow();

        });
        volumeDownButton.addActionListener(e -> {
            bgm.decreaseVolume(5);
            ui.requestFocusInWindow();
        });



        /*
            Labels for time and treasure
         */
        timeLabel = new JLabel(String.valueOf(timeLeft), SwingConstants.CENTER);
        treasureLabel = new JLabel("Treasure left: " + board.getChap().getTreasureLeft());
        timeLabel.setFont(new Font("Dialog", Font.BOLD, 48));


        /*
            Text area displaying current inventory
         */

        inventoryArea = new JTextArea();
        inventoryArea.setEditable(false);
        inventoryArea.setBorder(new LineBorder(Color.BLACK));
        inventoryArea.setPreferredSize(new Dimension(150, 80));


        infoPanel.add(timeLabel);
        infoPanel.add(pauseButton);
        infoPanel.add(stepBackButton);
        infoPanel.add(stepForwardButton);
        infoPanel.add(speedLabel);
        infoPanel.add(speedTextField);
        infoPanel.add(volumeDownButton);
        infoPanel.add(volumeUpButton);
        infoPanel.add(treasureLabel);
        infoPanel.add(inventoryArea);
        infoPanel.add(helpButton);



        levelPanel.add(infoPanel);

        JPanel containerPanel = new JPanel(); //Will hold everything in the level here, so that the size of levelpanel stays the same even when entering full screen.
        containerPanel.add(levelPanel);
        ui.setFocusable(true);
        ui.requestFocusInWindow();
        ui.add(containerPanel, BorderLayout.CENTER);
        ui.addKeyListener(controller);


        gameUpdateTimer = new Timer(100, e -> update(ui)); //updates game state every 100 ms.
        gameUpdateTimer.restart();
        gameUpdateTimer.start();
        Timer timer = new Timer(1000, e -> updateTimeLeft(ui, board)); //updates the timer every second.
        timer.start();

        containerPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ui.requestFocusInWindow();
            }
        });


        ui.setCloseUI(() -> {
            timer.stop();  // Stop the timer
            ui.remove(containerPanel);
            ui.removeKeyListener(controller);
            ui.setJMenuBar(null);
        });

        /*
        Playing background music
         */
        switch(currentGameState){
            case LEVEL1 -> bgm.setFile(0); //sets the background music to play
            case LEVEL2 -> bgm.setFile(1);
        }

        bgm.play();
        bgm.loop();
        bgm.setVolume(-30);



    }

    /**
     * Method that is called to display the help button.
     * @return helpButton
     */

    private static JButton getjButton() {
        JButton helpButton = new JButton("Help");
        helpButton.setPreferredSize(new Dimension(150, 40));
        helpButton.addActionListener(e -> {
            String message = "<html>"
                    + "<p> Controls: Arrow Keys to move Larry. Space to pause. CTRL+X to exit. CTRL+S to save. </p>"
                    + "<p> The colour of a key corresponds to the colour of a locked door. </p>"
                    + "<p> Avoid the enemies.</p>"
                    + "<p> Collect all the treasure within the maze. </p>"
                    + "<p> Once you have collected all the treasure, get to the exit before the time runs out to beat the level. </p>"
                    + "</html>";
            JOptionPane.showMessageDialog(null, message, "Guide", JOptionPane.INFORMATION_MESSAGE);

        });
        return helpButton;
    }


    /**
     * closeState method
     *
     * Stops all music, resets timer, and calls closeUI to remove all previous swing elements form the board.
     * @param ui
     */
    @Override
    public void closeState(UI ui) {
        if (bgm != null) bgm.stop();
        if(gameUpdateTimer != null) gameUpdateTimer.stop();
        timeLeft = 100;
        ui.closeUI.run();
    }


    /**
     * Update method
     * Updates the game state every 100 ms. Used to update treasure count, inventory, and check for GameState changes.
     * @param ui
     */
    @Override
    public void update(UI ui) {
        updateTreasureLeft();
        updateInventory();

        if(board.getGameState() == GameState.ENDED){
            this.closeState(ui);
            ui.changeState(new MainMenuState());
        }
    }


    /**
     * updateTreasureLeft
     *
     * Updates the treasure left field based on the amount of treasure the chap object has left to collect.
     * The amount of treasure left is defined in the JSON file and stored in chap.
     */

    private void updateTreasureLeft() {
        int treasureLeft = board.getChap().getTreasureLeft();
        treasureLabel.setText("Treasure Left: " + treasureLeft);
    }


    /**
     * updateInventory
     *
     * Updates the inventory with the current set of keys stored within chap.
     */
    private void updateInventory() {
        String keys = board.getChap().getKeys().stream()
                .map(key -> key.colour().name().charAt(0) + key.colour().name().substring(1).toLowerCase() + " Key") //Formats the keys. Makes the remaining letters in the name of a colour lower case (i.e. RED becomes Red)
                .toList()
                .toString();
        inventoryArea.setText(keys);
    }


    /**
     * Updates time left if the board is not paused.
     * Called every second by the gameUpdateTimer.
     * @param ui
     * @param board
     */
    private void updateTimeLeft(UI ui, Board board) {
        if(board.getGameState() != GameState.PAUSED) {
            timeLeft--;
            timeLabel.setText(String.valueOf(timeLeft));
            if (timeLeft <= 0) {
                board.setGameState(GameState.START);
                this.closeState(ui);
                ui.changeState(new MainMenuState());
            }
        }
    }


    /**
     * pauseGame
     * sets the gameState to paused, opens a window telling the user the game is paused.
     * Unpauses the game when the window is closed.
     * @param ui
     * @param board
     */

    public void pauseGame(UI ui, Board board){
        board.setGameState(Board.GameState.PAUSED);
        JOptionPane.showMessageDialog(null, "Game is Paused");
        board.setGameState(currentGameState);//when the user clicks off the message, pause is false again.
    }

    /**
     * resetLevel
     * Stops the timer, closes state, and creates a new LevelState object that the state changes to.
     * Used for
     * @param ui
     */
    public void resetLevel(UI ui) {
        if(gameUpdateTimer != null) gameUpdateTimer.stop();
        //renderer.repaint();
        closeState(ui);
        SwingUtilities.invokeLater(() -> ui.changeState(new LevelState()));
        //resets the state.
    }

    /**
     * getBoard
     * returns the current board
     * @return board
     */
    public Board getBoard(){
        return board;
    }


    /**
     * Load method
     *
     * Opens a prompt for the user to select a json file to load from, then creates a level object from that json file,
     * then calls the loadBoard method using the tiles/chap/thief within that level object.
     * @param board
     */

    public void load(Board board, UI ui){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File("src" + File.separator + "Persistency" + File.separator + "level" + File.separator));
        fileChooser.setDialogTitle("Select Level File to Load");
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getPath();
            Level loadedLevel = Persistency.loadLevel(filePath);
            if(loadedLevel != null){
                //set the game state to level 1 or 2 depending on if the loadedLevel is a level1 object or level2 object.
                if (loadedLevel instanceof Level1) {
                    board.loadBoard(loadedLevel.tiles(), loadedLevel.chap(), loadedLevel.thief(), GameState.LEVEL1);
                } else if (loadedLevel instanceof Level2) {
                    board.loadBoard(loadedLevel.tiles(), loadedLevel.chap(), loadedLevel.thief(), GameState.LEVEL2);
                }
                System.out.println("Row" + getBoard().getChap().getRow() + "Col:" + getBoard().getChap().getCol());
                System.out.println("Level loaded");

            }
            resetLevel(ui);


        } else{
            System.out.println("Level failed to load");
        }
    }

    /**
     * Save method
     *
     * Method to be called when clicking the save button. Grabs the current board, creates a level1 or level2 object
     * depending on the level, then calls the persistency
     * @param board
     * @param ui
     */

    public void save(Board board, UI ui){
        if (currentGameState == GameState.LEVEL1 || currentGameState == GameState.LEVEL2) {
            String baseDir = "src" + File.separator + "Persistency" + File.separator + "level" + File.separator;
            String fileName = "level_" + System.currentTimeMillis() + ".json";
            String filePath = baseDir + fileName;

            Level levelToSave = null;
            switch (currentGameState) {
                case LEVEL1 -> levelToSave = new Level1(board.getTiles(), board.getChap());
                case LEVEL2 -> levelToSave = new Level2(board.getTiles(), board.getChap(), board.getThief());
            }
            System.out.println(levelToSave.toString());
            boolean success = Persistency.save(filePath, levelToSave);

            if (success) {
                JOptionPane.showMessageDialog(ui, "Level saved as " + fileName);
            } else {
                JOptionPane.showMessageDialog(ui, "Error saving level.");
            }
        }

    }

    /**
     * GetController
     * returns the controller - used by recorder.
     * @return
     */
    public Controller getController() {
        return this.controller;
    }






}