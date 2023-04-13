package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.cable;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.components.CablePortType;

/**
 * CablePorts describe a connection to an Entity. There are two types of cable ports: input and output, with different ids.
 * Inputs can only connect to outputs.
 */
public class CablePort {
    private final int portId;
    private final CablePortType type;
    private Entity connectedEntity;
    private CablePortPosition position;

    /**
     * create new cable port, input/output
     * @param portId: the id of the port (only same ids of in/out can connect)
     * @param type: port type, input/output
     * @param position: direction on the game grid
     */
    public CablePort(int portId,
                     CablePortType type,
                     CablePortPosition position) {
        this.portId = portId;
        this.type = type;
        this.position = position;
    }

    /**
     * Set the position of the cable port to one of the 4 directions
     * @param position: the position
     */
    public void setPosition(CablePortPosition position) {
        this.position = position;
    }

    /**
     * Cycle the position of the cable port to the next position (left -> top -> right -> bottom)
     */
    public void cyclePosition() {
        position = CablePortPosition.next(position);
    }

    /**
     * Get the current port position
     * @return the position
     */
    public CablePortPosition getPosition() {
        return position;
    }

    /**
     * Set the connected entity of this port
     * @param e: the entity
     */
    public void setConnectedEntity(Entity e) {
        this.connectedEntity = e;
    }

    /**
     * Disconnect an entity. Sets the connected entity to null. Note: null has to be handled
     */
    public void disconnect() {
        this.connectedEntity = null;
    }

    /**
     * get the connected entity
     * @return the entity
     */
    public Entity getConnectedEntity() {
        return connectedEntity;
    }

    /**
     * Get the port type, either in or out
     * @return the type
     */
    public CablePortType getType() {
        return type;
    }

    /**
     * Get the port id
     * @return the id
     */
    public int getPortId() {
        return portId;
    }
}
