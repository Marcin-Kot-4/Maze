package data;

import user_interface.UI;
import controllers.StateManager;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;

import static controllers.Graphic.*;

/**
 * Klasa MainMenu wyświetla i obsługuje przyciski znajdujące się w menu głównym,
 * odpowiada za ustawianie stanów aplikacji, wyświetlanie informacji o aplikacji oraz zmianę tekstur przycisków.
 */
public class MainMenu {

    private Texture background;
    private UI menuUI;
    private boolean showINFO;
    boolean mouseButton1;

    /**
     * Wczytaj teksturę tła głównego menu aplikacji.
     * Utwórz interfejs użytkownika.
     * Dodaj przyciski.
     */
    public MainMenu() {
        background = FastLoad("mainmenu");
        menuUI = new UI();
        // metoda poniżej dodaje przycisk do listy przycisków składającej się z obiektów klasy Button będących atrybutem należącym do klasy UI,
        // której to obiekt jest atrybutem klasy MainMenu
        menuUI.addButton("FIND_WAY", "findwayButton", WIDTH / 4 - 256, (int) (HEIGHT * 0.30f)); // przycisk ma rozmiar 256x64px
        // wartości x oraz y są dodatkowo modyfikowane, aby odpowiednio pozycjonować przyciski
        menuUI.addButton("EDIT", "editButton", WIDTH / 4 - 256, (int) (HEIGHT * 0.40f));
        menuUI.addButton("INFO", "infoButton", WIDTH / 4 - 256, (int) (HEIGHT * 0.50f));
        menuUI.addButton("QUIT", "quitButton", WIDTH / 4 - 256, (int) (HEIGHT * 0.60f));
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
            if (menuUI.isCursorOnTheButton("INFO"))
                showINFO = !showINFO;
            if (menuUI.isCursorOnTheButton("QUIT"))
                System.exit(0);
        } else {
            menuUI.changeButtonTextureDependingCursorPosition("FIND_WAY", "findwayButtonClick", "findwayButton");
            menuUI.changeButtonTextureDependingCursorPosition("EDIT", "editButtonClick", "editButton");
            menuUI.changeButtonTextureDependingCursorPosition("INFO", "infoButtonClick", "infoButton");
            menuUI.changeButtonTextureDependingCursorPosition("QUIT", "quitButtonClick", "quitButton");
        }
        mouseButton1 = Mouse.isButtonDown(0);
    }

    /**
     * Metoda rysująca tło menu, przyciski i wykorzystująca metodę updateButtons do obsługi przycisków i akcji z nimi związanych.
     */
    public void update() {
        DrawTexture(background, 0, 0, 2048, 1024);
        menuUI.draw();
        updateButtons();
        if (showINFO)
            drawINFO();
    }

    public void drawINFO() {
        menuUI.drawString(450, 300, "Each tile on the edge of the map is an exit from the maze.");
        menuUI.drawString(450, 340, "You can edit maze in EDITOR.");
        menuUI.drawString(450, 380, "Each time you press FIND WAY the application randomizes starting point.");
        menuUI.drawString(450, 420, "The starting point cannot be the wall or edge of the map.");
    }
}
