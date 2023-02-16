package game.intent;

import engine.Game;
import engine.ecs.component.CursorComponent;
import engine.ecs.component.collision.CollisionComponent;
import engine.ecs.component.IntentComponent;
import engine.ecs.intent.Intent;
import engine.graphics.scene.Scene;
import game.entities.CursorEntity;
import game.handler.CursorSelectorHandler;
import game.scenes.GameScene;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.security.Key;

@Deprecated
public class StartIntent extends Intent {
    private Scene scene;

    public StartIntent() {

    }

    public Scene getScene() {
        return scene;
    }

    public StartIntent(Scene scene) {
        this.scene = scene;
    }

    @Override
    public void handleIntent(KeyEvent e) {
        if(Game.input().getHandler(CursorSelectorHandler.class) != null
                && Game.input().getHandler(CursorSelectorHandler.class).getCursor() != null) {
            CursorEntity cursor = Game.input().getHandler(CursorSelectorHandler.class).getCursor();
            if(cursor.getComponent(CursorComponent.class) == null || cursor.getComponent(CursorComponent.class).getSelected() != getIntentComponent().getEntity()) {
                return;
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                IntentComponent ic = getIntentComponent();
                if (ic != null && ic.getEntity().getComponent(CollisionComponent.class) != null) {
                    Game.logger().info("Setting scene to: " + getIntentComponent().getEntity().getName());
                    if (scene != null) {
                        if (scene instanceof GameScene gs) {
                            if (!gs.isUnlocked()) {
                                return;
                            }
                        }
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

    @Override
    public void handleIntent(MouseEvent e) {
        IntentComponent ic = getIntentComponent();
        if(ic != null && ic.getEntity().getComponent(CollisionComponent.class) != null) {
            if(ic.getEntity().getComponent(CollisionComponent.class).contains(e.getPoint()) && e.getButton() == MouseEvent.BUTTON1) {
                Game.logger().info("Setting scene to: " + getIntentComponent().getEntity().getName());
                if(scene != null) {
                    if(scene instanceof GameScene gs) {
                        if(!gs.isUnlocked()) {
                            return;
                        }
                    }
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
