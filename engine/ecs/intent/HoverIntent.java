package engine.ecs.intent;

import engine.ecs.component.collision.CollisionComponent;
import engine.ecs.component.graphics.GraphicsComponent;
import engine.ecs.component.IntentComponent;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

@Deprecated
public class HoverIntent extends Intent {
    @Override
    public void handleIntent(KeyEvent e) {
        // Do nothing
    }

    @Override
    public void handleIntent(MouseEvent e) {
        IntentComponent ic = getIntentComponent();
        if(ic != null && ic.getEntity().getComponent(CollisionComponent.class) != null) {
            if(ic.getEntity().getComponent(CollisionComponent.class).contains(e.getPoint())) {
                ic.getEntity().getComponent(GraphicsComponent.class).hovered();
            } else {
                ic.getEntity().getComponent(GraphicsComponent.class).unhovered();
            }
        }
    }
}
