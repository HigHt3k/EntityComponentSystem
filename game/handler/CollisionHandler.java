package game.handler;

import com.Game;
import com.ecs.component.CollisionComponent;
import com.ecs.component.object.ColliderObject;
import com.ecs.entity.Entity;
import com.ecs.system.SystemHandle;
import com.input.handler.Handler;
import com.input.handler.HandlerType;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CollisionHandler extends Handler {

    public CollisionHandler() {
        super(HandlerType.EVENT);
    }

    @Override
    public void handle(KeyEvent e) {

    }

    @Override
    public void handle(MouseEvent e) {
        ArrayList<Entity> entities = Game.scene().current().getEntities();

        for(Entity entity : entities) {
            if(entity.getComponent(CollisionComponent.class) != null) {
                for(ColliderObject co : entity.getComponent(CollisionComponent.class).getColliders()) {
                    if(co.getCollisionBox().contains(e.getPoint())) {
                        if(co.getCorrespondingGraphics() != null) {
                            co.getCorrespondingGraphics().setHovered(true);
                        }
                    } else {
                        if(co.getCorrespondingGraphics() != null) {
                            co.getCorrespondingGraphics().setHovered(false);
                        }
                    }
                }
            }
        }
    }
}
