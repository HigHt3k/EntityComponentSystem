package engine.input.gamepad;

public class GamePadAdapter {

    private final ControllerManager controllers;

    public GamePadAdapter() {
        controllers = new ControllerManager();
        controllers.initSDLGamepad();
    }

    Set<InputAction> actions() {
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
        return actions;
    }
}
