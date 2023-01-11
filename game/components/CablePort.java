package game.components;

import com.ecs.component.Component;
import com.ecs.entity.Entity;

import java.awt.geom.Ellipse2D;

public class CablePort {
    private CablePortType type;

    public CablePort(int id, CablePortType type) {
        this.id = id;
        this.type = type;
    }
    private Entity connectedEntity;

    public Ellipse2D getCollider() {
        return collider;
    }

    public CablePortType getType() {
        return type;
    }

    public void setCollider(Ellipse2D collider) {
        this.collider = collider;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private Ellipse2D collider;
    private int id;

    public void setConnectedEntity(Entity connectedEntity) {
        this.connectedEntity = connectedEntity;
    }

    public Entity getConnectedEntity() {
        return connectedEntity;
    }
}
