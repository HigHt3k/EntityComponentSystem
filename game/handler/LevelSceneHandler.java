package game.handler;

import com.Game;
import com.ecs.component.IntentComponent;
import com.ecs.component.collision.CollisionComponent;
import com.ecs.entity.Entity;
import com.graphics.scene.Scene;
import com.input.handler.Handler;
import com.input.handler.HandlerType;
import game.components.TooltipComponent;
import game.entities.LevelButton;
import game.intent.StartIntent;
import game.scenes.LevelScene;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.logging.Level;

public class LevelSceneHandler extends Handler {
    public LevelSceneHandler() {
        super(HandlerType.EVENT);
    }

    @Override
    public void handle(KeyEvent e) {

    }

    @Override
    public void handle(MouseEvent e) {
        ArrayList<Entity> entities = (ArrayList<Entity>) Game.scene().current().getEntities().clone();

        if (Game.scene().current() instanceof LevelScene ls) {
            for (Entity entity : entities) {
                if (entity instanceof LevelButton lb) {
                    if (entity.getComponent(CollisionComponent.class).contains(e.getPoint())) {
                        Scene s = lb.getComponent(IntentComponent.class).getIntent(StartIntent.class).getScene();
                        if (s != null) {
                            ls.addHighscores(s.getId());
                            System.out.println("Adding higschore:");
                            return;
                        }
                    }
                }
            }
            ls.removeHighscores();
        }
    }
}
