package OSProject.com;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.*;

class MemoryManagement extends JFrame {

    public MemoryManagement() {
        setTitle("Memory Management System");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        showMainMenu();
        setVisible(true);
    }

    private void showMainMenu() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(15, 0, 15, 0);

        JLabel titleLabel = new JLabel("Memory Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(new Color(51, 51, 51));
        gbc.gridy = 0;
        mainPanel.add(titleLabel, gbc);

        JLabel subtitleLabel = new JLabel("Operating System Simulator");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(102, 102, 102));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 25, 0);
        mainPanel.add(subtitleLabel, gbc);
        gbc.insets = new Insets(15, 0, 15, 0);

        JLabel contiguousLabel = new JLabel("Contiguous Allocation");
        contiguousLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        contiguousLabel.setForeground(new Color(70, 130, 180));
        gbc.gridy = 2;
        mainPanel.add(contiguousLabel, gbc);

        JButton firstFitBtn = createMenuButton("First Fit", new Color(72, 145, 220));
        gbc.gridy = 3;
        mainPanel.add(firstFitBtn, gbc);

        JButton bestFitBtn = createMenuButton("Best Fit", new Color(72, 145, 220));
        gbc.gridy = 4;
        mainPanel.add(bestFitBtn, gbc);

        JButton worstFitBtn = createMenuButton("Worst Fit", new Color(72, 145, 220));
        gbc.gridy = 5;
        mainPanel.add(worstFitBtn, gbc);

        JLabel nonContigLabel = new JLabel("Non-Contiguous Allocation");
        nonContigLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        nonContigLabel.setForeground(new Color(34, 139, 34));
        gbc.gridy = 6;
        gbc.insets = new Insets(30, 0, 15, 0);
        mainPanel.add(nonContigLabel, gbc);
        gbc.insets = new Insets(15, 0, 15, 0);

        JButton pagingBtn = createMenuButton("Paging", new Color(60, 179, 113));
        gbc.gridy = 7;
        mainPanel.add(pagingBtn, gbc);

        JButton lruBtn = createMenuButton("LRU Page Replacement", new Color(60, 179, 113));
        gbc.gridy = 8;
        mainPanel.add(lruBtn, gbc);

        getContentPane().removeAll();
        getContentPane().add(mainPanel);
        revalidate();
        repaint();

        firstFitBtn.addActionListener(e -> new ContiguousAllocation("First Fit"));
        bestFitBtn.addActionListener(e -> new ContiguousAllocation("Best Fit"));
        worstFitBtn.addActionListener(e -> new ContiguousAllocation("Worst Fit"));
        pagingBtn.addActionListener(e -> new PagingModule());
        lruBtn.addActionListener(e -> new LRUModule());
    }

    private JButton createMenuButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(320, 55));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    class ContiguousAllocation extends JFrame {
        private String algorithm;
        private int[] blockSizes, processSizes, allocation;

        ContiguousAllocation(String algo) {
            algorithm = algo;
            setTitle(algorithm + " - Memory Allocation");
            setSize(950, 650);
            setLocationRelativeTo(null);
            showInputScreen();
            setVisible(true);
        }

        private void showInputScreen() {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(new Color(245, 245, 250));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(10, 10, 10, 10);

            JLabel title = new JLabel(algorithm, SwingConstants.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 32));
            title.setForeground(new Color(51, 51, 51));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(title, gbc);
            gbc.gridwidth = 1;

            JLabel infoLabel = new JLabel("<html><center>Enter comma-separated values<br>(e.g., 100,200,300)</center></html>", SwingConstants.CENTER);
            infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            infoLabel.setForeground(new Color(102, 102, 102));
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            panel.add(infoLabel, gbc);
            gbc.gridwidth = 1;

            JLabel blockLabel = new JLabel("Block Sizes (KB):");
            blockLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(blockLabel, gbc);

            JTextField blockField = new JTextField(20);
            blockField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            blockField.setText("100,500,200,300,600");
            gbc.gridx = 1;
            panel.add(blockField, gbc);

            JLabel processLabel = new JLabel("Process Sizes (KB):");
            processLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            gbc.gridx = 0;
            gbc.gridy = 3;
            panel.add(processLabel, gbc);

            JTextField processField = new JTextField(20);
            processField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            processField.setText("212,417,112,426");
            gbc.gridx = 1;
            panel.add(processField, gbc);

            JButton executeBtn = new JButton("Execute Allocation");
            executeBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
            executeBtn.setBackground(new Color(72, 145, 220));
            executeBtn.setForeground(Color.WHITE);
            executeBtn.setPreferredSize(new Dimension(200, 45));
            executeBtn.setFocusPainted(false);
            executeBtn.setBorderPainted(false);
            executeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(25, 10, 10, 10);
            panel.add(executeBtn, gbc);

            JButton backBtn = new JButton("Back to Menu");
            backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            backBtn.setBackground(new Color(128, 128, 128));
            backBtn.setForeground(Color.WHITE);
            backBtn.setPreferredSize(new Dimension(150, 35));
            backBtn.setFocusPainted(false);
            backBtn.setBorderPainted(false);
            backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            gbc.gridy = 5;
            gbc.insets = new Insets(10, 10, 10, 10);
            panel.add(backBtn, gbc);

            getContentPane().removeAll();
            getContentPane().add(panel);
            revalidate();
            repaint();

            executeBtn.addActionListener(e -> {
                try {
                    blockSizes = Arrays.stream(blockField.getText().split(","))
                            .mapToInt(s -> Integer.parseInt(s.trim())).toArray();
                    processSizes = Arrays.stream(processField.getText().split(","))
                            .mapToInt(s -> Integer.parseInt(s.trim())).toArray();
                    performAllocation();
                    showResults();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid input! Please enter comma-separated numbers.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            backBtn.addActionListener(e -> dispose());
        }

        private void performAllocation() {
            allocation = new int[processSizes.length];
            Arrays.fill(allocation, -1);
            int[] temp = blockSizes.clone();

            for (int i = 0; i < processSizes.length; i++) {
                int index = -1;

                if (algorithm.equals("First Fit")) {
                    for (int j = 0; j < temp.length; j++) {
                        if (temp[j] >= processSizes[i]) {
                            index = j;
                            break;
                        }
                    }
                } else if (algorithm.equals("Best Fit")) {
                    int min = Integer.MAX_VALUE;
                    for (int j = 0; j < temp.length; j++) {
                        if (temp[j] >= processSizes[i] && temp[j] - processSizes[i] < min) {
                            min = temp[j] - processSizes[i];
                            index = j;
                        }
                    }
                } else { // Worst Fit
                    int max = -1;
                    for (int j = 0; j < temp.length; j++) {
                        if (temp[j] >= processSizes[i] && temp[j] - processSizes[i] > max) {
                            max = temp[j] - processSizes[i];
                            index = j;
                        }
                    }
                }

                if (index != -1) {
                    allocation[i] = index;
                    temp[index] -= processSizes[i];
                }
            }
        }

        private void showResults() {
            JPanel resultPanel = new JPanel(new BorderLayout(10, 10));
            resultPanel.setBackground(new Color(245, 245, 250));
            resultPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel titleLabel = new JLabel(algorithm + " - Allocation Results");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            titleLabel.setForeground(new Color(51, 51, 51));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            resultPanel.add(titleLabel, BorderLayout.NORTH);

            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Process ID", "Size (KB)", "Allocated Block", "Status"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable table = new JTable(model);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            table.setRowHeight(30);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            table.getTableHeader().setBackground(new Color(72, 145, 220));
            table.getTableHeader().setForeground(Color.WHITE);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            int allocated = 0, notAllocated = 0;
            for (int i = 0; i < processSizes.length; i++) {
                String status = allocation[i] == -1 ? "Not Allocated" : "Allocated";
                if (allocation[i] == -1) notAllocated++;
                else allocated++;

                model.addRow(new Object[]{
                        "P" + i,
                        processSizes[i],
                        allocation[i] == -1 ? "-" : "Block " + allocation[i],
                        status
                });
            }

            JScrollPane scrollPane = new JScrollPane(table);
            resultPanel.add(scrollPane, BorderLayout.CENTER);

            JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
            statsPanel.setBackground(new Color(245, 245, 250));

            JLabel allocatedLabel = new JLabel("✓ Allocated: " + allocated);
            allocatedLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            allocatedLabel.setForeground(new Color(34, 139, 34));

            JLabel notAllocatedLabel = new JLabel("✗ Not Allocated: " + notAllocated);
            notAllocatedLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            notAllocatedLabel.setForeground(new Color(220, 20, 60));

            statsPanel.add(allocatedLabel);
            statsPanel.add(notAllocatedLabel);

            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setBackground(new Color(245, 245, 250));
            bottomPanel.add(statsPanel, BorderLayout.NORTH);

            JButton backBtn = new JButton("Back to Input");
            backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            backBtn.setBackground(new Color(128, 128, 128));
            backBtn.setForeground(Color.WHITE);
            backBtn.setPreferredSize(new Dimension(150, 40));
            backBtn.setFocusPainted(false);
            backBtn.setBorderPainted(false);
            backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            backBtn.addActionListener(e -> showInputScreen());

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            btnPanel.setBackground(new Color(245, 245, 250));
            btnPanel.add(backBtn);
            bottomPanel.add(btnPanel, BorderLayout.CENTER);

            resultPanel.add(bottomPanel, BorderLayout.SOUTH);

            getContentPane().removeAll();
            getContentPane().add(resultPanel);
            revalidate();
            repaint();
        }
    }

    class PagingModule extends JFrame {
        private int[] pageTable;
        private int logical, physical, pageSize, pages, frames;

        PagingModule() {
            setTitle("Paging - Memory Management");
            setSize(950, 750);
            setLocationRelativeTo(null);
            showInput();
            setVisible(true);
        }

        private void showInput() {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(new Color(245, 245, 250));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel title = new JLabel("Paging Simulator", SwingConstants.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 32));
            title.setForeground(new Color(51, 51, 51));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(title, gbc);
            gbc.gridwidth = 1;

            JLabel infoLabel = new JLabel("<html><center>Configure paging parameters<br>Map format: page:frame (e.g., 0:2,1:0,2:1)</center></html>", SwingConstants.CENTER);
            infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            infoLabel.setForeground(new Color(102, 102, 102));
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            panel.add(infoLabel, gbc);
            gbc.gridwidth = 1;

            JTextField logicalField = new JTextField(10);
            logicalField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            logicalField.setText("1024");

            JTextField physicalField = new JTextField(10);
            physicalField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            physicalField.setText("2048");

            JTextField sizeField = new JTextField(10);
            sizeField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            sizeField.setText("256");

            JTextField mapField = new JTextField(15);
            mapField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            mapField.setText("0:2,1:0,2:1,3:3");

            JLabel logicalLabel = new JLabel("Logical Memory Size:");
            logicalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(logicalLabel, gbc);
            gbc.gridx = 1;
            panel.add(logicalField, gbc);

            JLabel physicalLabel = new JLabel("Physical Memory Size:");
            physicalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            gbc.gridx = 0;
            gbc.gridy = 3;
            panel.add(physicalLabel, gbc);
            gbc.gridx = 1;
            panel.add(physicalField, gbc);

            JLabel sizeLabel = new JLabel("Page/Frame Size:");
            sizeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            gbc.gridx = 0;
            gbc.gridy = 4;
            panel.add(sizeLabel, gbc);
            gbc.gridx = 1;
            panel.add(sizeField, gbc);

            JLabel mapLabel = new JLabel("Page:Frame Mapping:");
            mapLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            gbc.gridx = 0;
            gbc.gridy = 5;
            panel.add(mapLabel, gbc);
            gbc.gridx = 1;
            panel.add(mapField, gbc);

            JButton init = new JButton("Initialize Paging");
            init.setFont(new Font("Segoe UI", Font.BOLD, 16));
            init.setBackground(new Color(60, 179, 113));
            init.setForeground(Color.WHITE);
            init.setPreferredSize(new Dimension(200, 45));
            init.setFocusPainted(false);
            init.setBorderPainted(false);
            init.setCursor(new Cursor(Cursor.HAND_CURSOR));

            gbc.gridx = 0;
            gbc.gridy = 6;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(25, 10, 10, 10);
            panel.add(init, gbc);

            JButton backBtn = new JButton("Back to Menu");
            backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            backBtn.setBackground(new Color(128, 128, 128));
            backBtn.setForeground(Color.WHITE);
            backBtn.setPreferredSize(new Dimension(150, 35));
            backBtn.setFocusPainted(false);
            backBtn.setBorderPainted(false);
            backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            backBtn.addActionListener(e -> dispose());

            gbc.gridy = 7;
            gbc.insets = new Insets(10, 10, 10, 10);
            panel.add(backBtn, gbc);

            getContentPane().removeAll();
            getContentPane().add(panel);
            revalidate();
            repaint();

            init.addActionListener(e -> {
                try {
                    logical = Integer.parseInt(logicalField.getText());
                    physical = Integer.parseInt(physicalField.getText());
                    pageSize = Integer.parseInt(sizeField.getText());
                    pages = logical / pageSize;
                    frames = physical / pageSize;
                    pageTable = new int[pages];
                    Arrays.fill(pageTable, -1);

                    for (String m : mapField.getText().split(",")) {
                        String[] p = m.trim().split(":");
                        int page = Integer.parseInt(p[0].trim());
                        int frame = Integer.parseInt(p[1].trim());
                        if (page >= 0 && page < pages && frame >= 0 && frame < frames) {
                            pageTable[page] = frame;
                        }
                    }
                    showTable();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid input! Please check your values and mapping format.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        private void showTable() {
            JPanel resultPanel = new JPanel(new BorderLayout(10, 10));
            resultPanel.setBackground(new Color(245, 245, 250));
            resultPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel titleLabel = new JLabel("Page Table");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            titleLabel.setForeground(new Color(51, 51, 51));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            resultPanel.add(titleLabel, BorderLayout.NORTH);

            DefaultTableModel model = new DefaultTableModel(
                    new String[]{"Page Number", "Frame Number", "Status"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable table = new JTable(model);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            table.setRowHeight(30);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
            table.getTableHeader().setBackground(new Color(60, 179, 113));
            table.getTableHeader().setForeground(Color.WHITE);

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            int mapped = 0;
            for (int i = 0; i < pageTable.length; i++) {
                String status = pageTable[i] == -1 ? "Not Mapped" : "Mapped";
                if (pageTable[i] != -1) mapped++;
                model.addRow(new Object[]{i, pageTable[i] == -1 ? "-" : pageTable[i], status});
            }

            JScrollPane scrollPane = new JScrollPane(table);
            resultPanel.add(scrollPane, BorderLayout.CENTER);

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBackground(new Color(245, 245, 250));
            infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

            JLabel info1 = new JLabel("Total Pages: " + pages + " | Total Frames: " + frames + " | Page Size: " + pageSize + " bytes");
            info1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            info1.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel info2 = new JLabel("Mapped Pages: " + mapped + " / " + pages);
            info2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            info2.setForeground(new Color(60, 179, 113));
            info2.setAlignmentX(Component.CENTER_ALIGNMENT);

            infoPanel.add(info1);
            infoPanel.add(Box.createVerticalStrut(5));
            infoPanel.add(info2);

            JButton backBtn = new JButton("Back to Input");
            backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            backBtn.setBackground(new Color(128, 128, 128));
            backBtn.setForeground(Color.WHITE);
            backBtn.setPreferredSize(new Dimension(150, 40));
            backBtn.setFocusPainted(false);
            backBtn.setBorderPainted(false);
            backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            backBtn.addActionListener(e -> showInput());
            backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

            infoPanel.add(Box.createVerticalStrut(15));
            infoPanel.add(backBtn);

            resultPanel.add(infoPanel, BorderLayout.SOUTH);

            getContentPane().removeAll();
            getContentPane().add(resultPanel);
            revalidate();
            repaint();
        }
    }

    class LRUModule extends JFrame {
        LRUModule() {
            setTitle("LRU Page Replacement");
            setSize(950, 750);
            setLocationRelativeTo(null);
            showInput();
            setVisible(true);
        }

        private void showInput() {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBackground(new Color(245, 245, 250));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel title = new JLabel("LRU Page Replacement", SwingConstants.CENTER);
            title.setFont(new Font("Segoe UI", Font.BOLD, 32));
            title.setForeground(new Color(51, 51, 51));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            panel.add(title, gbc);
            gbc.gridwidth = 1;

            JLabel infoLabel = new JLabel("<html><center>Simulate Least Recently Used algorithm<br>Enter comma-separated page references</center></html>", SwingConstants.CENTER);
            infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            infoLabel.setForeground(new Color(102, 102, 102));
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            panel.add(infoLabel, gbc);
            gbc.gridwidth = 1;

            JTextField framesField = new JTextField(10);
            framesField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            framesField.setText("3");

            JTextField refsField = new JTextField(15);
            refsField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            refsField.setText("7,0,1,2,0,3,0,4,2,3,0,3,2");

            JLabel framesLabel = new JLabel("Number of Frames:");
            framesLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(framesLabel, gbc);
            gbc.gridx = 1;
            panel.add(framesField, gbc);

            JLabel refsLabel = new JLabel("Page References:");
            refsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            gbc.gridx = 0;
            gbc.gridy = 3;
            panel.add(refsLabel, gbc);
            gbc.gridx = 1;
            panel.add(refsField, gbc);

            JButton start = new JButton("Simulate LRU");
            start.setFont(new Font("Segoe UI", Font.BOLD, 16));
            start.setBackground(new Color(60, 179, 113));
            start.setForeground(Color.WHITE);
            start.setPreferredSize(new Dimension(200, 45));
            start.setFocusPainted(false);
            start.setBorderPainted(false);
            start.setCursor(new Cursor(Cursor.HAND_CURSOR));

            gbc.gridx = 0;
            gbc.gridy = 4;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(25, 10, 10, 10);
            panel.add(start, gbc);

            JButton backBtn = new JButton("Back to Menu");
            backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            backBtn.setBackground(new Color(128, 128, 128));
            backBtn.setForeground(Color.WHITE);
            backBtn.setPreferredSize(new Dimension(150, 35));
            backBtn.setFocusPainted(false);
            backBtn.setBorderPainted(false);
            backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            backBtn.addActionListener(e -> dispose());

            gbc.gridy = 5;
            gbc.insets = new Insets(10, 10, 10, 10);
            panel.add(backBtn, gbc);

            getContentPane().removeAll();
            getContentPane().add(panel);
            revalidate();
            repaint();

            start.addActionListener(e -> {
                try {
                    int f = Integer.parseInt(framesField.getText());
                    int[] refs = Arrays.stream(refsField.getText().split(","))
                            .mapToInt(s -> Integer.parseInt(s.trim())).toArray();
                    simulate(f, refs);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid input! Please enter valid numbers.",
                            "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

        private void simulate(int framesCount, int[] refs) {
            LinkedHashSet<Integer> frames = new LinkedHashSet<>();
            StringBuilder out = new StringBuilder();
            int faults = 0;
            int hits = 0;

            out.append("Step-by-Step LRU Simulation:\n");
            out.append("=" .repeat(60)).append("\n\n");

            for (int step = 0; step < refs.length; step++) {
                int r = refs[step];
                boolean isHit = frames.contains(r);

                if (!isHit) {
                    faults++;
                    if (frames.size() == framesCount) {
                        int removed = frames.iterator().next();
                        frames.remove(removed);
                        out.append(String.format("Step %2d: Page %d | PAGE FAULT | Removed: %d\n",
                                step + 1, r, removed));
                    } else {
                        out.append(String.format("Step %2d: Page %d | PAGE FAULT\n", step + 1, r));
                    }
                } else {
                    hits++;
                    frames.remove(r);
                    out.append(String.format("Step %2d: Page %d | HIT\n", step + 1, r));
                }

                frames.add(r);
                out.append("         Frames: ").append(frames).append("\n\n");
            }

            out.append("=" .repeat(60)).append("\n");
            out.append("SUMMARY STATISTICS\n");
            out.append("=" .repeat(60)).append("\n");
            out.append(String.format("Total References: %d\n", refs.length));
            out.append(String.format("Page Faults: %d\n", faults));
            out.append(String.format("Page Hits: %d\n", hits));
            out.append(String.format("Hit Rate: %.2f%%\n", (hits * 100.0 / refs.length)));
            out.append(String.format("Fault Rate: %.2f%%\n", (faults * 100.0 / refs.length)));

            JPanel resultPanel = new JPanel(new BorderLayout(10, 10));
            resultPanel.setBackground(new Color(245, 245, 250));
            resultPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JLabel titleLabel = new JLabel("LRU Simulation Results");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            titleLabel.setForeground(new Color(51, 51, 51));
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            resultPanel.add(titleLabel, BorderLayout.NORTH);

            JTextArea area = new JTextArea(out.toString());
            area.setEditable(false);
            area.setFont(new Font("Consolas", Font.PLAIN, 13));
            area.setBackground(Color.WHITE);
            area.setMargin(new Insets(10, 10, 10, 10));

            JScrollPane scrollPane = new JScrollPane(area);
            scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            resultPanel.add(scrollPane, BorderLayout.CENTER);

            JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
            statsPanel.setBackground(new Color(245, 245, 250));

            JLabel faultsLabel = new JLabel("✗ Faults: " + faults);
            faultsLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            faultsLabel.setForeground(new Color(220, 20, 60));

            JLabel hitsLabel = new JLabel("✓ Hits: " + hits);
            hitsLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            hitsLabel.setForeground(new Color(34, 139, 34));

            JLabel rateLabel = new JLabel(String.format("Hit Rate: %.1f%%", (hits * 100.0 / refs.length)));
            rateLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            rateLabel.setForeground(new Color(60, 179, 113));

            statsPanel.add(faultsLabel);
            statsPanel.add(hitsLabel);
            statsPanel.add(rateLabel);

            JButton backBtn = new JButton("Back to Input");
            backBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            backBtn.setBackground(new Color(128, 128, 128));
            backBtn.setForeground(Color.WHITE);
            backBtn.setPreferredSize(new Dimension(150, 40));
            backBtn.setFocusPainted(false);
            backBtn.setBorderPainted(false);
            backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            backBtn.addActionListener(e -> showInput());

            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.setBackground(new Color(245, 245, 250));
            bottomPanel.add(statsPanel, BorderLayout.NORTH);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            btnPanel.setBackground(new Color(245, 245, 250));
            btnPanel.add(backBtn);
            bottomPanel.add(btnPanel, BorderLayout.CENTER);

            resultPanel.add(bottomPanel, BorderLayout.SOUTH);

            getContentPane().removeAll();
            getContentPane().add(resultPanel);
            revalidate();
            repaint();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MemoryManagement::new);
    }
}
