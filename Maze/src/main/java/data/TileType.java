package data;

/**
 * Typ wyliczeniowy zawierający rodzaje terenu.
 */
public enum TileType {

    Room("room"),
    Wall("wall"),
    NULL("wall");

    public String textureName;

    /**
     * Konstruktor przyjmujący parametr
     * @param textureName - nazwa pliku tekstury tego typu kafelka
     */
    TileType(String textureName){
        this.textureName = textureName;
    }
}
