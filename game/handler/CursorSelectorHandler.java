package game.handler;

import engine.Game;
import engine.IdGenerator;
import engine.ecs.Query;
import engine.ecs.component.CursorComponent;
import engine.ecs.component.collision.ColliderComponent;
import engine.ecs.component.collision.CollisionObject;
import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.HoverObject;
import engine.ecs.entity.Entity;
import engine.input.handler.Handler;
import engine.input.handler.HandlerType;
import game.entities.simulation.CursorEntity;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CursorSelectorHandler extends Handler {
    private final CursorEntity cursor;

    public CursorSelectorHandler() {
        super(HandlerType.EVENT);

        cursor = new CursorEntity("cursor", IdGenerator.generateId());
        Game.scene().current().addEntityToScene(cursor);
        System.out.println("added Cursor");
    }

    public CursorEntity getCursor() {
        return cursor;
    }

    @Override
    public void handle(KeyEvent e) {
        ArrayList<Entity> collisionEntities = Query.getEntitiesWithComponent(ColliderComponent.class);
        int x = cursor.getComponent(RenderComponent.class).getRenderObjects().get(0).getBounds().getBounds().x;
        int y = cursor.getComponent(RenderComponent.class).getRenderObjects().get(0).getBounds().getBounds().y;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT -> {
                x += 1 * Game.config().getControls().getCursorSpeed();
            }
            case KeyEvent.VK_LEFT -> {
                x -= 1 * Game.config().getControls().getCursorSpeed();
            }
            case KeyEvent.VK_DOWN -> {
                y += 1 * Game.config().getControls().getCursorSpeed();
            }
            case KeyEvent.VK_UP -> {
                y -= 1 * Game.config().getControls().getCursorSpeed();
            }
        }

        cursor.getComponent(RenderComponent.class).reposition(new Point(x, y));
        cursor.getComponent(CursorComponent.class).reposition(new Point(x, y));

        for(Entity entity : collisionEntities) {
            for (CollisionObject c : entity.getComponent(ColliderComponent.class).getCollisionObjects()) {
                if (c.getCollisionBoundaries()
                        .contains(cursor.getComponent(CursorComponent.class).getCursorPosition())) {
                    cursor.getComponent(CursorComponent.class)
                            .setSelected(entity);
                    c.setHovered(true);
                } else {
                    c.setHovered(false);
                }
            }

        }
    }

    @Override
    public void handle(MouseEvent e) {

    }
}
