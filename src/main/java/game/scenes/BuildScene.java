package game.scenes;

import engine.Game;
import engine.IdGenerator;
import engine.ecs.component.action.ExitAction;
import engine.ecs.component.action.StartAction;
import engine.ecs.component.collision.ColliderComponent;
import engine.ecs.component.collision.CollisionObject;
import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.Layer;
import engine.ecs.component.graphics.objects.RenderObject;
import engine.ecs.entity.Entity;
import engine.ecs.entity.GenericButton;
import engine.ecs.entity.ImageEntity;
import engine.graphics.scene.Scene;
import engine.resource.ResourceManager;
import engine.resource.colorpalettes.Bit8;
import engine.resource.colorpalettes.ColorPalette;
import engine.resource.fonts.FontCollection;
import game.action.SaveAction;
import game.components.BuildComponent;
import game.components.GridComponent;
import game.entities.simulation.BuildPanelEntity;
import game.entities.simulation.GridEntity;
import game.entities.ui.*;
import game.handler.simulation.SimulationType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BuildScene extends Scene {
    private final int ITEM_MARGIN = 20;
    private final int ITEM_WIDTH = 300;
    private final int ITEM_HEIGHT = 60;
    private int X_MARGIN = 200;
    private int Y_MARGIN = 300;
    private final Color TEXT_COLOR = new Color(20, 20, 20, 255);
    private final Color BOX_COLOR = new Color(200, 90, 0, 240);
    private final Color BOX_BORDER_COLOR = new Color(40, 40, 40, 255);
    private final Color HOVER_COLOR = new Color(40, 40, 40, 150);
    private final int DESIGN_CELL_SIZE = 100;
    private int CELL_SIZE = DESIGN_CELL_SIZE;
    private final int BUILD_CELL_SIZE = DESIGN_CELL_SIZE;
    private String description;
    private final double goal = 10e-4;
    private int numberOfBuildPanelElements = 0;

    private final int BUILD_PANEL_X_MARGIN = 25;

    public GridSize gridSize = new GridSize(2, 2);

    private int accGoal;
    private int sensGoal;
    private int cGoal;

    public int getAccGoal() {
        return accGoal;
    }

    public int getcGoal() {
        return cGoal;
    }

    public int getSensGoal() {
        return sensGoal;
    }

    public BuildScene(String name, int id) {
        super(name, id);
        setupBuildPanel();

        setupDescriptionPanel();
        setupButtons();

        for (int x = 0; x < gridSize.x; x++) {
            for (int y = 0; y < gridSize.y; y++) {
                addGridElement(x, y);
            }
        }
        addToBuildPanel(201, 0, 1e-4f, 0, 0, 0.9f);
        addToBuildPanel(203, 0, 1e-4f, 1, 0, 0.9f);
        addToBuildPanel(205, 0, 1e-4f, 2, 0, 0.9f);
        addToBuildPanel(500, 1000, 0, 1, 0, 0f);
        addToBuildPanel(501, 1000, 0, 1, 0, 0f);
        addToBuildPanel(502, 1000, 0, 1, 0, 0f);
        addToBuildPanel(503, 1000, 0, 1, 0, 0f);
    }

    public int getxMax() {
        return gridSize.x;
    }

    public int getyMax() {
        return gridSize.y;
    }

    public void updateGridSize() {
        if (gridSize.x > gridSize.y && (gridSize.x > 7 || gridSize.y > 5)) {
            CELL_SIZE = DESIGN_CELL_SIZE * 7 / gridSize.x;
        } else if (gridSize.x > 7 || gridSize.y > 5) {
            CELL_SIZE = DESIGN_CELL_SIZE * 5 / gridSize.y;
        } else {
            CELL_SIZE = DESIGN_CELL_SIZE;
        }

        X_MARGIN = (1500 - gridSize.x * CELL_SIZE) / 2;
        Y_MARGIN = (850 - gridSize.y * CELL_SIZE) / 2;
    }

    public String getDescription() {
        return description;
    }

    public double getGoal() {
        return goal;
    }

    @Override
    public void init() {
        updateGridSize();
    }

    @Override
    public void update() {
        for(Entity e : getEntities()) {
            e.update();
        }
    }

    /**
     * add an Entity that snaps to the grid. This method can e.g. be called from the {@link ResourceManager} to parse
     * the level.XML file to the actual {@link GameScene}.
     * A {@link GridComponent} is added to the Entity to store the grid position data, which can be called to validate entities against
     * each other without having to calculate their grid position based on the actual screen position.
     * @param x: x position in the grid
     * @param y: y position in the grid
     */
    public void addGridElement(int x, int y) {
        GridEntity gridEntity = new GridEntity("grid_element_" + x + ":" + y, IdGenerator.generateId(),
                X_MARGIN + CELL_SIZE * x, Y_MARGIN + CELL_SIZE * y, CELL_SIZE, CELL_SIZE,
                x, y,
                Game.res().loadTile(1)
        );
        addEntityToScene(gridEntity);
    }

    public void removeAlLGridElementsOutside() {
        System.out.println(gridSize.x + " - " + gridSize.y);
        for(Entity e : new ArrayList<>(getEntities())) {
            if(e.getComponent(GridComponent.class) != null) {
                System.out.println(e.getComponent(GridComponent.class).getGridLocation());
                if (e.getComponent(GridComponent.class).getGridLocation().getX() >= gridSize.x) {
                    removeEntityFromScene(e);
                    continue;
                }
                if (e.getComponent(GridComponent.class).getGridLocation().getY() >= gridSize.y) {
                    removeEntityFromScene(e);
                    continue;
                }
            }
        }
    }

    public void updateGridSize(int some) {
        for (int xx = 0; xx < gridSize.x; xx++) {
            for (int yy = 0; yy < gridSize.y; yy++) {
                boolean isSet = false;
                for (Entity e : getEntities()) {
                    if (e.getComponent(GridComponent.class) != null
                            && e.getComponent(GridComponent.class).getGridLocation().getY() == yy
                            && e.getComponent(GridComponent.class).getGridLocation().getX() == xx) {
                        isSet = true;
                    }
                }
                if (!isSet) {
                    addGridElement(xx, yy);
                }
            }
        }

        removeAlLGridElementsOutside();
        updateGridSize();
        updateEntitySize();
    }

    private void updateEntitySize() {
        for(Entity e : getEntities()) {
            if (e.getComponent(RenderComponent.class) == null) {
                continue;
            }

            if (e.getComponent(GridComponent.class) == null) {
                continue;
            }
            int x = (int) e.getComponent(GridComponent.class).getGridLocation().getX();
            int y = (int) e.getComponent(GridComponent.class).getGridLocation().getY();
            Rectangle bounds = new Rectangle(X_MARGIN + CELL_SIZE * x, Y_MARGIN + CELL_SIZE * y, CELL_SIZE, CELL_SIZE);
            for (RenderObject r : e.getComponent(RenderComponent.class).getRenderObjects()) {
                r.setLocation(bounds.getLocation());
                r.setBounds(bounds);
            }
            for (CollisionObject c : e.getComponent(ColliderComponent.class).getCollisionObjects()) {
                c.setCollisionBoundaries(bounds);
            }
        }
    }

    public int getCellSize() {
        return CELL_SIZE;
    }

    /**
     * add an Entity to the buildpanel
     * @param imgId: the entity/img id
     * @param amount: how many times can this be built?
     * @param failureRatio: failure ratio of the entity
     */
    public void addToBuildPanel(int imgId, int amount, float failureRatio, int correctSignalsNeeded, int outOfControlSignalsAccepted, float failureDetectionRatio) {


        BuildPanelEntity buildPanelEntity = new BuildPanelEntity("build_element_" + imgId, IdGenerator.generateId(),
                BUILD_PANEL_X_MARGIN + numberOfBuildPanelElements * (BUILD_CELL_SIZE + ITEM_MARGIN),
                850 + BUILD_PANEL_X_MARGIN, BUILD_CELL_SIZE, BUILD_CELL_SIZE,
                Game.res().loadTile(imgId), imgId,
                amount, failureRatio,
                Game.res().getTileSet().getType(imgId),
                correctSignalsNeeded, outOfControlSignalsAccepted,
                Game.res().loadDescription(imgId), failureDetectionRatio
        );
        buildPanelEntity.getComponent(BuildComponent.class).setPortId(imgId - 500);
        addEntityToScene(buildPanelEntity);

        Font font = Game.res().loadFont("res/font/joystix monospace.ttf", 12f);

        if (buildPanelEntity.getComponent(BuildComponent.class).getSimulationType() == SimulationType.CABLE) {
            numberOfBuildPanelElements++;
            return;
        }

        NumberChooser nc = null;
        try {
            nc = new NumberChooser("number", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/minus.png")),
                    BUILD_PANEL_X_MARGIN + numberOfBuildPanelElements * (BUILD_CELL_SIZE + ITEM_MARGIN),
                    850 + BUILD_PANEL_X_MARGIN + BUILD_CELL_SIZE + 10,
                    25, 25, -1, buildPanelEntity
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        addEntityToScene(nc);
        NumberChooser nc2 = null;
        try {
            nc2 = new NumberChooser("number", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/plus.png")),
                    BUILD_PANEL_X_MARGIN + numberOfBuildPanelElements * (BUILD_CELL_SIZE + ITEM_MARGIN) + BUILD_CELL_SIZE - 25,
                    850 + BUILD_PANEL_X_MARGIN + BUILD_CELL_SIZE + 10,
                    25, 25, 1, buildPanelEntity
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        addEntityToScene(nc2);

        numberOfBuildPanelElements++;
    }

    /**
     * setup method for buttons in the GameScene
     */
    private void setupButtons() {
        GenericButton exitButton = new GenericButton(
                "Exit",
                IdGenerator.generateId(),
                1600, 850 + ITEM_HEIGHT + ITEM_MARGIN * 2,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@3",
                FontCollection.bit8FontHuge, new ExitAction(),
                Bit8.CHROME, null, null
        );
        this.addEntityToScene(exitButton);

        GenericButton mainMenuButton = new GenericButton(
                "Menu_button",
                IdGenerator.generateId(),
                1600, 850 + ITEM_MARGIN,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@4",
                FontCollection.bit8FontHuge, new StartAction(Game.scene().getScene(-255)),
                Bit8.CHROME, null, null
        );
        this.addEntityToScene(mainMenuButton);

        // Save button
        GenericButton saveButton = new GenericButton(
                "Save_button", IdGenerator.generateId(),
                1600, 850 - ITEM_HEIGHT,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@5",
                FontCollection.bit8FontHuge, new SaveAction(),
                Bit8.CHROME, null, null
        );
        this.addEntityToScene(saveButton);
    }

    /**
     * Setup method for the build panel
     */
    private void setupBuildPanel() {
        try {
            ImageEntity buildPanel = new ImageEntity("Build Panel", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/blueprint_scaled.png")), 0, 850, 1500, 1080 - 850, Layer.UI);
            addEntityToScene(buildPanel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * setup method for the description panel (right side)
     */
    private void setupDescriptionPanel() {
        Entity desc = new SimplePanel("desc", IdGenerator.generateId(),
                1500, 0, 402, 350, ColorPalette.setAlpha(Bit8.GREY, 100), Bit8.TRANSPARENT, Bit8.CHROME);
        addEntityToScene(desc);

        Entity goal = new SimplePanel("goal", IdGenerator.generateId(),
                1500, 350, 402, 200, ColorPalette.setAlpha(Bit8.GREY, 100), Bit8.TRANSPARENT, Bit8.CHROME);
        addEntityToScene(goal);

        Entity tips = new SimplePanel("tips", IdGenerator.generateId(),
                1500, 550, 402, 300, ColorPalette.setAlpha(Bit8.GREY, 100), Bit8.TRANSPARENT, Bit8.CHROME);
        addEntityToScene(tips);

        Entity xSize = new TextBody("xSize", IdGenerator.generateId(),
                1500, 50, 202, 50, FontCollection.bit8FontMedium, Bit8.CHROME, "@56");
        addEntityToScene(xSize);

        NumberChooserX minusGridSizeX = null;
        try {
            minusGridSizeX = new NumberChooserX(
                    "minus", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/minus.png")),
                    1702, 50, 50, 50, -1, gridSize
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        addEntityToScene(minusGridSizeX);
        NumberChooserX plusGridSizeX = null;
        try {
            plusGridSizeX = new NumberChooserX(
                    "plus", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/plus.png")),
                    1752, 50, 50, 50, +1, gridSize
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        addEntityToScene(plusGridSizeX);

        Entity ySize = new TextBody("ySize", IdGenerator.generateId(),
                1500, 150, 202, 50, FontCollection.bit8FontMedium, Bit8.CHROME, "@57");
        addEntityToScene(ySize);

        NumberChooserY minusGridSizeY = null;
        try {
            minusGridSizeY = new NumberChooserY(
                    "minus", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/minus.png")),
                    1702, 150, 50, 50, -1, gridSize
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        addEntityToScene(minusGridSizeY);
        NumberChooserY plusGridSizeY = null;
        try {
            plusGridSizeY = new NumberChooserY(
                    "plus", IdGenerator.generateId(),
                    ImageIO.read(new File("res/menus/gui/plus.png")),
                    1752, 150, 50, 50, +1, gridSize
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        addEntityToScene(plusGridSizeY);


    }
}
