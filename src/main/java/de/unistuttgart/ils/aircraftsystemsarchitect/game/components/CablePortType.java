package de.unistuttgart.ils.aircraftsystemsarchitect.game.components;

public enum CablePortType {
    IN,
    OUT;

    public static boolean canConnect(CablePortType cablePortType1, CablePortType cablePortType2) {
        if(cablePortType1 != cablePortType2) {
            return true;
        }
        return false;
    }
}
