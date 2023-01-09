package com.ecs.system;

import java.util.ArrayList;

public class SystemManager {
    private ArrayList<System> systems;

    public SystemManager() {
        this.systems = new ArrayList<>();
    }

    public void addSystem(System system) {
        this.systems.add(system);
    }

    public void resetSystems() {
        this.systems = new ArrayList<>();
    }

    public void handle() {
        for(System s : systems) {
            s.handle();
        }
    }
}
