package de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.test;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.IdGenerator;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.scene.Scene;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.simulation.BuildPanelEntity;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui.NumberChooser;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.SimulationType;

public class TestScene extends Scene {
    public TestScene(String name, int id) {
        super(name, id);

        BuildPanelEntity bp = new BuildPanelEntity("number", IdGenerator.generateId(),
                300, 300, 100, 100, Game.res().loadTile(205), 205, 5, 1e-3f, SimulationType.SENSOR,
                0, 0, "Random test object", 0.9f);
        addEntityToScene(bp);

        NumberChooser nc = new NumberChooser("number", IdGenerator.generateId(),
                "-", 300, 400, 33, 33, -1, bp
        );
        addEntityToScene(nc);

        NumberChooser nc2 = new NumberChooser("number", IdGenerator.generateId(),
                "+", 367, 400, 33, 33, 1, bp
        );
        addEntityToScene(nc2);

    }

    @Override
    public void init() {

    }
}
