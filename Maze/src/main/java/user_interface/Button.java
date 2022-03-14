package user_interface;

import org.newdawn.slick.opengl.Texture;

import static controllers.Graphic.FastLoad;

/**
 * Klasa Button to klasa, która odpowiada za tworzenie przycisków.
 */
public class Button {

    private String name, textureName;
    private Texture texture;
    private int x, y, width, height;

    public Button(String name, Texture texture, int x, int y, int width, int height) {
        this.name = name;
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Konstruktor, który szerokość i wysokość przycisku pobiera z pliku graficznego. Tekstura jest reprezentowana przez
     * obiekt klasy Texture.
     */
    public Button(String name, Texture texture, int x, int y) {
        this.name = name;
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = texture.getImageWidth();
        this.height = texture.getImageHeight();
    }

    /**
     * Konstruktor, który szerokość i wysokość przycisku pobiera z pliku graficznego. Tekstura jest reprezentowana przez
     * łańcuch znaków będący nazwą pliku.
     */
    public Button(String name, String textureName, int x, int y) {
        this.name = name;
        this.textureName = textureName;
        this.texture = FastLoad(textureName);
        this.x = x;
        this.y = y;
        this.width = texture.getImageWidth();
        this.height = texture.getImageHeight();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public String getTextureName() {
        return textureName;
    }

    public void setTextureName(String textureName) {
        this.textureName = textureName;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
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
}
