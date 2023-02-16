package game.handler;

import engine.Game;
import engine.ecs.entity.Entity;
import engine.input.handler.Handler;
import engine.input.handler.HandlerType;
import game.entities.LevelButton;
import game.scenes.LevelScene;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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

                        // TODO: reimplement highscore view
                        /* Scene s = lb.getComponent(ActionComponent.class).getActions().get(MouseEvent.BUTTON1).
                        if (s != null) {
                            ls.addHighscores(s.getId());
                            ls.showLevelInfo(s.getId());
                            System.out.println("Adding higschore:");
                            return;
                        }

                         */

                }
            }
            ls.removeHighscores();
            ls.removeLevelInfo();
        }
    }
}
