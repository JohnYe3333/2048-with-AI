package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Guyu on 10/22/2014.
 */
public class UserPanel extends JPanel{

    private ControlPanel controlPanel;
    private BenchMarkPanel benchMarkPanel;

    public UserPanel(GridPanel gridPanel) {
        setPreferredSize(new Dimension(GridPanel.size, GridPanel.size + 88));
        setLayout(new BorderLayout(32, 0));

        controlPanel = new ControlPanel(gridPanel);
        add(controlPanel, BorderLayout.NORTH);

        benchMarkPanel = new BenchMarkPanel();
        add(benchMarkPanel, BorderLayout.CENTER);
    }
}
