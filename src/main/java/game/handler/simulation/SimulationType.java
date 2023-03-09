package game.handler.simulation;

public enum SimulationType {
    CPU,
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
