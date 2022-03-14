package user_interface;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.util.ArrayList;

import static controllers.Graphic.*;

/**
 * Klasa UI zawiera m.in. metodę rysującą tekst o podanych współrzędnych,
 * metodę rysującą przyciski z listy buttonList w oknie gry
 * oraz metodę, która upraszcza dodawanie nowego przycisku do siatki przycisków menu.
 */
public class UI {

    private ArrayList<Button> buttonList;
    private ArrayList<Menu> menuList;
    private TrueTypeFont font; // klasa biblioteki slick
    private TrueTypeFont font_heading; // klasa biblioteki slick
    private TrueTypeFont font_description; // klasa biblioteki slick
    private Font awtFont; // java wbudowana klasa Font
    private Font awtHeadingFont; // java wbudowana klasa Font
    private Font awtDescriptionFont; // java wbudowana klasa Font

    public UI() {
        buttonList = new ArrayList<Button>();
        menuList = new ArrayList<Menu>();
        awtFont = new Font("Dubai", Font.BOLD, 24);
        awtHeadingFont = new Font("Dubai", Font.BOLD, 51);
        awtDescriptionFont = new Font("Dubai", Font.PLAIN, 16);
        font = new TrueTypeFont(awtFont, true);
        font_heading = new TrueTypeFont(awtHeadingFont, true);
        font_description = new TrueTypeFont(awtDescriptionFont, true);
    }

    /**
     * Metoda rysująca tekst w podanych współrzędnych (wszystkie te informacje są przekazywane przez parametr)
     * Używana między innymi do wyświetlania liczby odwiedzonych kafelków.
     * @param x - położenie napisu na osi x
     * @param y - położenie napisu na osi y
     * @param text - tekst do wyświetlenia
     */
    public void drawString(int x, int y, String text) {
        font.drawString(x, y, text);
    }

    /**
     * Dodaje przycisk do listy przycisków obiektu o podanej nazwie przycisku, nazwie pliku, w podanym położeniu w oknie aplikacji.
     * Robiąc to, tworzy nowy obiekt klasy Button, wykorzystując do tego konstruktor, który rozmiar przycisku pobiera z pliku graficznego.
     */
    public void addButton(String name, String textureName, int x, int y) {
        buttonList.add(new Button(name, FastLoad(textureName), x, y));
    }

    /**
     * Metoda isCursorOnTheButton sprawdza, czy kursor myszy znajduje się na przycisku o podanej nazwie.
     *
     * @param buttonName - nazwa przycisku, dla którego ma zostać wykonane sprawdzenie.
     * @return zwraca prawdę, jeśli kursor znajduje się na przycisku, fałsz w przeciwnym przypadku.
     */
    public boolean isCursorOnTheButton(String buttonName) {
        Button btn = getButton(buttonName);
        float mouseY = HEIGHT - Mouse.getY() - 1;
        if (Mouse.getX() > btn.getX() && Mouse.getX() < btn.getX() + btn.getWidth() &&
                mouseY > btn.getY() && mouseY < btn.getY() + btn.getHeight()) {
            return true;
        }
        return false;
    }

    /**
     * Metoda changeButtonTextureDependingCursorPosition wykorzystuje metodę isCursorOnTheButton do sprawdzania, czy
     * kursor myszy znajduje się nad przyciskiem, którego nazwę otrzymała przez parametr. W zależności od wyniku ustawia
     * przyciskowi odpowiednią teksturę.
     * Zapisz referencję przycisku o nazwie odebranej przez tę metodę jako parametr buttonName do zmiennej b.
     * Jeśli kursor znajduje się nad sprawdzanym przyciskiem:
     * - ustaw jego teksturę na tę, której nazwę metoda otrzymała jako parametr textureNameActive
     * W przeciwnym przypadku:
     * Jeśli aktualna tekstura przycisku nie jest taka, jaka powinna być w przypadku kiedy nie znajduje się nad nim kursor:
     * - zmień teksturę na tę, która reprezentuje przycisk ("niepodświetlony"), nad którym nie znajduje się kursor.
     * Nazwę tej tekstury metoda otrzymała jako parametr textureNameNormal.
     *
     * @param buttonName        - nazwa sprawdzanego i/lub aktualizowanego przycisku.
     * @param textureNameActive - nazwa "podświetlonej" tekstury przycisku.
     * @param textureNameNormal - nazwa "niepodświetlonej" tekstury przycisku.
     */
    public void changeButtonTextureDependingCursorPosition(String buttonName, String textureNameActive, String textureNameNormal) {
        Button btn = getButton(buttonName);
        if (isCursorOnTheButton(buttonName)) {
            btn.setTexture(FastLoad(textureNameActive));
        } else {
            if (!btn.getTexture().equals(FastLoad(textureNameNormal))) {
                btn.setTexture(FastLoad(textureNameNormal));
            }
        }
    }

    /**
     * Metoda odszukuje przycisk o podanej nazwie w liście przycisków i zwraca ten obiekt.
     */
    private Button getButton(String buttonName) {
        for (Button b : buttonList) {
            if (b.getName().equals(buttonName)) {
                return b;
            }
        }
        return null;
    }

    public void createMenu(String name, int x, int y, int width, int height, int optionsWidth, int optionsHeight) {
        menuList.add(new Menu(name, x, y, width, height, optionsWidth, optionsHeight));
    }

    /**
     * Zwraca menu z listy o przekazanej do metody przez parametr nazwie.
     *
     * @param name
     * @return
     */
    public Menu getMenu(String name) {
        for (Menu m : menuList)
            if (name.equals(m.getName()))
                return m;
        return null;
    }

    /**
     * Rysuje przyciski z listy buttonList w oknie aplikacji. (przyciski menu i wyboru terenu)
     * Pierwsza pętla rysuje główne menu.
     * Druga pętla rysuje menu wyboru terenu.
     */
    public void draw() {
        for (Button b : buttonList)
            DrawTexture(b.getTexture(), b.getX(), b.getY(), b.getWidth(), b.getHeight());
        for (Menu m : menuList)
            m.draw();
    }

    public class Menu {

        String name;
        private ArrayList<Button> menuButtons;
        private int x, y, width, height, buttonAmount, optionsWidth, optionsHeight, padding;

        public Menu(String name, int x, int y, int width, int height, int optionsWidth, int optionsHeight) {
            this.name = name;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.optionsWidth = optionsWidth;
            this.optionsHeight = optionsHeight;
            this.padding = (width - (optionsWidth * TILE_SIZE)) / (optionsWidth + 1); // wielkość odstępów na osi y pomiędzy przyciskami to
            // (szerokość przeznaczona na menu - (łączna szerokość wszystkich przycisków w jednej linii)) / (liczba przycisków + 1[czyli liczba odstępów w jednej linii])
            this.buttonAmount = 0;
            this.menuButtons = new ArrayList<Button>();
        }

        /**
         * Metoda upraszczająca dodawanie nowego przycisku do siatki przycisków menu.
         *
         * @param name
         * @param buttonTextureName
         */
        public void quickAdd(String name, String buttonTextureName) {
            Button b = new Button(name, buttonTextureName, 0, 0); // pozycja na osi x i y jest równa 0 dlatego, że
            // metoda ta jest wykorzystywana w metodzie, która ustawia pozycje przycisków tak, aby utworzyły siatkę przycisków menu
            setButton(b);
        }

        /**
         * Metoda obsługująca dodawanie przycisków w pozycji uzależnionej od tego, który z kolei jest to przycisk.
         * W efekcie uzyskujemy siatkę przycisków u ustalonej liczbie przycisków w jednej linii.
         * Każdy przycisk jest przesunięty względem innego o wielokrotność 64px.
         *
         * @param b
         */
        private void setButton(Button b) {
            if (optionsWidth != 0)
                // przesuwa kolejną linię przycisków o 64px pomnożone przez numer linii
                b.setY(y + (buttonAmount / optionsWidth) * (TILE_SIZE + 16)); // co optionsWidth przycisk z kolei, ustawia pozycje następnych przycisków
            // na osi y niżej o iloczyn (64px + 16) i liczby przycisków podzielonych przez optionsWidth
            // względem pierwszego przycisku
            // przesuwa kolejny przycisk w prawo względem pierwszego o 64px na osi x pomnożone przez numer przycisku z kolei w tej linii
            b.setX(x + (buttonAmount % 2) * (padding + TILE_SIZE) + padding); // przyciskowi, który został utworzony jako n-ty (buttonAmount) z kolei
            // ustawia pozycję na osi x równą: pozycji początkowej menu + (numer przycisku z kolei % liczba przycisków w jednej linii) *
            // (obliczony w konstruktorze odstęp dla tego menu + rozmiar kafelka (64px) + obliczony wcześniej odstęp
            // dodany na końcu jest po to, aby istniał odstęp również między pierwszym przyciskiem a początkiem menu.
            buttonAmount++;
            menuButtons.add(b);
        }

        /**
         * Metoda isCursorOnTheButton sprawdza, czy kursor myszy znajduje się na przycisku o podanej nazwie.
         *
         * @param buttonName - nazwa przycisku, dla którego ma zostać wykonane sprawdzenie.
         * @return zwraca prawdę, jeśli kursor znajduje się na przycisku, fałsz w przeciwnym przypadku.
         */
        public boolean isCursorOnTheButton(String buttonName) {
            Button btn = getButton(buttonName);
            float mouseY = HEIGHT - Mouse.getY() - 1;
            if (Mouse.getX() > btn.getX() && Mouse.getX() < btn.getX() + btn.getWidth() &&
                    mouseY > btn.getY() && mouseY < btn.getY() + btn.getHeight()) {
                return true;
            }
            return false;
        }

        /**
         * Metoda sprawdza, czy przycisk o podanej nazwie jest naciśnięty.
         *
         * @param buttonName
         * @return zwraca prawdę, jeśli przycisk jest naciśnięty, fałsz w przeciwnym przypadku.
         */
        public boolean isButtonClicked(String buttonName) {
            Button b = getButton(buttonName);
            float mouseY = HEIGHT - Mouse.getY() - 1;
            if (Mouse.getX() > b.getX() && Mouse.getX() < b.getX() + b.getWidth() &&
                    mouseY > b.getY() && mouseY < b.getY() + b.getHeight()) { // sprawdza, czy kursor znajduje się na przycisku
                return true;
            }
            return false;
        }

        /**
         * Metoda odszukuje przycisk o podanej nazwie w liście przycisków i zwraca ten obiekt.
         */
        private Button getButton(String buttonName) {
            for (Button b : menuButtons) {
                if (b.getName().equals(buttonName)) {
                    return b;
                }
            }
            return null;
        }

        /**
         * Metoda rysująca przyciski
         */
        public void draw() {
            for (Button b : menuButtons)
                DrawTexture(b.getTexture(), b.getX(), b.getY(), b.getWidth(), b.getHeight());
        }

        public String getName() {
            return name;
        }

        public ArrayList<Button> getMenuButtons() {
            return menuButtons;
        }
    }

}
