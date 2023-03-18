package de.unistuttgart.ils.aircraftsystemsarchitect.game.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action.Action;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.game.BuildScene;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.util.GridSize;

public class GridSizeXAction extends Action {
    GridSize i;
    int amount;

    public GridSizeXAction(GridSize i, int amount) {
        this.i = i;
        this.amount = amount;
    }

    @Override
    public void handle() {
        System.out.println("handling action");
        if (i.x > 0) {
            i.x += amount;
            if (Game.scene().current() instanceof BuildScene bs) {
                bs.updateGridSize(0);
            }
        } else if (i.x == 0 && amount > 0) {
            i.x += amount;
            if (Game.scene().current() instanceof BuildScene bs) {
                bs.updateGridSize(0);
            }
        }
    }
}
