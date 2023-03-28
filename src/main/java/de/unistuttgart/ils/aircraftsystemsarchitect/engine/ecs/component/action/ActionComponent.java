package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.Component;

import java.util.HashMap;

public class ActionComponent extends Component {
    private final HashMap<Integer, Action> actions;

    public ActionComponent() {
        actions = new HashMap<>();
    }

    public HashMap<Integer, Action> getActions() {
        return actions;
    }

    public void addAction(Integer event, Action action) {
        actions.put(event, action);
    }
}
