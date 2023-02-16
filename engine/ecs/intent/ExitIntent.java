package engine.ecs.intent;

import engine.Game;
import engine.ecs.component.collision.CollisionComponent;
import engine.ecs.component.IntentComponent;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

@Deprecated
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
