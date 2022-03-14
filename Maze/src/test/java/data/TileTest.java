package data;

import org.junit.jupiter.api.Test;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static controllers.Graphic.TILE_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TileTest {

    @Test
    void getXPlace() throws LWJGLException {
        Display.setDisplayMode(new DisplayMode(1280, 960));
        Display.create();
        Tile tile = new Tile(128, 128,64, 64, TileType.Room);
        int tmp = (int) tile.getX() / TILE_SIZE;
        assertEquals (tmp, 2);
    }

    @Test
    void getYPlace() throws LWJGLException {
        Display.setDisplayMode(new DisplayMode(1280, 960));
        Display.create();
        Tile tile = new Tile(128, 128,64, 64, TileType.Room);
        int tmp = (int) tile.getX() / TILE_SIZE;
        assertEquals (tmp, 2);
    }
}