package de.unistuttgart.ils.aircraftsystemsarchitect.game.components;

/**
 * An enum representing the types of cable ports, either IN or OUT.
 */
public enum CablePortType {
    IN,
    OUT;

    /**
     * Checks if two cable ports can be connected, based on their types.
     * @param cablePortType1 The type of the first cable port
     * @param cablePortType2 The type of the second cable port
     * @return True if they can be connected, false otherwise
     */
    public static boolean canConnect(CablePortType cablePortType1, CablePortType cablePortType2) {
        if(cablePortType1 != cablePortType2) {
            return true;
        }
        return false;
    }
}
