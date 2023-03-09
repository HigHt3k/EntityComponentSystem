package engine.ecs.system;

import engine.ecs.Query;
import engine.ecs.component.action.ActionComponent;
import engine.ecs.component.collision.ColliderComponent;
import engine.ecs.component.collision.CollisionObject;
import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.entity.Entity;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ActionSystem extends SystemHandle {
    private final ArrayList<Entity> entities;

    public ActionSystem() {
        entities = new ArrayList<>();
    }

    @Override
    public void handle() {
        recollectEntities();
        for (Entity e : entities) {
            if (e.getComponent(ColliderComponent.class).getCollisionObjects() != null) {
                if(e.getComponent(ColliderComponent.class).getCollisionObjects().get(0).isDeactivated()) {
                    continue;
                }
                for (CollisionObject c : e.getComponent(ColliderComponent.class).getCollisionObjects()) {
                    if (c.getConnectedHoverObject() == null) {
                        continue;
                    } else {
                        c.getConnectedHoverObject().setHidden(!c.isHovered());

                        if (c.isClicked()) {
                            if (e.getComponent(ActionComponent.class) != null) {
                                e.getComponent(ActionComponent.class).getActions().get(MouseEvent.BUTTON1).handle();
                                c.setClicked(false);
                            }
                        }
                        if (c.isHovered()) {
                            if (e.getComponent(ActionComponent.class) != null) {
                                if (e.getComponent(ActionComponent.class).getActions().get(MouseEvent.NOBUTTON) != null) {
                                    e.getComponent(ActionComponent.class).getActions().get(MouseEvent.NOBUTTON).handle();
                                }
                            }
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
