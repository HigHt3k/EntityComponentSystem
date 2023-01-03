package game.components;

import com.ecs.Entity;

public class CablePort {
    private Entity connectedEntity;

    public void setConnectedEntity(Entity connectedEntity) {
        this.connectedEntity = connectedEntity;
    }

    public Entity getConnectedEntity() {
        return connectedEntity;
    }
}
