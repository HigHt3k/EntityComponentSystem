package de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation;

public enum SimulationType {
    COMPUTER,
    ACTUATOR,
    CABLE,
    SENSOR,
    VOTE,
    PLACEHOLDER;

    public static boolean contains(String test) {

        for (SimulationType c : SimulationType.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }

        return false;
    }
}
