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
import engine.graphics.scene.Scene;
import engine.input.handler.Handler;
import engine.input.handler.HandlerType;
import game.entities.simulation.CursorEntity;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CursorSelectorHandler extends Handler {
    private CursorEntity cursor;

    public CursorSelectorHandler() {
        super(HandlerType.EVENT);
    }

    public void init() {
        cursor = new CursorEntity("cursor", IdGenerator.generateId());
        for (Scene s : Game.scene().getScenes()) {
            System.out.println(s.getName());
            s.addEntityToScene(cursor);
        }
    }

    public CursorEntity getCursor() {
        return cursor;
    }

    @Override
    public void handle(KeyEvent e) {
        int x = cursor.getComponent(RenderComponent.class).getRenderObjects().get(0).getBounds().getBounds().x;
        int y = cursor.getComponent(RenderComponent.class).getRenderObjects().get(0).getBounds().getBounds().y;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT -> {
                x += 1 * Game.config().getControls().getCursorSpeed();
                moveCursor(x, y);
            }
            case KeyEvent.VK_LEFT -> {
                x -= 1 * Game.config().getControls().getCursorSpeed();
                moveCursor(x, y);
            }
            case KeyEvent.VK_DOWN -> {
                y += 1 * Game.config().getControls().getCursorSpeed();
                moveCursor(x, y);
            }
            case KeyEvent.VK_UP -> {
                y -= 1 * Game.config().getControls().getCursorSpeed();
                moveCursor(x, y);
            }
            case KeyEvent.VK_ENTER -> {
                select();
            }
        }
    }

    @Override
    public void handle(MouseEvent e) {

    }

    private void moveCursor(int x, int y) {
        ArrayList<Entity> collisionEntities = Query.getEntitiesWithComponent(ColliderComponent.class);
        cursor.getComponent(RenderComponent.class).reposition(new Point(x, y));
        cursor.getComponent(CursorComponent.class).reposition(new Point(x, y));

        for (Entity entity : collisionEntities) {
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

    private void select() {
        ArrayList<CollisionObject> collisionObjects = cursor.getComponent(CursorComponent.class)
                .getSelected().getComponent(ColliderComponent.class).getCollisionObjects();
        for (CollisionObject c : collisionObjects) {
            if (c.isHovered()) {
                c.setClicked(true);
            }
        }
    }
}
