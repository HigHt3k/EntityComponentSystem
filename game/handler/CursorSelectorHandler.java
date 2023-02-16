package game.handler;

import engine.Game;
import engine.IdGenerator;
import engine.input.handler.Handler;
import engine.input.handler.HandlerType;
import game.entities.simulation.CursorEntity;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class CursorSelectorHandler extends Handler {
    private final CursorEntity cursor;

    public CursorSelectorHandler() {
        super(HandlerType.EVENT);

        cursor = new CursorEntity("cursor", IdGenerator.generateId());
        Game.scene().current().addEntityToScene(cursor);
    }

    public CursorEntity getCursor() {
        return cursor;
    }

    @Override
    public void handle(KeyEvent e) {
        /*ArrayList<Entity> collisionEntities = Query.getEntitiesWithComponent(CollisionComponent.class);
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

         */
    }

    @Override
    public void handle(MouseEvent e) {

    }
}
