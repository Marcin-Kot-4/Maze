package controllers;

import org.lwjgl.Sys;

/**
 * Klasa Time zawiera m.in. metodę, która zwraca czas pomiędzy teraźniejszością
 * a ostatnią aktualizacją klatki wyświetlanego obrazu aplikacji,
 * metodę aktualizującą czas uruchomienia aplikacji,
 * czy statyczną metodę do zatrzymywania i wznawiania aplikacji.
 */
public class Time {

    public static float deltaFrameTime = 0;
    public static long lastFrameTime, totalTime;

    /**
     * @return - zwraca typ long zwierający teraźniejszy czas
     */
    public static long GetTime() {
        return Sys.getTime() * 1000 / Sys.getTimerResolution();
    }

    /**
     * @return - zwraca czas pomiędzy teraźniejszością a ostatnią aktualizacją klatki wyświetlanego obrazu aplikacji.
     */
    public static float GetTimeDelta() {
        long currentTime = GetTime();
        int delta = (int) (currentTime - lastFrameTime);
        lastFrameTime = GetTime();
        if (delta * 0.001f > 0.05f)
            return 0.05f;
        return delta * 0.001f;
    }

    /**
     * Zaktualizuj czas uruchomienia aplikacji
     */
    public static void Update() {
        deltaFrameTime = GetTimeDelta();
        totalTime += deltaFrameTime;
    }

}















