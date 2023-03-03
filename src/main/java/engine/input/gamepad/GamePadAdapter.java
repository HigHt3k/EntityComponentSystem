package engine.input.gamepad;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import engine.Game;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GamePadAdapter {

    private final ControllerManager controllers;

    /**
     * Create a game pad adapter which uses jamepad library to find controllers and get their inputs
     */
    public GamePadAdapter() {
        controllers = new ControllerManager();
        controllers.initSDLGamepad();
        if (controllers.getState(0).isConnected) {
            Game.logger().info("Successfully initialized controller(s).\nAmount: " + controllers.getNumControllers());
        } else {
            Game.logger().info("No controllers connected.");
        }
    }

    /**
     * Get the action set currently detected, i.e. all currently pressed buttons.
     * All possible actions are defined here.
     *
     * @return a set of all distinct current actions
     */
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

    /**
     * check if a controller is currently connected
     *
     * @return true if controller connected, false else
     */
    public boolean isConnected() {
        return controllers.getState(0).isConnected;
    }
}
