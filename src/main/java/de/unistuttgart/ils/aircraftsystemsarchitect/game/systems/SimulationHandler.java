package de.unistuttgart.ils.aircraftsystemsarchitect.game.systems;

/**
 * static class that handles all simulation components in each time step
 */
public class SimulationHandler {


    public static void resetHandler() {
        // TODO: Reset the handler, method to be used whenever a new game scene is loaded up
    }

    public static void update() {
        //TODO: update the current system failure ratio. maybe return the value so it can be properly rendered to the component(s)
        collectSystem();
    }

    private static void collectSystem() {

    }

    public static void markov() {
        // TODO: implement a markov analyser

    }

    public static void detectSystemState() {
        // TODO: Detect the current system state: valid, duplex, triplex, quadruplex? maybe implement groups of components to make this easier.
        // For groups: enable a "debug" option to render a border around all parts of a group.
    }
}
