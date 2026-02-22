package OSProject.com;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ProcessManagement extends JFrame {

    // Components for process creation
    private JTextField nameField;
    private JTextField arrivalField;
    private JTextField burstField;

    // Tables and Models
    private JTable readyTable;
    private DefaultTableModel readyTableModel;
    private JTable runningTable;
    private DefaultTableModel runningTableModel;
    private JTable blockedTable;
    private DefaultTableModel blockedTableModel;
    private JTable suspendTable;
    private DefaultTableModel suspendTableModel;

    // Data lists for Ready Queue
    private ArrayList<String> processIDs;
    private ArrayList<String> processNames;
    private ArrayList<Integer> arrivalTimes;
    private ArrayList<Integer> burstTimes;

    // Data lists for Running Queue (only one process at a time)
    private String runningProcessID;
    private String runningProcessName;
    private Integer runningArrivalTime;
    private Integer runningBurstTime;

    // Data lists for Blocked Queue
    private ArrayList<String> blockedProcessIDs;
    private ArrayList<String> blockedProcessNames;
    private ArrayList<Integer> blockedArrivalTimes;
    private ArrayList<Integer> blockedBurstTimes;

    // Data lists for Suspend Queue
    private ArrayList<String> suspendProcessIDs;
    private ArrayList<String> suspendProcessNames;
    private ArrayList<Integer> suspendArrivalTimes;
    private ArrayList<Integer> suspendBurstTimes;

    public ProcessManagement() {
        // Initialize data lists for Ready Queue
        processIDs = new ArrayList<>();
        processNames = new ArrayList<>();
        arrivalTimes = new ArrayList<>();
        burstTimes = new ArrayList<>();

        // Initialize Running Queue (null = no process running)
        runningProcessID = null;
        runningProcessName = null;
        runningArrivalTime = null;
        runningBurstTime = null;

        // Initialize data lists for Blocked Queue
        blockedProcessIDs = new ArrayList<>();
        blockedProcessNames = new ArrayList<>();
        blockedArrivalTimes = new ArrayList<>();
        blockedBurstTimes = new ArrayList<>();

        // Initialize data lists for Suspend Queue
        suspendProcessIDs = new ArrayList<>();
        suspendProcessNames = new ArrayList<>();
        suspendArrivalTimes = new ArrayList<>();
        suspendBurstTimes = new ArrayList<>();

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Process Management System");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Panel
        JPanel titlePanel = createTitlePanel();
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Center Panel with input and buttons
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(245, 245, 250));

        JPanel inputPanel = createInputPanel();
        JPanel buttonPanel = createButtonPanel();

        centerPanel.add(inputPanel, BorderLayout.NORTH);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.NORTH);

        // Tables Panel
        JPanel tablesPanel = createTablesPanel();
        mainPanel.add(tablesPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 250));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Process Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 36));
        title.setForeground(new Color(51, 51, 51));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Manage process states and scheduling algorithms");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(102, 102, 102));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createVerticalStrut(5));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(15));

        return panel;
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel sectionTitle = new JLabel("Create New Process");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(72, 145, 220));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        panel.add(sectionTitle, gbc);
        gbc.gridwidth = 1;

        // Process Name
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel nameLabel = new JLabel("Process Name:");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nameField = createStyledTextField();
        panel.add(nameField, gbc);

        // Arrival Time
        gbc.gridx = 2;
        JLabel arrivalLabel = new JLabel("Arrival Time:");
        arrivalLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(arrivalLabel, gbc);

        gbc.gridx = 3;
        arrivalField = createStyledTextField();
        arrivalField.addActionListener(e -> burstField.requestFocus());
        panel.add(arrivalField, gbc);

        // Burst Time (next row)
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel burstLabel = new JLabel("Burst Time:");
        burstLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(burstLabel, gbc);

        gbc.gridx = 1;
        burstField = createStyledTextField();
        panel.add(burstField, gbc);

        return panel;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(15);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return field;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        panel.setLayout(new GridLayout(3, 4, 12, 12));

        // Row 1: Primary Actions
        panel.add(createStyledButton("Create Process", new Color(60, 179, 113), e -> createProcess()));
        panel.add(createStyledButton("Dispatch Process", new Color(72, 145, 220), e -> dispatchProcess()));
        panel.add(createStyledButton("Block Process", new Color(255, 140, 0), e -> blockProcess()));
        panel.add(createStyledButton("Destroy Process", new Color(220, 20, 60), e -> destroyProcess()));

        // Row 2: State Management
        panel.add(createStyledButton("Resume Process", new Color(60, 179, 113), e -> resumeProcess()));
        panel.add(createStyledButton("Suspend Process", new Color(255, 165, 0), e -> suspendProcess()));
        panel.add(createStyledButton("Wakeup Process", new Color(72, 145, 220), e -> wakeupProcess()));
        panel.add(createStyledButton("Terminate Running", new Color(178, 34, 34), e -> terminateRunning()));

        // Row 3: Utilities
        panel.add(createStyledButton("Auto Create 5", new Color(138, 43, 226), e -> autoCreate()));
        panel.add(createStyledButton("Apply Scheduling", new Color(34, 139, 34), e -> applyScheduling()));
        panel.add(createStyledButton("Clear All", new Color(128, 128, 128), e -> clearAll()));
        panel.add(createStyledButton("Refresh View", new Color(70, 130, 180), e -> ShowData()));

        return panel;
    }

    private JButton createStyledButton(String text, Color color, ActionListener listener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(listener);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(color);
            }
        });

        return button;
    }

    private JPanel createTablesPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 250));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Ready Queue (top left)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        panel.add(createTablePanel("Ready Queue", createReadyTable(), new Color(60, 179, 113)), gbc);

        // Running Queue (top right)
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(createTablePanel("Running Queue", createRunningTable(), new Color(72, 145, 220)), gbc);

        // Blocked Queue (bottom left)
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(createTablePanel("Blocked Queue", createBlockedTable(), new Color(255, 140, 0)), gbc);

        // Suspend Queue (bottom right)
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(createTablePanel("Suspend Queue", createSuspendTable(), new Color(138, 43, 226)), gbc);

        return panel;
    }

    private JPanel createTablePanel(String title, JTable table, Color accentColor) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(accentColor);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(220, 220, 220), 1));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JTable createReadyTable() {
        readyTableModel = new DefaultTableModel(
                new String[]{"Process ID", "Name", "Arrival", "Burst", "Status"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        readyTable = styleTable(new JTable(readyTableModel), new Color(60, 179, 113));
        return readyTable;
    }

    private JTable createRunningTable() {
        runningTableModel = new DefaultTableModel(
                new String[]{"Process ID", "Name", "Arrival", "Burst", "Status"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        runningTable = styleTable(new JTable(runningTableModel), new Color(72, 145, 220));
        return runningTable;
    }

    private JTable createBlockedTable() {
        blockedTableModel = new DefaultTableModel(
                new String[]{"Process ID", "Name", "Arrival", "Burst", "Status"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        blockedTable = styleTable(new JTable(blockedTableModel), new Color(255, 140, 0));
        return blockedTable;
    }

    private JTable createSuspendTable() {
        suspendTableModel = new DefaultTableModel(
                new String[]{"Process ID", "Name", "Arrival", "Burst", "Status"}, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        suspendTable = styleTable(new JTable(suspendTableModel), new Color(138, 43, 226));
        return suspendTable;
    }

    private JTable styleTable(JTable table, Color headerColor) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(28);
        table.setSelectionBackground(new Color(220, 235, 255));
        table.setGridColor(new Color(230, 230, 230));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(headerColor);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        return table;
    }

    // Button Action Methods
    private void createProcess() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            showError("Process Name cannot be empty.");
            return;
        }

        String id = generateRandomProcessID();

        int arrivalTime, burstTime;
        try {
            arrivalTime = Integer.parseInt(arrivalField.getText());
            burstTime = Integer.parseInt(burstField.getText());

            if (arrivalTime < 0 || burstTime <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            showError("Please enter valid positive numbers for times.");
            return;
        }

        processIDs.add(id);
        processNames.add(name);
        arrivalTimes.add(arrivalTime);
        burstTimes.add(burstTime);

        ShowData();
        showSuccess("Process " + id + " created successfully!");

        nameField.setText("");
        arrivalField.setText("");
        burstField.setText("");
        nameField.requestFocus();
    }

    private void autoCreate() {
        for (int i = 0; i < 5; i++) {
            String id = generateRandomProcessID();
            processIDs.add(id);
            processNames.add("Process" + (int)(Math.random() * 100));
            arrivalTimes.add((int)(Math.random() * 10));
            burstTimes.add((int)(Math.random() * 10) + 1);
        }
        ShowData();
        showSuccess("5 processes created automatically!");
    }

    private void dispatchProcess() {
        if (runningProcessID != null) {
            showError("A process (" + runningProcessID + ") is already running.");
            return;
        }

        if (processIDs.isEmpty()) {
            showError("Ready queue is empty.");
            return;
        }

        String pID = JOptionPane.showInputDialog(this, "Enter Process ID to dispatch (or leave empty for first):");

        if (pID == null) return;

        pID = pID.trim();
        int dispatchIndex;

        if (pID.isEmpty()) {
            dispatchIndex = 0;
        } else {
            dispatchIndex = processIDs.indexOf(pID);
            if (dispatchIndex == -1) {
                showError("Process ID " + pID + " not found in Ready queue.");
                return;
            }
        }

        runningProcessID = processIDs.get(dispatchIndex);
        runningProcessName = processNames.get(dispatchIndex);
        runningArrivalTime = arrivalTimes.get(dispatchIndex);
        runningBurstTime = burstTimes.get(dispatchIndex);

        processIDs.remove(dispatchIndex);
        processNames.remove(dispatchIndex);
        arrivalTimes.remove(dispatchIndex);
        burstTimes.remove(dispatchIndex);

        ShowData();
        showSuccess("Process " + runningProcessID + " dispatched and is now running.");
    }

    private void blockProcess() {
        if (runningProcessID == null) {
            showError("No process is currently running.");
            return;
        }

        blockedProcessIDs.add(runningProcessID);
        blockedProcessNames.add(runningProcessName);
        blockedArrivalTimes.add(runningArrivalTime);
        blockedBurstTimes.add(runningBurstTime);

        String blockedID = runningProcessID;

        runningProcessID = null;
        runningProcessName = null;
        runningArrivalTime = null;
        runningBurstTime = null;

        ShowData();
        showSuccess("Process " + blockedID + " moved to Blocked queue.");
    }

    private void destroyProcess() {
        String pID = JOptionPane.showInputDialog(this, "Enter Process ID to destroy:");
        if (pID == null || pID.trim().isEmpty()) return;
        pID = pID.trim();

        int index = processIDs.indexOf(pID);
        if (index != -1) {
            processIDs.remove(index);
            processNames.remove(index);
            arrivalTimes.remove(index);
            burstTimes.remove(index);
            ShowData();
            showSuccess("Process " + pID + " destroyed from Ready queue.");
            return;
        }

        if (runningProcessID != null && runningProcessID.equals(pID)) {
            runningProcessID = null;
            runningProcessName = null;
            runningArrivalTime = null;
            runningBurstTime = null;
            ShowData();
            showSuccess("Process " + pID + " destroyed from Running state.");
            return;
        }

        index = blockedProcessIDs.indexOf(pID);
        if (index != -1) {
            blockedProcessIDs.remove(index);
            blockedProcessNames.remove(index);
            blockedArrivalTimes.remove(index);
            blockedBurstTimes.remove(index);
            ShowData();
            showSuccess("Process " + pID + " destroyed from Blocked queue.");
            return;
        }

        index = suspendProcessIDs.indexOf(pID);
        if (index != -1) {
            suspendProcessIDs.remove(index);
            suspendProcessNames.remove(index);
            suspendArrivalTimes.remove(index);
            suspendBurstTimes.remove(index);
            ShowData();
            showSuccess("Process " + pID + " destroyed from Suspend queue.");
            return;
        }

        showError("Process ID " + pID + " not found.");
    }

    private void resumeProcess() {
        String pID = JOptionPane.showInputDialog(this, "Enter Process ID to resume:");
        if (pID == null || pID.trim().isEmpty()) return;
        pID = pID.trim();

        int index = blockedProcessIDs.indexOf(pID);
        if (index != -1) {
            processIDs.add(blockedProcessIDs.get(index));
            processNames.add(blockedProcessNames.get(index));
            arrivalTimes.add(blockedArrivalTimes.get(index));
            burstTimes.add(blockedBurstTimes.get(index));

            blockedProcessIDs.remove(index);
            blockedProcessNames.remove(index);
            blockedArrivalTimes.remove(index);
            blockedBurstTimes.remove(index);

            ShowData();
            showSuccess("Process " + pID + " moved to Ready queue.");
        } else {
            showError("Process ID " + pID + " not found in Blocked queue.");
        }
    }

    private void suspendProcess() {
        String pID = JOptionPane.showInputDialog(this, "Enter Process ID to suspend:");
        if (pID == null || pID.trim().isEmpty()) return;
        pID = pID.trim();

        int index = processIDs.indexOf(pID);
        if (index != -1) {
            suspendProcessIDs.add(processIDs.get(index));
            suspendProcessNames.add(processNames.get(index));
            suspendArrivalTimes.add(arrivalTimes.get(index));
            suspendBurstTimes.add(burstTimes.get(index));

            processIDs.remove(index);
            processNames.remove(index);
            arrivalTimes.remove(index);
            burstTimes.remove(index);

            ShowData();
            showSuccess("Process " + pID + " moved to Suspend queue.");
        } else {
            showError("Process ID " + pID + " not found in Ready queue.");
        }
    }

    private void wakeupProcess() {
        String pID = JOptionPane.showInputDialog(this, "Enter Process ID to wakeup:");
        if (pID == null || pID.trim().isEmpty()) return;
        pID = pID.trim();

        int index = suspendProcessIDs.indexOf(pID);
        if (index != -1) {
            processIDs.add(suspendProcessIDs.get(index));
            processNames.add(suspendProcessNames.get(index));
            arrivalTimes.add(suspendArrivalTimes.get(index));
            burstTimes.add(suspendBurstTimes.get(index));

            suspendProcessIDs.remove(index);
            suspendProcessNames.remove(index);
            suspendArrivalTimes.remove(index);
            suspendBurstTimes.remove(index);

            ShowData();
            showSuccess("Process " + pID + " moved to Ready queue.");
        } else {
            showError("Process ID " + pID + " not found in Suspend queue.");
        }
    }

    private void terminateRunning() {
        if (runningProcessID == null) {
            showError("No process is currently running.");
            return;
        }

        String terminatedID = runningProcessID;

        runningProcessID = null;
        runningProcessName = null;
        runningArrivalTime = null;
        runningBurstTime = null;

        ShowData();
        showSuccess("Process " + terminatedID + " has been terminated.");
    }

    private void clearAll() {
        int result = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to clear all processes?",
                "Confirm Clear", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            processIDs.clear();
            processNames.clear();
            arrivalTimes.clear();
            burstTimes.clear();
            runningProcessID = null;
            runningProcessName = null;
            runningArrivalTime = null;
            runningBurstTime = null;
            blockedProcessIDs.clear();
            blockedProcessNames.clear();
            blockedArrivalTimes.clear();
            blockedBurstTimes.clear();
            suspendProcessIDs.clear();
            suspendProcessNames.clear();
            suspendArrivalTimes.clear();
            suspendBurstTimes.clear();
            ShowData();
            showSuccess("All processes cleared.");
        }
    }

    private void applyScheduling() {
        if (processIDs.isEmpty()) {
            showError("No processes to schedule.");
            return;
        }

        String[] options = {"FCFS", "SJF (Non-Preemptive)", "Round Robin"};
        String choice = (String) JOptionPane.showInputDialog(
                this,
                "Select Scheduling Algorithm:",
                "Scheduling",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice != null) {
            switch (choice) {
                case "FCFS":
                    applyFCFS();
                    break;
                case "SJF (Non-Preemptive)":
                    applySJF();
                    break;
                case "Round Robin":
                    String qStr = JOptionPane.showInputDialog(this, "Enter Time Quantum:");
                    if (qStr != null) {
                        try {
                            int quantum = Integer.parseInt(qStr);
                            applyRoundRobin(quantum);
                        } catch (NumberFormatException ex) {
                            showError("Invalid quantum!");
                        }
                    }
                    break;
            }
        }
    }

    private String generateRandomProcessID() {
        String id;
        do {
            int randomNum = (int)(Math.random() * 9000 + 1000);
            id = String.valueOf(randomNum);
        } while (processIDs.contains(id) || blockedProcessIDs.contains(id) ||
                suspendProcessIDs.contains(id) || (runningProcessID != null && runningProcessID.equals(id)));
        return id;
    }

    private void applyFCFS() {
        int n = processIDs.size();
        int[] wt = new int[n];
        int[] tat = new int[n];
        int[] bt = new int[n];
        int[] at = new int[n];
        String[] ids = new String[n];
        String[] names = new String[n];

        for (int i = 0; i < n; i++) {
            bt[i] = burstTimes.get(i);
            at[i] = arrivalTimes.get(i);
            ids[i] = processIDs.get(i);
            names[i] = processNames.get(i);
        }

        for (int i = 0; i < n-1; i++) {
            for (int j = i+1; j < n; j++) {
                if (at[i] > at[j]) {
                    int temp = at[i]; at[i] = at[j]; at[j] = temp;
                    temp = bt[i]; bt[i] = bt[j]; bt[j] = temp;
                    String tempID = ids[i]; ids[i] = ids[j]; ids[j] = tempID;
                    String tempName = names[i]; names[i] = names[j]; names[j] = tempName;
                }
            }
        }

        int serviceTime = at[0];
        for (int i = 0; i < n; i++) {
            if (serviceTime < at[i]) {
                serviceTime = at[i];
            }
            wt[i] = serviceTime - at[i];
            serviceTime += bt[i];
            tat[i] = wt[i] + bt[i];
        }

        showSchedulingResult(ids, names, at, bt, wt, tat, "FCFS Scheduling");
    }

    private void applySJF() {
        int n = processIDs.size();
        int[] wt = new int[n];
        int[] tat = new int[n];
        int[] bt = new int[n];
        int[] at = new int[n];
        String[] ids = new String[n];
        String[] names = new String[n];

        for (int i = 0; i < n; i++) {
            bt[i] = burstTimes.get(i);
            at[i] = arrivalTimes.get(i);
            ids[i] = processIDs.get(i);
            names[i] = processNames.get(i);
        }

        boolean[] completed = new boolean[n];
        int completedCount = 0;
        int currentTime = 0;

        while (completedCount < n) {
            int idx = -1;
            int minBT = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (!completed[i] && at[i] <= currentTime && bt[i] < minBT) {
                    minBT = bt[i];
                    idx = i;
                }
            }

            if (idx == -1) {
                currentTime++;
                continue;
            }

            wt[idx] = currentTime - at[idx];
            currentTime += bt[idx];
            tat[idx] = wt[idx] + bt[idx];
            completed[idx] = true;
            completedCount++;
        }

        showSchedulingResult(ids, names, at, bt, wt, tat, "SJF Non-Preemptive Scheduling");
    }

    private void applyRoundRobin(int quantum) {
        int n = processIDs.size();
        int[] bt = new int[n];
        int[] at = new int[n];
        String[] ids = new String[n];
        String[] names = new String[n];

        for (int i = 0; i < n; i++) {
            bt[i] = burstTimes.get(i);
            at[i] = arrivalTimes.get(i);
            ids[i] = processIDs.get(i);
            names[i] = processNames.get(i);
        }

        int[] remBT = bt.clone();
        int[] wt = new int[n];
        int[] tat = new int[n];
        int currentTime = 0;
        boolean done;

        do {
            done = true;
            for (int i = 0; i < n; i++) {
                if (remBT[i] > 0) {
                    done = false;
                    if (remBT[i] > quantum) {
                        currentTime += quantum;
                        remBT[i] -= quantum;
                    } else {
                        currentTime += remBT[i];
                        wt[i] = currentTime - bt[i] - at[i];
                        remBT[i] = 0;
                    }
                }
            }
        } while (!done);

        for (int i = 0; i < n; i++) {
            tat[i] = bt[i] + wt[i];
        }

        showSchedulingResult(ids, names, at, bt, wt, tat, "Round Robin (Q=" + quantum + ")");
    }

    private void showSchedulingResult(String[] ids, String[] names, int[] at, int[] bt, int[] wt, int[] tat, String title) {
        JDialog resultDialog = new JDialog(this, title, true);
        resultDialog.setSize(900, 650);
        resultDialog.setLocationRelativeTo(this);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(245, 245, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columns = {"Process ID", "Name", "Arrival", "Burst", "Waiting", "Turnaround"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };

        double totalWT = 0;
        double totalTAT = 0;

        for (int i = 0; i < ids.length; i++) {
            model.addRow(new Object[]{ids[i], names[i], at[i], bt[i], wt[i], tat[i]});
            totalWT += wt[i];
            totalTAT += tat[i];
        }

        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(34, 139, 34));
        table.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Stats and Gantt Panel
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(new Color(245, 245, 250));

        // Stats Panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        JLabel avgWTLabel = new JLabel("Avg Waiting Time: " + String.format("%.2f", totalWT / ids.length));
        avgWTLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        avgWTLabel.setForeground(new Color(220, 20, 60));

        JLabel avgTATLabel = new JLabel("Avg Turnaround Time: " + String.format("%.2f", totalTAT / ids.length));
        avgTATLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        avgTATLabel.setForeground(new Color(34, 139, 34));

        statsPanel.add(avgWTLabel);
        statsPanel.add(avgTATLabel);

        // Gantt Chart Panel
        JPanel ganttPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int x = 30;
                int y = 25;
                int height = 50;
                Color[] colors = {
                        new Color(100, 181, 246),
                        new Color(255, 183, 77),
                        new Color(129, 199, 132),
                        new Color(240, 98, 146),
                        new Color(186, 104, 200),
                        new Color(255, 138, 101),
                        new Color(77, 208, 225)
                };

                for (int i = 0; i < ids.length; i++) {
                    g2d.setColor(colors[i % colors.length]);
                    int width = bt[i] * 25;
                    g2d.fillRoundRect(x, y, width, height, 8, 8);

                    g2d.setColor(Color.BLACK);
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = ids[i];
                    int textX = x + (width - fm.stringWidth(text)) / 2;
                    int textY = y + ((height - fm.getHeight()) / 2) + fm.getAscent();
                    g2d.drawString(text, textX, textY);

                    x += width + 5;
                }
            }
        };
        ganttPanel.setPreferredSize(new Dimension(800, 100));
        ganttPanel.setBackground(Color.WHITE);
        ganttPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        new LineBorder(new Color(220, 220, 220), 1),
                        "Gantt Chart",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14),
                        new Color(72, 145, 220)
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        bottomPanel.add(statsPanel, BorderLayout.NORTH);
        bottomPanel.add(ganttPanel, BorderLayout.CENTER);

        // Close Button
        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeBtn.setBackground(new Color(128, 128, 128));
        closeBtn.setForeground(Color.WHITE);
        closeBtn.setPreferredSize(new Dimension(120, 40));
        closeBtn.setFocusPainted(false);
        closeBtn.setBorderPainted(false);
        closeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeBtn.addActionListener(e -> resultDialog.dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setBackground(new Color(245, 245, 250));
        btnPanel.add(closeBtn);

        bottomPanel.add(btnPanel, BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        resultDialog.add(mainPanel);
        resultDialog.setVisible(true);
    }

    private void ShowData() {
        // Update Ready Queue Table
        readyTableModel.setRowCount(0);
        for (int i = 0; i < processIDs.size(); i++) {
            readyTableModel.addRow(new Object[]{
                    processIDs.get(i),
                    processNames.get(i),
                    arrivalTimes.get(i),
                    burstTimes.get(i),
                    "Ready"
            });
        }

        // Update Running Queue Table
        runningTableModel.setRowCount(0);
        if (runningProcessID != null) {
            runningTableModel.addRow(new Object[]{
                    runningProcessID,
                    runningProcessName,
                    runningArrivalTime,
                    runningBurstTime,
                    "Running"
            });
        }

        // Update Blocked Queue Table
        blockedTableModel.setRowCount(0);
        for (int i = 0; i < blockedProcessIDs.size(); i++) {
            blockedTableModel.addRow(new Object[]{
                    blockedProcessIDs.get(i),
                    blockedProcessNames.get(i),
                    blockedArrivalTimes.get(i),
                    blockedBurstTimes.get(i),
                    "Blocked"
            });
        }

        // Update Suspend Queue Table
        suspendTableModel.setRowCount(0);
        for (int i = 0; i < suspendProcessIDs.size(); i++) {
            suspendTableModel.addRow(new Object[]{
                    suspendProcessIDs.get(i),
                    suspendProcessNames.get(i),
                    suspendArrivalTimes.get(i),
                    suspendBurstTimes.get(i),
                    "Suspended"
            });
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProcessManagement::new);
    }
}
