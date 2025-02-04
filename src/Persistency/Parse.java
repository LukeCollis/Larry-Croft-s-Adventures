package Persistency;

import Domain.Tile.*;
import Domain.Tile.MovingTile.Chap;
import Domain.Tile.MovingTile.Thief;
import Domain.Tile.Objects.Key;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents all the parsing functionality for parsing Json files into Level objects
 */

public class Parse{

    /**
     * Parses a Json object into a level object
     *
     * @param json object
     * @return
     */
    public static Level parse(JSONObject json){
        JSONObject jsonTiles = json.getJSONObject("tiles");
        JSONObject jsonChap = json.getJSONObject("chap");

        Tile[][] tiles = parseTiles(jsonTiles);
        Chap chap = parseChap(jsonChap);

        if(json.has("thief")){
            JSONObject jsonThief = json.getJSONObject("thief");
            Thief thief = parseThief(jsonThief);
            return new Level2(tiles, chap, thief);
        }
        else{
            return new Level1(tiles, chap);
        }
    }

    /**
     * Parses a Json object into a 2D array of tiles
     *
     * @param json object
     * @return 2D array of tiles
     */
    public static Tile[][] parseTiles(JSONObject json){
        Tile[][] tiles = new Tile[32][32];

        JSONArray exitLock = json.getJSONArray("exitLock");
        JSONArray exit = json.getJSONArray("exit");
        JSONArray info = json.getJSONArray("info");
        JSONArray key = json.getJSONArray("key");
        JSONArray lockedDoor = json.getJSONArray("lockedDoors");
        JSONArray treasure = json.getJSONArray("treasure");
        JSONArray walls = json.getJSONArray("walls");
        JSONArray bomb = json.has("bombs") ? json.getJSONArray("bombs"): new JSONArray();
        JSONArray vents = json.has("vents") ? json.getJSONArray("vents"): new JSONArray();

        parseTileArray(tiles, exitLock, (x, y, tileData) -> new ExitLockTile());
        parseTileArray(tiles, exit, (x, y, tileData) -> new ExitTile());
        parseTileArray(tiles, info, (x, y, tileData) -> new InfoTile(tileData.getString("text")));

        parseTileArray(tiles, key, (x, y, tileData) -> {
            JSONObject keyData = tileData.getJSONObject("key");
            String color = keyData.getString("colour");
            Tile.KeyColour keyColour = Tile.KeyColour.valueOf(color.toUpperCase());
            Key keyInstance = new Key(keyColour);
            return new KeyTile(keyInstance);
        });

        parseTileArray(tiles, lockedDoor, (x, y, tileData) -> {
            JSONObject lockedDoorKeyData = tileData.getJSONObject("lockedDoorKey"); // Updated key name
            String color = lockedDoorKeyData.getString("colour");
            Tile.KeyColour keyColour = Tile.KeyColour.valueOf(color.toUpperCase());
            Key lockedDoorKeyInstance = new Key(keyColour);
            return new LockedDoorTile(lockedDoorKeyInstance);
        });

        parseTileArray(tiles, treasure, (x, y, tileData) -> new TreasureTile());
        parseTileArray(tiles, walls, (x, y, tileData) -> new WallTile());
        if(bomb != null){
            parseTileArray(tiles, bomb, (x, y, tileData) -> new BombTile());
        }

        if(vents.length() == 2){
            VentTile[] ventsArray = new VentTile[2];

            for(int i = 0; i < vents.length(); i++){
                JSONObject ventData = vents.getJSONObject(i);
                int x = ventData.getInt("x");
                int y = ventData.getInt("y");
                tiles[x][y] = new VentTile(x, y);
                ventsArray[i] = (VentTile) tiles[x][y];
            }

            for(int i = 0; i < vents.length(); i++){
                JSONObject ventData = vents.getJSONObject(i);
                JSONObject linkedVentData = ventData.getJSONObject("linkedVent");
                int linkedX = linkedVentData.getInt("x");
                int linkedY = linkedVentData.getInt("y");
                VentTile currentVent = ventsArray[i];
                VentTile linkedVent = (VentTile) tiles[linkedX][linkedY];
                currentVent.setLinkedVent(linkedVent);
            }
        }

        for(int i = 0; i < 32; i++){
            for(int j = 0; j < 32; j++){
                if(tiles[i][j] == null){
                    tiles[i][j] = new FreeTile();
                }
            }
        }

        return tiles;
    }

    /**
     * Parses a tile array from a Json array using a given tile constructor
     *
     * @param tiles 2D array
     * @param jsonArray
     * @param tileConstructor
     */
    private static void parseTileArray(Tile[][] tiles, JSONArray jsonArray, TileParser tileConstructor){
        for(Object obj : jsonArray){
            JSONObject tileData = (JSONObject) obj;
            int x = tileData.getInt("x");
            int y = tileData.getInt("y");
            tiles[x][y] = tileConstructor.parse(x, y, tileData);
        }
    }

    /**
     *  Functional interface for parsing a tile at specific coordinates from Json data
     */
    @FunctionalInterface
    private interface TileParser{
        Tile parse(int x, int y, JSONObject tileData);
    }

    /**
     * Parses a chap object from a Json object
     *
     * @param json
     * @return
     */
    public static Chap parseChap(JSONObject json){
        int row = json.getInt("x");
        int col = json.getInt("y");
        int treasureLeft = json.getInt("treasures");
        List<Key> keys = parseKeys(json.getJSONArray("keys"));
        return new Chap(row, col, treasureLeft, keys);
    }

    /**
     * Parses a thief object from a Json object
     *
     * @param json
     * @return
     */
    public static Thief parseThief(JSONObject json){
        int row = json.getInt("x");
        int col = json.getInt("y");
        return new Thief(row, col);
    }

    /**
     * Parses a list of keys from a Json array
     *
     * @param jsonKeys
     * @return
     */
    public static List<Key> parseKeys(JSONArray jsonKeys){
        List<Key> keys = new ArrayList<>();
        for (Object obj : jsonKeys) {
            JSONObject jsonKey = (JSONObject) obj;
            Tile.KeyColour colour = Tile.KeyColour.valueOf(jsonKey.getString("colour"));
            keys.add(new Key(colour));
        }
        return keys;
    }
}