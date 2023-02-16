package engine.ecs.system;

import engine.ecs.Query;
import engine.ecs.component.collision.ColliderComponent;
import engine.ecs.component.collision.CollisionObject;
import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.HoverObject;
import engine.ecs.entity.Entity;
import engine.input.handler.Handler;
import engine.input.handler.HandlerType;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class HoverSystem extends SystemHandle {
    private final ArrayList<Entity> entities;

    public HoverSystem() {
        entities = new ArrayList<>();
    }

    @Override
    public void handle() {
        recollectEntities();
        System.out.println(entities.size());
        for (Entity e : entities) {
            if (e.getComponent(ColliderComponent.class).getCollisionObjects() != null) {
                for (CollisionObject c : e.getComponent(ColliderComponent.class).getCollisionObjects()) {
                    if (c.getConnectedHoverObject() == null) {
                        continue;
                    } else {
                        if (c.isHovered()) {
                            c.getConnectedHoverObject().setHidden(false);
                        } else {
                            c.getConnectedHoverObject().setHidden(true);
                        }
                    }
                }
            }
        }
    }

    public void recollectEntities() {
        entities.clear();
        ArrayList<Entity> temp = Query.getEntitiesWithComponent(ColliderComponent.class);
        entities.addAll(Query.getEntitiesWithComponent(RenderComponent.class, temp));
    }
}
