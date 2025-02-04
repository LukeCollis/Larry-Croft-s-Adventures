package Renderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import Domain.Tile.Tile.TileType;
import Domain.Board.Board;
import Domain.Tile.*;
import Domain.Tile.MovingTile.*;
public class Renderer extends JPanel {
    private static final int VIEWPORT_SIZE = 9; // 9x9 viewport
    private final Board board;
    private final int TILE_SIZE = 50; // Size of each tile in pixels
    private Timer timer; // Timer for continuous movement
    private Map<TileType, Image> images_map = new HashMap<>(); //store image with name
    public Renderer(Board board) {
        this.board = board;
        loadImges();
        setPreferredSize(new Dimension(VIEWPORT_SIZE * TILE_SIZE, VIEWPORT_SIZE * TILE_SIZE));

        // init and set delay
        timer = new Timer(100, new ActionListener() { // Adjust the delay for speed
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint(); //refreshing
            }
        });
        timer.start();
    }

    /**
     *
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        render(g);
    }

    /**
     * Renders the visible portion of the map, centered around Chap
     */
    public void render(Graphics g) {
        Chap chap = board.getChap();
        int chapRow = chap.getRow();
        int chapCol = chap.getCol();

        // Calculate the top-left corner of the viewport (ensure it's within map bounds)
        int startRow = Math.max(0, chapRow - VIEWPORT_SIZE / 2);
        int startCol = Math.max(0, chapCol - VIEWPORT_SIZE / 2);

        // Ensure the viewport doesn't go outside the map boundaries
        if (startRow + VIEWPORT_SIZE > 32) {
            startRow = 32 - VIEWPORT_SIZE;
        }
        if (startCol + VIEWPORT_SIZE > 32) {
            startCol = 32 - VIEWPORT_SIZE;
        }

        // Render the 9x9 section of the map
        for (int row = startRow; row < startRow + VIEWPORT_SIZE; row++) {
            for (int col = startCol; col < startCol + VIEWPORT_SIZE; col++) {
                Tile tile = board.getTileAt(row, col);
                renderTile(g, tile, row - startRow, col - startCol);
            }
        }
        // Draw the grid lines
        g.setColor(Color.BLACK);
        for (int i = 0; i <= VIEWPORT_SIZE; i++) {
            g.drawLine(0, i * TILE_SIZE, VIEWPORT_SIZE * TILE_SIZE, i * TILE_SIZE); // Horizontal lines
            g.drawLine(i * TILE_SIZE, 0, i * TILE_SIZE, VIEWPORT_SIZE * TILE_SIZE); // Vertical lines
        }
    }

    /**
     * Renders an individual tile. Uses images for rendering.
     *
     * @param g    the Graphics context
     * @param tile the tile to render
     * @param row  the row offset for rendering
     * @param col  the column offset for rendering
     */
    private void renderTile(Graphics g, Tile tile, int row, int col) {
        g.drawImage(images_map.get(tile.getTileType()), col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
    }

    /**
     * take nothing just an helper method to load all the image required to render
     */
    private void loadImges(){
        loadImage(TileType.CHAP,"src/Resources/Chap.jpg");
        loadImage(TileType.KEY_RED,"src/Resources/RedKey.jpg");
        loadImage(TileType.KEY_GREEN,"src/Resources/GreenKey.jpg");
        loadImage(TileType.KEY_BLUE,"src/Resources/BlueKey.jpg");
        loadImage(TileType.LOCKED_DOOR_RED,"src/Resources/RedLockedDoor.jpg");
        loadImage(TileType.LOCKED_DOOR_GREEN,"src/Resources/GreenLockedDoor.jpg");
        loadImage(TileType.LOCKED_DOOR_BLUE,"src/Resources/BlueLockedDoor.jpg");


        loadImage(TileType.THIEF,"src/Resources/Thief.jpg"); // added based on  https://sagydemn.itch.io/basement-of-a-thief
        loadImage(TileType.VENT,"src/Resources/Vent.jpg");
        loadImage(TileType.BOMB,"src/Resources/Bomb.jpg"); // i have overlayed bomb on top of floor are from https://scooterloot.itch.io/bombs-etc


        loadImage(TileType.WALL,"src/Resources/WallTile.png");
        loadImage(TileType.EXIT,"src/Resources/Exit.jpg");
        loadImage(TileType.EXIT_LOCK,"src/Resources/ExitLock.jpg");
        loadImage(TileType.INFO,"src/Resources/InfoField.jpg");
        loadImage(TileType.TREASURE,"src/Resources/Treasure2.jpg");// edited base on https://mischeal.itch.io/treasure-asset-pack
        loadImage(TileType.FREE,"src/Resources/FreeTile.jpg"); // floor and wall tiles are from https://petricakegames.itch.io/cosmic-lilac
    }

    /**
     *
     * @param name
     * @param path
     * helper method of loadImages it help with load img
     */
    private void loadImage(TileType name, String path) {
        Image image = new ImageIcon(path).getImage();
        images_map.put(name, image); // Store the image in the map
    }
}
