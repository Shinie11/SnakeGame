package com.example.finalcoursework;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Панель главного меню приложения.
 */
public class MainMenuPane extends StackPane {
    // Константы для ширины и высоты экрана
    public static final int SCREEN_WIDTH = 400;
    public static final int SCREEN_HEIGHT = 300;

    private final ComboBox<String> speedComboBox;

    /**
     * Конструктор класса, инициализирующий элементы главного меню.
     */
    public MainMenuPane() {
        // Инициализация кнопки "Начать игру"
        Button startButton = new Button("Start game");
        startButton.setOnAction(event -> startGame());

        // Инициализация выпадающего списка для выбора скорости
        speedComboBox = new ComboBox<>();
        for (int i = 1; i <= 9; i++) {
            speedComboBox.getItems().add(String.valueOf(i));
        }
        speedComboBox.setValue("9");

        // Настройка внешнего вида выпадающего списка
        speedComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
            }
        });

        // Настройка внешнего вида кнопки в выпадающем списке
        speedComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
            }
        });

        // Создание вертикального контейнера с элементами меню
        VBox menuBox = new VBox(20, startButton, new Label("Выберите скорость:"), speedComboBox);
        menuBox.setAlignment(Pos.CENTER);

        // Добавление контейнера в StackPane (главный контейнер панели)
        getChildren().add(menuBox);
    }

    /**
     * Метод, вызываемый при нажатии на кнопку "Начать игру".
     */
    private void startGame() {
        // Создание игровой панели
        GamePane gamePane = new GamePane(Integer.parseInt(speedComboBox.getValue()));
        // Создание сцены для игровой панели
        Scene gameScene = new Scene(gamePane, GamePane.SCREEN_WIDTH, GamePane.SCREEN_HEIGHT);

        // Обработка нажатий клавиш в игровой сцене
        gameScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                gamePane.setDirection('L');
            } else if (event.getCode() == KeyCode.RIGHT) {
                gamePane.setDirection('R');
            } else if (event.getCode() == KeyCode.UP) {
                gamePane.setDirection('U');
            } else if (event.getCode() == KeyCode.DOWN) {
                gamePane.setDirection('D');
            } else {
                gamePane.handleKeyPress(event.getCode());
            }
        });

        // Получение объекта Stage из текущей сцены
        Stage stage = (Stage) getScene().getWindow();
        // Установка сцены с игровой панелью в главное окно
        stage.setScene(gameScene);
        // Запуск игры
        gamePane.startGame();

        // Обработка события закрытия окна
        stage.setOnCloseRequest(windowEvent -> {
            gamePane.stopGame();
            // Предотвращение закрытия окна, если игра запущена
            if (gamePane.isRunning()) {
                windowEvent.consume();
            }
        });
    }
}
