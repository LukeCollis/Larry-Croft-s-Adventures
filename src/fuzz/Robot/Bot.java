package fuzz.Robot;

import App.UI.LevelState;
import Domain.Tile.Tile;


import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

/**
 * The Bot class is responsible for navigating a game board by simulating a player’s movements using
 * a set of pre-defined actions. The bot uses a `Robot` to simulate key presses and interacts with the
 * game’s board to explore and map out tiles, making decisions based on visited tiles.
 * <p>
 * The bot prioritizes tiles it has visited the least, adjusting its strategy if it remains on the same
 * tile for too long. The bot continuously updates its surroundings and makes movement decisions in a loop
 * until the test’s time limit is reached.
 */
public class Bot {

    private ArrayList<ArrayList<MapTile>> map = new ArrayList<>();//2D Array the Games Map of tiles for tracking location
    private final ArrayList<KeyAction> moveActions = new ArrayList<>(List.of(KeyAction.UP,
            KeyAction.DOWN, KeyAction.LEFT, KeyAction.RIGHT));//Possible Moves that the bot can make
    private ArrayList<KeyAction> possibleMoves = new ArrayList<>();//Moves that are viable when robot analyzes its surroundings
    private Point prevPoint = null;
    private int numTimesOnSameTile = 0;
    private KeyAction lastMove = null;
    private final RobotChap robotChap;
    private Point robotCurrentPos;
    private final Robot robot;//java.awt.Robot


    /**
     * Initializes the Bot class, setting up the game environment and mapping out the board tiles.
     * <p>
     * This constructor configures the game by disabling popup dialogs and initializing the bot’s position
     * on the board at the player's current location. It also prepares a 32x32 map to track which tiles
     * the bot has visited, enabling the bot to make decisions about movement.
     *
     * @param bot   The `Robot` instance used to simulate key presses and interact with the game.
     * @param state The current game state, providing access to the board and its configuration for mapping
     *              the environment and setting gameplay options.
     */
    public Bot(Robot bot, LevelState state) {
        this.robot = bot;

        state.getBoard().setSendDialog(false);//Makes it so no JPanels will pop up when certain tiles are interacted with

        robotChap = new RobotChap(state.getBoard().getChap());//Will hold Player tile
        robotCurrentPos = robotChap.getPosition();//Holds the chap position within the 2D array map

        IntStream.range(0, 32).forEach(row -> {
            map.add(new ArrayList<>(Collections.nCopies(32, null))); // Or initialize with specific size
        });//inits size of map

        IntStream.range(0, 32).forEach(row ->
                IntStream.range(0, 32).forEach(col -> {
                    Tile.TileType type = state.getBoard().getTileAt(row, col).getTileType();
                    MapTile mapTile = new MapTile(type);
                    map.get(row).set(col, mapTile);
                })
        );//Will analyze the @board from the game level and determine will save info in @map for Robot to use for tracking


    }

    /**
     * Starts the bot's movement loop, which continues until the test times out or completes.
     * <p>
     * This method initiates the bot's main update cycle, where it continuously evaluates its surroundings
     * and makes decisions about movement based on the tiles it has visited.
     */
    public void run(){
        update();
    }

    /**
     * Updates the bot's surroundings and decides its next move.
     * <p>
     * This method repeatedly analyzes the bot's current position, checks the surrounding tiles, and updates
     * the bot's movement accordingly. The bot’s movement decisions are based on the number of visits to
     * surrounding tiles, prioritizing tiles it has visited the least.
     */
    private void update(){
        mapSurroundings();
        decide();
        update();//Reruns update so Bot will stay running

    }

    /**
     * Determines the bot's next move based on the number of visits to surrounding tiles.
     * <p>
     * The bot evaluates the tiles around its current position, prioritizing those it has visited the least.
     * If the bot remains on the same tile for too long, it will attempt to make a different move to avoid
     * staying in the same place.
     */
    private void decide(){

        //Makes sure the map notes that the robot has visited the tile it is on
        map.get(robotCurrentPos.row()).get(robotCurrentPos.col()).incrementNumVisit();

        moveActions.stream()
                .filter(KeyAction::canMove)
                .forEach(possibleMoves::add);//Fills list with possible moves the robot can do

        int minVisits = possibleMoves.stream()
                .mapToInt(KeyAction::getNumVisit)
                .min()
                .orElse(Integer.MAX_VALUE);//finds tile with the least amount of visits within bot possible movements

        possibleMoves.removeIf(action -> action.getNumVisit() > minVisits);//removes ones with > than the min NumVisits

        //If the robot gets stuck on a tile for to long
        //it will then record how long it has been there for
        //if it is > 3 then the robot will delete its possible move and either got LEFT or DOWN
        //Which where chosen as they can get the robot out of most stuck scenarios
        if(prevPoint != null && prevPoint.equals(robotCurrentPos)){
            if(numTimesOnSameTile > 3){
                possibleMoves.remove(lastMove);
                numTimesOnSameTile = 0;
                possibleMoves.addAll(List.of(KeyAction.DOWN,KeyAction.LEFT));
            }else{
                numTimesOnSameTile++;
            }

        }

        System.out.println(possibleMoves);//Prints the robots current possible moves
        Random random = new Random();//Enables to select random index of possible moves
        int randomInt = random.nextInt(possibleMoves.size());
        lastMove = possibleMoves.get(randomInt).move(robot);//Robot moves

        possibleMoves = new ArrayList<>();//resets movement list for next run

        //saves required data
        prevPoint = robotCurrentPos;
        robotCurrentPos = robotChap.getPosition();

    }

    /**
     * Updates the surrounding tiles for each direction, linking them to the appropriate movement actions.
     * <p>
     * This method evaluates the tiles adjacent to the bot's current position and updates the corresponding
     * `KeyAction` enums to reflect whether the bot can move to each tile. It assesses whether each tile
     * is a valid destination based on its type.
     */
    private void mapSurroundings(){
        //sets KeyAction Enum to new tiles that surround the Bot for next run of decisions
        KeyAction.UP.updateTileValue(map.get(robotCurrentPos.row() - 1).get(robotCurrentPos.col()));
        KeyAction.DOWN.updateTileValue(map.get(robotCurrentPos.row() + 1).get(robotCurrentPos.col()));
        KeyAction.LEFT.updateTileValue(map.get(robotCurrentPos.row()).get(robotCurrentPos.col() - 1));
        KeyAction.RIGHT.updateTileValue(map.get(robotCurrentPos.row()).get(robotCurrentPos.col() + 1));
    }





}