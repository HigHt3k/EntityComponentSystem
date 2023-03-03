package engine.input.gamepad;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import engine.Game;
import engine.input.handler.Handler;
import engine.input.handler.HandlerType;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GamePadAdapter {

    private final ControllerManager controllers;

    public GamePadAdapter() {
        controllers = new ControllerManager();
        controllers.initSDLGamepad();
        Game.logger().info("Successfully initialized controller.");
    }

    public Set<InputAction> actions() {
        ControllerState currState = controllers.getState(0);
        if (!currState.isConnected) {
            return Collections.emptySet();
        }

        Set<InputAction> actions = new HashSet<>();
        if (currState.dpadLeft) {
            actions.add(InputAction.MOVE_LEFT);
        }
        if (currState.dpadRight) {
            actions.add(InputAction.MOVE_RIGHT);
        }
        if (currState.dpadUp) {
            actions.add(InputAction.MOVE_UP);
        }
        if (currState.dpadDown) {
            actions.add(InputAction.MOVE_DOWN);
        }
        if (currState.a) {
            actions.add(InputAction.A);
        }
        return actions;
    }

    public boolean isConnected() {
        return controllers.getState(0).isConnected;
    }
}
