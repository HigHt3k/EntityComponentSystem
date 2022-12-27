package game.intent;

import com.Game;
import com.IdGenerator;
import com.ecs.Entity;
import com.ecs.component.CollisionComponent;
import com.ecs.component.GraphicsComponent;
import com.ecs.component.IntentComponent;
import com.ecs.intent.HoverIntent;
import com.ecs.intent.Intent;
import game.components.GridComponent;
import game.components.SimulationComponent;
import game.components.TooltipComponent;
import game.scenes.GameScene;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class BuildIntent extends Intent {
    private boolean isBuilding = false;

    @Override
    public void handleIntent(KeyEvent e) {

    }

    @Override
    public void handleIntent(MouseEvent e) {
        if(getIntentComponent().getEntity().getComponent(CollisionComponent.class).contains(e.getPoint())) {
            if(e.getButton() == MouseEvent.BUTTON1) {
                isBuilding = true;
            }


        }
    }
}
