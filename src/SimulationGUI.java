/**
 * File: SimulationGUI.java
 * Author: Ricardo Beckford
 * Date: December 2025
 * Purpose: Main Swing GUI for the traffic simulation.
 */
/**
 * SimulationGUI
 * Controls all visual components of the traffic simulation.
 * Works with the updated SimulationController, CarThread, and TrafficLightThread.
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
public class SimulationGUI extends JFrame {

    private final JLabel timeLabel = new JLabel("0 s");
    private final JPanel lightsContainer = new JPanel();
    private final DefaultTableModel carTableModel;
    private final JTable carTable;
    private final JTextArea logArea = new JTextArea(5, 40);

    private final SimulationController controller;

    public SimulationGUI() {
        super("CMSC 335 - Traffic Simulation");

        controller = new SimulationController(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --------------------------------------------
        // NORTH – CONTROL BUTTONS
        // --------------------------------------------
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton startButton = new JButton("Start");
        JButton pauseButton = new JButton("Pause");
        JButton continueButton = new JButton("Continue");
        JButton stopButton = new JButton("Stop");
        JButton addCarButton = new JButton("Add Car");
        JButton addLightButton = new JButton("Add Intersection");

        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(continueButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(addCarButton);
        buttonPanel.add(addLightButton);

        add(buttonPanel, BorderLayout.NORTH);

        // --------------------------------------------
        // WEST – TIME LABEL
        // --------------------------------------------
        JPanel timePanel = new JPanel();
        timePanel.setLayout(new BoxLayout(timePanel, BoxLayout.Y_AXIS));
        timePanel.setBorder(BorderFactory.createTitledBorder("Simulation Time"));
        timeLabel.setFont(timeLabel.getFont().deriveFont(Font.BOLD, 18f));
        timePanel.add(timeLabel);

        add(timePanel, BorderLayout.WEST);

        // --------------------------------------------
        // CENTER – TRAFFIC LIGHT PANELS
        // --------------------------------------------
        lightsContainer.setLayout(new FlowLayout(FlowLayout.CENTER));
        lightsContainer.setBorder(BorderFactory.createTitledBorder("Traffic Lights"));
        add(lightsContainer, BorderLayout.CENTER);

        // --------------------------------------------
        // EAST – CAR TABLE
        // --------------------------------------------
        carTableModel = new DefaultTableModel(
                new Object[]{"Car ID", "X Position (m)", "Y Position (m)", "Speed (m/s)"}, 0
        );
        carTable = new JTable(carTableModel);

        JScrollPane carScroll = new JScrollPane(carTable);
        carScroll.setBorder(BorderFactory.createTitledBorder("Cars"));
        add(carScroll, BorderLayout.EAST);

        // --------------------------------------------
        // SOUTH – LOG AREA
        // --------------------------------------------
        logArea.setEditable(false);
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Log"));
        add(logScroll, BorderLayout.SOUTH);

        // --------------------------------------------
        // BUTTON ACTIONS
        // --------------------------------------------
        wireButtons(startButton, pauseButton, continueButton, stopButton, addCarButton, addLightButton);

        // --------------------------------------------
        // INITIAL SETUP – 3 DEFAULT INTERSECTIONS
        // --------------------------------------------
        controller.initializeDefaultIntersections();
        pack();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void wireButtons(JButton startButton, JButton pauseButton, JButton continueButton,
                             JButton stopButton, JButton addCarButton, JButton addLightButton) {

        startButton.addActionListener(e -> {
            log("Starting simulation");
            controller.startSimulation();
        });

        pauseButton.addActionListener(e -> {
            log("Pausing simulation");
            controller.pauseSimulation();
        });

        continueButton.addActionListener(e -> {
            log("Continuing simulation");
            controller.continueSimulation();
        });

        stopButton.addActionListener(e -> {
            log("Stopping simulation");
            controller.stopSimulation();
        });

        addCarButton.addActionListener(e -> {
            String speedStr = JOptionPane.showInputDialog(
                    SimulationGUI.this,
                    "Enter car speed in m/s (e.g. 10.0):",
                    "Add Car",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (speedStr != null && !speedStr.trim().isEmpty()) {
                try {
                    double speed = Double.parseDouble(speedStr.trim());
                    controller.addCar(speed);
                    log("Added car with speed " + speed + " m/s");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            SimulationGUI.this,
                            "Invalid speed value.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        addLightButton.addActionListener(e -> {
            controller.addIntersection();
            log("Added new intersection");
            pack();
        });
    }

    // --------------------------------------------
    // ADD LIGHT PANEL TO UI
    // --------------------------------------------

    public TrafficLightPanel createAndAddTrafficLightPanel(TrafficLight light) {
        TrafficLightPanel panel = new TrafficLightPanel(light);
        lightsContainer.add(panel);
        lightsContainer.revalidate();
        lightsContainer.repaint();
        return panel;
    }

    // --------------------------------------------
    // TIME UPDATE FROM TimeThread
    // --------------------------------------------

    public void updateTime(String timeText) {
        timeLabel.setText(timeText);
    }

    // --------------------------------------------
    // REFRESH CAR TABLE
    // --------------------------------------------

    public void refreshCarTable() {
        carTableModel.setRowCount(0);
        for (Car car : controller.getCars()) {
            carTableModel.addRow(new Object[]{
                    car.getId(),
                    String.format("%.2f", car.getXPosition()),
                    String.format("%.2f", car.getYPosition()),
                    String.format("%.2f", car.getSpeedMetersPerSecond())
            });
        }
    }

    // --------------------------------------------
    // LOGGING
    // --------------------------------------------

    public void log(String message) {
        logArea.append(message + "\n");
    }
}
