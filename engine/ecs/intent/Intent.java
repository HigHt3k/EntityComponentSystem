package engine.ecs.intent;

import engine.ecs.component.IntentComponent;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

@Deprecated
public abstract class Intent {
    private IntentComponent intentComponent;
    private int key;

    public abstract void handleIntent(KeyEvent e);

    public abstract void handleIntent(MouseEvent e);

    public void setIntentComponent(IntentComponent intentComponent) {
        this.intentComponent = intentComponent;
    }

    public IntentComponent getIntentComponent() {return intentComponent; }
}
