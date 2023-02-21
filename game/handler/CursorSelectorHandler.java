package game.handler;

import engine.Game;
import engine.IdGenerator;
import engine.ecs.component.CursorComponent;
import engine.ecs.component.graphics.RenderComponent;
import engine.graphics.scene.Scene;
import engine.input.handler.Handler;
import engine.input.handler.HandlerType;
import game.entities.simulation.CursorEntity;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

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
            case KeyEvent.VK_ENTER -> sendNewMouseEvent(new MouseEvent(
                    Game.frame().getRenderPanel(),
                    MouseEvent.MOUSE_CLICKED,
                    1,
                    InputEvent.BUTTON1_MASK,
                    Game.scale().scaleX(x), Game.scale().scaleY(y), 1, false, MouseEvent.BUTTON1
            ));
            case KeyEvent.VK_ESCAPE -> sendNewMouseEvent(new MouseEvent(
                    Game.frame().getRenderPanel(),
                    MouseEvent.MOUSE_CLICKED,
                    1,
                    InputEvent.BUTTON3_MASK,
                    Game.scale().scaleX(x),
                    Game.scale().scaleY(y),
                    1, false,
                    MouseEvent.BUTTON3
            ));
            case KeyEvent.VK_TAB -> sendNewMouseEvent(new MouseEvent(
                    Game.frame().getRenderPanel(),
                    MouseEvent.MOUSE_CLICKED,
                    1,
                    InputEvent.BUTTON2_MASK,
                    Game.scale().scaleX(x),
                    Game.scale().scaleY(y),
                    1, false,
                    MouseEvent.BUTTON2
            ));
        }
    }

    @Override
    public void handle(MouseEvent e) {

    }

    private void sendNewMouseEvent(MouseEvent mouseEvent) {
        Game.input().queueEvent(mouseEvent);
    }

    private void moveCursor(int x, int y) {
        cursor.getComponent(RenderComponent.class).reposition(new Point(x, y));
        cursor.getComponent(CursorComponent.class).reposition(new Point(x, y));

        MouseEvent mouseEvent = new MouseEvent(
                Game.frame().getRenderPanel(),
                MouseEvent.MOUSE_MOVED,
                1,
                InputEvent.BUTTON1_MASK,
                Game.scale().scaleX(x), Game.scale().scaleY(y), 0, false, MouseEvent.NOBUTTON
        );
        sendNewMouseEvent(mouseEvent);
    }
}
