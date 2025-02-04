package Domain;

import Domain.Board.Board;
import Domain.Tile.*;
import Domain.Tile.MovingTile.Chap;
import Domain.Tile.MovingTile.Thief;
import Domain.Tile.Objects.Key;
import Persistency.Persistency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import Domain.Tile.Tile.KeyColour;
import Domain.Tile.Tile.TileType;
import Persistency.Level2;

public class DomainUnitTests {

    /**
     * BOARD LOGIC
     */
    @Test
    public void testBoardCreation() {
        Board b = new Board();
        new Persistency(b);
        b.loadLevelGameState(Board.GameState.LEVEL2);

        Level2 level2 = Persistency.getLevel2();
        Tile[][] levelTiles = level2.tiles();
        Assertions.assertEquals(levelTiles, b.getTiles());

        // Testing loading Chap
        Chap levelChap = level2.chap();
        Chap boardChap = b.getChap();
        Assertions.assertNotNull(boardChap);
        Assertions.assertEquals(levelChap.getRow(), boardChap.getRow());
        Assertions.assertEquals(levelChap.getCol(), boardChap.getCol());
        Assertions.assertEquals(levelChap.getTreasureLeft(), boardChap.getTreasureLeft());

        //Testing Loading Thief
        Thief levelThief = level2.thief();
        Thief boardThief = b.getThief();
        Assertions.assertNotNull(boardThief);
        Assertions.assertEquals(levelThief.getRow(), boardThief.getRow());
        Assertions.assertEquals(levelThief.getCol(), boardThief.getCol());

        //Check board length size
        Assertions.assertEquals(32, b.getTiles().length);
        Assertions.assertThrows(IllegalArgumentException.class, () -> b.setTileAt(5, 5, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> b.setTileAt(33, 33, levelChap));
    }

    @Test
    public void testGetTileAt() {
        Board board = new Board();
        new Persistency(board);
        board.loadLevelGameState(Board.GameState.LEVEL1);
        // Valid positions
        Assertions.assertNotNull(board.getTileAt(0, 0));
        Assertions.assertNotNull(board.getTileAt(15, 15));
        Assertions.assertNotNull(board.getTileAt(31, 31));

        // Out-of-bounds positions should throw exceptions
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.getTileAt(-1, 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.getTileAt(0, -1));
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.getTileAt(32, 0));
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.getTileAt(0, 32));
    }

    @Test
    public void testSetTileAt() {
        Board board = new Board();
        new Persistency(board);
        board.loadLevelGameState(Board.GameState.LEVEL1);

        FreeTile newTile = new FreeTile();
        board.setTileAt(5, 5, newTile);
        Assertions.assertEquals(newTile, board.getTileAt(5, 5));

        // Setting tile at out-of-bounds positions should throw exceptions
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.setTileAt(-1, 0, newTile));
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.setTileAt(0, -1, newTile));
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.setTileAt(32, 0, newTile));
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.setTileAt(0, 32, newTile));

        // Setting null tile should throw an exception
        Assertions.assertThrows(IllegalArgumentException.class, () -> board.setTileAt(5, 5, null));
    }

    @Test
    public void testMoveActorIncrementally() {
        Board b = new Board();
        new Persistency(b);
        b.loadLevelGameState(Board.GameState.LEVEL2);

        Thief thief = b.getThief();
        Assertions.assertEquals(TileType.THIEF, thief.getTileType());

        // This variable may need to be increased depending on whether the thief spawns around walls
        int attempts = 2; // attempts defines how many times you want the thief to move before

        try {
            Thread.sleep((1000 * attempts) + 100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Verify that the Thief's position has changed - Position may NOT have changed
        // If thief is surrounded by walls or moves into a wall twice, you may need to up the amount of attempts
        int row = thief.getRow();
        int col = thief.getCol();
        assert row != 20 || col != 20 : "Col or Row position should have changed";
    }

    @Test
    public void testChapLogic() {
        Board b = new Board();
        new Persistency(b);
        b.loadLevelGameState(Board.GameState.LEVEL1);
        Chap chap = b.getChap();

        Tile t = b.getTileAt(15, 15);
        Assertions.assertInstanceOf(FreeTile.class, t);
        // Test Chap movement
        chap.attemptUpdatePos(b, 15, 17, chap.getRow(), chap.getCol());
        Assertions.assertInstanceOf(Chap.class, b.getTileAt(15, 17));

        Assertions.assertEquals(15, chap.getRow());
        Assertions.assertEquals(17, chap.getCol());

        Assertions.assertEquals(Persistency.getLevel1().chap().getTreasureLeft(), chap.getTreasureLeft());
        chap.setTreasureLeft(5);
        Assertions.assertEquals(5, chap.getTreasureLeft());

        Assertions.assertNotNull(chap.getKeys());
        Key blueKey = new Key(KeyColour.BLUE);
        chap.addKey(blueKey);
        Assertions.assertTrue(chap.hasKeyColour(KeyColour.BLUE));
        Assertions.assertFalse(chap.hasKeyColour(KeyColour.RED));
        Assertions.assertFalse(chap.hasKeyColour(KeyColour.GREEN));
        Key redKey = new Key(KeyColour.RED);
        chap.addKey(redKey);
        Assertions.assertEquals(2, chap.getKeys().size());
        chap.removeKey(blueKey);
        Assertions.assertEquals(1, chap.getKeys().size());
        Assertions.assertEquals("[Key[colour=RED]]", chap.getKeys().toString());
        Assertions.assertEquals(TileType.CHAP, chap.getTileType());
    }

    @Test
    public void testAttemptMoveAndRunAllTiles() {
        Board b = new Board();
        new Persistency(b);
        b.loadLevelGameState(Board.GameState.LEVEL1);
        Chap chap = b.getChap();
        // Create instances of each tile type
        Tile bombTile = new BombTile();
        Tile exitLockTile = new ExitLockTile();
        Tile exitTile = new ExitTile();
        Tile freeTile = new FreeTile();
        Tile infoTile = new InfoTile("info tile text");
        Tile keyTile = new KeyTile(new Key(Tile.KeyColour.RED));
        Tile lockedDoorTile = new LockedDoorTile(new Key(Tile.KeyColour.RED));
        Tile treasureTile = new TreasureTile();
        VentTile vent1 = new VentTile(10, 10);
        VentTile vent2 = new VentTile(13, 13);
        vent1.setLinkedVent(vent2);
        vent2.setLinkedVent(vent1);
        Tile wallTile = new WallTile();

        // Test getTileType() for each tile
        Assertions.assertEquals(TileType.BOMB, bombTile.getTileType());
        Assertions.assertEquals(TileType.EXIT_LOCK, exitLockTile.getTileType());
        Assertions.assertEquals(TileType.EXIT, exitTile.getTileType());
        Assertions.assertEquals(TileType.FREE, freeTile.getTileType());
        Assertions.assertEquals(TileType.INFO, infoTile.getTileType());
        Assertions.assertEquals(TileType.KEY_RED, keyTile.getTileType());
        Assertions.assertEquals(TileType.LOCKED_DOOR_RED, lockedDoorTile.getTileType());
        Assertions.assertEquals(TileType.TREASURE, treasureTile.getTileType());
        Assertions.assertEquals(TileType.VENT, vent1.getTileType());
        Assertions.assertEquals(TileType.WALL, wallTile.getTileType());

        // Test attemptMoveAndRun() for each tile with a MovingTile Simulation Chap

        // ExitLockTile: Should throw an error if there is treasure left
        Assertions.assertThrows(IllegalArgumentException.class, () -> exitLockTile.attemptMoveAndRun(b, chap));
        // ExitTile: Assume it allows movement
        Assertions.assertTrue(exitTile.attemptMoveAndRun(b, chap), "ExitTile should allow movement");
        // FreeTile: Allows movement
        Assertions.assertTrue(freeTile.attemptMoveAndRun(b, chap), "FreeTile should allow movement");
        // InfoTile: Allows movement and shows text
        Assertions.assertTrue(infoTile.attemptMoveAndRun(b, chap), "InfoTile should allow movement");
        // LockedDoorTile: Denies movement as Chap does not have that key.
        Assertions.assertThrows(IllegalStateException.class, () -> lockedDoorTile.attemptMoveAndRun(b, chap), "LockedDoorTile should deny movement");
        // KeyTile: Allows movement and grants a key
        Assertions.assertTrue(keyTile.attemptMoveAndRun(b, chap), "KeyTile should allow movement and grant a key");

        // LockedDoorTile: Allows movement as Chap has the key
        Assertions.assertTrue(lockedDoorTile.attemptMoveAndRun(b, chap), "LockedDoorTile should allow movement");

        // TreasureTile: Allows movement and might give treasure
        Assertions.assertTrue(treasureTile.attemptMoveAndRun(b, chap), "TreasureTile should allow movement and give treasure");

        //VENT TILE LOGIC CHECKING
        // VentTile: Assume it allows movement but assert that it teleports the character
        Assertions.assertTrue(vent1.attemptMoveAndRun(b, chap), "VentTile should allow movement and teleport the actor");
        Assertions.assertTrue(chap.getRow() == vent2.getRow() && chap.getCol() == vent2.getCol());

        Assertions.assertTrue(vent2.attemptMoveAndRun(b, chap), "VentTile should allow movement and teleport the actor");
        Assertions.assertTrue(chap.getRow() == vent1.getRow() && chap.getCol() == vent1.getCol());

        // WallTile: Blocks movement
        Assertions.assertThrows(IllegalArgumentException.class, () -> wallTile.attemptMoveAndRun(b, chap),"WallTile should block movement");

        // BombTile: Assume it explodes and denies movement
        Assertions.assertTrue(bombTile.attemptMoveAndRun(b, chap), "BombTile should deny movement");
        Assertions.assertSame(Board.GameState.RESET_LEVEL, b.getGameState());
    }
}
