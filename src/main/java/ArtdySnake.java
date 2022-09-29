import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;

import java.io.FileInputStream;
import java.util.Properties;

public class ArtdySnake extends Game {
    private static Properties locale;
    private static Properties config;

    protected static int width;
    protected static int height;
    private static int goal;
    private static int turnDelay;

    private Snake snake;
    private Apple apple;
    private static boolean isGameStopped;
    private int score;

    @Override
    public void initialize() {
        initializeGlobalVariables();
        setScreenSize(width, height);
        createGame();
    }

    private void initializeGlobalVariables() {
        loadPropertyFiles();
        setGlobalVariablesFromPropertyFiles();
    }

    private void loadPropertyFiles() {
        try (FileInputStream localeFis = new FileInputStream("src/main/resources/locale_en.properties");
             FileInputStream configFis = new FileInputStream("src/main/resources/config.properties")) {
            locale = new Properties();
            locale.load(localeFis);
            config = new Properties();
            config.load(configFis);
        } catch (Exception ignored) { }
    }

    private void setGlobalVariablesFromPropertyFiles() {
        width = Integer.parseInt(config.getProperty("board.width"));
        height = Integer.parseInt(config.getProperty("board.height"));
        goal = Integer.parseInt(config.getProperty("game.goal"));
        turnDelay = Integer.parseInt(config.getProperty("game.turn.delay"));
    }

    private void createGame() {
        setScore(score);
        setTurnTimer(turnDelay);
        createStartingObjects();
        isGameStopped = false;
        score = 0;
        drawScene();
    }

    private void createStartingObjects() {
        snake = new Snake(width / 2, height / 2);
        createNewApple();
    }

    private void restartGame() {
        if(isGameStopped) {
            createGame();
        }
    }

    private void createNewApple() {
        Apple newApple;
        do {
            int x = getRandomNumber(width);
            int y = getRandomNumber(height);
            newApple = new Apple(x, y);
        } while (snake.checkCollision(newApple));
        apple = newApple;
    }

    @Override
    public void onKeyPress(Key key) {
        switch (key) {
            case UP -> snake.setDirection(Direction.UP);
            case RIGHT -> snake.setDirection(Direction.RIGHT);
            case DOWN -> snake.setDirection(Direction.DOWN);
            case LEFT -> snake.setDirection(Direction.LEFT);
            case SPACE -> restartGame();
        }
    }

    @Override
    public void onTurn(int step) {
        snake.move(apple);
        if (!apple.isAlive) {
            score += 5;
            setScore(score);
            turnDelay -= 10;
            setTurnTimer(turnDelay);
            createNewApple();
        }
        if (!snake.isAlive) {
            gameOver();
        }
        if (snake.getLength() > goal) {
            win();
        }
        snake.updateDirection();
        drawScene();
    }

    private void gameOver() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.WHITE, locale.getProperty("game.lose"), Color.BLACK, 10);
    }

    private void win() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.WHITE, locale.getProperty("game.win"), Color.BLACK, 10);
    }

    private void drawScene() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                setCellValueEx(j, i, Color.ORANGE, "");
            }
        }
        snake.draw(this);
        apple.draw(this);
    }
}
