import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\u26AB";

    public boolean isAlive;
    private List<GameObject> snakeParts = new ArrayList<>();
    private Direction direction;
    private Direction nextDirection;

    public Snake(int x, int y) {
        snakeParts.add(new GameObject(x, y));
        snakeParts.add(new GameObject(x + 1, y));
        snakeParts.add(new GameObject(x + 2, y));
        isAlive = true;
        direction = Direction.LEFT;
        nextDirection = direction;
    }

    public void updateDirection() {
        this.direction = nextDirection;
    }

    public void setDirection(Direction direction) {
        if (!(this.direction == direction.getOpposite())) {
            this.nextDirection = direction;
            System.out.println("Direction is " + direction.name());
        }
    }

    public int getLength() {
        return snakeParts.size();
    }

    public void move(Apple apple) {
        GameObject newHead = createNewHead();
        if (checkWallCollision(newHead)) {
            isAlive = false;
            return;
        }
        if (checkCollision(newHead)) {
            isAlive = false;
            return;
        }
        snakeParts.add(0, newHead);
        checkWallCollision(newHead);
        if (isAppleAte(newHead, apple)) {
            apple.isAlive = false;
        } else {
            removeTail();
        }
    }

    private boolean checkWallCollision(GameObject head) {
        return head.x < 0 || head.x >= ArtdySnake.WIDTH
                || head.y < 0 || head.y >= ArtdySnake.HEIGHT;
    }

    public boolean checkCollision(GameObject gameObject) {
        for (GameObject part : snakeParts) {
            if (part.x == gameObject.x && part.y == gameObject.y) {
                return true;
            }
        }
        return false;
    }

    public GameObject createNewHead() {
        int headX = snakeParts.get(0).x;
        int headY = snakeParts.get(0).y;
        switch (direction) {
            case UP -> headY--;
            case RIGHT -> headX++;
            case DOWN -> headY++;
            case LEFT -> headX--;
        }
        return new GameObject(headX, headY);
    }

    private boolean isAppleAte(GameObject head, Apple apple) {
        return head.x == apple.x && head.y == apple.y;
    }

    public void removeTail() {
        snakeParts.remove(snakeParts.size() - 1);
    }

    public void draw(Game game) {
        Color snakeColor;
        if (isAlive) {
            snakeColor = Color.BLACK;
        } else {
            snakeColor = Color.RED;
        }
        game.setCellValueEx(snakeParts.get(0).x, snakeParts.get(0).y, Color.NONE, HEAD_SIGN, snakeColor, 75);
        for (int i = 1; i < snakeParts.size(); i++) {
            game.setCellValueEx(snakeParts.get(i).x, snakeParts.get(i).y, Color.NONE, BODY_SIGN, snakeColor, 75);
        }
    }
}
