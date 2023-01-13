package com.ecs.intent;

import com.Game;
import com.ecs.entity.Entity;
import game.entities.CablePortEntity;
import game.components.CablePortsComponent;
import game.components.GridComponent;
import game.components.SimulationComponent;
import game.scenes.GameScene;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * An intent to do "manual" debugging on press of a button.
 */
public class DebugIntent extends Intent {
    @Override
    public void handleIntent(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_P) {
            printAllCablePorts();
        }
        if(e.getKeyCode() == KeyEvent.VK_O) {
            printAllSimComponents();
        }
    }

    @Override
    public void handleIntent(MouseEvent e) {

    }

    private void printAllSimComponents() {
        if(Game.scene().current() instanceof GameScene gs) {
            ArrayList<Entity> entities = gs.getEntities();
            System.out.println("-------- SIMULATION ENTITIES --------");

            for(Entity e : entities) {
                if(e.getComponent(SimulationComponent.class) != null) {
                    System.out.println("------- " + e.getName() + " - " + e.getId() + " --------");
                    if(e.getComponent(SimulationComponent.class) != null) {
                        System.out.println("Group ID: " + e.getComponent(SimulationComponent.class).getGroupIds());
                    }
                    if(e.getComponent(GridComponent.class) != null)
                        System.out.println("at position: " + e.getComponent(GridComponent.class).getGridLocation());
                }
            }
        }
    }

    public void printAllCablePorts() {
        if(Game.scene().current() instanceof GameScene gs) {
            ArrayList<Entity> entities = gs.getEntities();

            for(Entity e : entities) {
                if(e.getComponent(CablePortsComponent.class) != null) {
                    System.out.println("------- " + e.getName() + " - " + e.getId() + " --------");
                    if(e.getComponent(SimulationComponent.class) != null) {
                        System.out.println("Group ID: " + e.getComponent(SimulationComponent.class).getGroupIds());
                    }
                    if(e.getComponent(GridComponent.class) != null)
                        System.out.println("at position: " + e.getComponent(GridComponent.class).getGridLocation());
                    for(CablePortEntity cpe : e.getComponent(CablePortsComponent.class).getCablePorts()) {
                        if(cpe.getConnectedEntity() != null)
                            System.out.println(cpe.getId() + "-" + cpe.getType() + ": " + cpe.getConnectedEntity().getName());
                        else
                            System.out.println(cpe.getId() + "-" + cpe.getType() + ": " + cpe.getConnectedEntity());
                    }
                }
            }
        }
    }
}
