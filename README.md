# CMSC 335 – Traffic Simulation

Java Swing GUI traffic simulator with multiple threads for time, traffic lights, and cars.

Project: CMSC 335 Traffic Simulation
Author: Ricardo Beckford
Language: Java
Interface: Java Swing
Date: December 2025

Description

This project is a traffic simulation that uses threads to update cars, traffic lights, and a timer at the same time. The program displays the simulation in a Java Swing GUI. Users can start, pause, continue, and stop the simulation. They can also add new cars and new intersections while the program is running.

How to Run

Open the project in IntelliJ or any Java IDE.

Make sure the src folder contains all Java files.

Run Main.java.

The simulation window will appear and is ready for use.

Controls

Start: Begins the simulation.

Pause: Freezes all threads.

Continue: Resumes activity.

Stop: Ends all threads.

Add Car: Adds a car with a user selected speed.

Add Intersection: Adds a new traffic light panel.

Features

Real time clock updated every second

Animated traffic lights with three states

Cars that move and stop at red lights

Ability to add objects during simulation

Thread safe updates to the GUI

Requirements

Java 17 or later

IntelliJ or any IDE that supports Swing

No external libraries needed

File Structure
src/
    Main.java
    SimulationGUI.java
    SimulationController.java
    Car.java
    CarThread.java
    TrafficLight.java
    TrafficLightThread.java
    TrafficLightPanel.java
    TimeThread.java
    LightState.java
documentation/
    ProjectReport.pdf
    UML.png
    screenshots/
Notes

This simulation is for educational purposes. Cars move in a straight line and follow simple logic. Timing is approximate and based on thread sleeping.