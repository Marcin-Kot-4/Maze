package data;

import controllers.StateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import user_interface.UI;

import static controllers.Graphic.*;

/**
 * Klasa MainMenu wyświetla i obsługuje przyciski znajdujące się w menu głównym,
 * odpowiada za ustawianie stanów aplikacji, wyświetlanie informacji o aplikacji oraz zmianę tekstur przycisków.
 */
public class MainMenu {

    private Texture background;
    private UI menuUI;
    private UI resolutionUI;
    private boolean showINFO;
    private boolean showOPTIONS;
    boolean mouseButton1;

    /**
     * Wczytaj teksturę tła głównego menu aplikacji.
     * Utwórz interfejs użytkownika.
     * Dodaj przyciski.
     */
    public MainMenu() {
        background = FastLoad("mainmenu");
        menuUI = new UI();
        resolutionUI = new UI();
        // metoda poniżej dodaje przycisk do listy przycisków składającej się z obiektów klasy Button będących atrybutem należącym do klasy UI,
        // której to obiekt jest atrybutem klasy MainMenu
        menuUI.addButton("FIND_WAY", "findwayButton", WIDTH / 4 - (int)(256 * SCALING), (int) (HEIGHT * 0.25f), (int)(256 * SCALING), (int)(64 * SCALING));
        menuUI.addButton("EDIT", "editButton", WIDTH / 4 - (int)(256 * SCALING), (int) (HEIGHT * 0.32f), (int)(256 * SCALING), (int)(64 * SCALING));
        menuUI.addButton("INFO", "infoButton", WIDTH / 4 - (int)(256 * SCALING), (int) (HEIGHT * 0.39f), (int)(256 * SCALING), (int)(64 * SCALING));
        menuUI.addButton("RESOLUTION", "resolutionButton", WIDTH / 4 - (int)(256 * SCALING), (int) (HEIGHT * 0.46f), (int)(256 * SCALING), (int)(64 * SCALING));
        menuUI.addButton("QUIT", "quitButton", WIDTH / 4 - (int)(256 * SCALING), (int) (HEIGHT * 0.53f), (int)(256 * SCALING), (int)(64 * SCALING));

        resolutionUI.addButton("736X480", "736x480Button", WIDTH / 3, (int) (HEIGHT * 0.46f), (int)(256 * SCALING), (int)(64 * SCALING));
        resolutionUI.addButton("1472X960", "1472x960Button", (int)(WIDTH / 3 + (276 * SCALING)), (int) (HEIGHT * 0.46f), (int)(256 * SCALING), (int)(64 * SCALING));
        resolutionUI.addButton("2208X1440", "2208x1440Button", (int)(WIDTH / 3 + (552 * SCALING)), (int) (HEIGHT * 0.46f), (int)(256 * SCALING), (int)(64 * SCALING));
    }

    /**
     * Metoda updateButtons odpowiada za ustawianie stanów aplikacji dostępnych w głównym menu, wyświetlanie informacji o aplikacji,
     * oraz zmianę tekstur przycisków.
     * Jeśli sprawdzono, że LPM jest wciśnięty po raz pierwszy, od kąd nie był naciśnięty:
          Jeśli kursor znajduje się nad przyciskiem "FIND_WAY":
          - zmień stan aplikacji na FIND_WAY - czyli rozpocznij poszukiwanie drogi w labiryncie.
          Jeśli kursor znajduje się nad przyciskiem "EDIT":
          - zmień stan aplikacji na EDITOR - czyli wejdź do aplikacji.
          Jeśli kursor znajduje się nad przyciskiem "INFO":
          - zmień wartość logiczną zmiennej showINFO na przeciwną (sprawi to, że w metodzie update zostanie wywołana
                                                                            metoda rysująca informacje o grze).
          Jeśli kursor znajduje się nad przyciskiem "QUIT":
     * - zamknij aplikację.
     * W przeciwnym przypadku:
     * Wywołaj metodę changeButtonTextureDependingCursorPosition na rzecz wszystkich przycisków w menu.
     * Metoda ta odpowiada za ustawianie odpowiedniej tekstury przycisków w zależności od aktualnego położenia kursora myszy.
     */
    private void updateButtons() {
        if (Mouse.isButtonDown(0) && !mouseButton1) {
            if (menuUI.isCursorOnTheButton("FIND_WAY"))
                StateManager.setState(StateManager.AppState.GAME);
            if (menuUI.isCursorOnTheButton("EDIT"))
                StateManager.setState(StateManager.AppState.EDITOR);
            if (menuUI.isCursorOnTheButton("INFO")) {
                showINFO = !showINFO;
                showOPTIONS = false;
            }
            if (menuUI.isCursorOnTheButton("RESOLUTION")){
                showOPTIONS = !showOPTIONS;
                showINFO = false;
            }
            if (resolutionUI.isCursorOnTheButton("736X480") && showOPTIONS)
                changeResolution(0.5f, 736, 480, 32);
            if (resolutionUI.isCursorOnTheButton("1472X960") && showOPTIONS)
                changeResolution(1.0f, 1472, 960, 64);
            if (resolutionUI.isCursorOnTheButton("2208X1440") && showOPTIONS)
                changeResolution(1.5f, 2208, 1440, 96);
            if (menuUI.isCursorOnTheButton("QUIT"))
                System.exit(0);
        } else {
            menuUI.changeButtonTextureDependingCursorPosition("FIND_WAY", "findwayButtonClick", "findwayButton");
            menuUI.changeButtonTextureDependingCursorPosition("EDIT", "editButtonClick", "editButton");
            menuUI.changeButtonTextureDependingCursorPosition("INFO", "infoButtonClick", "infoButton");
            menuUI.changeButtonTextureDependingCursorPosition("RESOLUTION", "resolutionButtonClick", "resolutionButton");
            menuUI.changeButtonTextureDependingCursorPosition("QUIT", "quitButtonClick", "quitButton");
            resolutionUI.changeButtonTextureDependingCursorPosition("736X480", "736x480ButtonClick", "736x480Button");
            resolutionUI.changeButtonTextureDependingCursorPosition("1472X960", "1472x960ButtonClick", "1472x960Button");
            resolutionUI.changeButtonTextureDependingCursorPosition("2208X1440", "2208x1440ButtonClick", "2208x1440Button");
        }
        mouseButton1 = Mouse.isButtonDown(0);
    }

    /**
     * Metoda rysująca tło menu, przyciski i wykorzystująca metodę updateButtons do obsługi przycisków i akcji z nimi związanych.
     */
    public void update() {
        DrawTexture(background, 0, 0, 2048 * SCALING, 1024 * SCALING);
        menuUI.draw();
        updateButtons();
        if (showINFO)
            drawINFO();
        if (showOPTIONS)
            resolutionUI.draw();
    }

    /**
     * Metoda rysująca informacje o tym jak używać programu
     */
    public void drawINFO() {
        menuUI.drawString(WIDTH / 3, (int) (HEIGHT * 0.30f), "Each time you press FIND WAY the application randomizes starting point.");
        menuUI.drawString(WIDTH / 3, (int) (HEIGHT * 0.35f), "The starting point cannot be the wall or edge of the map.");
        menuUI.drawString(WIDTH / 3, (int) (HEIGHT * 0.40f), "Tile with the \"Exit\" on it is an exit from the maze.");
        menuUI.drawString(WIDTH / 3, (int) (HEIGHT * 0.45f), "You can edit maze in EDITOR.");
        menuUI.drawString(WIDTH / 3, (int) (HEIGHT * 0.50f), "You can change the size of the application window in RESOLUTION.");
        menuUI.drawString(WIDTH / 3, (int) (HEIGHT * 0.55f), "Authors: Patrycja Kalita, Marcin Kot. 2022");
    }

    /**
     * Metoda zmieniająca rozdzielczość. W przypadku kiedy był używany edytor przed zmianą rozdzielczości: stary jest
     * usuwany i tworzony nowy na podstawie mapy zapisanej w pliku.
     * @param scaling - używane do skalowania tekstur
     * @param width - szerokość okna
     * @param height - wysokość okna
     * @param tile_size - rozmiar kafelków
     */
    public void changeResolution(float scaling, int width, int height, int tile_size) {
        Display.destroy();
        SCALING = scaling;
        WIDTH = width;
        HEIGHT = height;
        TILE_SIZE = tile_size;
        StartSession();
        background = FastLoad("mainmenu");
        menuUI = new UI();
        resolutionUI = new UI();
        // metoda poniżej dodaje przycisk do listy przycisków składającej się z obiektów klasy Button będących atrybutem należącym do klasy UI,
        // której to obiekt jest atrybutem klasy MainMenu
        menuUI.addButton("FIND_WAY", "findwayButton", WIDTH / 4 - (int)(256 * SCALING), (int) (HEIGHT * 0.25f), (int)(256 * SCALING), (int)(64 * SCALING));
        menuUI.addButton("EDIT", "editButton", WIDTH / 4 - (int)(256 * SCALING), (int) (HEIGHT * 0.32f), (int)(256 * SCALING), (int)(64 * SCALING));
        menuUI.addButton("INFO", "infoButton", WIDTH / 4 - (int)(256 * SCALING), (int) (HEIGHT * 0.39f), (int)(256 * SCALING), (int)(64 * SCALING));
        menuUI.addButton("RESOLUTION", "resolutionButton", WIDTH / 4 - (int)(256 * SCALING), (int) (HEIGHT * 0.46f), (int)(256 * SCALING), (int)(64 * SCALING));
        menuUI.addButton("QUIT", "quitButton", WIDTH / 4 - (int)(256 * SCALING), (int) (HEIGHT * 0.53f), (int)(256 * SCALING), (int)(64 * SCALING));

        resolutionUI.addButton("736X480", "736x480Button", WIDTH / 3, (int) (HEIGHT * 0.46f), (int)(256 * SCALING), (int)(64 * SCALING));
        resolutionUI.addButton("1472X960", "1472x960Button", (int)(WIDTH / 3 + (276 * SCALING)), (int) (HEIGHT * 0.46f), (int)(256 * SCALING), (int)(64 * SCALING));
        resolutionUI.addButton("2208X1440", "2208x1440Button", (int)(WIDTH / 3 + (552 * SCALING)), (int) (HEIGHT * 0.46f), (int)(256 * SCALING), (int)(64 * SCALING));

        if (StateManager.editor != null) // W przypadku kiedy był używany edytor przed zmianą rozdzielczości: stary jest
                                         // usuwany i tworzony nowy na podstawie mapy zapisanej w pliku.
            StateManager.editor = new Editor();
    }
}
