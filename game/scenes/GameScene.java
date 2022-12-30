package game.scenes;

import com.Game;
import com.IdGenerator;
import com.ecs.component.CollisionComponent;
import com.ecs.Entity;
import com.ecs.component.GraphicsComponent;
import com.ecs.component.IntentComponent;
import com.ecs.intent.ExitIntent;
import com.ecs.intent.HoverIntent;
import com.graphics.elements.ToolTip;
import com.graphics.scene.Scene;
import game.components.BuildComponent;
import game.components.GridComponent;
import game.components.SimulationComponent;
import game.components.TooltipComponent;
import game.intent.BuildIntent;
import game.intent.SimulationIntent;
import game.intent.StartIntent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GameScene extends Scene {
    private static final int ITEM_MARGIN = 20;
    private static final int ITEM_WIDTH = 300;
    private static final int ITEM_HEIGHT = 60;
    private static final Color TEXT_COLOR = new Color(20, 20, 20, 255);
    private static final Color BOX_COLOR = new Color(200, 90, 0, 240);
    private static final Color BOX_BORDER_COLOR = new Color(40, 40, 40, 255);
    private static final Color HOVER_COLOR = new Color(40, 40, 40, 150);
    private static final int CELL_SIZE = 128;
    private String description;
    private double goal = 10e-4;
    private int numberOfBuildPanelElements = 0;
    private Entity currentlyBuilding = null;

    public Entity getCurrentlyBuilding() {
        return currentlyBuilding;
    }

    public void setCurrentlyBuilding(Entity entity) {
        currentlyBuilding = entity;
    }

    public GameScene(String name, int id) {
        super(name, id);
        setupBuildPanel();
    }

    public void init() {
        // Create the GUI including buttons going back to menu, exit etc.
        setupDescriptionPanel();
        setupButtons();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGoal(double goal) {
        this.goal = goal;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void update() {

    }

    public void finalizeBuilding() {
        for(Entity e : getEntities()) {
            if(e.getComponent(GridComponent.class) != null) {
                if(e.getComponent(GraphicsComponent.class)
                        .getBounds()
                        .contains(currentlyBuilding
                        .getComponent(GraphicsComponent.class)
                        .getBounds()
                        .getLocation())) {
                    GridComponent gc = new GridComponent();
                    gc.setGridLocation((Point) e.getComponent(GridComponent.class).getGridLocation());
                    currentlyBuilding.addComponent(gc);
                    gc.setEntity(currentlyBuilding);

                    currentlyBuilding.getComponent(GraphicsComponent.class).reposition(e.getComponent(GraphicsComponent.class)
                            .getBounds().getLocation());

                    return;
                }
            }
        }

        currentlyBuilding = null;
    }

    public void addGridElement(int x, int y) {
        Entity gridElement = new Entity("grid_element_" + x + ":" + y, IdGenerator.generateId());
        GraphicsComponent gridElementGraphicsComponent = new GraphicsComponent();
        Rectangle gridElementBounds = new Rectangle(CELL_SIZE * x, CELL_SIZE * y, CELL_SIZE, CELL_SIZE);
        gridElementGraphicsComponent.setBounds(gridElementBounds);
        gridElementGraphicsComponent.setHoverColor(HOVER_COLOR);
        gridElementGraphicsComponent.setImage(Game.res().loadTile(1));
        gridElement.addComponent(gridElementGraphicsComponent);
        gridElementGraphicsComponent.setEntity(gridElement);

        GridComponent gridElementGridComponent = new GridComponent();
        gridElementGridComponent.setGridLocation(new Point(x, y));
        gridElement.addComponent(gridElementGridComponent);
        gridElementGridComponent.setEntity(gridElement);

        CollisionComponent gridElementCollisionComponent = new CollisionComponent();
        gridElementCollisionComponent.setCollisionBox(gridElementBounds);
        gridElement.addComponent(gridElementCollisionComponent);
        gridElementCollisionComponent.setEntity(gridElement);

        IntentComponent gridElementIntentComponent = new IntentComponent();
        HoverIntent gridElementIntentComponentHoverIntent = new HoverIntent();
        gridElementIntentComponentHoverIntent.setIntentComponent(gridElementIntentComponent);
        gridElementIntentComponent.addIntent(gridElementIntentComponentHoverIntent);
        gridElement.addComponent(gridElementIntentComponent);
        gridElementIntentComponent.setEntity(gridElement);

        addEntityToScene(gridElement);
    }

    public void buildElement(int imgId, float failureRatio, boolean interactable) {

    }

    public void addSimulationElement(int x, int y, int imgId, float failureRatio, boolean interactable) {
        Entity simElement = new Entity("simulation_element_" + x + ":" + y, IdGenerator.generateId());

        GraphicsComponent simElementGraphicsComponent = new GraphicsComponent();
        Rectangle simElementBounds = new Rectangle(CELL_SIZE * x, CELL_SIZE * y, CELL_SIZE, CELL_SIZE);
        simElementGraphicsComponent.setBounds(simElementBounds);
        simElementGraphicsComponent.setImage(Game.res().loadTile(imgId));
        simElementGraphicsComponent.setHoverColor(HOVER_COLOR);
        simElement.addComponent(simElementGraphicsComponent);
        simElementGraphicsComponent.setEntity(simElement);

        GridComponent simElementGridComponent = new GridComponent();
        simElementGridComponent.setGridLocation(new Point(x, y));
        simElement.addComponent(simElementGridComponent);
        simElementGridComponent.setEntity(simElement);

        SimulationComponent simElementSimulationComponent = new SimulationComponent();
        simElementSimulationComponent.setFailureRatio(failureRatio);
        simElement.addComponent(simElementSimulationComponent);
        simElementSimulationComponent.setEntity(simElement);

        CollisionComponent simElementCollisionComponent = new CollisionComponent();
        simElementCollisionComponent.setCollisionBox(simElementBounds);
        simElement.addComponent(simElementCollisionComponent);
        simElementCollisionComponent.setEntity(simElement);

        IntentComponent simElementIntentComponent = new IntentComponent();
        simElement.addComponent(simElementIntentComponent);
        simElementIntentComponent.setEntity(simElement);

        HoverIntent simElementIntentComponentHoverIntent = new HoverIntent();
        simElementIntentComponentHoverIntent.setIntentComponent(simElementIntentComponent);
        simElementIntentComponent.addIntent(simElementIntentComponentHoverIntent);

        SimulationIntent simElementIntentComponentSimulationIntent = new SimulationIntent();
        simElementIntentComponentSimulationIntent.setIntentComponent(simElementIntentComponent);
        simElementIntentComponent.addIntent(simElementIntentComponentSimulationIntent);

        TooltipComponent simElementTooltipComponent = new TooltipComponent();
        simElementTooltipComponent.setTooltipText(Game.res().loadDescription(imgId));
        simElementTooltipComponent.setFailureRatio(String.valueOf(failureRatio));
        simElementTooltipComponent.setEntity(simElement);
        simElement.addComponent(simElementTooltipComponent);

        addEntityToScene(simElement);
    }

    public void addToBuildPanel(int imgId, int amount, float failureRatio) {
        Entity buildElement = new Entity("build_element_" + imgId, IdGenerator.generateId());

        GraphicsComponent buildElementGC = new GraphicsComponent();
        Rectangle buildElementBounds = new Rectangle((int) (150 +
                numberOfBuildPanelElements * (CELL_SIZE + ITEM_MARGIN)),
                875,
                (int) (CELL_SIZE),
                (int) (CELL_SIZE)
                );
        buildElementGC.setBounds(buildElementBounds);
        buildElementGC.setImage(Game.res().loadTile(imgId));
        buildElementGC.setHoverColor(HOVER_COLOR);
        ToolTip tt = new ToolTip();
        tt.setFont(buildElementGC.getFont());
        tt.setText(Game.res().loadDescription(imgId));
        buildElementGC.setToolTip(tt);
        buildElementGC.setFont(Game.res().loadFont("game/res/font/joystix monospace.ttf", 18f));
        buildElementGC.addText(String.valueOf(amount));
        buildElementGC.addLocation(new Point((int) buildElementBounds.getX(),
                (int) buildElementBounds.getY() + buildElementBounds.height));

        buildElementGC.setEntity(buildElement);
        buildElement.addComponent(buildElementGC);
        numberOfBuildPanelElements++;

        BuildComponent buildElementBC = new BuildComponent();
        buildElementBC.setAmount(amount);
        buildElementBC.setFailureRatio(failureRatio);
        buildElementBC.setEntity(buildElement);
        buildElement.addComponent(buildElementBC);

        CollisionComponent buildElementCC = new CollisionComponent();
        buildElementCC.setCollisionBox(buildElementBounds);
        buildElementCC.setEntity(buildElement);
        buildElement.addComponent(buildElementCC);

        IntentComponent buildElementIC = new IntentComponent();
        buildElementIC.setEntity(buildElement);
        buildElement.addComponent(buildElementIC);

        HoverIntent buildElementICHI = new HoverIntent();
        buildElementICHI.setIntentComponent(buildElementIC);
        buildElementIC.addIntent(buildElementICHI);

        BuildIntent buildElementICBI = new BuildIntent();
        buildElementICBI.setIntentComponent(buildElementIC);
        buildElementIC.addIntent(buildElementICBI);

        addEntityToScene(buildElement);
    }

    private void setupButtons() {
        // Exit button
        Entity exitButton = new Entity("Exit", IdGenerator.generateId());
        GraphicsComponent exitButtonGraphicsComponent = new GraphicsComponent();
        Rectangle exitButtonBounds = new Rectangle(1600, 900, ITEM_WIDTH, ITEM_HEIGHT);
        exitButtonGraphicsComponent.setBounds(exitButtonBounds);
        exitButtonGraphicsComponent.setShape(exitButtonBounds);
        exitButtonGraphicsComponent.addText("EXIT");
        exitButtonGraphicsComponent.addLocation(exitButtonBounds.getLocation());
        exitButtonGraphicsComponent.setFont(Game.res().loadFont("game/res/font/joystix monospace.ttf", 18f));
        exitButtonGraphicsComponent.setTextColor(TEXT_COLOR);
        exitButtonGraphicsComponent.setBorderColor(BOX_BORDER_COLOR);
        exitButtonGraphicsComponent.setFillColor(BOX_COLOR);
        exitButtonGraphicsComponent.setHoverColor(HOVER_COLOR);

        exitButton.addComponent(exitButtonGraphicsComponent);
        exitButtonGraphicsComponent.setEntity(exitButton);

        CollisionComponent exitButtonCollisionComponent = new CollisionComponent();
        exitButtonCollisionComponent.setCollisionBox(exitButtonBounds);

        exitButton.addComponent(exitButtonCollisionComponent);
        exitButtonCollisionComponent.setEntity(exitButton);

        IntentComponent exitButtonIntentComponent = new IntentComponent();
        HoverIntent hi = new HoverIntent();
        hi.setIntentComponent(exitButtonIntentComponent);
        exitButtonIntentComponent.addIntent(hi);
        ExitIntent ei = new ExitIntent();
        ei.setIntentComponent(exitButtonIntentComponent);
        exitButtonIntentComponent.addIntent(ei);

        exitButtonIntentComponent.setEntity(exitButton);
        exitButton.addComponent(exitButtonIntentComponent);

        addEntityToScene(exitButton);

        // back to menu button
        Entity mainMenuButton = new Entity("Menu_button", IdGenerator.generateId());
        GraphicsComponent mainMenuButtonGraphicsComponent = new GraphicsComponent();
        Rectangle mainMenuButtonBounds = new Rectangle(1600, 800, ITEM_WIDTH, ITEM_HEIGHT);
        mainMenuButtonGraphicsComponent.setBounds(mainMenuButtonBounds);
        mainMenuButtonGraphicsComponent.setShape(mainMenuButtonBounds);
        mainMenuButtonGraphicsComponent.addText("MAIN MENU");
        mainMenuButtonGraphicsComponent.addLocation(mainMenuButtonBounds.getLocation());
        mainMenuButtonGraphicsComponent.setFont(Game.res().loadFont("game/res/font/joystix monospace.ttf", 18f));
        mainMenuButtonGraphicsComponent.setTextColor(TEXT_COLOR);
        mainMenuButtonGraphicsComponent.setBorderColor(BOX_BORDER_COLOR);
        mainMenuButtonGraphicsComponent.setFillColor(BOX_COLOR);
        mainMenuButtonGraphicsComponent.setHoverColor(HOVER_COLOR);

        mainMenuButton.addComponent(mainMenuButtonGraphicsComponent);
        mainMenuButtonGraphicsComponent.setEntity(mainMenuButton);

        CollisionComponent mainMenuButtonCollisionComponent = new CollisionComponent();
        mainMenuButtonCollisionComponent.setCollisionBox(mainMenuButtonBounds);

        mainMenuButton.addComponent(mainMenuButtonCollisionComponent);
        mainMenuButtonCollisionComponent.setEntity(mainMenuButton);

        IntentComponent mainMenuButtonIntentComponent = new IntentComponent();
        HoverIntent mainMenuButtonHoverIntent = new HoverIntent();
        mainMenuButtonHoverIntent.setIntentComponent(mainMenuButtonIntentComponent);
        mainMenuButtonIntentComponent.addIntent(mainMenuButtonHoverIntent);
        StartIntent mainMenuButtonStartIntent = new StartIntent();
        mainMenuButtonStartIntent.setIntentComponent(mainMenuButtonIntentComponent);
        mainMenuButtonIntentComponent.addIntent(mainMenuButtonStartIntent);

        mainMenuButtonIntentComponent.setEntity(mainMenuButton);
        mainMenuButton.addComponent(mainMenuButtonIntentComponent);

        addEntityToScene(mainMenuButton);
    }

    private void setupBuildPanel() {
        Entity buildPanel = new Entity("Build Panel", IdGenerator.generateId());

        GraphicsComponent buildPanelGC = new GraphicsComponent();
        Rectangle buildPanelBounds = new Rectangle(0, 850, 1500, (1080-850));
        buildPanelGC.setBounds(buildPanelBounds);
        try {
            buildPanelGC.setImage(ImageIO.read(new File("game/res/menus/blueprint_scaled.png")));
        } catch (IOException e) {
            Game.logger().severe("Could not load image from file\n" + e.getMessage());
        }
        buildPanel.addComponent(buildPanelGC);
        buildPanelGC.setEntity(buildPanel);

        addEntityToScene(buildPanel);
    }

    private void setupDescriptionPanel() {
        Entity descriptionPanel = new Entity("Description Panel", IdGenerator.generateId());

        GraphicsComponent descriptionPanelGC = new GraphicsComponent();
        Rectangle descriptionPanelBounds = new Rectangle(1500, 0, 420, 1080);
        descriptionPanelGC.setBounds(descriptionPanelBounds);
        try {
            descriptionPanelGC.setImage(ImageIO.read(new File("game/res/menus/box1.png")));
        } catch (IOException e) {
            Game.logger().severe("Could not load image from file\n" + e.getMessage());
        }

        descriptionPanelGC.setTextColor(TEXT_COLOR);
        descriptionPanelGC.setFont(Game.res().loadFont("game/res/font/joystix monospace.ttf", 18f));

        descriptionPanelGC.addText(description);
        descriptionPanelGC.addLocation(descriptionPanelBounds.getLocation());
        descriptionPanelGC.addText(String.valueOf(goal));
        descriptionPanelGC.addLocation(new Point(descriptionPanelBounds.getLocation().x, descriptionPanelBounds.getLocation().y + 600));

        descriptionPanel.addComponent(descriptionPanelGC);
        descriptionPanelGC.setEntity(descriptionPanel);

        addEntityToScene(descriptionPanel);
    }

    public int getCellSize() {
        return CELL_SIZE;
    }
}
