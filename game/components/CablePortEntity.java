package game.components;

import com.ecs.component.CollisionComponent;
import com.ecs.component.GraphicsComponent;
import com.ecs.component.IntentComponent;
import com.ecs.entity.Entity;
import com.ecs.intent.HoverIntent;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class CablePortEntity extends Entity {
    private int portId;
    private CablePortType type;
    private Entity connectedEntity;

    public CablePortEntity(String name, int id,
                           int x, int y, int width, int height,
                           int portId,
                           CablePortType type) {
        super(name, id);
        this.portId = portId;
        this.type = type;

        Ellipse2D shape = new Ellipse2D.Double(x, y, width, height);

        GraphicsComponent graphics = new GraphicsComponent();
        graphics.setShape(shape);
        graphics.setFillColor(Color.WHITE);
        graphics.setBorderColor(Color.RED);
        graphics.setHoverColor(Color.BLUE);
        graphics.setEntity(this);
        this.addComponent(graphics);

        CollisionComponent collider = new CollisionComponent();
        collider.setCollisionBox(graphics.get_SHAPE());
        collider.setEntity(this);
        this.addComponent(collider);

        IntentComponent intents = new IntentComponent();
        intents.setEntity(this);
        this.addComponent(intents);

        HoverIntent hover = new HoverIntent();
        intents.addIntent(hover);
        hover.setIntentComponent(intents);
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
