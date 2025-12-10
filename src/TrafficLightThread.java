/**
 * File: TrafficLightThread.java
 * Author: Ricardo Beckford
 * Date: December 2025
 * Purpose: Thread that cycles a single traffic light through its states.
 */
public class TrafficLightThread implements Runnable {

    private boolean running = true;
    private boolean paused = false;

    private final Object lock = new Object();
    private final TrafficLight light;
    private final TrafficLightPanel panel;

    public TrafficLightThread(TrafficLight light, TrafficLightPanel panel) {
        this.light = light;
        this.panel = panel;
    }

    public void pauseLight() {
        paused = true;
    }

    public void resumeLight() {
        paused = false;
        synchronized (lock) {
            lock.notifyAll(); // wake from pause
        }
    }

    public void stopLight() {
        running = false;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    @Override
    public void run() {
        while (running) {

            // TRUE PAUSE behavior
            synchronized (lock) {
                while (paused) {
                    try { lock.wait(); }
                    catch (InterruptedException ignored) {}
                }
            }

            // Sleep for current light state's duration
            long duration = light.getDuration();
            try {
                Thread.sleep(duration);
            } catch (InterruptedException ignored) {}

            // Move to next light color
            light.next();

            // Refresh GUI light panel
            javax.swing.SwingUtilities.invokeLater(panel::refresh);
        }
    }
}
