package data;

import user_interface.UI;

import static controllers.Graphic.*;

/**
 * Klasa siatki kafelków. Przechowuje informacje na temat labiryntu.
 */
public class TileGrid {

    public Tile[][] map;
    private int tilesWide, tilesHigh;

    /**
     * Konstruktor bez parametrów. Wypełnia planszę takimi samymi kafelkami.
     */
    public TileGrid(){
        this.tilesWide = 20;
        this.tilesHigh = 15;
        map = new Tile[20][15];
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[i].length; j++){
                map[i][j] = new Tile(i * TILE_SIZE, j*TILE_SIZE, TILE_SIZE, TILE_SIZE, TileType.Room);
            }
        }
    }

    /**
     * Setter umożliwiający zmianę typu terenu kafelka
     * @param xCoord - współrzędne na osi x kafelka do zmiany terenu
     * @param yCoord - współrzędne na osi y kafelka do zmiany terenu
     * @param type - nowy typ terenu
     */
    public void setTile(int xCoord, int yCoord, TileType type){
        map[xCoord][yCoord] = new Tile(xCoord * TILE_SIZE, yCoord * TILE_SIZE, TILE_SIZE, TILE_SIZE, type);
    }

    /**
     * Zwraca obiekt klasy Tile (czyli kafelek) znajdujący się na współrzędnych przekazanych przez parametry metody.
     * Współrzędne te są podane jako numery indeksów tablicy dwuwymiarowej składającej się z obiektów klasy Tile.
     * Indeksy te pomnożone przez TILE_SIZE (64) to położenie kafelków na mapie w pikselach.
     * @param xPlace - współrzędne na osi x kafelka
     * @param yPlace - współrzędne na osi y kafelka
     * @return - zwraca obiekt klasy Tile (czyli kafelek) znajdujący się na współrzędnych przekazanych przez parametry
     *           metody o ile jest to pozycja na mapie. Jeśli wykracza ona poza mapę, to metoda zwraca kafelek, który
     *           oznacza wykroczenie poza mapę (typ NULL).
     */
    public Tile getTile(int xPlace, int yPlace){
        if (xPlace < tilesWide && yPlace < tilesHigh && xPlace >= 0 && yPlace >= 0)
            return map[xPlace][yPlace];
        else
            return new Tile(0, 0, 0, 0, TileType.NULL);
    }

    /**
     * Metoda zwracająca kafelek o podanym przez parametr numerze. Numer ten jest przypisywany kafelkowi podczas
     * szukania wyjścia z labiryntu.
     */
    public Tile getTile(int number) {
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[i].length; j++){
                if (map[i][j].getNumber() == number)
                    return map[i][j];
            }
        }
        return new Tile(0, 0, 0, 0, TileType.NULL);
    }

    /**
     * Metoda rysująca kafelki na planszy. Są one obiektami klasy Tile.
     * Wykorzystana jest tutaj metoda klasy Tile o nazwie Draw.
     */
    public void draw(UI gameUI){
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[i].length; j++){
                map[i][j].draw();
                if (map[i][j].isPartOfPath()){
                    DrawTexture(FastLoad("dottPath"), i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE); // jeśli ten kafelek jest elementem najkrótszej drogi do wyjścia
                    gameUI.drawString(i * TILE_SIZE, j * TILE_SIZE, "" + map[i][j].getNumber());          //  wówczas narysuj pomarańczową kropkę
                    continue;
                }
                if (map[i][j].isVisited()){
                    DrawTexture(FastLoad("dott"), i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE); // jeśli ten kafelek był odwiedzony i nie jest elementem
                                                                                                   // najkrótszej drogi do wyjścia wówczas narysuj żółtą kropkę
                    gameUI.draw();
                    gameUI.drawString(i * TILE_SIZE, j * TILE_SIZE, "" + map[i][j].getNumber());
                }
            }
        }
    }

    /**
     * Ta sama metoda co wyżej tylko bez parametru i rysowania numerów oraz odwiedzonych kafelków. Wykorzystywana do
     * rysowania labiryntu w trybie EDIT.
     */
    public void draw(){
        for (int i = 0; i < map.length; i++){
            for (int j = 0; j < map[i].length; j++){
                map[i][j].draw();
            }
        }
    }

    public int getTilesWide() {
        return tilesWide;
    }

    public void setTilesWide(int tilesWide) {
        this.tilesWide = tilesWide;
    }

    public int getTilesHigh() {
        return tilesHigh;
    }

    public void setTilesHigh(int tilesHigh) {
        this.tilesHigh = tilesHigh;
    }
}
