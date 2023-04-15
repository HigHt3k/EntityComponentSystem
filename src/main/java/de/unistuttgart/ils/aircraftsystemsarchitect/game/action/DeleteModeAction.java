package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.ImageObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.builder.BuildHandler;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.builder.BuilderState;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * Action to toggle the build mode between removing and not building
 */
public class DeleteModeAction extends Action {

    /**
     * Handles toggling of build mode between removing and not building
     */
    @Override
    public void handle() {
        BuildHandler buildHandler = Objects.requireNonNull(Game.input().getHandler(BuildHandler.class));

        if(buildHandler.getCurrentBuildState() == BuilderState.REMOVING) {
            buildHandler.setCurrentBuildState(BuilderState.NOT_BUILDING);
            try {
                updateImage(false);
            } catch (IOException e) {
                Game.logger().severe("Can't update bulldozer Image");
            }
        }
        else if(buildHandler.getCurrentBuildState() == BuilderState.NOT_BUILDING) {
            buildHandler.setCurrentBuildState(BuilderState.REMOVING);
            try {
                updateImage(true);
            } catch (IOException e) {
                Game.logger().severe("Can't update bulldozer Image");
            }
        }
    }

    /**
     * Updates the image of the bulldozer entity based on whether the build mode is active or not
     *
     * @param active whether the build mode is active or not
     * @throws IOException if there is an error in reading the image file
     */
    private void updateImage(boolean active) throws IOException {
        if(active) {
            Game.scene()
                    .current()
                    .getEntityByName("bulldozer")
                    .getComponent(RenderComponent.class)
                    .getRenderObjectsOfType(ImageObject.class)
                    .get(0)
                    .setImage(ImageIO.read(new File("res/menus/bulldozer_active.png")));
        } else {
            Game.scene()
                    .current()
                    .getEntityByName("bulldozer")
                    .getComponent(RenderComponent.class)
                    .getRenderObjectsOfType(ImageObject.class)
                    .get(0)
                    .setImage(ImageIO.read(new File("res/menus/bulldozer.png")));
        }
    }
}
