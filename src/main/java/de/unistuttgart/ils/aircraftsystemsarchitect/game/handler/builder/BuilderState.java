package de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.builder;

/**
 * Enumeration representing the different states of the builder.
 */
public enum BuilderState {
    /**
     * The builder is currently in the process of building a cable.
     */
    BUILDING_CABLE,

    /**
     * The builder is currently in the process of building a simulation entity.
     */
    BUILDING_SIMULATION,

    /**
     * The builder is not currently building anything.
     */
    NOT_BUILDING,

    /**
     * The builder is currently in the process of removing an entity.
     */
    REMOVING
}
