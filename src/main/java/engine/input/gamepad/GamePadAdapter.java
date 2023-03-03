package engine.input.gamepad;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import engine.Game;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GamePadAdapter {

    /**
     * Create a game pad adapter which uses jamepad library to find controllers and get their inputs
     */
    public GamePadAdapter() {
        Thread controllerInputThread = new Thread(() -> {
            ControllerManager controllers = new ControllerManager();
            controllers.initSDLGamepad();

            if (controllers.getState(0) != null) {
                Game.logger().info("Successfully initialized controller(s).\nAmount: " + controllers.getNumControllers() + "\nState"
                        + controllers.getState(0).isConnected + "\nType" +
                        controllers.getState(0).controllerType);
            } else {
                Game.logger().info("No controllers connected.");
            }

            if (controllers.getState(0).isConnected) {
                while (true) {
                    ControllerState currState = controllers.getState(0);
                    if (currState.a) {
                        Game.input().queueEvent(InputAction.A);
                    }
                    if(currState.b) {
                        Game.input().queueEvent(InputAction.B);
                    }
                    if(currState.dpadDown) {
                        Game.input().queueEvent(InputAction.MOVE_DOWN);
                    }
                    if(currState.dpadRight) {
                        Game.input().queueEvent(InputAction.MOVE_RIGHT);
                    }
                    if(currState.dpadUp) {
                        Game.input().queueEvent(InputAction.MOVE_UP);
                    }
                    if(currState.dpadLeft) {
                        Game.input().queueEvent(InputAction.MOVE_LEFT);
                    }
                    if(currState.lb) {
                        Game.input().queueEvent(InputAction.LB);
                    }
                    if(currState.rb) {
                        Game.input().queueEvent(InputAction.RB);
                    }
                }
            }
        });
        controllerInputThread.start();
    }
}
