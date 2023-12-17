package com.example.finalcoursework;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Класс GamePane представляет игровую панель для игры в Змейку.
 */
public class GamePane extends StackPane {


    private static final Logger logger = LogManager.getLogger(GamePane.class);


    /** Ширина игрового экрана. */
    public static final int SCREEN_WIDTH = 600;

    /** Высота игрового экрана. */
    public static final int SCREEN_HEIGHT = 600;

    /** Размер единичной ячейки на игровом экране. */
    public static final int UNIT_SIZE = 25;

    private final Canvas canvas;
    private char direction;
    private boolean running;
    private Timeline timeline;
    private int delay;

    private final int[] x = new int[100];
    private final int[] y = new int[100];
    private int bodyParts;
    private int applesEaten;

    private int appleX;
    private int appleY;

    private final Random random;

    /**
     * Конструктор класса GamePane.
     *
     * @param initialSpeed Начальная скорость игры.
     */
    public GamePane(int initialSpeed) {
        canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        getChildren().add(canvas);
        setAlignment(Pos.CENTER);
        random = new Random();
        delay = 100 - initialSpeed * 10;
        setOnKeyPressed(event -> handleKeyPress(event.getCode()));
    }

    /**
     * Метод запускает игру.
     */
    public void startGame() {

        // Логирование при запуске игры
        logger.info("The game started. Speed: {}", (100 - delay) / 10);




        direction = getRandomDirection();
        running = true;
        bodyParts = 3;
        applesEaten = 0;
        createSnake();
        newApple();
        draw();

        timeline = new Timeline(new KeyFrame(Duration.millis(delay), event -> {
            if (running) {
                move();
                checkApple();
                checkCollisions();
                draw();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Метод останавливает игру.
     */
    public void stopGame() {
        running = false;
        if (timeline != null) {
            timeline.stop();
        }
    }

    /**
     * Метод устанавливает новое направление движения.
     *
     * @param newDirection Новое направление движения.
     */
    public void setDirection(char newDirection) {
        char oppositeDirection = getOppositeDirection();
        if (newDirection != oppositeDirection) {
            direction = newDirection;
        }
        switch (newDirection) {
            case 'U':
                logger.info("The player turned up");
                break;
            case 'D':
                logger.info("The player turned down");
                break;
            case 'L':
                logger.info("The player turned left");
                break;
            case 'R':
                logger.info("ИThe player turned right");
                break;
            default:
                logger.warn("Unsupported direction: {}", newDirection);
                break;
        }
    }

    /**
     * Метод возвращает противоположное текущему направление движения.
     *
     * @return Противоположное направление движения.
     */
    private char getOppositeDirection() {
        return switch (direction) {
            case 'U' -> 'D';
            case 'D' -> 'U';
            case 'L' -> 'R';
            case 'R' -> 'L';
            default -> ' ';
        };
    }

    /**
     * Метод проверяет, работает ли игра.
     *
     * @return true, если игра работает, в противном случае - false.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Метод устанавливает новую задержку для таймлайна, что влияет на скорость игры.
     *
     * @param newDelay Новая задержка.
     */
    public void setDelay(int newDelay) {
        delay = 100 - newDelay * 10;
        if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
            timeline.stop();
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(delay), event -> {
                if (running) {
                    move();
                    checkApple();
                    checkCollisions();
                    draw();
                }
            }));
            timeline.play();

            // Логирование изменения скорости в реальном времени
            logger.info("The speed was changed in real time. New speed: {}", (100 - delay) / 10);
        }
    }

    /**
     * Метод обрабатывает нажатия клавиш.
     *
     * @param code Код нажатой клавиши.
     */
    public void handleKeyPress(KeyCode code) {
        if (code.isDigitKey()) {
            int speed = Integer.parseInt(code.getName());
            if (speed >= 1 && speed <= 9) {
                setDelay(speed);
            }
        }
    }

    /**
     * Метод генерирует случайное направление движения.
     *
     * @return Случайное направление движения ('U', 'D' или 'R').
     */
    private char getRandomDirection() {
        char[] directions = {'U', 'D', 'R'};
        return directions[random.nextInt(directions.length)];
    }

    /**
     * Метод создает змею на игровом поле.
     */
    private void createSnake() {
        int initialX = (SCREEN_WIDTH / 2 / UNIT_SIZE) * UNIT_SIZE;
        int initialY = (SCREEN_HEIGHT / 2 / UNIT_SIZE) * UNIT_SIZE;

        for (int i = 0; i < UNIT_SIZE; i++) {
            x[i] = initialX - i * UNIT_SIZE;
            y[i] = initialY;
        }
    }

    /**
     * Метод размещает новое яблоко на игровом поле.
     */
    private void newApple() {
        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    /**
     * Метод перемещает змею на игровом поле в соответствии с текущим направлением.
     */
    private void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    /**
     * Метод проверяет, съела ли змея яблоко, и обновляет положение яблока при необходимости.
     */
    private void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();



            // Логирование при поедании яблока
            logger.info("Snake eat an apple. Score: {}", applesEaten);
        }
    }

    /**
     * Метод проверяет столкновения змеи с самой собой или границами игрового поля.
     */
    private void checkCollisions() {
        try {
            for (int i = bodyParts; i > 0; i--) {
                if (x[0] == x[i] && y[0] == y[i]) {
                    running = false;
                    gameOver();
                }
            }
            if (x[0] <   0) {
                x[0] = SCREEN_WIDTH - UNIT_SIZE;
            } else if (x[0] >= SCREEN_WIDTH) {
                x[0] = 0;
            } else if (y[0] < 0) {
                y[0] = SCREEN_HEIGHT - UNIT_SIZE;
            } else if (y[0] >= SCREEN_HEIGHT) {
                y[0] = 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Обработка исключения
            logger.error("Error Message", e); // Вывод ошибки в логи
        }
    }


    /**
     * Метод отображает диалоговое окно при завершении игры.
     */
    private void gameOver() {
        timeline.stop();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Game Over\nScore: " + applesEaten, ButtonType.OK);
        alert.setHeaderText(null);
        alert.setTitle("Конец игры");
        alert.setOnHidden(event -> {
            com.example.finalcoursework.MainMenuPane mainMenuPane = new com.example.finalcoursework.MainMenuPane();
            Scene menuScene = new Scene(mainMenuPane, com.example.finalcoursework.MainMenuPane.SCREEN_WIDTH, com.example.finalcoursework.MainMenuPane.SCREEN_HEIGHT);
            Stage stage = (Stage) getScene().getWindow();
            stage.setScene(menuScene);
        });
        alert.show();
        // Логирование при поражении пользователя
        logger.info("The game is over. Score: {}", applesEaten);
    }

    /**
     * Метод отрисовывает игровое поле.
     */
    private void draw() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        for (int i = 0; i < UNIT_SIZE; i++) {
            gc.strokeLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
            gc.strokeLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
        }

        gc.setFill(Color.RED);
        gc.fillRect(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                gc.setFill(Color.GREEN);
                gc.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            } else {
                gc.setFill(new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1));
                gc.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }

        gc.setFill(Color.RED);
        gc.setFont(new javafx.scene.text.Font("Arial", 20));
        gc.fillText("Счет: " + applesEaten, 10, 30);
    }
}
