package model;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by Guyu on 10/18/2014.
 */
public class TwentyFortyEight {

    private int[][] grid; // the underlying grid
    private final int size; // grid dimension

    // probability of generating the 4-tile
    private final double fourProb = 0.1;
    // constants denoting move success and move failure
    public static final int MOVE_SUCCESS = 2048;
    public static final int MOVE_FAILURE = -2048;
    // map between direction and initial-tile positions
    private HashMap<Direction, int[][]> initTilePosMap
            = new HashMap<Direction,int[][]>(5, 1);


    public TwentyFortyEight(int size) {
        this.size = size;
        grid = new int[size][size];

        initTilePosMap.put(Direction.UP, new int[][]
                {{3, 0}, {3, 1}, {3, 2}, {3, 3}});
        initTilePosMap.put(Direction.DOWN, new int[][]
                {{0, 0}, {0, 1}, {0, 2}, {0, 3}});
        initTilePosMap.put(Direction.LEFT, new int[][]
                {{0, 3}, {1, 3}, {2, 3}, {3, 3}});
        initTilePosMap.put(Direction.RIGHT, new int[][]
                {{0, 0}, {1, 0}, {2, 0}, {3, 0}});
    }

    private int[] merge(int[] line) {
        // result[0...size - 1] == merged line
        // result[size] = merge score
        int[] result = new int[size + 1];
        boolean[] merged = new boolean[size];
        for (int i = 0; i < size; i++)
            merged[i] = false;
        int mergeScore = 0;

        // merge tiles
        for (int i = size - 2; i >= 0; i--) {
            int j = i + 1;
            while (j < size - 1 && line[j] == 0) j++;
            if (line[i] == line[j] && merged[j] == false) {
                merged[j] = true;
                line[j] *= 2;
                mergeScore += line[j];
                line[i] = 0;
                continue;
            }
        }

        // move zeros
        int k = size - 1;
        for (int i = size - 1; i >= 0; i--)
            if (line[i] != 0)
                result[k--] = line[i];

        // append merge score
        result[size] = mergeScore;

        return result;
    }

    private boolean isFull() {
        for (int row = 0; row < size; row++)
            for (int col = 0; col < size; col++)
                if (grid[row][col] == 0)
                    return false;
        return true;
    }

    // given a move direction, convert the grid into lines to be merged
    private int[][] gridToLines(Direction dir) {
        int[][] lines = new int[size][size];
        int[][] initialTilesPos = initTilePosMap.get(dir);

        int m = 0;
        int n;
        if (dir.ROW_OR_COLUNM == Direction.isRow)
            for (int[] tilePos : initialTilesPos) {
                n = 0;
                for (int i = tilePos[1]; i >= 0 && i < size; i += dir.OFFSET)
                    lines[m][n++] = grid[tilePos[0]][i];
                m++;
            }
        else
            for (int[] tilePos: initialTilesPos) {
                n = 0;
                for (int i = tilePos[0]; i >= 0 && i < size; i += dir.OFFSET)
                    lines[m][n++] = grid[i][tilePos[1]];
                m++;
            }

        return lines;
    }

    // given a move direction, restore the merged lines back to the grid
    private void linesToGrid(Direction dir, int[][] lines) {
        int[][] initialTilePos = initTilePosMap.get(dir);

        int m = 0;
        int n;
        if (dir.ROW_OR_COLUNM == Direction.isRow)
            for (int[] tilePos: initialTilePos) {
                n = 0;
                for (int i = tilePos[1]; i >= 0 && i < size; i += dir.OFFSET)
                    grid[tilePos[0]][i] = lines[m][n++];
                m++;
            }
        else if (dir.ROW_OR_COLUNM == Direction.isCol)
            for (int[] tilePos: initialTilePos) {
                n = 0;
                for (int i = tilePos[0]; i >= 0 && i < size; i += dir.OFFSET)
                    grid[i][tilePos[1]] = lines[m][n++];
                m++;
            }
    }

    private boolean isSameGrid(int[][] grid1, int[][] grid2) {
        for (int row = 0; row < size; row++)
            for (int col = 0; col < size; col++)
                if (grid1[row][col] != grid2[row][col])
                    return false;
        return true;
    }

    // add a either a 2 tile or a 4 tile to the grid
    public void addNewTile() {
        if (isFull()) return;

        Random rand = new Random();
        int tile = (rand.nextDouble() > fourProb)? 2 : 4;

        int x = rand.nextInt(size);
        int y = rand.nextInt(size);
        if (grid[x][y] != 0)
            while (grid[x][y] != 0) {
                x = rand.nextInt(size);
                y = rand.nextInt(size);
            }
        grid[x][y] = tile;
    }

    // move the grid in a given direction
    public int[] move(Direction dir) {
        // make a copy of the current grid
        int[][] gridCopy = new int[size][size];
        for (int row = 0; row < size; row++)
            for (int col = 0; col < size; col++)
                gridCopy[row][col] = grid[row][col];

        // get lines to be merged
        int[][] lines = gridToLines(dir);

        // merge the lines
        int[] mergedline;
        int scoreIncrement = 0;
        for (int[] line: lines) {
            mergedline = merge(line);
            scoreIncrement += mergedline[size];
            for (int i = 0; i < size; i++)
                line[i] = mergedline[i];
        }

        // restore lines to grid
        linesToGrid(dir, lines);

        // construct result
        // result[0] = whether moved successfully
        // result[1] = score increment resulted from move
        int[] result = new int[2];
        if (!isSameGrid(grid, gridCopy)) {
            addNewTile();
            result[0] = MOVE_SUCCESS;
            result[1] = scoreIncrement;
        } else {
            result[0] = MOVE_FAILURE;
            result[1] = 0;
        }

        return result;
    }

    // return a copy of the grid
    public int[][] getGrid() {
        int[][] gridCopy = new int[size][size];
        for (int row = 0; row < size; row++)
            for (int col = 0; col < size; col++)
                gridCopy[row][col] = grid[row][col];

        return gridCopy;
    }

    // set the grid to a new grid
    public void setGrid(int[][] newGrid) {
        for (int row = 0; row < size; row++)
            for (int col = 0; col < size; col++)
                grid[row][col] = newGrid[row][col];
    }

    public Direction[] getValidMoves() {
        int[][] gridCopy = getGrid();
        int num = 0;
        Direction[] directions = new Direction[4];

        for (Direction dir: Direction.values()) {
            move(dir);
            if (!isSameGrid(grid, gridCopy))
                directions[num++] = dir;
            grid = gridCopy;
            gridCopy = getGrid();
        }

        Direction[] moves = new Direction[num];
        for (int i = 0; i < num; i++)
            moves[i] = directions[i];
        return moves;
    }

    public boolean isOver() {
        int[][] gridCopy = getGrid();
        for (Direction dir: Direction.values()) {
            move(dir);
            if (!isSameGrid(gridCopy, grid)) {
                grid = gridCopy;
                return false;
            }
            grid = gridCopy;
            gridCopy = getGrid();
        }
        return true;
    }

    public void reset() {
        for (int row = 0; row < size; row++)
            for (int col = 0; col < size; col++)
                grid[row][col] = 0;
    }

    public int getMaxTile() {
        int max = 2;
        for (int row = 0; row < size; row++)
            for (int col = 0; col < size; col++)
                if (grid[row][col] > max)
                    max = grid[row][col];
        return max;
    }

    private int getNumDigit(int num) {
        if (num < 10) return 1;
        else return getNumDigit(num / 10) + 1;
    }

    @Override
    public String toString() {
        String s = "";
        s += "+=====+=====+=====+=====+\n";
        for (int row = 0; row < size; row++) {
            s += "|";
            for (int col = 0; col < size; col++) {
                int tile = grid[row][col];

                int space = 5 - getNumDigit(tile);
                String spaceString = "";
                for (int i = 0; i < space; i++) spaceString += " ";

                s += tile == 0? " " : tile;
                s += spaceString;
                s += "|";
            }
            s += "\n+=====+=====+=====+=====+\n";
        }
        return s;
    }
}
