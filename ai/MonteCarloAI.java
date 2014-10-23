package ai;

import model.*;
import model.Direction;

/**
 * Created by Guyu on 10/19/2014.
 */
public class MonteCarloAI implements TFEAI{

    public static boolean debug = false;

    protected TFEGameState currentGameState;
    private int[] scores;

    public MonteCarloAI(TwentyFortyEightGame currentGame) {
        currentGameState = TFEGameState.constructGameState(currentGame);
        scores = new int[4];
    }


    private int simulateOnce(Direction startDir) {
        TwentyFortyEightGame simGame = TwentyFortyEightGame.constructGame(currentGameState);
        if (debug) simGame.display();

        boolean moved = simGame.move(startDir);
        if (!moved) return -1;

        if (debug) System.out.print(startDir);

        while (!simGame.isOver()) {
            Direction dir = Direction.getRandomDirection();
            if(simGame.move(dir) && debug)
                System.out.print("->" + dir);;
        }
        if (debug) System.out.println();

        if (debug) {
            simGame.display();
            System.out.println("extra score : " + (simGame.getScore() - currentGameState.getScore()));
            System.out.println("----------------------------------------------");
        }

        //return simGame.getMoveNum() * 8 + simGame.getScore() - currentGameState.getScore();
        return simGame.getScore() - currentGameState.getScore();
    }

    private void simulate(int simNum) {
        Direction[] validDir =
                TwentyFortyEightGame.constructGame(currentGameState).getValidMoves();

        for (Direction dir: validDir) {
            int scoreIndex = Direction.directionToInt(dir);
            int sim = 0;
            while (sim < simNum) {
                int score = simulateOnce(dir);
                if (score == -1) continue;

                sim++;
                scores[scoreIndex] += score;
            }
        }
    }

    private Direction findGreatestScore() {
        int maxIndex = -1;
        int maxScore = -1;
        for (int i = 0; i < 4; i++)
            if (scores[i] > maxScore) {
                maxIndex = i;
                maxScore = scores[i];
            }

        return Direction.intToDirection(maxIndex);
    }

    public Direction getBestDirection(int simNum) {
        TwentyFortyEightGame game = TwentyFortyEightGame.constructGame(currentGameState);
        if (game.isOver())
            return null;
        simulate(simNum);
        return findGreatestScore();
    }

}
