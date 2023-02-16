package engine.ecs.system;

import engine.Game;
import engine.ecs.Query;
import engine.ecs.component.collision.ColliderComponent;
import engine.ecs.component.collision.CollisionObject;
import engine.ecs.entity.Entity;
import engine.input.handler.Handler;
import engine.input.handler.HandlerType;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Detect collision of objects, such as cursor with buttons or hovers
 */
public class CollisionDetectionSystem extends Handler {
    private final ArrayList<Entity> entities;

    public CollisionDetectionSystem() {
        super(HandlerType.EVENT);
        entities = new ArrayList<>();
    }

    @Override
    public void handle(KeyEvent e) {

    }

    @Override
    public void handle(MouseEvent e) {
        //TODO: move entitie collection
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

    public void recollectEntities() {
        entities.clear();
        entities.addAll(Query.getEntitiesWithComponent(ColliderComponent.class));
    }
}
