package game.intent;

import com.Game;
import com.ecs.component.CollisionComponent;
import com.ecs.component.IntentComponent;
import com.ecs.intent.Intent;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class StartIntent extends Intent {
    @Override
    public void handleIntent(KeyEvent e) {

    }

    @Override
    public void handleIntent(MouseEvent e) {
        IntentComponent ic = getIntentComponent();
        if(ic != null && ic.getEntity().getComponent(CollisionComponent.class) != null) {
            if(ic.getEntity().getComponent(CollisionComponent.class).contains(e.getPoint()) && e.getButton() == MouseEvent.BUTTON1) {
                Game.logger().info("Setting scene to: " + getIntentComponent().getEntity().getName());
                Game.scene().setCurrentScene(getIntentComponent().getEntity().getName().replace("_button",""));
                Game.scene().current().init();
            }
        }
    }
}
