public enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    public Direction getOpposite() {
        switch (this) {
            case LEFT:
                return RIGHT;
            case UP:
                return DOWN;
            case RIGHT:
                return LEFT;
            case DOWN:
                return UP;
            default:
                throw new IllegalStateException();
        }
    }
}
