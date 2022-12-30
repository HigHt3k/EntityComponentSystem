package game.intent;

import com.Game;
import com.IdGenerator;
import com.ecs.Entity;
import com.ecs.component.CollisionComponent;
import com.ecs.component.GraphicsComponent;
import com.ecs.intent.Intent;
import game.components.BuildComponent;
import game.components.SimulationComponent;
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
        if(Game.scene().current() instanceof GameScene) {
            GameScene gs = (GameScene) Game.scene().current();
            if (getIntentComponent().getEntity().getComponent(CollisionComponent.class).contains(e.getPoint()) &&
                    e.getButton() == MouseEvent.BUTTON1
                        && !isBuilding
                        && this.getIntentComponent().getEntity().getComponent(BuildComponent.class).getAmount() > 0) {
                // now building; create a new entity that can be dragged around
                System.out.println("Starting to build");

                Entity entity = new Entity(this.getIntentComponent().getEntity().getName() + "_simulation",
                        IdGenerator.generateId());

                GraphicsComponent other = this.getIntentComponent().getEntity().getComponent(GraphicsComponent.class);

                GraphicsComponent gc = new GraphicsComponent();
                gc.setBounds(new Rectangle(
                            other.get_BOUNDS().x,
                            other.get_BOUNDS().y,
                            other.get_BOUNDS().width,
                            other.get_BOUNDS().height
                        )
                );
                gc.setImage(this.getIntentComponent().getEntity().getComponent(GraphicsComponent.class).getImage());

                entity.addComponent(gc);
                gc.setEntity(entity);

                SimulationComponent sc = new SimulationComponent();
                sc.setFailureRatio(this.getIntentComponent().getEntity().getComponent(BuildComponent.class).getFailureRatio());
                entity.addComponent(sc);
                sc.setEntity(entity);

                CollisionComponent cc = new CollisionComponent();
                cc.setCollisionBox(
                        new Rectangle(
                                other.get_BOUNDS().x,
                                other.get_BOUNDS().y,
                                other.get_BOUNDS().width,
                                other.get_BOUNDS().height
                        )
                );
                cc.setEntity(entity);
                entity.addComponent(cc);

                gs.setCurrentlyBuilding(entity);
                gs.addEntityToScene(entity);

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
        }
    }
}
