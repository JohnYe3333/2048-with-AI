package model;

/**
 * Created by Guyu on 10/19/2014.
 */
public class TFEGameState {

    private int[][] grid;
    private int score;
    private int moveNum;
    private boolean hasWon;

    private final int GRID_SIZE = TwentyFortyEightGame.GRID_SIZE;

    public static TFEGameState constructGameState(TwentyFortyEightGame game) {
        TFEGameState gameState = new TFEGameState();
        gameState.grid = game.getGrid();
        gameState.score = game.getScore();
        gameState.moveNum = game.getMoveNum();
        gameState.hasWon = game.hasWon();

        return gameState;
    }

    public int[][] getGrid() {
        int[][] gridCopy = new int[GRID_SIZE][GRID_SIZE];
        for (int row = 0; row < GRID_SIZE; row++)
            for (int col = 0; col < GRID_SIZE; col++)
                gridCopy[row][col] = grid[row][col];

        return gridCopy;
    }

    public int getScore() {
        return score;
    }

    public boolean hasWon() {
        return hasWon;
    }

}
