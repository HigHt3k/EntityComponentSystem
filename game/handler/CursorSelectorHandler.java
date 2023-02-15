package game.handler;

import engine.Game;
import engine.IdGenerator;
import engine.ecs.Query;
import engine.ecs.component.CursorComponent;
import engine.ecs.component.IntentComponent;
import engine.ecs.component.collision.CollisionComponent;
import engine.ecs.component.graphics.GraphicsComponent;
import engine.ecs.entity.Entity;
import engine.input.handler.Handler;
import engine.input.handler.HandlerType;
import game.entities.CursorEntity;
import game.intent.StartIntent;
import game.scenes.GameScene;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CursorSelectorHandler extends Handler {
    private CursorEntity cursor;

    public CursorSelectorHandler() {
        super(HandlerType.EVENT);

        cursor = new CursorEntity("cursor", IdGenerator.generateId());
        Game.scene().current().addEntityToScene(cursor);
    }

    @Override
    public void handle(KeyEvent e) {
        ArrayList<Entity> collisionEntities = Query.getEntitiesWithComponent(CollisionComponent.class);
        int x = cursor.getComponent(GraphicsComponent.class).getBounds().x;
        int y = cursor.getComponent(GraphicsComponent.class).getBounds().y;

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

        cursor.getComponent(GraphicsComponent.class).reposition(new Point(x, y));
        cursor.getComponent(CursorComponent.class).reposition(new Point(x, y));

        for(Entity entity : collisionEntities) {
            if(entity.getComponent(CollisionComponent.class).contains(cursor.getComponent(CursorComponent.class).getCursorPosition())) {
                if(cursor.getComponent(CursorComponent.class).getSelected() != null) {
                    cursor.getComponent(CursorComponent.class).getSelected().getComponent(GraphicsComponent.class).unhovered();
                }
                cursor.getComponent(CursorComponent.class).setSelected(entity);
                entity.getComponent(GraphicsComponent.class).hovered();
            }
        }
    }

    @Override
    public void handle(MouseEvent e) {

    }
}
