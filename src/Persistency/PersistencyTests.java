package Persistency;

import Domain.Board.Board;
import Domain.Tile.Objects.Key;
import Domain.Tile.Tile;
import Domain.Tile.MovingTile.Chap;
import Domain.Tile.MovingTile.Thief;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PersistencyTests {

    private Board mockBoard;
    private Persistency persistency;

    @BeforeEach
    void setUp() {
        mockBoard = new Board();
        persistency = new Persistency(mockBoard);
    }

    // Level Tests
    @Test
    void testLevel1Constructor() {
        Tile[][] tiles = new Tile[32][32];
        Chap chap = new Chap(0, 0, 0, null);

        Level1 level1 = new Level1(tiles, chap);
        assertNotNull(level1);
        assertArrayEquals(tiles, level1.tiles());
        assertEquals(chap, level1.chap());
    }

    @Test
    void testLevel1ConstructorNullTiles() {
        Chap chap = new Chap(0, 0, 0, null);
        assertThrows(IllegalArgumentException.class, () -> new Level1(null, chap));
    }

    @Test
    void testLevel1ConstructorNullChap() {
        Tile[][] tiles = new Tile[32][32];
        assertThrows(IllegalArgumentException.class, () -> new Level1(tiles, null));
    }

    @Test
    void testLevel2Constructor() {
        Tile[][] tiles = new Tile[32][32];
        Chap chap = new Chap(0, 0, 0, null);
        Thief thief = new Thief(1, 1);

        Level2 level2 = new Level2(tiles, chap, thief);
        assertNotNull(level2);
        assertArrayEquals(tiles, level2.tiles());
        assertEquals(chap, level2.chap());
        assertEquals(thief, level2.thief());
    }

    @Test
    void testLevel2ConstructorNullTiles() {
        Chap chap = new Chap(0, 0, 0, null);
        Thief thief = new Thief(1, 1);
        assertThrows(IllegalArgumentException.class, () -> new Level2(null, chap, thief));
    }

    @Test
    void testLevel2ConstructorNullChap() {
        Tile[][] tiles = new Tile[32][32];
        Thief thief = new Thief(1, 1);
        assertThrows(IllegalArgumentException.class, () -> new Level2(tiles, null, thief));
    }

    @Test
    void testLevel2ConstructorNullThief() {
        Tile[][] tiles = new Tile[32][32];
        Chap chap = new Chap(0, 0, 0, null);
        assertThrows(IllegalArgumentException.class, () -> new Level2(tiles, chap, null));
    }

    // Parse Tests
    @Test
    void testParseLevel1() {
        String jsonString = "{" + "\"tiles\": {" + "\"exitLock\": [], " + "\"exit\": [], " + "\"info\": [], " + "\"key\": [], " + "\"lockedDoors\": [], " + "\"treasure\": [], " + "\"walls\": [], " + "\"bombs\": [], " + "\"vents\": []" + "}, " + "\"chap\": {\"x\": 0, \"y\": 0, \"treasures\": 0, \"keys\": []}" + "}";

        JSONObject json = new JSONObject(jsonString);

        Level level = Parse.parse(json);
        assertInstanceOf(Level1.class, level);
    }


    @Test
    void testParseLevel2() {
        String jsonString = "{" + "\"tiles\": {" + "\"exitLock\": [], " + "\"exit\": [], " + "\"info\": [], " + "\"key\": [], " + "\"lockedDoors\": [], " + "\"treasure\": [], " + "\"walls\": [], " + "\"bombs\": [], " + "\"vents\": []" + "}, " + "\"chap\": {\"x\": 0, \"y\": 0, \"treasures\": 0, \"keys\": []}, " + "\"thief\": {\"x\": 1, \"y\": 1}" + "}";

        JSONObject json = new JSONObject(jsonString);

        Level level = Parse.parse(json);
        assertInstanceOf(Level2.class, level);
    }

    // Persistency Tests
    @Test
    void testGetBoard() {
        assertEquals(mockBoard, persistency.getBoard());
    }

    @Test
    void testLoadLevel1() {
        Persistency.loadLevel1();
        Level1 level1 = Persistency.getLevel1();
        assertNotNull(level1);
    }

    @Test
    void testLoadLevel2() {
        Persistency.loadLevel2();
        Level2 level2 = Persistency.getLevel2();
        assertNotNull(level2);
    }

    @Test
    void testSaveValidFile() {
        Tile[][] tiles = new Tile[32][32];
        List<Key> keys = new ArrayList<>();
        Chap chap = new Chap(0, 0, 0, keys);
        Thief thief = new Thief(1, 1);
        Level level = new Level2(tiles, chap, thief);

        String filePath = "src" + File.separator + "Persistency" + File.separator + "level" + File.separator + "testLevel.json";
        boolean saved = Persistency.save(filePath, level);
        assertTrue(saved);
    }


    @Test
    void testSaveInvalidFile() {
        Tile[][] tiles = new Tile[32][32];
        Chap chap = new Chap(0, 0, 0, null);
        Thief thief = new Thief(1, 1);
        Level level = new Level2(tiles, chap, thief);

        String filePath = "invalid/path/level";
        assertThrows(IllegalArgumentException.class, () -> Persistency.save(filePath, level));
    }
}

