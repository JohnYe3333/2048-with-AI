package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Guyu on 10/19/2014.
 */

public class TwentyFortyEightFrame extends JFrame {

    private GamePanel gamePanel;
    private UserPanel userPanel;

    private final ImageIcon APP_ICON =
            new ImageIcon(getClass().getResource("graphics/logo.png"));

    public TwentyFortyEightFrame() {
        setLayout(new BorderLayout(16, 32));
        setTitle("2048 with AI (by Guyu Fan)");
        setIconImage(APP_ICON.getImage());
        setLocation(200, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gamePanel = new GamePanel();
        userPanel = new UserPanel(gamePanel.gridPanel);

        add(gamePanel, BorderLayout.WEST);
        add(userPanel, BorderLayout.EAST);
        pack();
    }
}
