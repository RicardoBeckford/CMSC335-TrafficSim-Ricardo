/**
 * File: Main.java
 * Author: Ricardo Beckford
 * Date: December 2025
 * Purpose: Entry point for the traffic simulation application.
 */
public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SimulationGUI();
            }
        });
    }
}
