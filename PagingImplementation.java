package OSProject.com;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

public class PagingImplementation {
    private JFrame frame;
    private JTextField lasField, pasField, sizeField;
    private ButtonGroup lasGroup, pasGroup, sizeGroup;

    public PagingImplementation() {
        frame = new JFrame("Paging");
        frame.setSize(450, 250);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        initializeUI();
        frame.setVisible(true);
    }

    private void initializeUI() {
        // Labels
        addLabel("LAS:", 20, 20, 50, 25);
        addLabel("PAS:", 20, 60, 50, 25);
        addLabel("Size:", 20, 100, 50, 25);

        // Text Fields
        lasField = addTextField(80, 20, 120, 25);
        pasField = addTextField(80, 60, 120, 25);
        sizeField = addTextField(80, 100, 120, 25);

        // Radio Button Groups
        lasGroup = addRadioButtonGroup(210, 20);
        pasGroup = addRadioButtonGroup(210, 60);
        sizeGroup = addRadioButtonGroup(210, 100);

        // Buttons
        JButton calculateButton = new JButton("Calculate");
        calculateButton.setBounds(100, 160, 100, 30);
        frame.add(calculateButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(220, 160, 100, 30);
        frame.add(backButton);

        // Action Listeners
        calculateButton.addActionListener(e -> calculatePaging());
        backButton.addActionListener(e -> {
            frame.dispose();
            //new MemoryManagement(new JFrame());
        });
    }

    private void calculatePaging() {
        try {
            long las = parseSize(lasField.getText(), getSelectedUnit(lasGroup));
            long pas = parseSize(pasField.getText(), getSelectedUnit(pasGroup));
            long pageSize = parseSize(sizeField.getText(), getSelectedUnit(sizeGroup));

            if (pageSize <= 0) {
                showError("Page size must be a positive value.");
                return;
            }

            long numPages = las / pageSize;
            long numFrames = pas / pageSize;

            // Assuming Page Table Entry (PTE) size is 4 bytes
            long pteSize = 4;
            long pageTableSize = numPages * pteSize;

            DecimalFormat formatter = new DecimalFormat("#,###");

            String result = "Number of Pages: " + formatter.format(numPages) + "\n" +
                            "Number of Frames: " + formatter.format(numFrames) + "\n" +
                            "Page Table Size: " + formatSize(pageTableSize);

            JOptionPane.showMessageDialog(frame, result, "Paging Calculation Result", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            showError("Invalid input. Please enter numeric values.");
        } catch (Exception ex) {
            showError("An error occurred during calculation: " + ex.getMessage());
        }
    }

    private long parseSize(String value, String unit) {
        long multiplier = 1;
        switch (unit) {
            case "KB": multiplier = 1024; break;
            case "MB": multiplier = 1024 * 1024; break;
            case "GB": multiplier = 1024 * 1024 * 1024; break;
        }
        return (long) (Double.parseDouble(value) * multiplier);
    }

    private String getSelectedUnit(ButtonGroup group) {
        return group.getSelection().getActionCommand();
    }

    private String formatSize(long bytes) {
        if (bytes < 1024) return bytes + " Bytes";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTP".charAt(exp - 1) + "";
        return String.format("%.2f %sB", bytes / Math.pow(1024, exp), pre);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // --- UI Helper Methods ---
    private void addLabel(String text, int x, int y, int w, int h) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, w, h);
        frame.getContentPane().add(label);
    }

    private JTextField addTextField(int x, int y, int w, int h) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, w, h);
        frame.getContentPane().add(textField);
        return textField;
    }

    private ButtonGroup addRadioButtonGroup(int x, int y) {
        ButtonGroup group = new ButtonGroup();
        String[] units = {"KB", "MB", "GB"};
        int currentX = x;
        for (int i = 0; i < units.length; i++) {
            JRadioButton button = new JRadioButton(units[i]);
            button.setBounds(currentX, y, 60, 25);
            button.setActionCommand(units[i]);
            if (i == 0) button.setSelected(true);
            group.add(button);
            frame.getContentPane().add(button);
            currentX += 65;
        }
        return group;
    }
}
