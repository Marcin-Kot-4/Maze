package data;

import org.newdawn.slick.opengl.Texture;

import java.util.LinkedList;
import java.util.List;

import static controllers.Graphic.*;

/**
 * Klasa Tile zawiera m.in. metodę, która rysuje obiekt klasy Tile na ekranie,
 * metodę, która zwraca pozycję kafelka na osi x oraz metodę, która zwraca pozycję kafelka na osi y.
 */
public class Tile {
    private float x, y;
    private int width, height;
    private Texture texture;
    private TileType type;
    private int number;
    private boolean visited; // czy kafelek był już odwiedzony podczas poszukiwania drogi
    private boolean partOfPath; // czy kafelek jest częścią drogi do wyjścia
    public List<Tile> neighbors = new LinkedList<>(); // sąsiednie kafelki typu "room" czyli przejścia

    /**
     * Konstruktor
     * @param x - pozycja na osi x, w której ma znajdować się lewy górny róg czworokąta
     * @param y - pozycja na osi y, w której ma znajdować się lewy górny róg czworokąta
     * @param width - szerokość czworokąta
     * @param height - wysokość czworokąta
     * @param type - obiekt typu wyliczeniowego reprezentujący typ terenu
     */
    public Tile(float x, float y, int width, int height, TileType type){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.type = type;
        this.texture = FastLoad(type.textureName);
        this.visited = false;
        number = 0;
    }

    /**
     * Metoda rysująca obiekt klasy Tile na ekranie.
     * Wykorzystuje do tego statyczną metodę DrawTexture klasy Graphic.
     */
    public void draw(){
        DrawTexture(texture, x, y, width, height);
    }

    public int getXPlace(){
        return (int) x / TILE_SIZE;
    }

    public int getYPlace(){
        return (int) y / TILE_SIZE;
    }

    /**
     * @return x - zwraca pozycję na osi x, w której znajduje się lewy górny róg czworokąta
     */
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    /**
     * @return y - zwraca pozycję na osi y, w której znajduje się lewy górny róg czworokąta
     */
    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isPartOfPath() {
        return partOfPath;
    }

    public void setPartOfPath(boolean partOfPath) {
        this.partOfPath = partOfPath;
    }

    /**
     * Metoda dodająca do listy kafelka możliwe przejścia na inne kafelki w zależności o tego, jakiego są typu. Zapisuje
     * również te informacje do statycznego atrybutu klasy Maze, który jest listą obiektów klasy Connection. Obiekt
     * klasy Connection zawiera informacje na temat bezpośredniego sąsiedztwa i możliwości
     * przejścia pomiędzy dwoma kafelkami typu room.
     * @param tileGrid - siatka kafelków (cały labirynt)
     * @return - lista kafelków, na które można przejść z danego kafelka.
     */
    public List<Tile> getNeighbors(TileGrid tileGrid) {
        int ix = (int)x/TILE_SIZE, iy = (int)y/TILE_SIZE;

        if (tileGrid.getTile((int)ix - 1, (int)iy).getType() == TileType.Room) {
            if (tileGrid.getTile((int)ix - 1, (int)iy).getNumber() == 0) {
                Maze.tilesChecked++;
                tileGrid.getTile((int)ix - 1, (int)iy).setNumber(Maze.tilesChecked);
            }
            this.neighbors.add(tileGrid.getTile((int)ix - 1, (int)iy));
            Maze.connections.add(new Connection(tileGrid.getTile((int)ix - 1, (int)iy).getNumber(), tileGrid.getTile((int)ix, (int)iy).getNumber()));
            if (ix - 1 == 0){
                tileGrid.getTile((int)ix - 1, (int)iy).setVisited(true);
                Maze.foundWayOut = true;
                Maze.escapePoint[0] =  ix - 1;
                Maze.escapePoint[1] =  iy;
                return neighbors;
            }
        }
        if (tileGrid.getTile((int)ix + 1, (int)iy).getType() == TileType.Room) {
            if (tileGrid.getTile((int)ix + 1, (int)iy).getNumber() == 0) {
                Maze.tilesChecked++;
                tileGrid.getTile((int)ix + 1, (int)iy).setNumber(Maze.tilesChecked);
            }
            this.neighbors.add(tileGrid.getTile((int)ix + 1, (int)iy));
            Maze.connections.add(new Connection(tileGrid.getTile((int)ix + 1, (int)iy).getNumber(), tileGrid.getTile((int)ix, (int)iy).getNumber()));
            if (ix + 1 == 19){
                tileGrid.getTile((int)ix + 1, (int)iy).setVisited(true);
                Maze.foundWayOut = true;
                Maze.escapePoint[0] =  ix + 1;
                Maze.escapePoint[1] =  iy;
                return neighbors;
            }
        }
        if (tileGrid.getTile((int)ix, (int)iy - 1).getType() == TileType.Room) {
            if (tileGrid.getTile((int)ix, (int)iy - 1).getNumber() == 0) {
                Maze.tilesChecked++;
                tileGrid.getTile((int)ix, (int)iy - 1).setNumber(Maze.tilesChecked);
            }
            this.neighbors.add(tileGrid.getTile((int)ix, (int)iy - 1));
            Maze.connections.add(new Connection(tileGrid.getTile((int)ix, (int)iy - 1).getNumber(), tileGrid.getTile((int)ix, (int)iy).getNumber()));
            if (iy - 1 == 0){
                tileGrid.getTile((int)ix, (int)iy - 1).setVisited(true);
                Maze.foundWayOut = true;
                Maze.escapePoint[0] =  ix;
                Maze.escapePoint[1] =  iy - 1;
                return neighbors;
            }
        }
        if (tileGrid.getTile((int)ix, (int)iy + 1).getType() == TileType.Room) {
            if (tileGrid.getTile((int)ix, (int)iy + 1).getNumber() == 0) {
                Maze.tilesChecked++;
                tileGrid.getTile((int)ix, (int)iy + 1).setNumber(Maze.tilesChecked);
            }
            this.neighbors.add(tileGrid.getTile((int)ix, (int)iy + 1));
            Maze.connections.add(new Connection(tileGrid.getTile((int)ix, (int)iy + 1).getNumber(), tileGrid.getTile((int)ix, (int)iy).getNumber()));
            if (iy + 1 == 14){
                tileGrid.getTile((int)ix, (int)iy + 1).setVisited(true);
                Maze.foundWayOut = true;
                Maze.escapePoint[0] = ix;
                Maze.escapePoint[1] = iy + 1;
                return neighbors;
            }
        }

        return this.neighbors;
    }
}
