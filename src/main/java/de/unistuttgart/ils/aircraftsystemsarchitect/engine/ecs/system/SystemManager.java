package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.system;

import java.util.ArrayList;

/**
 * Class that manages a collection of SystemHandle instances within an aircraft system architecture.
 * This class provides functionality to add, reset, and retrieve systems, as well as invoking their handle methods.
 */
public class SystemManager {
    /**
     * A collection of SystemHandle instances managed by the SystemManager.
     */
    private ArrayList<SystemHandle> systems;

    /**
     * Constructor for the SystemManager class.
     * Initializes the systems list as an empty ArrayList.
     */
    public SystemManager() {
        this.systems = new ArrayList<>();
    }

    /**
     * Adds a SystemHandle instance to the systems list.
     *
     * @param system The SystemHandle instance to be added.
     */
    public void addSystem(SystemHandle system) {
        this.systems.add(system);
    }

    /**
     * Resets the systems list, creating a new empty ArrayList.
     */
    public void resetSystems() {
        this.systems = new ArrayList<>();
    }

    /**
     * Calls the handle method of each SystemHandle instance in the systems list.
     */
    public void handle() {
        for(SystemHandle s : systems) {
            s.handle();
        }
    }

    /**
     * Retrieves a SystemHandle instance of the specified type from the systems list.
     *
     * @param systemClass The Class of the desired SystemHandle instance.
     * @return The SystemHandle instance of the specified type, or null if not found.
     */
    public <T extends SystemHandle> T getSystem(Class<T> systemClass) {
        for(SystemHandle c : systems) {
            if(systemClass.isAssignableFrom(c.getClass())) {
                try {
                    return systemClass.cast(c);
                } catch(ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
