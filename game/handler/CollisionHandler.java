package game.handler;

import engine.Game;
import engine.ecs.component.collision.CollisionComponent;
import engine.ecs.component.collision.ColliderObject;
import engine.ecs.entity.Entity;
import engine.ecs.entity.NumberSelectorEntity;
import engine.input.handler.Handler;
import engine.input.handler.HandlerType;
import game.components.BuildComponent;

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

                        if(entity instanceof NumberSelectorEntity numberSelectorEntity) {
                            if(e.getButton() == MouseEvent.BUTTON1) {
                                if(co == numberSelectorEntity.getLeft()) {
                                    numberSelectorEntity.getChange().getComponent(BuildComponent.class).subtractFromAmount();
                                } else if(co == numberSelectorEntity.getRight()) {
                                    numberSelectorEntity.getChange().getComponent(BuildComponent.class).addToAmount();
                                }
                            }
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
