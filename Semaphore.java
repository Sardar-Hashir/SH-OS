package OSProject.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Semaphore {

    private final java.util.concurrent.Semaphore semaphore = new java.util.concurrent.Semaphore(1);
    private JTextArea logArea;
    private JPanel threadPanel;

    public void showUI(JFrame parentFrame) {
        JFrame frame = new JFrame("OS - Control Panel");
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(parentFrame);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.BLACK);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.BLACK);
        JLabel label = new JLabel("Number of Processes:");
        label.setForeground(Color.WHITE);

        JSpinner processSpinner = new JSpinner(new SpinnerNumberModel(2, 1, 10, 1));
        JButton startButton = new JButton("Start Simulation");
        startButton.setBackground(Color.GRAY);
        startButton.setForeground(Color.WHITE);

        topPanel.add(label);
        topPanel.add(processSpinner);
        topPanel.add(startButton);

        // -------- CENTER PANEL: Thread Status --------
        threadPanel = new JPanel();
        threadPanel.setLayout(new BoxLayout(threadPanel, BoxLayout.Y_AXIS));
        JScrollPane threadScroll = new JScrollPane(threadPanel);
        threadScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED), "Thread Status"));

        // -------- BOTTOM PANEL: Log Area --------
        logArea = new JTextArea(8, 40);
        logArea.setEditable(false);
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(Color.WHITE);
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.RED), "Execution Log"));

        // -------- Button Action --------
        startButton.addActionListener((ActionEvent e) -> {
            threadPanel.removeAll();
            logArea.setText("");
            int processCount = (Integer) processSpinner.getValue();
            for (int i = 1; i <= processCount; i++) {
                JLabel statusLabel = new JLabel("Process " + i + ": CREATED");
                statusLabel.setOpaque(true);
                statusLabel.setBackground(Color.YELLOW);
                statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                threadPanel.add(statusLabel);
                new DynamicProcessThread("Process " + i, semaphore, logArea, statusLabel).start();
            }
            threadPanel.revalidate();
            threadPanel.repaint();
        });

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(threadScroll, BorderLayout.CENTER);
        frame.add(logScroll, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    // -------- Thread Class --------
    class DynamicProcessThread extends Thread {
        private final String name;
        private final java.util.concurrent.Semaphore semaphore;
        private final JTextArea logArea;
        private final JLabel statusLabel;

        public DynamicProcessThread(String name, java.util.concurrent.Semaphore semaphore, JTextArea logArea, JLabel statusLabel) {
            this.name = name;
            this.semaphore = semaphore;
            this.logArea = logArea;
            this.statusLabel = statusLabel;
        }

        @Override
        public void run() {
            updateStatus("WAITING", Color.ORANGE);
            log(name + " is waiting to enter the critical section...");
            try {
                semaphore.acquire();
                updateStatus("IN CRITICAL SECTION", Color.GREEN);
                log(name + " entered the critical section ✅");
                Thread.sleep(2000); // Simulate work
                updateStatus("EXITED", Color.LIGHT_GRAY);
                log(name + " exited the critical section ❌");
            } catch (InterruptedException e) {
                log(name + " was interrupted.");
            } finally {
                semaphore.release();
            }
        }

        private void log(String message) {
            SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
        }

        private void updateStatus(String status, Color color) {
            SwingUtilities.invokeLater(() -> {
                statusLabel.setText(name + ": " + status);
                statusLabel.setBackground(color);
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Semaphore().showUI(null);
        });
    }
}
