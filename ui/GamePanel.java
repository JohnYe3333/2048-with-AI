package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Guyu on 10/22/2014.
 */
public class GamePanel extends JPanel{

    public GridPanel gridPanel;
    private InstructionPanel instructionPanel;
    private StatsPanel statsPanel;

    public GamePanel() {
        setLayout(new BorderLayout());

        instructionPanel = new InstructionPanel();
        statsPanel = new StatsPanel();
        gridPanel = new GridPanel(statsPanel);

        add(instructionPanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(statsPanel, BorderLayout.SOUTH);
    }
}
