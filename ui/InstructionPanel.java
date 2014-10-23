package ui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Guyu on 10/22/2014.
 */
public class InstructionPanel extends JPanel{

    private Icon help = new ImageIcon(getClass().
            getResource("graphics/help.png"));
    private Icon arrowKeys = new ImageIcon(getClass().
            getResource("graphics/arrows.png"));

    public InstructionPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 32, 5));
        setPreferredSize(new Dimension(GridPanel.size, 64));
        add(new InsLabel("Try getting to the 2048 tile by " +
                "moving and merging smaller tiles.", help));
        add(new InsLabel("Use arrow keys to move tiles.", arrowKeys));
    }

    private class InsLabel extends JLabel {
        public InsLabel(String instruction, Icon icon) {
            setText(instruction);
            setFont(new Font("Arial", Font.BOLD, 17));
            setIcon(icon);
        }
    }

}
