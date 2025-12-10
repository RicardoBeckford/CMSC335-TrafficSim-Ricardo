/**
 * File: TimeThread.java
 * Author: Ricardo Beckford
 * Date: December 2025
 * Purpose: Thread that updates the simulation time in one-second intervals.
 */
public class TimeThread implements Runnable {

    private boolean running = true;
    private boolean paused = false;

    private final Object lock = new Object();
    private final SimulationGUI gui;

    private long secondsElapsed = 0;

    public TimeThread(SimulationGUI gui) {
        this.gui = gui;
    }

    public void pauseTimer() {
        paused = true;
    }

    public void resumeTimer() {
        paused = false;
        synchronized (lock) {
            lock.notifyAll(); // wake thread
        }
    }

    public void stopTimer() {
        running = false;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    @Override
    public void run() {
        while (running) {

            // true pause behavior
            synchronized (lock) {
                while (paused) {
                    try { lock.wait(); }
                    catch (InterruptedException ignored) {}
                }
            }

            secondsElapsed++;
            String timeText = secondsElapsed + " s";

            javax.swing.SwingUtilities.invokeLater(() ->
                    gui.updateTime(timeText)
            );

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
        }
    }
}
