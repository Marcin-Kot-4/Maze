package data;


import controllers.StateManager;
import controllers.Time;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import user_interface.UI;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import static controllers.Graphic.*;
import static controllers.Map.LoadMaze;

/**
 * Klasa Maze odpowiada za realizacje wszystkiego, co następuje po naciśnięciu przycisku "Find Way".
 */
public class Maze {

    private TileGrid tileGrid;
    private UI gameUI;
    private Texture menuBackground;
    private float currentMapTimer;
    public int[] startingPoint;
    public static int[] escapePoint = new int[2];
    public static boolean foundWayOut;
    public static int tilesChecked;
    public static List<Connection> connections;

    /**
     * Konstruktor trybu gry.
     * Wczytaj labirynt z pliku.
     * Wczytaj tło interfejsu znajdującego się po prawej stronie.
     * Stwórz interfejs użytkownika. W tym przypadku odpowiedzialny tylko za wyświetlanie liczby odwiedzonych kafelków.
     * Wylosuj punkt startowy.
     * Znajdź wyjście przy pomocy algorytmu Breadth First Search.
     * Znajdź najkrótszą drogę przy pomocy algorytmu Backtracking.
     */
    public Maze() {
        this.tileGrid = LoadMaze("map_1");
        this.menuBackground = FastLoad("menu_background");
        setupUI();
        currentMapTimer = Time.GetTime();
        this.startingPoint = randomPoint();
        foundWayOut = false;
        tilesChecked = 1;
        connections = new LinkedList<>();
        breadthFirstSearch();
        backtracking();
    }

    /**
     * Metoda implementująca algorytm BreadthFirstSearch. Zaczyna poszukiwanie od punktu startowego. Tam sprawdza, w
     * którą stronę może się poruszać. Wszystkie dostępne ruchy na sąsiadujące kafelki umieszcza w kolejce FIFO.
     * Następnie przemieszcza się do pierwszego z kafelka z kolejki (przy okazji usuwa go z kolejki). Sprawdza, czy
     * jest przy wyjściu, jeśli nie to sprawdza, w którą stronę może się poruszać. Wszystkie dostępne ruchy na
     * sąsiadujące kafelki umieszcza w kolejce FIFO. Następnie przemieszcza się do kolejnego kafelka z kolejki (przy
     * okazji usuwając go z niej) itd. aż znajdzie wyjście. Na koniec wypisuje współrzędne punktu wyjścia.
     */
    public void breadthFirstSearch() {
        Queue<Tile> queue = new LinkedList<>();
        queue.add(tileGrid.getTile(startingPoint[0], startingPoint[1]));
        tileGrid.getTile(startingPoint[0], startingPoint[1]).setNumber(1);

        while (!queue.isEmpty()) {
            if (Time.GetTime() - 130 < currentMapTimer) { // jeśli nie upłynęło x ms
                Time.Update();
                update();
                // Obsługa klawiatury
                /** Zaktualizuj okno - wyświetl to co zostało narysowane. Odpytaj klawiaturę i myszkę. **/
                Display.update();
                /** Ustaw określoną liczbę klatek na sekundę. Gra jest usypiana na dodatkowy dowolny czas w przypadku
                 *  kiedy liczba FPS miałaby przekroczyć liczbę podaną w argumencie metody **/
//                Display.sync(240);
                if (StateManager.appState == StateManager.AppState.MAINMENU) { // jeśli został naciśnięty ESC wówczas zakończ działanie funkcji
                    return;
                }
            } else {
                Time.Update();
                currentMapTimer = Time.GetTime();
                Tile current = queue.poll();

                if (!current.isVisited()) {
                    current.setVisited(true);
                    queue.addAll(current.getNeighbors(tileGrid));
                }

                if (foundWayOut == true) {
                    System.out.println("Found way out! x: " + escapePoint[0] + ", y: " + escapePoint[1]);
                    break;
                }
            }
        }

//        for (int i = 0; i < connections.size(); i++) {
//            System.out.println("key: " + connections.get(i).getKey() + " value: " + connections.get(i).getValue());
//        }
    }

    /**
     * Algorytm wraca od punktu wyjścia do punktu startowego po kafelkach, które zostały ponumerowane przy okazji
     * szukania wyjścia z labiryntu. Algorytm kieruje się parami klucz wartość. Każdy kafelek jako wartość przyjmuje
     * numer kafelka, z którym bezpośrednio sąsiaduje. W drodze powrotnej wybierane są te sąsiadujące kafelki, które
     * mają najmniejszą liczbę w polu wartość.
     */
    public void backtracking() {
        System.out.println(tileGrid.getTile(tilesChecked).

                getX() / TILE_SIZE);
        System.out.println(tilesChecked);
        if (tileGrid.getTile(tilesChecked).getType().textureName.equals("exit")) { // jeśli znaleziony punkt to wyjście
            boolean backToStart = false;
            tileGrid.getTile(tilesChecked).setPartOfPath(true); // ostatni kafelek (wyjście) jest częścią drogi do wyjścia
            int backTracing = tilesChecked;
            while (!backToStart) {
                if (Time.GetTime() - 150 < currentMapTimer) { // jeśli nie upłynęło 150ms
                    Time.Update();
                    update();
                    /** Zaktualizuj okno - wyświetl to co zostało narysowane. Odpytaj klawiaturę i myszkę. **/
                    Display.update();
                    /** Ustaw określoną liczbę klatek na sekundę. Gra jest usypiana na dodatkowy dowolny czas w przypadku
                     *  kiedy liczba FPS miałaby przekroczyć liczbę podaną w argumencie metody **/
//                    Display.sync(144);
                    if (StateManager.appState == StateManager.AppState.MAINMENU) { // jeśli został naciśnięty ESC wówczas zakończ działanie funkcji
                        return;
                    }
                } else {
                    currentMapTimer = Time.GetTime();
                    for (int i = 0; i < connections.size(); i++) {
                        if (connections.get(i).getKey() == backTracing) {
                            tileGrid.getTile(connections.get(i).getValue()).setPartOfPath(true);
                            backTracing = connections.get(i).getValue();
                            if (backTracing == 1) {
                                backToStart = true;
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Metoda losująca punkt startowy w labiryncie. Punkty przy krawędzi labiryntu są pomijane, dzięki czemu nigdy nie
     * zostanie wylosowane wyjście jako punkt startowy.
     *
     * @return - zwraca dwuelementową tablicę typu int. Pierwszy element tablicy reprezentuje kafelek na osi x. Drugi
     * element tablicy reprezentuje kafelek na osi y.
     */
    private int[] randomPoint() {
        Random random = new Random();
        int x, y;
        x = random.nextInt(18) + 1;
        y = random.nextInt(13) + 1;
        while (tileGrid.map[x][y].getType() != TileType.Room) {
            x = random.nextInt(18) + 1;
            y = random.nextInt(13) + 1;
        }

        int[] randomPoint = new int[2];
        randomPoint[0] = x;
        randomPoint[1] = y;

        System.out.println("Starting point. x: " + x + ", y: " + y);

        return randomPoint;
    }

    /**
     * Metoda rysująca punkt startowy w labiryncie
     */
    private void drawStartingPoint() {
        DrawTexture(FastLoad("monster"), startingPoint[0] * TILE_SIZE, startingPoint[1] * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    /**
     * Tworzy interfejs użytkownika.
     */
    private void setupUI() {
        gameUI = new UI();
    }

    /**
     * Rysuje zaktualizowany interfejs użytkownika.
     */
    private void updateUI() {
        gameUI.draw();
        gameUI.drawString((int)(1284 * SCALING), (int)(500 * SCALING), "Tiles checked: " + tilesChecked);
        gameUI.drawString((int)(1320 * SCALING), (int)(870 * SCALING), "ESC - go to");
        gameUI.drawString((int)(1320 * SCALING), (int)(890 * SCALING), "main menu");
    }

    /**
     * Metoda aktualizująca stan gry.
     * Rysuj labirynt.
     * Rysuj punkt startowy.
     * Narysuj tło dla "Tiles checked: x".
     * Jeśli naciśnięto przycisk ESC:
     * - wyjdź do głównego menu.
     */
    public void update() {
        tileGrid.draw(gameUI);
        drawStartingPoint();

        // Pasek po prawej stronie w grze. Załadowana tekstura jest rozmiaru 256x1024
        // a nie 192x960 ponieważ biblioteka slick ma problemy z teksturami, których rozmiary nie są potęgą dwójki.
        DrawTexture(menuBackground, (int)(1280 * SCALING), 0, (int)(192 * SCALING), (int)(960 * SCALING)); // tło dla "Tiles checked: x"
        updateUI();

        while (Keyboard.next()) {
            if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE && Keyboard.getEventKeyState()) {
                StateManager.setState(StateManager.AppState.MAINMENU);
                StateManager.update();
            }
        }
    }

}
