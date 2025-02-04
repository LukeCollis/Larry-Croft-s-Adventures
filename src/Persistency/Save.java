package Persistency;

import Domain.Tile.*;
import Domain.Tile.MovingTile.Thief;
import Domain.Tile.MovingTile.Chap;
import Domain.Tile.Objects.Key;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Collection;

/**
 * Represents all the functionality for saving Level objects into Json files
 */
public class Save{

    /**
     * Converts a Level object to a Json object.
     *
     * @param save the Level object to save
     * @return a JSONObject representing the Level
     */
    public static JSONObject saveJson(Level save){
        JSONObject json = new JSONObject();
        JSONObject tiles = saveTiles(save.tiles());
        JSONObject chap = saveChap(save.chap());

        json.put("tiles", tiles);
        json.put("chap", chap);

        Thief thief = save.thief();
        if (thief != null) {
            JSONObject thiefJson = saveThief(thief);
            json.put("thief", thiefJson);
        }

        return json;
    }

    /**
     * Converts a 2D array of tiles into a Json object.
     *
     * @param t 2D array of tiles
     * @return a JSONObject representing the tiles
     */
    public static JSONObject saveTiles(Tile[][] t) {
        JSONObject tiles = new JSONObject();
        JSONArray exitLock = new JSONArray();
        JSONArray exit = new JSONArray();
        JSONArray info = new JSONArray();
        JSONArray key = new JSONArray();
        JSONArray lockedDoor = new JSONArray();
        JSONArray treasure = new JSONArray();
        JSONArray wall = new JSONArray();
        JSONArray bomb = new JSONArray();
        JSONArray vents = new JSONArray();

        for (int x = 0; x < 32; x++) {
            for (int y = 0; y < 32; y++) {
                Tile tile = t[x][y];

                if(tile instanceof ExitLockTile){
                    addTile(exitLock, x, y);
                }
                else if (tile instanceof ExitTile){
                    addTile(exit, x, y);
                }
                else if(tile instanceof InfoTile infoTile){
                    JSONObject infoObject = new JSONObject();
                    infoObject.put("x", x);
                    infoObject.put("y", y);
                    infoObject.put("text", infoTile.text());
                    tiles.put("info", infoObject);
                }
                else if(tile instanceof KeyTile keyTile){
                    addKeyTile(key, x, y, keyTile.key());
                }
                else if(tile instanceof LockedDoorTile lockedDoorTile){
                    addLockedDoorTile(lockedDoor, x, y, lockedDoorTile.keyRequired());
                }

                else if(tile instanceof TreasureTile){
                    addTile(treasure, x, y);
                }
                else if(tile instanceof WallTile){
                    addTile(wall, x, y);
                }
                else if(tile instanceof BombTile){
                    addTile(bomb, x, y);
                }
                else if(tile instanceof VentTile ventTile){
                    JSONObject ventObject = new JSONObject();
                    ventObject.put("x", x);
                    ventObject.put("y", y);
                    VentTile ventTo = ventTile.getLinkedVent();
                    if(ventTo != null){
                        ventObject.put("ventToX", ventTo.getRow());
                        ventObject.put("ventToY", ventTo.getCol());
                    }
                    vents.put(ventObject);
                }
            }
        }

        tiles.put("exitLock", exitLock);
        tiles.put("exit", exit);
        tiles.put("info", info);
        tiles.put("key", key);
        tiles.put("lockedDoors", lockedDoor);
        tiles.put("treasure", treasure);
        tiles.put("walls", wall);
        tiles.put("bombs", bomb);

        return tiles;
    }

    /**
     * Adds tile coordinates to a JSON array.
     *
     * @param arr the JSONArray to add to
     * @param x the x-coordinate of the tile
     * @param y the y-coordinate of the tile
     */
    public static void addTile(JSONArray arr, int x, int y){
        JSONObject object = new JSONObject();
        object.put("x", x);
        object.put("y", y);
        arr.put(object);
    }

    /**
     * Adds a key tile's information to a JSON array.
     *
     * @param jsonArray the JSONArray to add to
     * @param x the x-coordinate of the key tile
     * @param y the y-coordinate of the key tile
     * @param key the Key associated with the tile
     */
    private static void addKeyTile(JSONArray jsonArray, int x, int y, Key key) {
        JSONObject jsonKeyTile = new JSONObject();
        jsonKeyTile.put("x", x);
        jsonKeyTile.put("y", y);
        JSONObject keyObject = new JSONObject();
        keyObject.put("colour", key.colour().name());
        jsonKeyTile.put("key", keyObject);
        jsonArray.put(jsonKeyTile);
    }

    /**
     * Adds a locked door tile's information to a JSON array.
     *
     * @param jsonArray the JSONArray to add to
     * @param x the x-coordinate of the locked door tile
     * @param y the y-coordinate of the locked door tile
     * @param key the Key required to unlock the door
     */
    private static void addLockedDoorTile(JSONArray jsonArray, int x, int y, Key key) {
        JSONObject jsonLockedDoorTile = new JSONObject();
        jsonLockedDoorTile.put("x", x);
        jsonLockedDoorTile.put("y", y);
        JSONObject lockedDoorKeyObject = new JSONObject();
        lockedDoorKeyObject.put("colour", key.colour().name());
        jsonLockedDoorTile.put("lockedDoorKey", lockedDoorKeyObject);
        jsonArray.put(jsonLockedDoorTile);
    }

    /**
     * Converts a Chap object to a Json object.
     *
     * @param c the Chap object to save
     * @return a JSONObject representing the Chap
     */
    public static JSONObject saveChap(Chap c){
        JSONObject chap = new JSONObject();
        if(c != null){
            chap.put("x", c.getRow());
            chap.put("y", c.getCol());
            chap.put("keys", saveKeys(c.getKeys()));
            chap.put("treasures", c.getTreasureLeft());
        }
        return chap;
    }

    /**
     * Converts a set of keys to a Json array.
     *
     * @param keys the list of keys to save
     * @return a JSONArray representing the keys
     */
    public static JSONArray saveKeys(Collection<Key> keys){
        JSONArray keyArray = new JSONArray();
        for(Key key : keys){
            JSONObject keyObject = new JSONObject();
            keyObject.put("colour", key.colour().name());
            keyArray.put(keyObject);
        }
        return keyArray;
    }

    /**
     * Converts a Thief object to a Json object.
     *
     * @param t the Thief object to save
     * @return a JSONObject representing the Thief
     */
    public static JSONObject saveThief(Thief t){
        JSONObject thief = new JSONObject();
        if(t != null){
            thief.put("x", t.getRow());
            thief.put("y", t.getCol());
        }
        return thief;
    }
}
