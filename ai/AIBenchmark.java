package ai;

import model.*;
/**
 * Created by Guyu on 10/20/2014.
 */
public class AIBenchmark {

    private final int startSimNum;
    private final int increment;
    private final int endSimNum;
    private final int repeat;

    private final int totalNumTrials;
    private int currentSimNum; // current simulation numbers
    private int numTrialsSoFar; // number of trials run so far
    private int repeatBeforeInc;

    private BenchmarkStats bs;

    public AIBenchmark(int start, int inc, int end, int repeat) {
        startSimNum = start;
        increment = inc;
        endSimNum = end;
        this.repeat = repeat;

        totalNumTrials = repeat * ((endSimNum - startSimNum) / increment + 1);
        currentSimNum = start;
        numTrialsSoFar = 0;
        repeatBeforeInc = repeat;

        bs = new BenchmarkStats(totalNumTrials / repeat);
    }

    private class TrialStats {
        private int simNum;
        private int maxTile;
        private int score;

        private int simNumOrder;

        public TrialStats(int simNum, int biggestTile, int score, int simNumOrder) {
            this.simNum = simNum;
            this.maxTile = biggestTile;
            this.score = score;
            this.simNumOrder = simNumOrder;
        }
    }

    private class BenchmarkStats {
        
        int simNumNum; // number of different numbers of simulation
        int[] simNums, simNumsTimes;
        int[] maxTiles;
        int[] totalScores, averageScores;
        
        // 2 columns: one for num_positive, one for percentage;
        double[][] percent2048, percent4096, percent8192;

        public BenchmarkStats(int simNumNum) {
            this.simNumNum = simNumNum;
            initDataArrays(simNumNum);
        }

        private void initDataArrays(int simNumNum) {
            simNums = new int[simNumNum];
            simNumsTimes = new int[simNumNum];
            maxTiles = new int[simNumNum];
            percent2048 = new double[simNumNum][3];
            percent4096 = new double[simNumNum][3];
            percent8192 = new double[simNumNum][3];
            totalScores = new int[simNumNum];
            averageScores = new int[simNumNum];
        }
        
        public void add(TrialStats ts) {
            int index = ts.simNumOrder;

            simNums[index] = ts.simNum;
            simNumsTimes[index]++;

            if (ts.maxTile > maxTiles[index])
                maxTiles[index] = ts.maxTile;

            totalScores[index] += ts.score;
            averageScores[index] = (simNumsTimes[index] == 0) ?
                    totalScores[index] : totalScores[index] / simNumsTimes[index];
            
            if (ts.maxTile > 8192)
                System.out.println("I achieved 16384!");
            else if (ts.maxTile > 4096) {
                percent8192[index][0]++;
                percent8192[index][1] = percent8192[index][0] / simNumsTimes[index];

                percent4096[index][0]++;
                percent4096[index][1] = percent4096[index][0] / simNumsTimes[index];

                percent2048[index][0]++;
                percent2048[index][1] = percent2048[index][0] / simNumsTimes[index];
            } else if (ts.maxTile > 2048) {
                percent4096[index][0]++;
                percent4096[index][1] = percent4096[index][0] / simNumsTimes[index];

                percent2048[index][0]++;
                percent2048[index][1] = percent2048[index][0] / simNumsTimes[index];
            } else if (ts.maxTile == 2048) {
                percent2048[index][0]++;
                percent2048[index][1] = percent2048[index][0] / simNumsTimes[index];
            }

        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < simNumNum; i++) {
                sb.append(simNums[i] + " runs per move:\n");
                sb.append("    Max tile achieved: " + maxTiles[i] + "\n");
                sb.append("    Average score: " + averageScores[i] + "\n");

                sb.append("    2048 Percentage: " + (percent2048[i][1] * 100) + "%\n");
                sb.append("    4096 Percentage: " + (percent4096[i][1] * 100) + "%\n");
                sb.append("    8192 Percentage: " + (percent8192[i][1] * 100) + "%\n");

                sb.append("\n");
            }
            return sb.toString();
        }
    }

    private TrialStats runOnce(int simNum, int simNumOrder) {
        TwentyFortyEightGame game = new TwentyFortyEightGame();
        game.start();
        while (!game.isOver()) {
            TFEAI tfeAI = new MonteCarloAI(game);
            Direction dir = tfeAI.getBestDirection(simNum);
            if (dir != null) game.move(dir);
        }
        return new TrialStats(simNum, game.getMaxTile(),
                game.getScore(), simNumOrder);
    }

    public void runBenchmark() {
        int order = 0;
        for (int simNum = startSimNum; simNum <= endSimNum; simNum += increment) {
            for (int r = 0; r < repeat; r++) {
                bs.add(runOnce(simNum, order));
                numTrialsSoFar++;
                System.out.println(numTrialsSoFar + " games run. "
                        + (totalNumTrials - numTrialsSoFar) + " games left.");
            }
            order++;
        }
    }

    public boolean isComplete() {
        return numTrialsSoFar == totalNumTrials;
    }

    public void runOneTrial() {
        bs.add(runOnce(currentSimNum, numTrialsSoFar / repeat));
        numTrialsSoFar++;
        repeatBeforeInc--;

        if (repeatBeforeInc == 0) {
            repeatBeforeInc = repeat;
            currentSimNum += increment;
        }
    }

    public String getStatsString() {
        return bs.toString();
    }

    public int[] getStatus() {
        int[] status = new int[3];
        status[0] = numTrialsSoFar + 1;
        status[1] = totalNumTrials;
        status[2] = currentSimNum;

        return status;
    }

    public static void main(String[] args) {
        AIBenchmark benchmark = new AIBenchmark(1, 1, 1, 5);
        //while (!benchmark.isComplete()) benchmark.runOneTrial();
        benchmark.runBenchmark();
        System.out.println(benchmark.getStatsString());
    }
}
