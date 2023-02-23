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
import java.security.Key;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CursorSelectorHandler extends Handler {
    private CursorEntity cursor;
    private final Set<Integer> pressedKeys = new HashSet<>();

    /**
     * Creates a new CursorSelectorHandler of the HandlerType "EVENT", that listens to inputs of keyboard and
     * controllers and converts them to their according mouse inputs.
     * A MouseEvent is distributed and added to the EventQueue in the InputManager, which is then processed by other
     * Handlers.
     */
    public CursorSelectorHandler() {
        super(HandlerType.EVENT);
    }

    /**
     * Initialize the CursorSelectorHandler and add a cursor
     */
    public void init() {
        cursor = new CursorEntity("cursor", IdGenerator.generateId());
        for (Scene s : Game.scene().getScenes()) {
            System.out.println(s.getName());
            s.addEntityToScene(cursor);
        }
    }


    /**
     * Get the cursor entity
     *
     * @return the cursor
     */
    public CursorEntity getCursor() {
        return cursor;
    }

    /**
     * Handle incoming key events. Update cursor position and emulate MouseEvents that are queued
     * to the {@link engine.input.InputManager}'s mouse event queue.
     *
     * @param e: the {@link KeyEvent}
     */
    @Override
    public void handle(KeyEvent e) {
        int x = cursor.getComponent(RenderComponent.class).getRenderObjects().get(0).getBounds().getBounds().x;
        int y = cursor.getComponent(RenderComponent.class).getRenderObjects().get(0).getBounds().getBounds().y;

        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT
                || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                pressedKeys.add(e.getKeyCode());
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                pressedKeys.remove(e.getKeyCode());
            }

            // handle movement
            for (Integer pressedKey : pressedKeys) {
                switch (pressedKey) {
                    //TODO: Add controller keycodes here
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
            }
            moveCursor(x, y);
            return;
        }

        // handle other keys
        switch (e.getKeyCode()) {
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

    /**
     * queue a new {@link MouseEvent} to the {@link engine.input.InputManager}
     *
     * @param mouseEvent
     */
    private void sendNewMouseEvent(MouseEvent mouseEvent) {
        Game.input().queueEvent(mouseEvent);
    }

    /**
     * Move the cursor on the screen based on coordinates x and y
     *
     * @param x: x position to move cursor to
     * @param y: y position to move cursor to
     */
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
