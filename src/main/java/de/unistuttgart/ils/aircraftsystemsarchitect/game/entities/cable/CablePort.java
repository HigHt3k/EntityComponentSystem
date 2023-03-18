package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.cable;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.components.CablePortType;

public class CablePort {
    private final int portId;
    private final CablePortType type;
    private Entity connectedEntity;
    private CablePortPosition position;

    public CablePort(int portId,
                     CablePortType type,
                     CablePortPosition position) {
        this.portId = portId;
        this.type = type;
        this.position = position;
    }

    public void setPosition(CablePortPosition position) {
        this.position = position;
    }

    public void cyclePosition() {
        position = CablePortPosition.next(position);
    }

    public CablePortPosition getPosition() {
        return position;
    }

    public void setConnectedEntity(Entity e) {
        this.connectedEntity = e;
    }

    public void disconnect() {
        this.connectedEntity = null;
    }

    public Entity getConnectedEntity() {
        return connectedEntity;
    }

    public CablePortType getType() {
        return type;
    }

    public int getPortId() {
        return portId;
    }
}
