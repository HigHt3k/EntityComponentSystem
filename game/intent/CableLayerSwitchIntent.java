package game.intent;

import engine.Game;
import engine.ecs.component.collision.CollisionComponent;
import engine.ecs.component.IntentComponent;
import engine.ecs.intent.Intent;
import game.handler.BuildHandler;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

@Deprecated
public class CableLayerSwitchIntent extends Intent {
    private int layer;

    @Override
    public void handleIntent(KeyEvent e) {
        if(e.getKeyCode() == 48 + layer) {
            Game.input().getHandler(BuildHandler.class).setCurrentCableLayer(layer);
        }
    }

    @Override
    public void handleIntent(MouseEvent e) {
        IntentComponent ic = getIntentComponent();
        if(ic != null && ic.getEntity().getComponent(CollisionComponent.class) != null) {
            if (ic.getEntity().getComponent(CollisionComponent.class).contains(e.getPoint()) && e.getButton() == MouseEvent.BUTTON1) {
                Game.input().getHandler(BuildHandler.class).setCurrentCableLayer(layer);
            }
        }
    }

    public CableLayerSwitchIntent(int layer) {
        this.layer = layer;
    }
}
