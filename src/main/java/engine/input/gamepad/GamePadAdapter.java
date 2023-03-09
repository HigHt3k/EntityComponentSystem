package engine.input.gamepad;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import engine.Game;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Thread.sleep;

public class GamePadAdapter {
    private boolean ltPressed = false;
    private boolean rtPressed = false;
    private HashMap<InputAction, Boolean> buttonsPressed;

    /**
     * Create a game pad adapter which uses jamepad library to find controllers and get their inputs
     */
    public GamePadAdapter() {
        buttonsPressed = new HashMap<>();
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
                    try {
                        sleep((long) (1000/Game.config().renderConfiguration().getFrameRate()));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    ControllerState currState = controllers.getState(0);
                    if(currState.leftStickX < -0.5f) {
                        Game.input().queueEvent(InputAction.MOVE_LEFT);
                    }
                    if(currState.leftStickX > 0.5f) {
                        Game.input().queueEvent(InputAction.MOVE_RIGHT);
                    }
                    if(currState.leftStickY < -0.5f) {
                        Game.input().queueEvent(InputAction.MOVE_DOWN);
                    }
                    if(currState.leftStickY > 0.5f) {
                        Game.input().queueEvent(InputAction.MOVE_UP);
                    }
                }
            }
        });

        Thread controllerInputThreadButtons = new Thread(() -> {
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
                    try {
                        sleep((long) (75));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    ControllerState currState = controllers.getState(0);
                    if (currState.a) {
                        if(buttonsPressed.get(InputAction.A) == null) {
                            buttonsPressed.put(InputAction.A, true);
                            Game.input().queueEvent(InputAction.A);
                        }
                    }
                    if(currState.b) {
                        if(buttonsPressed.get(InputAction.B) == null) {
                            buttonsPressed.put(InputAction.B, true);
                            Game.input().queueEvent(InputAction.B);
                        }
                    }
                    if(currState.lb) {
                        if(buttonsPressed.get(InputAction.LB) == null) {
                            buttonsPressed.put(InputAction.LB, true);
                            Game.input().queueEvent(InputAction.LB);
                        }
                    }
                    if(currState.rb) {
                        if(buttonsPressed.get(InputAction.RB) == null) {
                            buttonsPressed.put(InputAction.RB, true);
                            Game.input().queueEvent(InputAction.RB);
                        }
                    }
                    if(currState.dpadLeft) {
                        if (buttonsPressed.get(InputAction.DPAD_LEFT) == null) {
                            buttonsPressed.put(InputAction.DPAD_LEFT, true);
                            Game.input().queueEvent(InputAction.DPAD_LEFT);
                        }
                    }
                    if(currState.dpadDown) {
                        if (buttonsPressed.get(InputAction.DPAD_DOWN) == null) {
                            buttonsPressed.put(InputAction.DPAD_DOWN, true);
                            Game.input().queueEvent(InputAction.DPAD_DOWN);
                        }
                    }
                    if(currState.dpadUp) {
                        if (buttonsPressed.get(InputAction.DPAD_UP) == null) {
                            buttonsPressed.put(InputAction.DPAD_UP, true);
                            Game.input().queueEvent(InputAction.DPAD_UP);
                        }
                    }
                    if(currState.dpadRight) {
                        if (buttonsPressed.get(InputAction.DPAD_RIGHT) == null) {
                            buttonsPressed.put(InputAction.DPAD_RIGHT, true);
                            Game.input().queueEvent(InputAction.DPAD_RIGHT);
                        }
                    }
                    if(currState.leftTrigger > 0.5f) {
                        if (buttonsPressed.get(InputAction.LT) == null) {
                            if (!ltPressed) {
                                ltPressed = true;
                                buttonsPressed.put(InputAction.LT, true);
                                Game.input().queueEvent(InputAction.LT);
                            } else {
                                ltPressed = false;
                            }
                        }
                    }
                    if(currState.rightTrigger > 0.5f) {
                        if (buttonsPressed.get(InputAction.RT) == null) {
                            if (!rtPressed) {
                                rtPressed = true;
                                buttonsPressed.put(InputAction.RT, true);
                                Game.input().queueEvent(InputAction.RT);
                            } else {
                                rtPressed = false;
                            }
                        }
                    }
                }
            }
        });

        controllerInputThread.start();
        controllerInputThreadButtons.start();
    }

    public HashMap<InputAction, Boolean> getButtonsPressed() {
        return buttonsPressed;
    }
}
