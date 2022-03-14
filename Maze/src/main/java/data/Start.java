package data;

import controllers.Time;
import controllers.StateManager;
import org.lwjgl.opengl.Display;

/** Zaimportuj wszystko z klasy Graphic **/
import static controllers.Graphic.*;

public class Start {

    public Start() {

        /** Wywołuje statyczną metodę klasy Graphic, żeby zainicjować wywołania OpenGL */
        StartSession();

        /** Główna pętla aplikacji */
        while (!Display.isCloseRequested()) {

            Time.Update();
            StateManager.update();
            /** Zaktualizuj okno - wyświetl to co zostało narysowane. Odpytaj klawiaturę i myszkę. **/
            Display.update();
            /** Ustaw określoną liczbę klatek na sekundę. Gra jest usypiana na dodatkowy dowolny czas w przypadku
             *  kiedy liczba FPS miałaby przekroczyć liczbę podaną w argumencie metody **/
            Display.sync(144);
        }
        Display.destroy();
    }

    public static void main(String[] args) {
        new Start();
    }
}
