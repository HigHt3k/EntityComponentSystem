package de.unistuttgart.ils.skyengine.ecs.system;

import java.util.ArrayList;

public class SystemManager {
    private ArrayList<SystemHandle> systems;

    public SystemManager() {
        this.systems = new ArrayList<>();
    }

    public void addSystem(SystemHandle system) {
        this.systems.add(system);
    }

    public void resetSystems() {
        this.systems = new ArrayList<>();
    }

    public void handle() {
        for(SystemHandle s : systems) {
            s.handle();
        }
    }

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
