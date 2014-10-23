package ui;

/**
 * Created by Guyu on 10/22/2014.
 */

import ai.AIBenchmark;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class BenchMarkPanel extends JPanel{

    private AIBenchmark benchmark;
    private boolean benchmarkInit = false;
    private OptionsPanel optionsPanel;
    private ControlAndResultPanel controlAndResultPanel;

    public BenchMarkPanel() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 16, 4));
        setPreferredSize(new Dimension(GridPanel.size, 650));
        optionsPanel = new OptionsPanel();
        add(optionsPanel);
        controlAndResultPanel = new ControlAndResultPanel();
        add(controlAndResultPanel);
    }

    private void initBenchMark() {
        int start = optionsPanel.getStart();
        int inc = optionsPanel.getInc();
        int end = optionsPanel.getEnd();
        int repeat = optionsPanel.getRepeat();
        benchmark = new AIBenchmark(start, inc, end, repeat);
        benchmarkInit = true;
    }

    private class SettingsInstructionPanel extends JPanel {
        private JLabel instructionLabel1;
        private JLabel instructionLabel2;

        public SettingsInstructionPanel() {
            setPreferredSize(new Dimension(GridPanel.size, 60));
            setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

            instructionLabel1 = new JLabel("You can perform test runs to estimate AI's performance");
            instructionLabel1.setFont(new Font("Arial", Font.BOLD, 16));
            instructionLabel2 = new JLabel("under different number of simulation runs per move.");
            instructionLabel2.setFont(new Font("Arial", Font.BOLD, 16));

            add(instructionLabel1);
            add(instructionLabel2);
        }
    }

    private class SettingPanel extends JPanel {
        public SettingPanel(JLabel label, JTextField field) {
            setPreferredSize(new Dimension(GridPanel.size, 24));
            setLayout(new FlowLayout(FlowLayout.LEFT, 20, 5));

            add(label);
            add(field);
        }
    }
    
    private class SettingsPanel extends JPanel {

        private final Font SettingsFont = new Font("Arial", Font.ITALIC, 15);

        private JLabel startLabel;
        private JLabel incLabel;
        private JLabel endLabel;
        private JLabel repeatLabel;

        private JTextField startField;
        private JTextField incField;
        private JTextField endField;
        private JTextField repeatField;

        public SettingsPanel() {
            setLayout(new GridLayout(4, 1));
            setPreferredSize(new Dimension(GridPanel.size, 140));

            startLabel = new JLabel("Starting number of simulation runs/move: ");
            startLabel.setFont(SettingsFont);
            incLabel = new JLabel("Simulation runs increment: ");
            incLabel.setFont(SettingsFont);
            endLabel = new JLabel("Upper Limit of the number of simulation runs/move: ");
            endLabel.setFont(SettingsFont);
            repeatLabel = new JLabel("Number of repeat runs: ");
            repeatLabel.setFont(SettingsFont);

            startField = new JTextField(5);
            incField = new JTextField(5);
            endField = new JTextField(5);
            repeatField = new JTextField(5);

            add(new SettingPanel(startLabel, startField));
            add(new SettingPanel(incLabel, incField));
            add(new SettingPanel(endLabel, endField));
            add(new SettingPanel(repeatLabel, repeatField));
        }
        
        public int getStart() {
            int start = -1;
            try {
                start = Integer.parseInt(startField.getText().trim());
            } catch (NumberFormatException ex) {
                System.out.println(ex);
            }
            return start;
        }

        public int getInc() {
            int inc = -1;
            try {
                inc = Integer.parseInt(incField.getText().trim());
            } catch (NumberFormatException ex) {
                System.out.println(ex);
            }
            return inc;
        }

        public int getEnd() {
            int end = -1;
            try {
                end = Integer.parseInt(endField.getText().trim());
            } catch (NumberFormatException ex) {
                System.out.println(ex);
            }
            return end;
        }

        public int getRepeat() {
            int repeat = -1;
            try {
                repeat = Integer.parseInt(repeatField.getText().trim());
            } catch (NumberFormatException ex) {
                System.out.println(ex);
            }
            return repeat;
        }
    }

    private class OptionsPanel extends JPanel {
        private JLabel status;
        private SettingsPanel settingsPanel;

        public OptionsPanel() {
            setLayout(new BorderLayout(0, 15));
            setPreferredSize(new Dimension(GridPanel.size, 230));

            add(new SettingsInstructionPanel(), BorderLayout.NORTH);
            settingsPanel = new SettingsPanel();
            add(settingsPanel, BorderLayout.CENTER);

            JPanel statusPanel = new JPanel();
            statusPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
            status = new JLabel();
            status.setFont(new Font("Arial", Font.BOLD, 15));
            statusPanel.add(status);
            add(statusPanel, BorderLayout.SOUTH);
        }

        public void updateStatus(int[] status) {
            int gameNum = status[0];
            int numGames = status[1];
            int simNum = status[2];
            int gamesLeft = numGames - gameNum;

            this.status.setText("Running game No. " + gameNum + " at " + simNum
                + " runs per move... " + gamesLeft + " games left.");
        }

        public void setStatusDone() {
            this.status.setText("Benchmark Done!");
        }

        public int getStart() {
            return settingsPanel.getStart();
        }

        public int getEnd() {
            return settingsPanel.getEnd();
        }

        public int getInc() {
            return settingsPanel.getInc();
        }

        public int getRepeat() {
            return settingsPanel.getRepeat();
        }
    }

    private class ControlAndResultPanel extends JPanel{

        private JButton run;
        private JTextArea result;
        private JScrollPane resultPane;

        private AtomicBoolean paused = new AtomicBoolean(true);
        private Thread threadObject;

        public ControlAndResultPanel() {
            setPreferredSize(new Dimension(GridPanel.size, 400));
            setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

            run = new JButton("Run AI Benchmark");
            run.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!paused.get()) {
                        run.setText("Run AI Benchmark");
                        paused.set(true);
                    } else {
                        if (!benchmarkInit)
                            initBenchMark();
                        run.setText("Pause Benchmark");
                        paused.set(false);
                        synchronized (threadObject) {
                            threadObject.notify();
                        }
                    }
                }
            });
            add(run);

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

                            if (!benchmark.isComplete()) {
                                optionsPanel.updateStatus(benchmark.getStatus());
                                result.setText("");
                                benchmark.runOneTrial();
                            } else {
                                optionsPanel.setStatusDone();
                                run.setText("Run AI Benchmark");
                                benchmarkInit = false;
                                paused.set(true);
                                result.setText(benchmark.getStatsString());
                            }
                        }
                }
            };

            result = new JTextArea(10, 35);
            result.setLineWrap(true);
            result.setFont(new Font("Arial", Font.BOLD, 15));
            resultPane = new JScrollPane(result);
            add(resultPane);

            threadObject = new Thread(runnable);
            threadObject.start();
        }
    }
}
