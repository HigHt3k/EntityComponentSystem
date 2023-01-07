package game.intent;

import com.Game;
import com.IdGenerator;
import com.ecs.Entity;
import com.ecs.component.CollisionComponent;
import com.ecs.component.GraphicsComponent;
import com.ecs.intent.Intent;
import game.components.BuildComponent;
import game.components.SimulationComponent;
import game.entities.CableEntity;
import game.entities.SimulationEntity;
import game.scenes.GameScene;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * this intent is used to build new entities.
 */
public class BuildIntent extends Intent {
    private boolean isBuilding = false;

    /**
     * handle the intent based on given key events
     * @param e
     */
    @Override
    public void handleIntent(KeyEvent e) {

    }

    /**
     * handle the intent based on given mouse events
     * @param e
     */
    @Override
    public void handleIntent(MouseEvent e) {
        // check if current scene is a GameScene instance & handle the build panel
        if(Game.scene().current() instanceof GameScene gs
                && this.getIntentComponent().getEntity().getComponent(BuildComponent.class) != null) {
            if (getIntentComponent().getEntity().getComponent(CollisionComponent.class).contains(e.getPoint())
                    && e.getButton() == MouseEvent.BUTTON1
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

        // Handle removed items from grid
        } else if (Game.scene().current() instanceof GameScene
                && e.getButton() == MouseEvent.BUTTON3
                && getIntentComponent().getEntity().getComponent(CollisionComponent.class).contains(e.getPoint())
                && getIntentComponent().getEntity().isRemovable()
        ) {
            Game.logger().info("Removing from grid: " + getIntentComponent().getEntity().getName());
            // delete object if right clicked but put back to the stack for building; only if component is interactable
            GameScene gs = (GameScene) Game.scene().current();
            gs.removeComponent(this.getIntentComponent().getEntity());
        // handle cable build
        } else if (!isBuilding
                && Game.scene().current() instanceof GameScene gs
                && e.getButton() == MouseEvent.BUTTON1
                && getIntentComponent().getEntity().getComponent(CollisionComponent.class).contains(e.getPoint())
                && gs.getCurrentlyBuilding() == null
        ) {
            CableEntity newCable = new CableEntity(
                    "cable", IdGenerator.generateId(),
                    e.getPoint().x,
                    e.getPoint().y,
                    this.getIntentComponent().getEntity().getComponent(GraphicsComponent.class).get_BOUNDS().width,
                    this.getIntentComponent().getEntity().getComponent(GraphicsComponent.class).get_BOUNDS().height,
                    -1, -1,
                    this.getIntentComponent().getEntity(), null
            );
            gs.setCurrentlyBuilding(newCable);
            gs.addEntityToScene(newCable);
            isBuilding = true;
        } else if(isBuilding
                && Game.scene().current() instanceof GameScene gs
                && gs.getCurrentlyBuilding() instanceof CableEntity
                && e.getButton() == MouseEvent.NOBUTTON
        ) {
            gs.getCurrentlyBuilding().getComponent(GraphicsComponent.class).setLine(gs.getCurrentlyBuilding().getComponent(GraphicsComponent.class).get_LINESTART(), e.getPoint());
        } else if(isBuilding
                && Game.scene().current() instanceof GameScene gs
                && gs.getCurrentlyBuilding() instanceof CableEntity
                && e.getButton() == MouseEvent.BUTTON1
        ) {
            System.out.println("trying to finalize");
            if(gs.finalizeBuilding(e.getPoint())) {
                isBuilding = false;
            }
        } else if(isBuilding
                && Game.scene().current() instanceof GameScene gs
                && gs.getCurrentlyBuilding() instanceof CableEntity
                && e.getButton() == MouseEvent.BUTTON3
        ) {
            Game.logger().info("Removing from grid: " + getIntentComponent().getEntity().getName());
            // delete object if right clicked but put back to the stack for building; only if component is interactable
            gs.removeComponent(gs.getCurrentlyBuilding());
        }
    }
}
