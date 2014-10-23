package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import model.*;
import ai.*;

/**
 * Created by Guyu on 10/22/2014.
 */
public class GridPanel extends JPanel{
    private TwentyFortyEightGame tfeGame;
    private StatsPanel statsPanel;

    private static final int RIM_DIMENSION = 10;
    private static final int GRID_SIZE = TwentyFortyEightGame.GRID_SIZE;
    private static final int TILE_SIZE = 128;
    public static final int size = GRID_SIZE * TILE_SIZE + RIM_DIMENSION * 2;

    private Image BLANK;
    private Image TILES_IMG;
    private Image RIM_IMG;

    public GridPanel(StatsPanel statsPanel) {
        setPreferredSize(new Dimension(GRID_SIZE * TILE_SIZE + RIM_DIMENSION * 2,
                GRID_SIZE * TILE_SIZE + RIM_DIMENSION * 2));
        BLANK = new ImageIcon(getClass().getResource
                ("graphics/blank.png")).getImage();
        TILES_IMG = new ImageIcon(getClass().getResource
                ("graphics/tiles.png")).getImage();
        RIM_IMG = new ImageIcon(getClass().getResource
                ("graphics/rim.png")).getImage();

        tfeGame = new TwentyFortyEightGame();
        tfeGame.start();
        this.statsPanel = statsPanel;

        setKeyStrokeAction();
        setFocusable(true);
        requestFocusInWindow();
    }

    private void setKeyStrokeAction() {
        Action upAction = new MoveAction(Direction.UP);
        Action downAction = new MoveAction(Direction.DOWN);
        Action leftAction = new MoveAction(Direction.LEFT);
        Action rightAction = new MoveAction(Direction.RIGHT);

        InputMap imap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        imap.put(KeyStroke.getKeyStroke("UP"), "move_up");
        imap.put(KeyStroke.getKeyStroke("DOWN"), "move_down");
        imap.put(KeyStroke.getKeyStroke("LEFT"), "move_left");
        imap.put(KeyStroke.getKeyStroke("RIGHT"), "move_right");

        ActionMap amap = getActionMap();
        amap.put("move_up", upAction);
        amap.put("move_down", downAction);
        amap.put("move_left", leftAction);
        amap.put("move_right", rightAction);
    }

    @Override
    public void paintComponent(Graphics g) {
        drawRim(g);

        int[][] grid = tfeGame.getGrid();
        for (int row = 0; row < GRID_SIZE; row++)
            for (int col = 0; col < GRID_SIZE; col++)
                drawTile(row, col, grid[row][col], g);
    }

    private int logBase2(int num) {
        if (num == 2) return 1;
        else return logBase2(num / 2) + 1;
    }

    private int[] tileNumToImageLocation(int tileNum) {
        if (tileNum == 0) return null;

        int order = logBase2(tileNum);
        int row = ((order - 1) / 4) * TILE_SIZE;
        int col = ((order - 1) % 4) * TILE_SIZE;

        int[] location = {row, col};
        return location;
    }

    private void drawTile(int row, int col, int tileNum, Graphics g) {
        int dx = col * TILE_SIZE + RIM_DIMENSION;
        int dy = row * TILE_SIZE + RIM_DIMENSION;
        int[] location = tileNumToImageLocation(tileNum);

        if (location == null)
            g.drawImage(BLANK, dx, dy, dx + TILE_SIZE, dy + TILE_SIZE, 0, 0,
                    TILE_SIZE, TILE_SIZE, null);
        else {
            int sx = location[1];
            int sy = location[0];
            g.drawImage(TILES_IMG, dx, dy, dx + TILE_SIZE, dy + TILE_SIZE,
                    sx, sy, sx + TILE_SIZE, sy + TILE_SIZE, null);
        }
    }

    private void drawRim(Graphics g) {
        int size = GRID_SIZE * TILE_SIZE + RIM_DIMENSION * 2;
        g.drawImage(RIM_IMG, 0, 0, size, size, 0, 0, size, size, null);
    }

    private class MoveAction extends AbstractAction {

        public MoveAction(model.Direction dir) {
            putValue("direction", dir);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Direction dir = (Direction) getValue("direction");
            if (tfeGame.move(dir)) {
                statsPanel.updateStats(tfeGame);
                statsPanel.updateLabels();
                repaint();
            }
        }
    }

    public void restart() {
        tfeGame.restart();
        statsPanel.resetStats();
        statsPanel.updateLabels();
        repaint();
    }

    public void runAIOnce(int simNum) {
        if (tfeGame.isOver()) return;

        TFEAI tfeAI = new MonteCarloAI(tfeGame);
        Direction dir = tfeAI.getBestDirection(simNum);

        if (dir == null) {
            System.out.println("Run AI Once - Null Direction Bug");
            return;
        }

        tfeGame.move(dir);
        statsPanel.updateStats(tfeGame);
        statsPanel.updateLabels();
        repaint();
    }

    public boolean gameOver() {
        return tfeGame.isOver();
    }
}
