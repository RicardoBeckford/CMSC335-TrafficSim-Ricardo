/**
 * File: TrafficLight.java
 * Author: Ricardo Beckford
 * Date: December 2025
 * Purpose: Model class representing a single traffic light.
 */
public class TrafficLight {

    private LightState state = LightState.RED;
    private final int index;

    // durations in milliseconds
    private final long redDuration = 5000;
    private final long yellowDuration = 2000;
    private final long greenDuration = 5000;

    public TrafficLight(int index) {
        this.index = index;
    }

    public synchronized LightState getState() {
        return state;
    }

    public synchronized void next() {
        switch (state) {
            case RED -> state = LightState.GREEN;
            case GREEN -> state = LightState.YELLOW;
            case YELLOW -> state = LightState.RED;
        }
    }

    public long getDuration() {
        return switch (state) {
            case RED -> redDuration;
            case GREEN -> greenDuration;
            case YELLOW -> yellowDuration;
        };
    }

    public int getIndex() {
        return index;
    }
}
