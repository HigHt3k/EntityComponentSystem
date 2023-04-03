package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.Component;

import java.util.HashMap;

/**
 * The action component is used by the {@link de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.system.ActionSystem}
 * to handle different actions in the implementation.
 */
public class ActionComponent extends Component {
    private final HashMap<Integer, Action> actions;

    public ActionComponent() {
        actions = new HashMap<>();
    }

    /**
     * get all actions stored in the component
     * @return
     */
    public HashMap<Integer, Action> getActions() {
        return actions;
    }

    /**
     * add an action-button click mapping to the action map. Note: mappings must be unique, there may be no 2
     * actions on the same button.
     * Todo: implement above mentioned behavior, that multiple actions may be handled
     * @param event: the event key
     * @param action: action to be handled by key
     */
    public void addAction(Integer event, Action action) {
        actions.put(event, action);
    }
}
