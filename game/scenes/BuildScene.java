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
import game.action.SaveAction;
import game.components.BuildComponent;
import game.components.GridComponent;
import game.entities.simulation.BuildPanelEntity;
import game.entities.simulation.GridEntity;
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

    private int xMax = 2;
    private int yMax = 2;

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

    public int getxMax() {
        return xMax;
    }

    public int getyMax() {
        return yMax;
    }

    public BuildScene(String name, int id) {
        super(name, id);
        setupBuildPanel();

        setupDescriptionPanel();
        setupButtons();

        for(int x = 0; x < xMax; x++) {
            for(int y = 0; y < yMax; y++) {
                addGridElement(x, y);
            }
        }
        addToBuildPanel(201, 0, 1e-4f, 0, 0, 0.9f);
        addToBuildPanel(203, 0, 1e-4f, 1, 0, 0.9f);
        addToBuildPanel(205, 0, 1e-4f, 2, 0, 0.9f);
        addToBuildPanel(500, 1000, 1e-25f, 1, 0, 0f);
        addToBuildPanel(501, 1000, 1e-25f, 1, 0, 0f);
        addToBuildPanel(502, 1000, 1e-25f, 1, 0, 0f);
        addToBuildPanel(503, 1000, 1e-25f, 1, 0, 0f);
    }

    public void updateGridSize() {
        if(xMax > yMax && (xMax > 7 || yMax > 5)) {
            CELL_SIZE = DESIGN_CELL_SIZE * 7 / xMax;
        } else if(xMax > 7 || yMax > 5) {
            CELL_SIZE = DESIGN_CELL_SIZE * 5 / yMax;
        } else {
            CELL_SIZE = DESIGN_CELL_SIZE;
        }

        X_MARGIN = (1500 - xMax*CELL_SIZE)/2;
        Y_MARGIN = (850 - yMax*CELL_SIZE)/2;
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
        System.out.println(xMax + " - " + yMax);
        for(Entity e : new ArrayList<>(getEntities())) {
            if(e.getComponent(GridComponent.class) != null) {
                System.out.println(e.getComponent(GridComponent.class).getGridLocation());
                if(e.getComponent(GridComponent.class).getGridLocation().getX() >= xMax) {
                    removeEntityFromScene(e);
                    continue;
                }
                if(e.getComponent(GridComponent.class).getGridLocation().getY() >= yMax) {
                    removeEntityFromScene(e);
                    continue;
                }
            }
        }
    }

    public void updateGridSize(int x, int y) {
        if(x != -1)
            xMax = x;

        if(y != -1)
            yMax = y;

        for(int xx = 0; xx < xMax; xx++) {
            for(int yy = 0; yy < yMax; yy++) {
                boolean isSet = false;
                for(Entity e : getEntities()) {
                    if(e.getComponent(GridComponent.class) != null
                            && e.getComponent(GridComponent.class).getGridLocation().getY() == yy
                            && e.getComponent(GridComponent.class).getGridLocation().getX() == xx) {
                        isSet = true;
                    }
                }
                if(!isSet) {
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

        Font font = Game.res().loadFont("game/res/font/joystix monospace.ttf", 12f);

        if (buildPanelEntity.getComponent(BuildComponent.class).getSimulationType() == SimulationType.CABLE) {
            numberOfBuildPanelElements++;
            return;
        }
        //TODO: Reimplement
        /*NumberSelectorEntity numberSelectorEntity = new NumberSelectorEntity("build_element_" + imgId + "_selector", IdGenerator.generateId(),
                BUILD_PANEL_X_MARGIN + numberOfBuildPanelElements * (BUILD_CELL_SIZE + ITEM_MARGIN) + BUILD_CELL_SIZE/8,
                850 + BUILD_PANEL_X_MARGIN + BUILD_CELL_SIZE * 5/4,
                BUILD_CELL_SIZE/4 * 3, BUILD_CELL_SIZE/4, font, TEXT_COLOR, BOX_COLOR, BOX_BORDER_COLOR
                );
        numberSelectorEntity.setChange(buildPanelEntity);
        addEntityToScene(numberSelectorEntity);

         */

        numberOfBuildPanelElements++;
    }

    /**
     * setup method for buttons in the GameScene
     */
    private void setupButtons() {
        Font font = Game.res().loadFont("game/res/font/joystix monospace.ttf", 18f);
        GenericButton exitButton = new GenericButton(
                "Exit",
                IdGenerator.generateId(),
                1600, 900,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@3",
                font, new ExitAction()
        );
        this.addEntityToScene(exitButton);

        GenericButton mainMenuButton = new GenericButton(
                "Menu_button",
                IdGenerator.generateId(),
                1600, 800,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@4",
                font, new StartAction(-255)
        );

        this.addEntityToScene(mainMenuButton);

        // Save button
        GenericButton saveButton = new GenericButton(
                "Save_button", IdGenerator.generateId(),
                1600, 700,
                ITEM_WIDTH, ITEM_HEIGHT,
                "@5",
                font, new SaveAction()
        );
        this.addEntityToScene(saveButton);
    }

    /**
     * Setup method for the build panel
     */
    private void setupBuildPanel() {
        try {
            ImageEntity buildPanel = new ImageEntity("Build Panel", IdGenerator.generateId(),
                    ImageIO.read(new File("game/res/menus/blueprint_scaled.png")), 0, 850, 1500, 1080 - 850, Layer.UI);
            addEntityToScene(buildPanel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * setup method for the description panel (right side)
     */
    private void setupDescriptionPanel() {
        Font font = Game.res().loadFont("game/res/font/joystix monospace.ttf", 18f);
        GenericButton exitButton = new GenericButton(
                "Exit",
                IdGenerator.generateId(),
                1600, 900,
                ITEM_WIDTH, ITEM_HEIGHT,
                "EXIT",
                font, new ExitAction()
        );

        try {
            ImageEntity descriptionPanel = new ImageEntity("Description Panel", IdGenerator.generateId(),
                    ImageIO.read(new File("game/res/menus/box_new.png")), 1500, 0, 420, 850, Layer.UI);
            addEntityToScene(descriptionPanel);
        } catch (IOException e) {
            e.printStackTrace();
        }


        for(int i = 1; i < 11; i++) {
            String number = "";
            if (i == 1) {
                number = "1";
            }
            if (i == 10) {
                number = "10";
            }
            //TODO: reimplement build size

            /* GenericButton sliderX = new GenericButton(
                    "Slider_X", IdGenerator.generateId(),
                    1580 + i*20, 300,
                    20, 10,
                    number, font,
            );
            sliderX.addIntent(new GridSizeIntent(i, -1));
            addEntityToScene(sliderX);

            GenericButton sliderY = new GenericButton(
                    "Slider_Y", IdGenerator.generateId(),
                    1580 + i*20, 400,
                    20, 10,
                    number, font
            );
            sliderY.addIntent(new GridSizeIntent(-1, i));
            addEntityToScene(sliderY);

             */
        }
    }
}
