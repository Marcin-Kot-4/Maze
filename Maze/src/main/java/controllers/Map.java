package controllers;

import data.Tile;
import data.TileGrid;
import data.TileType;

import java.io.*;

/**
 * Wczytywanie, zapisywanie map i funkcjonalności kafelków.
 */
public class Map {


    /**
     * Metoda saveMap przegląda kolumnami wszystkie kafelki i zapisuje ich typy do zmiennej String.
     * Szerokość i wysokość planszy w kafelkach zwraca metoda klasy TileGrid.
     * @param mazeName - nazwa pliku, w którym mają zostać zapisane dane o labiryncie.
     * @param grid - siatka mapy, która ma zostać zapisana w pliku.
     */
    public static void SaveMaze(String mazeName, TileGrid grid) {
        String mazeData = "";
        for (int i = 0; i < grid.getTilesWide(); i++) {
            for (int j = 0; j < grid.getTilesHigh(); j++) {
                mazeData += GetTileID(grid.getTile(i, j));
            }
        }

        try {
            File file = new File("src\\main\\resources\\mazes\\" + mazeName); // nazwa pliku "mazeName"
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(mazeData); // zapis zmiennej mazeData do pliku
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda LoadMap wczytuje mapę z pliku konwertując jego zawartość.
     * @param mazeName - nazwa pliku zawierającego ciąg cyfr. Każda cyfra oznacza typ kafelka w labiryncie. Kolejność oznacza
     *                gdzie w labiryncie będzie się on znajdował
     * @return - zwraca obiekt reprezentujący wczytaną mapę, na której program będzie pracował.
     */
    public static TileGrid LoadMaze(String mazeName) {
        TileGrid grid = new TileGrid();
        try {
            BufferedReader br = new BufferedReader(new FileReader("src\\main\\resources\\mazes\\" + mazeName)); // otwórz plik do odczytu
            String data = br.readLine(); // zapisz dane z pliku do zmiennej data (pierwszą linię[w pierwszej linii jest cała zawartość pliku])
            for (int i = 0; i < grid.getTilesWide(); i++) {
                for (int j = 0; j < grid.getTilesHigh(); j++) {
                    // substring to metoda, która zwraca zmienną typu String będącą fragmentem innej zmiennej typu String
                    // i * grid.getTilesHigh() + j to indeks znaku określającego typ aktualnie wczytywanego kafelka
                    // i oznacza kolumnę, grid.getTilesHigh() to rozmiar kolumny, j to wiersz w kolumnie
                    // na podstawie wczytanego konkretnego znaku jest ustawiany konkretny typ konkretnego kafelka w labiryncie
                    grid.setTile(i, j, GetTileType(data.substring(i * grid.getTilesHigh() + j, i * grid.getTilesHigh() + j + 1)));
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return grid;
    }

    /**
     * Metoda zwracająca odpowiedni kafelek na podstawie znaku przekazanego przez parametr
     * @param ID - ID typu kafelka
     * @return - typ kafelka
     */
    public static TileType GetTileType(String ID) {
        TileType type = TileType.NULL;
        switch (ID) {
            case "0":
                type = TileType.Room;
                break;
            case "1":
                type = TileType.Wall;
                break;
            case "2":
                type = TileType.NULL;
                break;
        }
        return type;
    }

    /**
     * Metoda zwracająca ID typu kafelka przekazanego jej przez parametr.
     * @param tile - obiekt kafelek
     * @return - znak reprezentujący typ kafelka
     */
    public static String GetTileID(Tile tile) {
        String ID = "E";

        switch (tile.getType()) {
            case Room:
                ID = "0";
                break;
            case Wall:
                ID = "1";
                break;
            case NULL:
                ID = "2";
                break;
        }

        return ID;
    }
}
