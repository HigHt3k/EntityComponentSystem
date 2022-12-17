package com.ecs.intent;

import com.Game;
import com.ecs.component.CollisionComponent;
import com.ecs.component.IntentComponent;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class ExitIntent extends Intent {

    @Override
    public void handleIntent(KeyEvent e) {

    }

    @Override
    public void handleIntent(MouseEvent e) {
        IntentComponent ic = getIntentComponent();
        if(ic != null && ic.getEntity().getComponent(CollisionComponent.class) != null) {
            if(ic.getEntity().getComponent(CollisionComponent.class).contains(e.getPoint()) && e.getButton() == MouseEvent.BUTTON1) {
                Game.logger().info("Exiting game after click on exit.");
                System.exit(1);
            }
        }
    }
}
