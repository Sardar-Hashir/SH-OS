package OSProject.com;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class OSProject {

    private JFrame frame;

    public OSProject() {
        frame = new JFrame("Operating System Project");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 650);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(245, 245, 250));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        // Header Section
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel);
        mainPanel.add(Box.createVerticalStrut(40));

        // Menu Card Panel
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)
        ));

        // Menu Title
        JLabel menuTitle = new JLabel("Select Module");
        menuTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        menuTitle.setForeground(new Color(72, 145, 220));
        menuTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(menuTitle);
        cardPanel.add(Box.createVerticalStrut(25));

        // Menu Buttons
        JButton btnProcess = createMenuButton(
                "Process Management",
                "Manage process states and scheduling",
                new Color(60, 179, 113),
                "⚙"
        );
        JButton btnMemory = createMenuButton(
                "Memory Management",
                "Handle memory allocation and paging",
                new Color(72, 145, 220),
                "💾"
        );
        JButton btnSemaphore = createMenuButton(
                "Synchronization",
                "Process synchronization with semaphores",
                new Color(255, 140, 0),
                "🔄"
        );

        cardPanel.add(btnProcess);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(btnMemory);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(btnSemaphore);
        cardPanel.add(Box.createVerticalStrut(25));

        // Exit Button
        JButton btnExit = createExitButton();
        cardPanel.add(btnExit);

        mainPanel.add(cardPanel);

        // Footer
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(footerPanel);

        frame.add(mainPanel);

        // Action Listeners
        btnProcess.addActionListener(e -> {
            new ProcessManagement();
            // Optionally hide main menu: frame.setVisible(false);
        });

        btnMemory.addActionListener(e -> {
            new MemoryManagement();
            // Optionally hide main menu: frame.setVisible(false);
        });

        btnSemaphore.addActionListener(e -> {
            new Semaphore().showUI(null);
            // Optionally hide main menu: frame.setVisible(false);
        });

        btnExit.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(
                    frame,
                    "Are you sure you want to exit?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        frame.setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 250));

        JLabel title = new JLabel("Operating System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 42));
        title.setForeground(new Color(51, 51, 51));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Simulation & Management Suite");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitle.setForeground(new Color(102, 102, 102));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel version = new JLabel("Version 1.0");
        version.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        version.setForeground(new Color(150, 150, 150));
        version.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(8));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(5));
        panel.add(version);

        return panel;
    }

    private JButton createMenuButton(String title, String description, Color color, String icon) {
        JPanel buttonPanel = new JPanel(new BorderLayout(15, 0));
        buttonPanel.setBackground(color);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        buttonPanel.setPreferredSize(new Dimension(500, 85));
        buttonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));
        buttonPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Icon Label
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        iconLabel.setForeground(Color.WHITE);

        // Text Panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(color);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(new Color(255, 255, 255, 220));

        textPanel.add(titleLabel);
        textPanel.add(Box.createVerticalStrut(4));
        textPanel.add(descLabel);

        // Arrow Label
        JLabel arrowLabel = new JLabel("→");
        arrowLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        arrowLabel.setForeground(Color.WHITE);

        buttonPanel.add(iconLabel, BorderLayout.WEST);
        buttonPanel.add(textPanel, BorderLayout.CENTER);
        buttonPanel.add(arrowLabel, BorderLayout.EAST);

        // Wrap in JButton for click functionality
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.add(buttonPanel);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            Color originalColor = color;
            Color hoverColor = color.darker();

            public void mouseEntered(MouseEvent evt) {
                buttonPanel.setBackground(hoverColor);
                textPanel.setBackground(hoverColor);
            }

            public void mouseExited(MouseEvent evt) {
                buttonPanel.setBackground(originalColor);
                textPanel.setBackground(originalColor);
            }
        });

        return button;
    }

    private JButton createExitButton() {
        JButton button = new JButton("Exit Application");
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(220, 20, 60));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(200, 45));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(new Color(178, 34, 34));
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(new Color(220, 20, 60));
            }
        });

        return button;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 250));

        JLabel footer = new JLabel("© 2024 Operating System Project");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(150, 150, 150));

        panel.add(footer);

        return panel;
    }

    public static void main(String[] args) {
        // Set system look and feel for better appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(OSProject::new);
    }
}
