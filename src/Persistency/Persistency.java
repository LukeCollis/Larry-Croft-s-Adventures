package Persistency;
import Domain.Board.Board;
import org.json.JSONObject;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Represents the centralised class for all group members to call methods in relation to the persistency module.
 * Handles loading and saving of levels by calling methods from Save and Parse in an easy-to-access place.
 */
public class Persistency{

    private final Board board;
    private static Level1 level1;
    private static Level2 level2;

    /**
     * Returns the current board.
     *
     * @return the board object
     */
    public Board getBoard(){
        return board;
    }

    /**
     * Initializes persistency object with a given board and loads levels 1 and 2.
     *
     * @param board the game board
     */
    public Persistency(Board board){
        this.board = board;
        loadLevel1();
        loadLevel2();
    }

    /**
     * Loads level 1 from the Json file.
     */
    public static void loadLevel1(){
        level1 = (Level1) loadLevel("src" + File.separator + "Persistency" + File.separator + "level" + File.separator + "level1.json");
    }

    /**
     * Loads level 2 from the Json file.
     */
    public static void loadLevel2(){
        level2 = (Level2) loadLevel("src" + File.separator + "Persistency" + File.separator + "level" + File.separator + "level2.json");
    }

    /**
     * Loads a level from the specified file.
     *
     * @param file the file path to load
     * @return the loaded level
     */
    public static Level loadLevel(String file){
        if(!file.contains(".json")){
            throw new IllegalArgumentException("Must end with .json");
        }
        JSONObject json = getJson(file);
        Level level = Parse.parse(json);
        return level;
    }

    /**
     * Reads the Json content from a file.
     *
     * @param file the file path
     * @return the Json object
     */
    public static JSONObject getJson(String file){
        StringBuilder json = new StringBuilder();
        try(Scanner sc = new Scanner(new File(file))){
            while(sc.hasNextLine()){
                json.append(sc.nextLine().trim());
            }
        }
        catch(IOException e){
            System.err.println("Error reading JSON file: " + e.getMessage());
            return null;
        }
        return new JSONObject(json.toString());
    }

    /**
     * Returns the loaded Level 1.
     *
     * @return Level 1 object
     */
    public static Level1 getLevel1(){
        return level1;
    }

    /**
     * Returns the loaded Level 2.
     *
     * @return Level 2 object
     */
    public static Level2 getLevel2() { return level2; }

    /**
     * Saves the given level to the specified file.
     *
     * @param file the file path to save to
     * @param level the level to save
     * @return true if saved successfully, false otherwise
     */
    public static boolean save(String file, Level level){
        if(!file.endsWith(".json")){
            throw new IllegalArgumentException("Must end with .json");
        }
        JSONObject saveJson = Save.saveJson(level);

        File newFile = new File(file);
        try(FileWriter jsonLevel = new FileWriter(newFile)){
            jsonLevel.write(saveJson.toString());
            return true;
        }
        catch(IOException e){
            return false;
        }
    }
}
