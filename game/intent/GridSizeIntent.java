package game.intent;

import engine.Game;
import engine.ecs.component.collision.CollisionComponent;
import engine.ecs.intent.Intent;
import game.scenes.BuildScene;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

@Deprecated
public class GridSizeIntent extends Intent {
    private int x;
    private int y;

    public GridSizeIntent(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void handleIntent(KeyEvent e) {

    }

    @Override
    public void handleIntent(MouseEvent e) {
        if(this.getIntentComponent().getEntity().getComponent(CollisionComponent.class).getCollisionBox().contains(e.getPoint())
                && e.getButton() == MouseEvent.BUTTON1) {
            ((BuildScene) Game.scene().current()).updateGridSize(x, y);
        }
    }
}
