package ui;

import model.TwentyFortyEightGame;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Guyu on 10/22/2014.
 */
public class StatsPanel extends JPanel{

    private int score;
    private int highScore;
    private int moveNum;
    private boolean gameWon;
    private boolean gameOver;

    private JLabel scoreLabel;
    private JLabel highScoreLabel;
    private JLabel moveNumLabel;
    private JLabel gameWonLabel;
    private JLabel gameOverLabel;
    
    
    public StatsPanel() {
        setLayout(new GridLayout(1, 5));
        setPreferredSize(new Dimension(GridPanel.size, 24));

        initStatsAndLabels();
        updateLabels();
        
        add(scoreLabel);
        add(highScoreLabel);
        add(moveNumLabel);
        add(gameWonLabel);
        add(gameOverLabel);
    }

    public void updateLabels() {
        scoreLabel.setText("Score: " + score);
        highScoreLabel.setText("High Score: " + highScore);
        moveNumLabel.setText("   " + moveNum + " Moves");
        gameWonLabel.setText(gameWon? "You won!" : "        ");
        gameOverLabel.setText(gameOver? "Game over." : "Keep going!");
    }

    public void updateStats(TwentyFortyEightGame game) {
        score = game.getScore();
        if (score > highScore)
            highScore = score;
        moveNum = game.getMoveNum();
        gameWon = game.hasWon();
        gameOver = game.isOver();
    }

    public void resetStats() {
        score = 0;
        moveNum = 0;
        gameWon = false;
        gameOver = false;
    }

    private void initStatsAndLabels() {
        resetStats();
        highScore = 0;

        scoreLabel = new JLabel();
        highScoreLabel = new JLabel();
        moveNumLabel = new JLabel();
        gameWonLabel = new JLabel();
        gameOverLabel = new JLabel();
    }

}
