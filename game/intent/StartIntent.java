package game.intent;

import com.Game;
import com.ecs.component.collision.CollisionComponent;
import com.ecs.component.IntentComponent;
import com.ecs.intent.Intent;
import com.graphics.scene.Scene;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class StartIntent extends Intent {
    private Scene scene;

    public StartIntent() {

    }

    public StartIntent(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void handleIntent(KeyEvent e) {

    }

    @Override
    public void handleIntent(MouseEvent e) {
        IntentComponent ic = getIntentComponent();
        if(ic != null && ic.getEntity().getComponent(CollisionComponent.class) != null) {
            if(ic.getEntity().getComponent(CollisionComponent.class).contains(e.getPoint()) && e.getButton() == MouseEvent.BUTTON1) {
                Game.logger().info("Setting scene to: " + getIntentComponent().getEntity().getName());
                if(scene != null) {
                    Game.scene().addScene(scene);
                    Game.scene().setCurrentScene(scene);
                    Game.scene().current().init();
                } else {
                    Game.scene().setCurrentScene(getIntentComponent().getEntity().getName().replace("_button", ""));
                    Game.scene().current().init();
                }
            }
        }
    }
}
