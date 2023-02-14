package game.intent;

import engine.Game;
import engine.ecs.component.collision.CollisionComponent;
import engine.ecs.component.IntentComponent;
import engine.ecs.intent.Intent;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class SaveIntent extends Intent {
    @Override
    public void handleIntent(KeyEvent e) {

    }

    @Override
    public void handleIntent(MouseEvent e) {
        IntentComponent ic = getIntentComponent();
        if(ic != null && ic.getEntity().getComponent(CollisionComponent.class) != null) {
            if(ic.getEntity().getComponent(CollisionComponent.class).contains(e.getPoint()) && e.getButton() == MouseEvent.BUTTON1) {
                Game.res().saveLevel(Game.scene().current());
            }
        }
    }
}
