package game.entities;

import com.ecs.component.CollisionComponent;
import com.ecs.component.GraphicsComponent;
import com.ecs.component.IntentComponent;
import com.ecs.entity.Entity;
import com.ecs.intent.HoverIntent;
import game.components.CablePortType;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class CablePort {
    private int portId;
    private CablePortType type;
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
