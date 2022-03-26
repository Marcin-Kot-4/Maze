package data;

import user_interface.*;
import user_interface.UI.*;
import controllers.StateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;

import static controllers.Graphic.*;
import static controllers.Map.*;

/**
 * Klasa Editor wczytuje labirynt z pliku i umożliwia jego edycję.
 */
public class Editor {

    private boolean showHelp;
    private TileGrid grid;
    private int index;
    private TileType[] types;
    private UI editorUI;
    private Menu tilePickerMenu;
    private Texture menuBackground;

    /**
     * Wczytuje labirynt z pliku.
     * Indeks typu kafelka ustawia na 0 (determinuje on rodzaj terenu [droga, ściana]).
     * Pobiera typy kafelków.
     * Wczytuje teksturę tła, które jest wyświetlane po prawej stronie ekranu od współrzędnej x = 1280.
     * Tworzy interfejs użytkownika.
     * Ustawia na false zmienną determinującą czy użytkownik chce, aby pomoc była wyświetlana.
     */
    public Editor() {
        this.grid = LoadMaze("map_1");
        this.index = 0;
        this.types = new TileType[4];
        this.types[0] = TileType.Room;
        this.types[1] = TileType.Wall;
        this.types[2] = TileType.Exit;
        this.menuBackground = FastLoad("menu_background");
        setupUI();
        showHelp = false;
    }

    /**
     * Metoda tworząca interfejs użytkownika w Edytorze labiryntu.
     * Utwórz obiekt klasy UI.
     * Stwórz menu przycisków z jego lewym górnym rogiem na pozycji x = 1280, y = 16. O szerokości 192 px i wysokości 960 px.
     * Pobierz i zapisz w atrybucie tilePickerMenu referencję do obiektu klasy menu będącego częścią kompozycji obiektu klasy UI.
     * Dodaj przyciski odpowiadające za kafelki trawy, drogi oraz wody.
     */
    private void setupUI() {
        editorUI = new UI();
        editorUI.createMenu("TilePicker", (int)(1280 * SCALING), (int)(16 * SCALING), (int)(192 * SCALING), (int)(960 * SCALING), 2, 0);
        tilePickerMenu = editorUI.getMenu("TilePicker");
        tilePickerMenu.quickAdd("Room", "room");
        tilePickerMenu.quickAdd("Wall", "wall");
        tilePickerMenu.quickAdd("Exit", "exit");
    }

    /**
     * Rysowanie oraz obsługa myszy i klawiatury.
     */
    public void update() {
        draw();

        if (Mouse.next()) { // jeśli zarejestrowano nowe zdarzenie związane z myszą
            boolean mouseClicked = Mouse.isButtonDown(0);
            if (mouseClicked) {
                if (tilePickerMenu.isButtonClicked("Room")) {
                    index = 0;
                } else if (tilePickerMenu.isButtonClicked("Wall")) {
                    index = 1;
                } else if (tilePickerMenu.isButtonClicked("Exit")) {
                    index = 2;
                } else
                    setTile();
            }
        }

        // Obsługa klawiatury
        while (Keyboard.next()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_S && Keyboard.getEventKeyState()) {
                SaveMaze("map_1", grid);
            }
            if (Keyboard.getEventKey() == Keyboard.KEY_F1 && Keyboard.getEventKeyState()) {
                showHelp = !showHelp;
            }
            if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()) {
                StateManager.setState(StateManager.AppState.MAINMENU);
            }
        }
    }

    /**
     * Rysuje tło wyboru kafelków, mapę, menu kafelków oraz pomoc.
     */
    private void draw() {
        DrawTexture(menuBackground, (int)(1280 * SCALING), 0, (int)(192 * SCALING), (int)(960 * SCALING)); // załadowana tekstura jest rozmiaru 256x1024
        grid.draw();
        editorUI.draw();
        if (showHelp){
            DrawTexture(menuBackground, 380, 100, 635, 140);
            editorUI.drawString(410, 110, "Press 'S' to save edited map.");
            editorUI.drawString(410, 150, "Each tile on the edge of the map is an exit from the maze.");
            editorUI.drawString(410, 190, "Press 'ESC' to exit the editor.");
        }
        editorUI.drawString(1310, 870, "S - SAVE");
        editorUI.drawString(1310, 900, "F1 - HELP");
    }

    /**
     * Metoda zmieniająca typ terenu kafelka, który znajduje się pod kursorem myszki.
     * Przed próbą zmiany sprawdza, czy kursor myszy znajduje się nad planszą.
     */
    private void setTile() {
        if (Mouse.getX() < (int)(1280 * SCALING) && Mouse.getY() < (int)(960 * SCALING)){
            grid.setTile((int) Math.floor((float) Mouse.getX() / TILE_SIZE), (int) Math.floor((float) (HEIGHT - Mouse.getY() - 1) / TILE_SIZE), types[index]);
        }
    }
}
