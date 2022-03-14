package controllers;

import data.Editor;
import data.Maze;
import data.MainMenu;

/**
 * Klasa StateManager zawiera w sobie początkowy stan aplikacji, czyli Główne Menu,
 * obsługę stanu aplikacji i mierzenie liczby klatek na sekundę.
 */
public class StateManager {
    public enum AppState {
        MAINMENU, GAME, EDITOR
    }

    public static AppState appState = AppState.MAINMENU; // początkowy stan aplikacji to Główne Menu
    public static MainMenu mainMenu;
    public static Maze maze;
    public static Editor editor;

    public static long currentTime = System.currentTimeMillis();
    public static long nextSecond = System.currentTimeMillis() + 1000; // następna sekunda po aktualnej
    public static int framesInLastSecond = 0;
    public static int framesInCurrentSecond = 0;
    public static int appTime = 0;

    /**
     * Obsługuje stany aplikacji (menu, szukanie wyjścia, edytor)
     */
    public static void update(){
        switch (appState){
            case MAINMENU:
                maze = null; // dodane po to, żeby po powrocie do głównego menu dokonany do tej pory proces aplikacji został usunięty
                if (mainMenu == null)
                    mainMenu = new MainMenu();
                mainMenu.update();
                break;
            case GAME:
                if (maze == null){
                    maze = new Maze();
                    appTime = 0;
                }
                maze.update();
                break;
            case EDITOR:
                if (editor == null)
                    editor = new Editor();
                editor.update();
                break;
        }

        // Mierzenie liczby klatek na sekundę
        currentTime = System.currentTimeMillis();
        if (currentTime > nextSecond){ // jeśli minęła sekunda
            nextSecond += 1000; // ustaw następną na 1000 ms po poprzedniej
            framesInLastSecond = framesInCurrentSecond; // do zmiennej framesInLastSecond przypisz te zliczone w pętli w zmiennej framesInCurrentSecond
            framesInCurrentSecond = 0; // wyzeruj liczbę klatek w aktualnej sekundzie
            if (maze != null) // czas działania aplikacji
                appTime++;
        }

        framesInCurrentSecond++; // co wykonanie pętli dodaj jedną klatkę do licznika
    }

    /**
     * Metoda zmieniająca stan aplikacji na ten przekazany przez parametr. Może to być na przykład MAINMENU, FIND WAY, EDITOR.
     * @param newState - stan aplikacji, na jaki ma zostać zmieniony aktualny.
     */
    public static void setState(AppState newState){
        appState = newState;
    }
}
