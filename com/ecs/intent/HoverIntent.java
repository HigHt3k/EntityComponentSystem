package com.ecs.intent;

import com.ecs.CollisionComponent;
import com.ecs.GraphicsComponent;
import com.ecs.IntentComponent;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

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
