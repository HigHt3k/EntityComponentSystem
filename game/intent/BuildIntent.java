package game.intent;

import com.Game;
import com.IdGenerator;
import com.ecs.Entity;
import com.ecs.component.CollisionComponent;
import com.ecs.component.GraphicsComponent;
import com.ecs.intent.Intent;
import game.components.BuildComponent;
import game.components.SimulationComponent;
import game.entities.SimulationEntity;
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
        if(Game.scene().current() instanceof GameScene && this.getIntentComponent().getEntity().getComponent(BuildComponent.class) != null) {
            GameScene gs = (GameScene) Game.scene().current();
            if (getIntentComponent().getEntity().getComponent(CollisionComponent.class).contains(e.getPoint()) &&
                    e.getButton() == MouseEvent.BUTTON1
                        && !isBuilding
                        && this.getIntentComponent().getEntity().getComponent(BuildComponent.class).getAmount() > 0) {
                // now building; create a new entity that can be dragged around

                SimulationEntity newEntity = new SimulationEntity(
                        this.getIntentComponent().getEntity().getName() + "_simulation", IdGenerator.generateId(),
                        this.getIntentComponent().getEntity().getComponent(GraphicsComponent.class).get_BOUNDS().x,
                        this.getIntentComponent().getEntity().getComponent(GraphicsComponent.class).get_BOUNDS().y,
                        this.getIntentComponent().getEntity().getComponent(GraphicsComponent.class).get_BOUNDS().width,
                        this.getIntentComponent().getEntity().getComponent(GraphicsComponent.class).get_BOUNDS().height,
                        -1, -1,
                        this.getIntentComponent().getEntity().getComponent(GraphicsComponent.class).getImage(),
                        this.getIntentComponent().getEntity().getComponent(BuildComponent.class).getFailureRatio(),
                        this.getIntentComponent().getEntity().getComponent(BuildComponent.class).getSimulationType(),
                        this.getIntentComponent().getEntity().getComponent(BuildComponent.class).getAmount(),
                        true
                );

                gs.setCurrentlyBuilding(newEntity);
                gs.addEntityToScene(newEntity);

                this.getIntentComponent().getEntity().getComponent(BuildComponent.class).subtractFromAmount();
                this.getIntentComponent()
                        .getEntity()
                        .getComponent(GraphicsComponent.class)
                        .getTexts()
                        .set(0,
                                String.valueOf(this.getIntentComponent()
                                        .getEntity()
                                        .getComponent(BuildComponent.class)
                                        .getAmount()
                                )
                        );
                isBuilding = true;
            }

            else if (isBuilding && (e.getButton() == MouseEvent.BUTTON3)) {
                System.out.println("Resetting build");
                gs.removeEntityFromScene(gs.getCurrentlyBuilding());
                gs.setCurrentlyBuilding(null);
                this.getIntentComponent().getEntity().getComponent(BuildComponent.class).addToAmount();

                this.getIntentComponent()
                        .getEntity()
                        .getComponent(GraphicsComponent.class)
                        .getTexts()
                        .set(0,
                                String.valueOf(this.getIntentComponent()
                                        .getEntity()
                                        .getComponent(BuildComponent.class)
                                        .getAmount()
                                )
                        );

                isBuilding = false;
            }

            else if(isBuilding && e.getButton() == MouseEvent.BUTTON1) {
                // Finalize building
                if(gs.finalizeBuilding(e.getPoint()))
                    isBuilding = false;
            }

            else if (isBuilding) {
                gs.getCurrentlyBuilding().getComponent(GraphicsComponent.class).reposition(e.getPoint());
            }

        } else if (Game.scene().current() instanceof GameScene
                && e.getButton() == MouseEvent.BUTTON3
                && getIntentComponent().getEntity().getComponent(CollisionComponent.class).contains(e.getPoint())
                && getIntentComponent().getEntity().isRemovable()
        ) {
            Game.logger().info("Removing from grid: " + getIntentComponent().getEntity().getName());
            // delete object if right clicked but put back to the stack for building; only if component is interactable
            GameScene gs = (GameScene) Game.scene().current();
            gs.removeComponent(this.getIntentComponent().getEntity());
        }
    }
}
