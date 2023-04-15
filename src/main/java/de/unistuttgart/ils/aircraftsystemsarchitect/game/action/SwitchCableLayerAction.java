package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.ImageObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.builder.BuildHandler;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class SwitchCableLayerAction extends Action {

    /**
     * Changes the current cable layer in the build mode and updates the corresponding image to reflect the change.
     */
    @Override
    public void handle() {
        BuildHandler buildHandler = Objects.requireNonNull(Game.input().getHandler(BuildHandler.class));
        if (buildHandler.getCurrentCableLayer() == 3) {
            buildHandler.setCurrentCableLayer(0);
        } else {
            buildHandler.setCurrentCableLayer(buildHandler.getCurrentCableLayer() + 1);
        }
        try {
            updateImage(buildHandler.getCurrentCableLayer());
        } catch (IOException e) {
            // todo Handle
        }
    }

    /**
     * Updates the image for the cable switch based on the current cable layer.
     *
     * @param layer The current cable layer.
     * @throws IOException If an I/O error occurs.
     */
    private void updateImage(int layer) throws IOException {
        int layerImg = layer + 1;
        Game.scene().current().getEntityByName("cable_switch")
                .getComponent(RenderComponent.class).getRenderObjectsOfType(ImageObject.class).get(0)
                .setImage(ImageIO.read(new File("res/menus/cable_switch_" + layerImg + ".png")));
    }
}
