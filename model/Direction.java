package model;

import java.util.Random;

/**
 * Created by Guyu on 10/19/2014.
 */
public enum Direction {
    UP (-1, -1),
    DOWN (-1, 1),
    LEFT (1, -1),
    RIGHT (1, 1);

    public final int ROW_OR_COLUNM;
    public final int OFFSET;

    public static final int isRow = 1;
    public static final int isCol = -1;
    public static final Direction[] DIRECTIONS = Direction.values();

    private static Random rand = new Random();

    Direction(int rowOrCol, int offset) {
        this.ROW_OR_COLUNM = rowOrCol;
        this.OFFSET = offset;
    }

    public static Direction getRandomDirection() {
        return DIRECTIONS[rand.nextInt(DIRECTIONS.length)];
    }

    public static int directionToInt(Direction dir) {
        if (dir == UP) return 0;
        if (dir == DOWN) return 1;
        if (dir == LEFT) return 2;
        return 3;
    }

    public static Direction intToDirection(int n) {
        if (n < 0 || n > 3) return null;
        if (n == 0) return UP;
        if (n == 1) return DOWN;
        if (n == 2) return LEFT;
        return RIGHT;
    }
}


