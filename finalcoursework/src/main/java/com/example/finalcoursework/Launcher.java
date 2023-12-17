package com.example.finalcoursework;



/**
 * Класс Launcher представляет точку входа в приложение.
 * Здесь осуществляется вызов метода main класса SnakeGame.
 */
public class Launcher {

    /**
     * Точка входа в приложение. Метод main создает экземпляр класса SnakeGame
     * и вызывает его метод main, передавая аргументы командной строки.
     *
     * @param args Аргументы командной строки, передаваемые при запуске приложения.
     */
    public static void main(String[] args) {
        SnakeGame.main(args);
    }
}
