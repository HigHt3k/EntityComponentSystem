package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.game.BuildScene;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.util.GridSize;

/**
 * Action for changing the x-component of the grid size
 */
public class GridSizeXAction extends Action {
    private final GridSize gridSize;
    private final int amount;

    public GridSizeXAction(GridSize gridSize, int amount) {
        this.gridSize = gridSize;
        this.amount = amount;
    }

    /**
     * Increases/decreases the x-component of the grid size and updates the build scene
     */
    @Override
    public void handle() {
        if (gridSize.x > 0) {
            gridSize.x += amount;
            if (Game.scene().current() instanceof BuildScene bs) {
                bs.updateGridSize(0);
            }
        } else if (gridSize.x == 0 && amount > 0) {
            gridSize.x += amount;
            if (Game.scene().current() instanceof BuildScene bs) {
                bs.updateGridSize(0);
            }
        }
    }
}
