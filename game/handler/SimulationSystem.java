package game.handler;

import com.Game;
import com.ecs.entity.Entity;
import com.ecs.system.System;
import game.components.CablePortsComponent;
import game.components.SimulationComponent;

import java.util.ArrayList;

public class SimulationSystem extends System {

    @Override
    public void handle() {

    }

    private ArrayList<Entity> gatherRelevantEntities() {
        ArrayList<Entity> relevantEntities = new ArrayList<>();

        for(Entity e : Game.scene().current().getEntities()) {
            if(e.getComponent(SimulationComponent.class) != null || e.getComponent(CablePortsComponent.class) != null) {
                relevantEntities.add(e);
            }
        }

        return relevantEntities;
    }
}
