package de.unistuttgart.ils.skyengine.input.gamepad;

import com.studiohartman.jamepad.ControllerManager;
import com.studiohartman.jamepad.ControllerState;
import de.unistuttgart.ils.skyengine.Game;

import static java.lang.Thread.sleep;

public class GamePadAdapter {

    private boolean ltPressed = false;
    private boolean rtPressed = false;
    private boolean aPressed = false;
    private boolean bPressed = false;
    private boolean xPressed = false;
    private boolean yPressed = false;
    private boolean dpadUpPressed = false;
    private boolean dpadDownPressed = false;
    private boolean dpadRightPressed = false;
    private boolean dpadLeftPressed = false;
    private boolean rbPressed = false;
    private boolean lbPressed = false;

    /**
     * Create a game pad adapter which uses jamepad library to find controllers and get their inputs
     */
    public GamePadAdapter() {
        try {
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
                            sleep((long) (1000 / Game.config().renderConfiguration().getFrameRate()));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        ControllerState currState = controllers.getState(0);
                        if (currState.leftStickX < -0.5f) {
                            Game.input().queueEvent(InputAction.MOVE_LEFT);
                        }
                        if (currState.leftStickX > 0.5f) {
                            Game.input().queueEvent(InputAction.MOVE_RIGHT);
                        }
                        if (currState.leftStickY < -0.5f) {
                            Game.input().queueEvent(InputAction.MOVE_DOWN);
                        }
                        if (currState.leftStickY > 0.5f) {
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
                            sleep((long) (20));
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        ControllerState currState = controllers.getState(0);

                        // Handle A Button
                        if (currState.a) {
                            if (!aPressed) {
                                aPressed = true;
                                Game.input().queueEvent(InputAction.A);
                            }
                        } else {
                            aPressed = false;
                        }

                        // Handle B Button
                        if (currState.b) {
                            if (!bPressed) {
                                bPressed = true;
                                Game.input().queueEvent(InputAction.B);
                            }
                        } else {
                            bPressed = false;
                        }

                        // Handle LB
                        if (currState.lb) {
                            if (!lbPressed) {
                                lbPressed = true;
                                Game.input().queueEvent(InputAction.LB);
                            }
                        } else {
                            lbPressed = false;
                        }

                        // Handle RB
                        if (currState.rb) {
                            if (!rbPressed) {
                                rbPressed = true;
                                Game.input().queueEvent(InputAction.RB);
                            }
                        } else {
                            rbPressed = false;
                        }

                        // Handle dpad left
                        if (currState.dpadLeft) {
                            if (!dpadLeftPressed) {
                                dpadLeftPressed = true;
                                Game.input().queueEvent(InputAction.DPAD_LEFT);
                            }
                        } else {
                            dpadLeftPressed = false;
                        }

                        // Handle dpad down
                        if (currState.dpadDown) {
                            if (!dpadDownPressed) {
                                dpadDownPressed = true;
                                Game.input().queueEvent(InputAction.DPAD_DOWN);
                            }
                        } else {
                            dpadDownPressed = false;
                        }

                        // Handle dpad Up
                        if (currState.dpadUp) {
                            if (!dpadUpPressed) {
                                dpadUpPressed = true;
                                Game.input().queueEvent(InputAction.DPAD_UP);
                            }
                        } else {
                            dpadUpPressed = false;
                        }

                        // handle dpad right
                        if (currState.dpadRight) {
                            if (!dpadRightPressed) {
                                dpadRightPressed = true;
                                Game.input().queueEvent(InputAction.DPAD_RIGHT);
                            }
                        } else {
                            dpadRightPressed = false;
                        }

                        // Handle LT
                        if (currState.leftTrigger > 0.5f) {
                            if (!ltPressed) {
                                ltPressed = true;
                                Game.input().queueEvent(InputAction.LT);
                            }
                        } else {
                            ltPressed = false;
                        }

                        // Handle RT
                        if (currState.rightTrigger > 0.5f) {
                            if (!rtPressed) {
                                rtPressed = true;
                                Game.input().queueEvent(InputAction.RT);
                            }
                        } else {
                            rtPressed = false;
                        }
                    }
                }
            });

            controllerInputThread.start();
            controllerInputThreadButtons.start();

        } catch(IllegalStateException ex) {
            Game.logger().info("No controller connected. Continue without controller usage");
        }
    }
}
