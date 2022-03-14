package data;

/**
 * Klasa odpowiedzialna za przechowywanie informacji na temat połączenia pomiędzy odwiedzanymi kafelkami.
 * key to aktualnie odwiedzany kafelek
 * value to kafelek, na który możemy się przemieścić. Sąsiadujący bezpośrednio z kafelkiem, na którym stoimy.
 */
public class Connection {
    private int key, value;

    public Connection(int key, int value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
