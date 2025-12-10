/**
 * File: CarThread.java
 * Author: Ricardo Beckford
 * Date: December 2025
 * Purpose: Thread that moves a car along a straight line and reports position to the GUI.
 *
 * Assumptions:
 *  - Simple straight road.
 *  - Y = 0 for all cars.
 *  - No collisions, no physics, instant stops allowed.
 */
import javax.swing.*;

public class CarThread implements Runnable {

    private static final double STEP_SECONDS = 0.2;

    private final Car car;
    private final SimulationGUI gui;
    private final SimulationController controller;

    private boolean running = true;
    private boolean paused = false;

    private final Object lock = new Object();

    public CarThread(Car car, SimulationGUI gui, SimulationController controller) {
        this.car = car;
        this.gui = gui;
        this.controller = controller;
    }

    public void pauseThread() {
        paused = true;
    }

    public void resumeThread() {
        paused = false;
        synchronized (lock) {
            lock.notifyAll();  // WAKE THREAD
        }
    }

    public void stopThread() {
        running = false;
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    @Override
    public void run() {
        while (running) {

            // PAUSE BEHAVIOR: thread truly sleeps
            synchronized (lock) {
                while (paused) {
                    try { lock.wait(); }
                    catch (InterruptedException ignored) {}
                }
            }

            // RED LIGHT STOP BEHAVIOR (temporary stop, not permanent)
            if (controller.shouldStopAtRed(car)) {
                car.stopTemporarily();
            } else {
                car.resumeSpeed();
            }

            car.updatePosition(STEP_SECONDS);

            SwingUtilities.invokeLater(gui::refreshCarTable);

            try {
                Thread.sleep((long) (STEP_SECONDS * 1000));
            } catch (InterruptedException ignored) {}
        }
    }
}
