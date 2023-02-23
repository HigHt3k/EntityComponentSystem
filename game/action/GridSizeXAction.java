package game.action;

import engine.Game;
import engine.ecs.component.action.Action;
import game.scenes.BuildScene;
import game.scenes.GridSize;

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
