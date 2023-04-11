package de.unistuttgart.ils.aircraftsystemsarchitect.game.components;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.Component;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.ImageObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.cable.CablePort;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.cable.CablePortPosition;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.SimulationType;

import java.util.ArrayList;

/**
 * The cable ports component stores information about attached
 * entities to this parent entity.
 */
public class CablePortsComponent extends Component {
    private final ArrayList<CablePort> cablePorts = new ArrayList<>();
    private int[] inIds;
    private int[] outIds;

    /**
     * Update the image of the RenderComponent based on the id of this component, if
     * the parent entity also has a cable type simulation component.
     * TODO: this should be done by either of the systems instead, preferably simulation system
     */
    public void updateImage() {
        if(this.getEntity().getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CABLE) {
            int baseId = inIds[0] + 500;

            CablePortPosition in = getCablePort(inIds[0], CablePortType.IN).getPosition();
            CablePortPosition out = getCablePort(outIds[0], CablePortType.OUT).getPosition();
            int imgToLoad = baseId*100 + CablePortPosition.valueOf(in) * 10 + CablePortPosition.valueOf(out);

            this.getEntity().getComponent(RenderComponent.class).getRenderObjectsOfType(ImageObject.class).get(0).setImage(Game.res().getTileSet().getTile(imgToLoad));
        }
    }

    public void setIds(int[] inIds, int[] outIds) {
        this.inIds = inIds;
        this.outIds = outIds;
    }

    public int[] getInIds() {
        return inIds;
    }

    public int[] getOutIds() {
        return outIds;
    }

    public ArrayList<CablePort> getCablePorts() {
        return cablePorts;
    }

    public CablePort getCablePort(int id, CablePortType type) {
        for(CablePort port : cablePorts) {
            if(port.getType() == type) {
                if(port.getPortId() == id) {
                    return port;
                }
            }
        }
        return null;
    }

    /**
     * get all entities connected to the input ports of this component, only non null
     * @return
     */
    public ArrayList<Entity> getAllConnectedEntities() {
        ArrayList<Entity> all = new ArrayList<>();

        for(CablePort c : cablePorts) {
            if(c.getType() == CablePortType.IN) {
                if(c.getConnectedEntity() != null) {
                    all.add(c.getConnectedEntity());
                }
            }
        }
        return all;
    }

    /**
     * Get the next available port in the port list of a specified type
     * @param type in or out
     * @return the next free port
     */
    public CablePort getNextFreePort(CablePortType type) {
        for(CablePort port : cablePorts) {
            if(port.getType() == type) {
                if(port.getConnectedEntity() == null) {
                    return port;
                }
            }
        }
        return null;
    }

    public void addCablePort(CablePort cablePortEntity) {
        this.cablePorts.add(cablePortEntity);
    }

    /**
     * Get the port id of an entity that is connected to this component
     * @param e the entity
     * @return the cable port id or -1 if not found
     */
    public int getPortIdOfConnectedEntity(Entity e) {
        for(CablePort port : cablePorts) {
            if(port.getConnectedEntity() == e) {
                return port.getPortId();
            }
        }
        return -1;
    }

    /**
     * Get the port type of a connected entity to this component
     * @param e: the connected entity
     * @return the port type the entity is connected to on this component
     */
    public CablePortType getPortTypeOfConnectedEntity(Entity e) {
        for(CablePort port : cablePorts) {
            if(port.getConnectedEntity() == e) {
                return port.getType();
            }
        }
        return null;
    }
}
