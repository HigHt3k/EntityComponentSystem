package game.entities;

public enum CablePortPosition {
    LEFT(0),
    TOP(1),
    RIGHT(2),
    BOTTOM(3);

    CablePortPosition(int val) {

    }

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

