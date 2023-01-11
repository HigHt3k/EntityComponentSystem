package com.ecs.intent;

import com.Game;
import com.ecs.entity.Entity;
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
    }

    @Override
    public void handleIntent(MouseEvent e) {

    }

    public void printAllCablePorts() {
        if(Game.scene().current() instanceof GameScene gs) {
            ArrayList<Entity> entities = gs.getEntities();

            for(Entity e : entities) {
                if(e.getComponent(CablePortsComponent.class) != null) {
                    System.out.println("------- " + e.getName() + " - " + e.getId() + " --------");
                    if(e.getComponent(SimulationComponent.class) != null) {
                        System.out.println("Group ID: " + e.getComponent(SimulationComponent.class).getGroupId());
                    }
                    if(e.getComponent(GridComponent.class) != null)
                        System.out.println("at position: " + e.getComponent(GridComponent.class).getGridLocation());
                    System.out.println("Ports-Amount: " + e.getComponent(CablePortsComponent.class).getCablePortAmount());
                    for(int i = 0; i < e.getComponent(CablePortsComponent.class).getCablePortAmount(); i++) {
                        if(e.getComponent(CablePortsComponent.class).getCablePortIn(i).getConnectedEntity() == null) {
                            System.out.println("Port" + i + "In: " + "null");
                        } else {
                            System.out.println("Port" + i + "In: " +
                                    e.getComponent(CablePortsComponent.class).getCablePortIn(i).getConnectedEntity().getName() + " - " +
                                    e.getComponent(CablePortsComponent.class).getCablePortIn(i).getConnectedEntity().getId());
                        }

                        if(e.getComponent(CablePortsComponent.class).getCablePortOut(i).getConnectedEntity() == null) {
                            System.out.println("Port" + i + "Out: " + "null");
                        } else {
                            System.out.println("Port" + i + "Out: " +
                                    e.getComponent(CablePortsComponent.class).getCablePortOut(i).getConnectedEntity().getName() + " - " +
                                    e.getComponent(CablePortsComponent.class).getCablePortOut(i).getConnectedEntity().getId());
                        }
                    }
                }
            }
        }
    }
}
