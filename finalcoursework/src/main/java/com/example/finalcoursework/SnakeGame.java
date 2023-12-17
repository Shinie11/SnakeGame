package com.example.finalcoursework;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Главный класс приложения, запускающий JavaFX приложение.
 */
public class SnakeGame extends Application {

    static final Logger logger = LogManager.getLogger(SnakeGame.class);


    /**
     * Точка входа в приложение.
     *
     * @param args Аргументы командной строки.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Метод, вызываемый при запуске JavaFX приложения.
     *
     * @param primaryStage Объект Stage, представляющий главное окно приложения.
     */
    @Override
    public void start(Stage primaryStage) {
        // Создание панели главного меню
        MainMenuPane mainMenuPane = new MainMenuPane();
        // Создание сцены для главного меню
        Scene menuScene = new Scene(mainMenuPane, MainMenuPane.SCREEN_WIDTH, MainMenuPane.SCREEN_HEIGHT);

        // Настройка основных параметров главного окна
        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(menuScene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // Вывод версии JavaFX в консоль (это может быть полезно для отладки)
        System.out.println("JavaFX Version: " + javafx.scene.control.Control.class.getPackage().getImplementationVersion());
    }

}

