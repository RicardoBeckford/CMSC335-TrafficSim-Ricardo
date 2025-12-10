/**
 * File: SimulationController.java
 * Author: Ricardo Beckford
 * Date: December 2025
 * Purpose: Manages cars, traffic lights, and their threads for the simulation.
 */

/**
 * SimulationController
 * Handles all simulation logic:
 * - managing cars
 * - managing lights
 * - threading and timing
 * - determining when cars must stop at red lights
 */

import java.util.ArrayList;
import java.util.List;

public class SimulationController {

    public static final double INTERSECTION_DISTANCE_METERS = 1000.0;

    private final SimulationGUI gui;

    private TimeThread timeThreadLogic;
    private Thread timeWorker;

    private final List<TrafficLight> lights = new ArrayList<>();
    private final List<TrafficLightPanel> lightPanels = new ArrayList<>();
    private final List<TrafficLightThread> lightThreads = new ArrayList<>();
    private final List<Thread> lightWorkers = new ArrayList<>();

    private final List<Car> cars = new ArrayList<>();
    private final List<CarThread> carThreads = new ArrayList<>();
    private final List<Thread> carWorkers = new ArrayList<>();

    private int nextCarId = 1;

    public SimulationController(SimulationGUI gui) {
        this.gui = gui;
    }

    public List<Car> getCars() {
        return cars;
    }

    public List<TrafficLight> getLights() {
        return lights;
    }

    // ---------------------------------------------
    // LIGHT MANAGEMENT
    // ---------------------------------------------

    public void initializeDefaultIntersections() {
        for (int i = 0; i < 3; i++) {
            addIntersection();
        }
    }

    public TrafficLight addIntersection() {
        int index = lights.size();
        TrafficLight light = new TrafficLight(index);
        lights.add(light);

        TrafficLightPanel panel = gui.createAndAddTrafficLightPanel(light);
        lightPanels.add(panel);

        TrafficLightThread logic = new TrafficLightThread(light, panel);
        lightThreads.add(logic);

        Thread worker = new Thread(logic, "TrafficLight-" + index);
        lightWorkers.add(worker);

        worker.start(); // ALWAYS start immediately

        return light;
    }

    // ---------------------------------------------
    // CAR MANAGEMENT
    // ---------------------------------------------

    public Car addCar(double speedMetersPerSecond) {
        Car car = new Car(nextCarId++, speedMetersPerSecond);
        cars.add(car);

        CarThread logic = new CarThread(car, gui, this);
        carThreads.add(logic);

        Thread worker = new Thread(logic, "Car-" + car.getId());
        carWorkers.add(worker);

        worker.start(); // ALWAYS start immediately

        gui.refreshCarTable();
        return car;
    }

    // ---------------------------------------------
    // SIMULATION START/PAUSE/CONTINUE/STOP
    // ---------------------------------------------

    public void startSimulation() {
        if (timeThreadLogic == null) {
            timeThreadLogic = new TimeThread(gui);
            timeWorker = new Thread(timeThreadLogic, "Timer");
            timeWorker.start();
        }

        // resume all threads
        lightThreads.forEach(TrafficLightThread::resumeLight);
        carThreads.forEach(CarThread::resumeThread);
    }

    public void pauseSimulation() {
        if (timeThreadLogic != null) timeThreadLogic.pauseTimer();

        lightThreads.forEach(TrafficLightThread::pauseLight);
        carThreads.forEach(CarThread::pauseThread);
    }

    public void continueSimulation() {
        if (timeThreadLogic != null) timeThreadLogic.resumeTimer();

        lightThreads.forEach(TrafficLightThread::resumeLight);
        carThreads.forEach(CarThread::resumeThread);
    }

    public void stopSimulation() {
        if (timeThreadLogic != null) timeThreadLogic.stopTimer();

        lightThreads.forEach(TrafficLightThread::stopLight);
        carThreads.forEach(CarThread::stopThread);
    }

    // ---------------------------------------------
    // RED LIGHT STOP LOGIC
    // ---------------------------------------------

    public boolean shouldStopAtRed(Car car) {
        double xPos = car.getXPosition();

        for (TrafficLight light : lights) {
            double intersectionX = light.getIndex() * INTERSECTION_DISTANCE_METERS;

            if (Math.abs(xPos - intersectionX) < 5 &&
                    light.getState() == LightState.RED) {

                return true;
            }
        }
        return false;
    }
}
