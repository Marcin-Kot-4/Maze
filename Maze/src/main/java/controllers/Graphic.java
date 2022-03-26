package controllers;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;

/**
 * Klasa Graphic m.in. ustawia tytuł okna „Tower Defense”, sprawdza, czy wystąpiła kolizja dwóch
 * obiektów, rysuje teksturę czworokąta w podanych współrzędnych, zwraca obiekty klasy Texture.
 */
public class Graphic {
    // I 736/480
    // II 1472/960
    // III 2208/1440
    public static float SCALING = 0.5f;
//    public static float SCALING = 1.0f;
//    public static float SCALING = 1.5f;
    public static int WIDTH = 736, HEIGHT = 480;
//    public static int WIDTH = 1472, HEIGHT = 960;
//    public static int WIDTH = 2208, HEIGHT = 1440;
    public static int TILE_SIZE = 32;
//    public static int TILE_SIZE = 64;
//    public static int TILE_SIZE = 96;

    public static void StartSession(){
        /** Ustaw tytuł okna "Maze" */
        Display.setTitle("Maze");
        try {
            /** Ustaw rozmiar wyświetlanego okna */
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            /** Stwórz natywne okno **/
            Display.create();
        }
        /** Zwróć wyjątek, jeśli nie można ustawić trybu wyświetlania okna lub go stworzyć */
        catch (LWJGLException e) {
            e.printStackTrace();
        }

        /** Ustaw orthographic projection (czyli reprezentację trójwymiarowych obiektów w dwóch wymiarach)
         *  pomiędzy 0 i 600 na osi X, pomiędzy 400 i 0 na osi Y
         *  oraz pomiędzy 1 i -1 na osi Z określającej odległość od renderowanego obiektu */
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, WIDTH, HEIGHT, 0, 1, -1);
        glMatrixMode(GL_MODELVIEW);
        /** Umożliwia umieszczanie tekstur na ekranie */
        glEnable(GL_TEXTURE_2D);
        /** Pozwala na blendowanie tła na kanale alfa - czyli przezroczystość obrazu */
        glEnable(GL_BLEND);
        /** Ustawienia dla kanału alfa i sposobu blendowania */
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * Metoda rysująca tekstury czworokąta w podanych współrzędnych <-(będzie się tam znajdował jego lewy górny róg)
     *  o podanych rozmiarach i teksturze
     * @param texture - obiekt klasy Texture - wczytana tekstura z pliku
     * @param x - pozycja na osi x, w której ma się znajdować lewy górny róg czworokąta
     * @param y - pozycja na osi y, w której ma się znajdować lewy górny róg czworokąta
     * @param width - szerokość prostokąta
     * @param height - wysokość prostokąta
     */
    public static void DrawTexture(Texture texture, float x, float y, float width, float height){
        /** Powiąż określony kontekst GL z teksturą **/
        texture.bind();
        /** Translacja współrzędnych czworokąta względem okna na współrzędne poszczególnych wierzchołków względem siebie
         *  lewy górny: x = 0, y = 0         |        prawy górny: x = 1, y = 0
         *  lewy dolny: x = 0, y = 1         |        prawy dolny: x = 1, y = 1      **/
        glTranslatef(x, y, 0);
        glBegin(GL_QUADS);
        glTexCoord2f(0, 0);
        glVertex2f(0, 0);
        glTexCoord2f(1, 0);
        glVertex2f(width, 0);
        glTexCoord2f(1, 1);
        glVertex2f(width, height);
        glTexCoord2f(0, 1);
        glVertex2f(0, height);
        glEnd();
        /** Zapobieganie przed "rozrywaniem ekranu" - ang. screen tearing **/
        glLoadIdentity();
    }

    /**
     * Metoda zwracająca obiekt klasy Texture - teksturę pobraną z pliku z podanej ścieżki o podanym typie pliku.
     *  IOException - obsługa wyjątku metody LoadTexture klasy Texture
     * @param path - ścieżka do pliku
     * @param fileType - typ pliku
     * @return - obiekt klasy Texture - wczytana tekstura z pliku
     */
    public static Texture LoadTexture(String path, String fileType) {
        Texture texture = null;
        InputStream in = ResourceLoader.getResourceAsStream(path);
        try {
            texture = TextureLoader.getTexture(fileType, in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return texture;
    }

    /**
     * Metoda automatyzująca część wprowadzania ścieżki i typu pliku wykorzystywanego jako tekstura.
     *  Dzięki niej wystarczy podać nazwę pliku, który chcemy załadować jako teksturę.
     *  Zwraca obiekt klasy Texture.
     * @param name - nazwa pliku
     * @return - obiekt klasy Texture - wczytana tekstura z pliku
     */
    public static Texture FastLoad(String name){
        Texture texture = null;
        texture = LoadTexture("src/main/resources/img/" + name + ".png", "PNG");
        return texture;
    }
}
