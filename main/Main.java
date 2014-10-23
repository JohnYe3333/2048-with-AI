package main;

import ui.*;
import java.awt.*;

/**
 * Created by Guyu on 10/22/2014.
 */

public class Main {
    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TwentyFortyEightFrame().setVisible(true);
            }
        });
    }
}
