package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Guyu on 10/22/2014.
 */

public class ControlPanel extends JPanel{
    private GridPanel gridPanel;
    private AIInfoPanel AIInfoPanel;
    private ButtonsPanel buttonsPanel;

    public ControlPanel(GridPanel gridPanel) {
        this.gridPanel = gridPanel;
        AIInfoPanel = new AIInfoPanel();
        buttonsPanel= new ButtonsPanel(AIInfoPanel, gridPanel);

        setLayout(new BorderLayout(32, 0));
        setPreferredSize(new Dimension(GridPanel.size, 128));
        add(AIInfoPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private class AIInfoPanel extends JPanel {
        private JLabel introLabel;
        private JLabel simNumLabel;
        private JTextField simNumField;

        public AIInfoPanel() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 16, 10));
            setPreferredSize(new Dimension(GridPanel.size, 80));
            
            introLabel = new JLabel("The AI is powered by Monte Carlo Simulation.");
            introLabel.setFont(new Font("Arial", Font.BOLD, 16));
            add(introLabel);

            simNumLabel = new JLabel("Number (< 10000) of simulations/move: ");
            simNumLabel.setFont(new Font("Arial", Font.BOLD, 16));
            add(simNumLabel);

            simNumField = new JTextField(6);
            simNumField.setText("300");
            add(simNumField);
        }

        public int getSimNum() {
            int simNum = -1;
            try {
                String text = simNumField.getText().trim();
                simNum = Integer.parseInt(text);
            } catch (NumberFormatException ex) {
                System.out.println(ex);
            }
            return simNum;
        }
    }

    private class ButtonsPanel extends JPanel {

        AIInfoPanel aiInfoPanel;
        GridPanel gridPanel;

        private JButton runOnce;
        private JButton autoPlay;
        private JButton restart;

        private AtomicBoolean paused;
        private final Thread threadObject;

        public ButtonsPanel(final AIInfoPanel aiInfoPanel, final GridPanel gridPanel) {
            setLayout(new FlowLayout(FlowLayout.CENTER, 32, 0));
            setPreferredSize(new Dimension(gridPanel.size, 48));

            this.aiInfoPanel = aiInfoPanel;
            this.gridPanel = gridPanel;

            runOnce = new JButton("AI Plays 1 Move");
            runOnce.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int simNum = aiInfoPanel.getSimNum();
                    if (simNum != -1)
                        gridPanel.runAIOnce(simNum);
                }
            });
            add(runOnce, BorderLayout.WEST);

            paused = new AtomicBoolean(true);
            autoPlay = new JButton("Auto Play");
            autoPlay.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!paused.get()) {
                        autoPlay.setText("Auto Play");
                        paused.set(true);
                    } else {
                        autoPlay.setText("Stop");
                        paused.set(false);

                        synchronized (threadObject) {
                            threadObject.notify();
                        }
                    }
                }
            });
            add(autoPlay, BorderLayout.CENTER);

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    while (true)
                        for (int i = 0; i < Integer.MAX_VALUE; i++) {
                            if (paused.get())
                                synchronized (threadObject) {
                                    try {
                                        threadObject.wait();
                                    } catch (InterruptedException ex) {
                                        System.out.println(ex);
                                    }
                                }

                            int simNum = aiInfoPanel.getSimNum();
                            if (simNum != -1) {
                                gridPanel.runAIOnce(simNum);
                                if (gridPanel.gameOver()) {
                                    autoPlay.setText("Auto Play");
                                    paused.set(true);
                                }
                            }

                            try {
                                if (simNum < 200)
                                    Thread.sleep(5);
                            } catch (InterruptedException ex) {
                                System.out.println(ex);
                            }
                        }
                }
            };
            threadObject = new Thread(runnable);
            threadObject.start();

            restart = new JButton("Restart");
            restart.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    gridPanel.restart();
                }
            });
            add(restart, BorderLayout.EAST);
        }
    }
}
