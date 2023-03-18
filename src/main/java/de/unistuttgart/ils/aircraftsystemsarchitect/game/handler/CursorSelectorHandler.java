package de.unistuttgart.ils.aircraftsystemsarchitect.game.handler;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.InputManager;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.IdGenerator;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.Query;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.CursorComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.ImageObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.scene.Scene;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.gamepad.InputAction;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.handler.Handler;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.handler.HandlerType;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.components.BuildComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.simulation.CursorEntity;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.base.BaseGameFieldScene;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.builder.BuildHandler;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CursorSelectorHandler extends Handler {
    private CursorEntity cursor;
    private final Set<Integer> pressedKeys = new HashSet<>();
    private boolean isInBuildPanel = false;
    private int buildPanelIndex = 0;

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
     * to the {@link InputManager}'s mouse event queue.
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

    @Override
    public synchronized void handle(InputAction e) {
        int x = cursor.getComponent(RenderComponent.class).getRenderObjects().get(0).getBounds().getBounds().x;
        int y = cursor.getComponent(RenderComponent.class).getRenderObjects().get(0).getBounds().getBounds().y;

        if(e == InputAction.MOVE_DOWN || e == InputAction.MOVE_UP || e == InputAction.MOVE_LEFT || e == InputAction.MOVE_RIGHT) {
            switch (e) {
                case MOVE_LEFT -> x -= 1 * Game.config().getControls().getControllerSpeed();
                case MOVE_RIGHT -> x += 1 * Game.config().getControls().getControllerSpeed();
                case MOVE_UP -> y -= 1 * Game.config().getControls().getControllerSpeed();
                case MOVE_DOWN -> y += 1 * Game.config().getControls().getControllerSpeed();
            }
            moveCursor(x, y);
            return;
        }

        switch(e) {
            case A, RT -> {
                sendNewMouseEvent(new MouseEvent(
                        Game.frame().getRenderPanel(),
                        MouseEvent.MOUSE_CLICKED,
                        1,
                        InputEvent.BUTTON1_MASK,
                        Game.scale().scaleX(x), Game.scale().scaleY(y), 1, false, MouseEvent.BUTTON1
                ));
            }
            case B -> sendNewMouseEvent(new MouseEvent(
                    Game.frame().getRenderPanel(),
                    MouseEvent.MOUSE_CLICKED,
                    1,
                    InputEvent.BUTTON3_MASK,
                    Game.scale().scaleX(x), Game.scale().scaleY(y), 1, false, MouseEvent.BUTTON3
            ));
            case LT -> sendNewMouseEvent(new MouseEvent(
                    Game.frame().getRenderPanel(),
                    MouseEvent.MOUSE_CLICKED,
                    1,
                    InputEvent.BUTTON2_MASK,
                    Game.scale().scaleX(x), Game.scale().scaleY(y), 1, false, MouseEvent.BUTTON2
            ));
            case RB -> {
                BuildHandler buildHandler = Game.input().getHandler(BuildHandler.class);
                int curLayer = buildHandler.getCurrentCableLayer();

                if (curLayer < 3) {
                    buildHandler.setCurrentCableLayer(curLayer + 1);
                }
            }
            case LB -> {
                BuildHandler buildHandler = Game.input().getHandler(BuildHandler.class);
                int curLayer = buildHandler.getCurrentCableLayer();

                if (curLayer > 0) {
                    buildHandler.setCurrentCableLayer(curLayer - 1);
                }
            }
            case DPAD_DOWN -> {
                // open the build panel, set cursor there
                if(Game.scene().current() instanceof BaseGameFieldScene scene) {
                    isInBuildPanel = true;

                    // get any build element
                    ArrayList<Entity> entities = Query.getEntitiesWithComponent(BuildComponent.class);
                    if(entities.isEmpty()) {
                        break;
                    }
                    buildPanelIndex = 0;

                    int xBuild = entities.get(buildPanelIndex).getComponent(RenderComponent.class).getRenderObjectsOfType(ImageObject.class).get(0).getLocation().x;
                    int yBuild = entities.get(buildPanelIndex).getComponent(RenderComponent.class).getRenderObjectsOfType(ImageObject.class).get(0).getLocation().y;

                    moveCursor(xBuild, yBuild);
                }
            }
            case DPAD_RIGHT -> {
                if(isInBuildPanel) {
                    ArrayList<Entity> entities = Query.getEntitiesWithComponent(BuildComponent.class);
                    if(entities.isEmpty()) {
                        break;
                    }

                    buildPanelIndex++;
                    if(buildPanelIndex >= entities.size()) {
                        buildPanelIndex--;
                    }

                    int xBuild = entities.get(buildPanelIndex).getComponent(RenderComponent.class).getRenderObjectsOfType(ImageObject.class).get(0).getLocation().x;
                    int yBuild = entities.get(buildPanelIndex).getComponent(RenderComponent.class).getRenderObjectsOfType(ImageObject.class).get(0).getLocation().y;

                    moveCursor(xBuild, yBuild);
                }
            }
            case DPAD_LEFT -> {
                if(isInBuildPanel) {
                    ArrayList<Entity> entities = Query.getEntitiesWithComponent(BuildComponent.class);
                    if(entities.isEmpty()) {
                        break;
                    }

                    buildPanelIndex--;
                    if(buildPanelIndex < 0 ) {
                        buildPanelIndex++;
                    }

                    int xBuild = entities.get(buildPanelIndex).getComponent(RenderComponent.class).getRenderObjectsOfType(ImageObject.class).get(0).getLocation().x;
                    int yBuild = entities.get(buildPanelIndex).getComponent(RenderComponent.class).getRenderObjectsOfType(ImageObject.class).get(0).getLocation().y;

                    moveCursor(xBuild, yBuild);
                }
            }
        }
    }

    /**
     * queue a new {@link MouseEvent} to the {@link InputManager}
     *
     * @param mouseEvent
     */
    private synchronized void sendNewMouseEvent(MouseEvent mouseEvent) {
        Game.input().queueEvent(mouseEvent);
    }

    /**
     * Move the cursor on the screen based on coordinates x and y
     *
     * @param x: x position to move cursor to
     * @param y: y position to move cursor to
     */
    private synchronized void moveCursor(int x, int y) {
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
