package model;

/**
 * Created by Guyu on 10/19/2014.
 */
public class TwentyFortyEightGame{

    private TwentyFortyEight tfe;
    public static final int GRID_SIZE = 4;
    private int moveNum;
    private int score;
    private boolean hasWon;

    public TwentyFortyEightGame() {
        tfe = new TwentyFortyEight(GRID_SIZE);
        score = 0;
        hasWon = false;
    }

    public static TwentyFortyEightGame constructGame(TFEGameState state) {
        TwentyFortyEightGame tfeGame = new TwentyFortyEightGame();
        tfeGame.tfe.setGrid(state.getGrid());
        tfeGame.score = state.getScore();
        tfeGame.hasWon = state.hasWon();

        return tfeGame;
    }

    public int[][] getGrid() {
        return tfe.getGrid();
    }

    public void display() {
        System.out.println(tfe);
        System.out.println("current score: " + score);
        System.out.println("\n");
    }

    public boolean move(Direction dir) {
        int[] moveResult = tfe.move(dir);
        if (moveResult[0] == TwentyFortyEight.MOVE_SUCCESS) {
            moveNum++;
            score += moveResult[1];
            if (hasWon == false && tfe.getMaxTile() >= 2048)
                hasWon = true;
            return true;
        }
        return false;
    }

    public void start() {
        tfe.addNewTile();
    }

    public void restart() {
        tfe.reset();
        tfe.addNewTile();
        moveNum = 0;
        score = 0;
        hasWon = false;
    }

    public int getScore() {
        return score;
    }

    public boolean isOver() {
        return tfe.isOver();
    }

    public Direction[] getValidMoves() {
        return tfe.getValidMoves();
    }

    public boolean hasWon() {
        return hasWon;
    }

    public int getMaxTile() {
        return tfe.getMaxTile();
    }

    public int getMoveNum() {
        return moveNum;
    }

}
