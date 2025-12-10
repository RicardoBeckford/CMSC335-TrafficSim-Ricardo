/**
 * File: Car.java
 * Author: Ricardo Beckford
 * Date: December 2025
 * Purpose: Model class representing a car in the simulation.
 *
 * Note: We assume a straight line and Y = 0 as required.
 */
public class Car {

    private final int id;
    private final double originalSpeed;
    private double speedMetersPerSecond;
    private double xPosition;
    private final double yPosition = 0.0;

    public Car(int id, double speedMetersPerSecond) {
        this.id = id;
        this.speedMetersPerSecond = speedMetersPerSecond;
        this.originalSpeed = speedMetersPerSecond;
        this.xPosition = 0.0;
    }

    public int getId() {
        return id;
    }

    public synchronized double getSpeedMetersPerSecond() {
        return speedMetersPerSecond;
    }

    public synchronized void setSpeedMetersPerSecond(double speedMetersPerSecond) {
        this.speedMetersPerSecond = speedMetersPerSecond;
    }

    public synchronized double getXPosition() {
        return xPosition;
    }

    public double getYPosition() {
        return yPosition;
    }

    public synchronized void updatePosition(double deltaSeconds) {
        xPosition += speedMetersPerSecond * deltaSeconds;
    }

    public synchronized void stopTemporarily() {
        speedMetersPerSecond = 0.0;
    }

    public synchronized void resumeSpeed() {
        speedMetersPerSecond = originalSpeed;
    }
}

