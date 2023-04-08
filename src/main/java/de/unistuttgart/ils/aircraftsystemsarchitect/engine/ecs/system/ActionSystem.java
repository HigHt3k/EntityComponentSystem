package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.system;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.ActionComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.collision.ColliderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.Query;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.collision.CollisionObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * The ActionSystem is a system to handle actions depending available input to action mappings of ActionComponents.
 */
public class ActionSystem extends SystemHandle {
    private final ArrayList<Entity> entities;

    /**
     * Instantiate new ActionSystem
     */
    public ActionSystem() {
        entities = new ArrayList<>();
    }

    /**
     * handle actions on collision objects.
     * handles hovering and actions mapped to either "NOBUTTON" or BUTTON1.
     * Checks the state of the component, i.e. if it is already clicked or not
     */
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

    /**
     * recollect all entities that contain collider components, by using the Query class.
     */
    public void recollectEntities() {
        entities.clear();
        ArrayList<Entity> temp = Query.getEntitiesWithComponent(ColliderComponent.class);
        entities.addAll(Query.getEntitiesWithComponent(RenderComponent.class, temp));
    }
}
