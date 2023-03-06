package game.action;

import engine.Game;
import engine.ecs.component.action.Action;
import game.scenes.game.BuildScene;
import game.scenes.util.GridSize;

public class GridSizeYAction extends Action {
    GridSize i;
    int amount;

    public GridSizeYAction(GridSize i, int amount) {
        this.i = i;
        this.amount = amount;
    }

    @Override
    public void handle() {
        System.out.println("handling action");
        if (i.y > 0) {
            i.y += amount;
            if (Game.scene().current() instanceof BuildScene bs) {
                bs.updateGridSize(0);
            }
        } else if (i.y == 0 && amount > 0) {
            i.y += amount;
            if (Game.scene().current() instanceof BuildScene bs) {
                bs.updateGridSize(0);
            }
        }
    }
}
