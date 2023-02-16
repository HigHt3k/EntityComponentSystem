package engine.ecs.component.action;

import engine.ecs.component.Component;

import java.awt.*;
import java.util.HashMap;

public class ActionComponent extends Component {
    private final HashMap<Integer, Action> actions;

    public ActionComponent() {
        actions = new HashMap<>();
    }

    @Override
    public void update() {

    }

    public HashMap<Integer, Action> getActions() {
        return actions;
    }

    public void addAction(Integer event, Action action) {
        actions.put(event, action);
    }
}
