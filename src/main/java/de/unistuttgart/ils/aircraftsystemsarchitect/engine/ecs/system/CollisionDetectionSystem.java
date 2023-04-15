package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.system;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.collision.ColliderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.handler.Handler;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.handler.HandlerType;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.Query;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.collision.CollisionObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.gamepad.InputAction;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Detect collision of objects, such as cursor with buttons or hovers
 */
public class CollisionDetectionSystem extends Handler {
    private final ArrayList<Entity> entities;

    /**
     * Create a new CollisionDetectionSystem with the EVENT type.
     * Initializes an empty arraylist of entities that should be used for this handler.
     * Add entities to handle them
     */
    public CollisionDetectionSystem() {
        super(HandlerType.EVENT);
        entities = new ArrayList<>();
    }

    /**
     * Handle possible key events
     * @param e: the key event
     */
    @Override
    public void handle(KeyEvent e) {

    }

    /**
     * Handle MouseEvents
     * @param e: the mouse event
     */
    @Override
    public void handle(MouseEvent e) {
        //TODO: move entity collection, only collect entities / add entities on entity removal or addition
        recollectEntities();
        for (Entity entity : entities) {
            for (CollisionObject collisionObject : entity.getComponent(ColliderComponent.class).getCollisionObjects()) {
                if (Game.scale().scaleShape(collisionObject.getCollisionBoundaries()).contains(e.getPoint())) {
                    collisionObject.setHovered(true);
                    collisionObject.setClicked(e.getButton() == MouseEvent.BUTTON1);
                } else {
                    collisionObject.setHovered(false);
                    collisionObject.setClicked(false);
                }
            }
        }
    }

    /**
     * Handle gamepad inputs
     * @param e: the gamepad input
     */
    @Override
    public void handle(InputAction e) {

    }

    /**
     * clear list of entities to consider by the system and query for relevant entities using the {@link ColliderComponent}
     */
    public void recollectEntities() {
        entities.clear();
        entities.addAll(Query.getEntitiesWithComponent(ColliderComponent.class));
    }
}
