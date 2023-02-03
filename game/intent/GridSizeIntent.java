package game.intent;

import com.Game;
import com.ecs.component.collision.CollisionComponent;
import com.ecs.intent.Intent;
import game.scenes.BuildScene;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

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
