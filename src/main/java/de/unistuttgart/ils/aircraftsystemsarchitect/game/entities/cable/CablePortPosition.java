package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.cable;

/**
 * An enumeration representing the position of a cable port.
 */
public enum CablePortPosition {
    LEFT(0),
    TOP(1),
    RIGHT(2),
    BOTTOM(3);

    /**
     * Constructs a CablePortPosition object with the given value.
     *
     * @param val the value of the cable port position
     */
    CablePortPosition(int val) {

    }

    /**
     * Returns the integer value of the given cable port position.
     *
     * @param pos the cable port position
     * @return the integer value of the cable port position
     */
    public static int valueOf(CablePortPosition pos) {
        switch(pos) {
            case LEFT -> {
                return 0;
            }
            case TOP -> {
                return 1;
            }
            case RIGHT -> {
                return 2;
            }
            case BOTTOM -> {
                return 3;
            }

        }
        return -1;
    }

    /**
     * Returns the next cable port position in the sequence.
     *
     * @param pos the current cable port position
     * @return the next cable port position
     */
    public static CablePortPosition next(CablePortPosition pos) {
        switch(pos) {
            case TOP -> {
                return RIGHT;
            }
            case LEFT -> {
                return TOP;
            }
            case BOTTOM -> {
                return LEFT;
            }
            case RIGHT -> {
                return BOTTOM;
            }
        }
        return null;
    }
}
