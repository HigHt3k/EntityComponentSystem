package game.handler.builder;

import engine.Game;
import engine.IdGenerator;
import engine.ecs.Query;
import engine.ecs.component.collision.ColliderComponent;
import engine.ecs.component.graphics.RenderComponent;
import engine.ecs.component.graphics.objects.ImageObject;
import engine.ecs.component.graphics.objects.TextObject;
import engine.ecs.entity.Entity;
import engine.input.handler.Handler;
import engine.input.handler.HandlerType;
import game.components.*;
import game.entities.simulation.BuildPanelEntity;
import game.entities.cable.CablePort;
import game.entities.cable.CablePortPosition;
import game.entities.simulation.SimulationEntity;
import game.handler.simulation.SimulationState;
import game.handler.simulation.SimulationType;
import game.handler.simulation.markov.MarkovProcessor;
import game.scenes.BuildScene;
import game.scenes.GameScene;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildHandler extends Handler {
    private BuilderState currentBuildState = BuilderState.NOT_BUILDING;
    private Entity currentBuilding = null;
    private int currentCableLayer = 0;
    private Entity cableBuildRepetitive = null;

    private int cellSize;

    public BuildHandler() {
        super(HandlerType.EVENT);
    }

    @Override
    public void handle(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_D) {
            printAllEntitiesGridPosition();
            return;
        }
        if(e.getKeyCode() == KeyEvent.VK_O) {
            printAllCablePorts();
            return;
        }
        if(e.getKeyCode() == KeyEvent.VK_P) {
            printAllSimComponents();
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_M) {
            MarkovProcessor.printMarkov();
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_Q) {
            MarkovProcessor.printCurrentSystemState();
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_1) {
            currentCableLayer = 0;
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_2) {
            currentCableLayer = 1;
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_3) {
            currentCableLayer = 2;
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_4) {
            currentCableLayer = 3;
        }
    }

    private void printAllSimComponents() {
        if(Game.scene().current() instanceof GameScene gs) {
            ArrayList<Entity> entities = gs.getEntities();
            System.out.println("-------- SIMULATION ENTITIES --------");

            for(Entity e : entities) {
                if(e.getComponent(SimulationComponent.class) != null) {
                    System.out.println("------- " + e.getName() + " - " + e.getId() + " --------");
                    if(e.getComponent(SimulationComponent.class) != null) {
                        System.out.println("Group ID: " + e.getComponent(SimulationComponent.class).getGroupIds());
                    }
                    if(e.getComponent(GridComponent.class) != null)
                        System.out.println("at position: " + e.getComponent(GridComponent.class).getGridLocation());
                    System.out.println();
                }
            }
        }
    }

    public void printAllCablePorts() {
        if(Game.scene().current() instanceof GameScene gs) {
            ArrayList<Entity> entities = gs.getEntities();

            System.out.println("--------- Grid Elements ---------");
            for(Entity e : entities) {
                if(e.getComponent(CablePortsComponent.class) != null) {
                    System.out.println("------- " + e.getName() + " - " + e.getId() + " --------");
                    if(e.getComponent(SimulationComponent.class) != null) {
                        System.out.println("Group ID: " + e.getComponent(SimulationComponent.class).getGroupIds());
                        System.out.println("Own ID: " + e.getComponent(SimulationComponent.class).getOwnId());
                        System.out.println("Input Ids: " + e.getComponent(SimulationComponent.class).getInputIds());
                        System.out.println("Current State: " + e.getComponent(SimulationComponent.class).getSimulationState());
                        System.out.println("Minimum Correct signals: " + e.getComponent(SimulationComponent.class).getCorrectSignalsNeeded());
                        System.out.println("Maximum Out Of Control signals: " + e.getComponent(SimulationComponent.class).getOutOfControlSignalsAccepted());
                        System.out.println("Failure Prob: " + e.getComponent(SimulationComponent.class).getFailureRatio());
                        System.out.println("Failure Recognition Ratio: " + e.getComponent(SimulationComponent.class).getFailureRecognitionRatio());
                    }
                    if(e.getComponent(GridComponent.class) != null)
                        System.out.println("at position: " + e.getComponent(GridComponent.class).getGridLocation());
                    for(CablePort cpe : e.getComponent(CablePortsComponent.class).getCablePorts()) {
                        if(cpe.getConnectedEntity() != null)
                            System.out.println(cpe.getPortId() + "-" + cpe.getType() + "-" + cpe.getPosition() + ": " + cpe.getConnectedEntity().getName() + "-" + cpe.getConnectedEntity().getId());
                        else
                            System.out.println(cpe.getPortId() + "-" + cpe.getType() + "-" + cpe.getPosition() + ": " + cpe.getConnectedEntity());
                    }
                    System.out.println();
                }
            }
        }
    }

    private void updateCellSize() {
        if (Game.scene().current() instanceof GameScene gs) {
            cellSize = gs.getCellSize();
        } else if (Game.scene().current() instanceof BuildScene bs) {
            cellSize = bs.getCellSize();
        }
    }

    /**
     * Handle the mouse event on the different buttons
     *
     * @param e: mouse event
     */
    private void handleButtons(MouseEvent e) {
        Entity prev;
        Entity next;
        if (Game.scene().current() instanceof GameScene gs) {
            prev = gs.getPrevious();
            next = gs.getNext();

            if (e.getButton() == MouseEvent.BUTTON1) {
                if (prev.getComponent(ColliderComponent.class).getCollisionObjects().get(0).getCollisionBoundaries()
                        .contains(Game.scale().upscalePoint(e.getPoint()))) {
                    gs.setDescriptionDisplayUsingOffset(-1);
                    System.out.println("set to prev");
                } else if (next.getComponent(ColliderComponent.class).getCollisionObjects().get(0).getCollisionBoundaries()
                        .contains(Game.scale().upscalePoint(e.getPoint()))) {
                    gs.setDescriptionDisplayUsingOffset(+1);
                } else if (Game.scene().current().getEntityByName("back") != null) {
                    if (Game.scene().current().getEntityByName("back").getComponent(ColliderComponent.class).getCollisionObjects().get(0).getCollisionBoundaries()
                            .contains(Game.scale().upscalePoint(e.getPoint()))) {
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("back"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("scorebox"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("aircraft"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("scale"));
                    }
                }
            }
        }
    }

    private void handleToolTips(MouseEvent e) {
        ArrayList<Entity> entities = Query.getEntitiesWithComponent(TooltipComponent.class);

        for (Entity entity : entities) {
            if (Game.scene().current() instanceof GameScene gs && entity.getComponent(ColliderComponent.class) != null) {
                if (entity.getComponent(ColliderComponent.class).getCollisionObjects().get(0).getCollisionBoundaries()
                        .contains(Game.scale().upscalePoint(e.getPoint()))) {
                    gs.displayToolTip(entity);
                    break;
                } else {
                    gs.displayEmptyToolTip();
                }
            }
        }
    }

    private void setBuildPanelText(Entity entity) {
        if (entity instanceof BuildPanelEntity buildPanelEntity) {
            if (buildPanelEntity.getComponent(BuildComponent.class).getAmount() < 100)
                ((TextObject) buildPanelEntity.getComponent(RenderComponent.class)
                        .getRenderObjectsOfType(TextObject.class)
                        .get(0))
                        .setText(String.valueOf(buildPanelEntity.getComponent(BuildComponent.class).getAmount()));
            else
                ((TextObject) buildPanelEntity.getComponent(RenderComponent.class)
                        .getRenderObjectsOfType(TextObject.class)
                        .get(0))
                        .setText("");
        }
    }

    private SimulationEntity makeSimulationEntity(Entity entity) {
        int[] cablePortsIn = new int[]{};
        int[] cablePortsOut = new int[]{};
        switch (entity.getComponent(BuildComponent.class).getSimulationType()) {
            case SENSOR -> {
                cablePortsIn = new int[]{};
                cablePortsOut = new int[]{0, 1, 2, 3};
            }
            case CPU, VOTE -> {
                cablePortsIn = new int[]{0, 1, 2, 3};
                cablePortsOut = new int[]{0, 1, 2, 3};
            }
            case ACTUATOR -> {
                cablePortsIn = new int[]{0, 1, 2, 3};
                cablePortsOut = new int[]{};
            }
            case CABLE -> {
                cablePortsIn = new int[]{entity.getComponent(BuildComponent.class).getPortId()};
                cablePortsOut = new int[]{entity.getComponent(BuildComponent.class).getPortId()};
            }
        }
        return new SimulationEntity(
                entity.getName() + "_simulation", IdGenerator.generateId(),
                entity.getComponent(RenderComponent.class).getRenderObjects().get(0).getLocation().x,
                entity.getComponent(RenderComponent.class).getRenderObjects().get(0).getLocation().y,
                cellSize,
                cellSize,
                -1, -1,
                ((ImageObject) entity.getComponent(RenderComponent.class).getRenderObjectsOfType(ImageObject.class).get(0))
                        .getImage(),
                entity.getComponent(BuildComponent.class).getTileId(),
                entity.getComponent(BuildComponent.class).getFailureRatio(),
                entity.getComponent(BuildComponent.class).getSimulationType(),
                entity.getComponent(BuildComponent.class).getCorrectSignalsNeeded(),
                entity.getComponent(BuildComponent.class).getOutOfControlSignalsAccepted(),
                cablePortsIn, cablePortsOut,
                true, entity.getComponent(BuildComponent.class).getFailureDetectionRatio()
        );
    }

    private boolean handleNotBuilding(Entity entity, MouseEvent e) {
        // check location of the click
        if (entity.getComponent(ColliderComponent.class) != null
                && entity.getComponent(ColliderComponent.class).getCollisionObjects().get(0).getCollisionBoundaries() != null
                && entity.getComponent(ColliderComponent.class).getCollisionObjects().get(0).getCollisionBoundaries()
                .contains(Game.scale().upscalePoint(e.getPoint()))) {
            // check if the click was on a BuildComponent first -> create new Simulation Entity
            if (entity.getComponent(BuildComponent.class) != null) {
                // check amount left, >0? -> new Entity
                if (entity.getComponent(BuildComponent.class).getAmount() > 0
                        && entity.getComponent(BuildComponent.class).getSimulationType() != SimulationType.CABLE) {
                    SimulationEntity newEntity = null;

                    if (entity.getComponent(BuildComponent.class).getSimulationType() == SimulationType.CPU) {
                        newEntity = makeSimulationEntity(entity);
                    } else if (entity.getComponent(BuildComponent.class).getSimulationType() == SimulationType.SENSOR) {
                        newEntity = makeSimulationEntity(entity);
                        newEntity.getComponent(SimulationComponent.class).setSimulationState(SimulationState.CORRECT);
                    } else if (entity.getComponent(BuildComponent.class).getSimulationType() == SimulationType.ACTUATOR) {
                        newEntity = makeSimulationEntity(entity);
                    } else if (entity.getComponent(BuildComponent.class).getSimulationType() == SimulationType.VOTE) {
                        newEntity = makeSimulationEntity(entity);
                    }

                    currentBuilding = newEntity;
                    Game.scene().current().addEntityToScene(newEntity);

                    entity.getComponent(BuildComponent.class)
                            .subtractFromAmount();
                    if (entity.getComponent(BuildComponent.class).getAmount() > 100) {
                        ((TextObject) entity.getComponent(RenderComponent.class)
                                .getRenderObjectsOfType(TextObject.class)
                                .get(0))
                                .setText("");
                    } else {
                        ((TextObject) entity.getComponent(RenderComponent.class)
                                .getRenderObjectsOfType(TextObject.class)
                                .get(0))
                                .setText(String.valueOf(entity.getComponent(BuildComponent.class)
                                        .getAmount()
                                ));
                    }
                    currentBuildState = BuilderState.BUILDING_SIMULATION;

                    return true;
                } else if (entity.getComponent(BuildComponent.class).getAmount() > 0
                        && entity.getComponent(BuildComponent.class).getSimulationType() == SimulationType.CABLE) {
                    SimulationEntity newEntity = makeSimulationEntity(entity);
                    cableBuildRepetitive = entity;
                    currentBuilding = newEntity;
                    Game.scene().current().addEntityToScene(newEntity);

                    entity.getComponent(BuildComponent.class)
                            .subtractFromAmount();
                    if (entity.getComponent(BuildComponent.class).getAmount() > 100) {
                        ((TextObject) entity.getComponent(RenderComponent.class)
                                .getRenderObjectsOfType(TextObject.class)
                                .get(0))
                                .setText("");
                    } else {
                        ((TextObject) entity.getComponent(RenderComponent.class)
                                .getRenderObjectsOfType(TextObject.class)
                                .get(0))
                                .setText(String.valueOf(entity.getComponent(BuildComponent.class)
                                        .getAmount()
                                ));
                    }

                    currentBuildState = BuilderState.BUILDING_CABLE;
                    return true;
                }
            }
            // check if clicked component is a cable; left click changes the output direction, mid click the input direction
            else if (entity.getComponent(SimulationComponent.class) != null) {
                if (entity.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CABLE) {
                    int outId = entity.getComponent(CablePortsComponent.class).getOutIds()[0];
                    if (outId == currentCableLayer) {
                        entity.getComponent(CablePortsComponent.class).getCablePort(outId, CablePortType.OUT).cyclePosition();
                        entity.getComponent(CablePortsComponent.class).updateImage();
                        updateConnection(entity, CablePortType.OUT);
                    }
                }

            }
        }
        return false;
    }

    private void buildRepetitive() {
        if (cableBuildRepetitive.getComponent(BuildComponent.class).getAmount() > 0
                && cableBuildRepetitive.getComponent(BuildComponent.class).getSimulationType() == SimulationType.CABLE) {
            SimulationEntity newEntity = makeSimulationEntity(cableBuildRepetitive);

            currentBuilding = newEntity;
            Game.scene().current().addEntityToScene(newEntity);

            cableBuildRepetitive.getComponent(BuildComponent.class)
                    .subtractFromAmount();
            if (cableBuildRepetitive.getComponent(BuildComponent.class).getAmount() > 100) {
                ((TextObject) cableBuildRepetitive.getComponent(RenderComponent.class)
                        .getRenderObjectsOfType(TextObject.class)
                        .get(0))
                        .setText("");
            } else {
                ((TextObject) cableBuildRepetitive.getComponent(RenderComponent.class)
                        .getRenderObjectsOfType(TextObject.class)
                        .get(0))
                        .setText(String.valueOf(cableBuildRepetitive.getComponent(BuildComponent.class)
                                .getAmount()
                        ));
            }
            currentBuildState = BuilderState.BUILDING_CABLE;
        }
    }

    /**
     * Cycle a cable
     *
     * @param entity: the cable to cycle
     * @param e:      Mouse Event
     */
    private void cycleCable(Entity entity, MouseEvent e) {
        if (entity.getComponent(ColliderComponent.class) != null
                && entity.getComponent(ColliderComponent.class).getCollisionObjects().get(0).getCollisionBoundaries()
                .contains(Game.scale().upscalePoint(e.getPoint()))) {
            if (entity.getComponent(SimulationComponent.class) != null) {
                if (entity.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CABLE) {
                    int inId = entity.getComponent(CablePortsComponent.class).getInIds()[0];
                    if (inId == currentCableLayer) {
                        entity.getComponent(CablePortsComponent.class).getCablePort(inId, CablePortType.IN).cyclePosition();
                        entity.getComponent(CablePortsComponent.class).updateImage();
                        updateConnection(entity, CablePortType.IN);
                    }

                }
            }
        }
    }

    @Override
    public void handle(MouseEvent e) {
        updateCellSize();
        handleButtons(e);
        handleToolTips(e);

        ArrayList<Entity> entities = Query.getEntitiesWithComponent(BuildComponent.class);

        for (Entity entity : entities) {
            // check for click on a next/prev button
            setBuildPanelText(entity);
            // check the button clicked is left click
            if (e.getButton() == MouseEvent.BUTTON1) {
                // Handle events when builder is not building
                if (currentBuildState == BuilderState.NOT_BUILDING) {
                    if (handleNotBuilding(entity, e)) {
                        break;
                    }
                }
                // check if state is building
                else if (currentBuildState == BuilderState.BUILDING_SIMULATION) {
                    // try to place the component
                    if (placeComponent(currentBuilding)) {
                        currentBuilding = null;
                        currentBuildState = BuilderState.NOT_BUILDING;
                        break;
                    }
                }
                // check if state is building cable
                else if (currentBuildState == BuilderState.BUILDING_CABLE) {
                    // try to place component
                    if (placeCable(currentBuilding)) {
                        currentBuilding = null;
                        currentBuildState = BuilderState.NOT_BUILDING;
                        buildRepetitive();
                        break;
                    }
                }
            }
            // check mid click to rotate

            else if(e.getButton() == MouseEvent.BUTTON2) {
                cycleCable(entity, e);
            }

            // check the button clicked is right click
            else if (e.getButton() == MouseEvent.BUTTON3) {
                removeComponent(e);
                // check if no button is clicked
            } else if (e.getButton() == MouseEvent.NOBUTTON) {
                if (currentBuildState == BuilderState.BUILDING_SIMULATION || currentBuildState == BuilderState.BUILDING_CABLE) {
                    currentBuilding.getComponent(RenderComponent.class).reposition(Game.scale().upscalePoint(e.getPoint()));
                }
            }
        }
    }

    /**
     * Remove the Component currently building or from grid
     *
     * @param e: Mouese Event
     */
    private void removeComponent(MouseEvent e) {
        // handle if is building simulation component
        if (currentBuildState == BuilderState.BUILDING_SIMULATION || currentBuildState == BuilderState.BUILDING_CABLE) {
            // remove the component
            if (putBackToStack(currentBuilding)) {
                currentBuildState = BuilderState.NOT_BUILDING;

                cableBuildRepetitive = null;
                currentBuilding = null;
            }
        }
        // handle if building cable -> remove cable
        else if (currentBuildState == BuilderState.NOT_BUILDING) {
            // remove component at positon
            Point gridLocation = findEntityGridPosition(Game.scale().upscalePoint(e.getPoint()));
            if (gridLocation == null) {
                return;
            } else {
                ArrayList<Entity> entitiesAtSameCell = new ArrayList<>();
                entitiesAtSameCell = getEntitiesAtGridPosition(gridLocation);

                for (Entity remove : entitiesAtSameCell) {
                    if (remove.isRemovable() && remove.getComponent(SimulationComponent.class) != null) {
                        if (putBackToStack(remove)) {
                            return;
                        }
                    }
                }
            }

        }
    }

    /**
     * Update connections of cable & Entity
     *
     * @param e:    Entity
     * @param type: Type of cable port (IN/OUT)
     */
    private void updateConnection(Entity e, CablePortType type) {
        int id;
        if (type == CablePortType.IN) {
            id = e.getComponent(CablePortsComponent.class).getInIds()[0];
        } else {
            id = e.getComponent(CablePortsComponent.class).getOutIds()[0];
        }

        // disconnect the component
        CablePort c = e.getComponent(CablePortsComponent.class).getCablePort(id, type);

        if(c.getConnectedEntity() != null) {
            c.getConnectedEntity().getComponent(SimulationComponent.class).resetGroupIds();
            c.getConnectedEntity()
                    .getComponent(CablePortsComponent.class)
                    .getCablePort(
                            c.getConnectedEntity()
                                    .getComponent(CablePortsComponent.class)
                                    .getPortIdOfConnectedEntity(e),
                            c.getConnectedEntity()
                                    .getComponent(CablePortsComponent.class)
                                    .getPortTypeOfConnectedEntity(e)
                    )
                    .setConnectedEntity(null);
            c.setConnectedEntity(null);
            e.getComponent(SimulationComponent.class).resetGroupIds();
        }

        Point gridPos = (Point) e.getComponent(GridComponent.class).getGridLocation();

        ArrayList<Entity> left;
        ArrayList<Entity> top;
        ArrayList<Entity> right;
        ArrayList<Entity> bottom;
        // left
        left = getEntitiesAtGridPosition(new Point(gridPos.x - 1, gridPos.y));
        right = getEntitiesAtGridPosition(new Point(gridPos.x + 1, gridPos.y));
        top = getEntitiesAtGridPosition(new Point(gridPos.x, gridPos.y - 1));
        bottom = getEntitiesAtGridPosition(new Point(gridPos.x, gridPos.y + 1));

        int inId = e.getComponent(CablePortsComponent.class).getInIds()[0];
        int outId = e.getComponent(CablePortsComponent.class).getInIds()[0];
        CablePort in = e.getComponent(CablePortsComponent.class).getCablePort(inId, CablePortType.IN);
        CablePort out = e.getComponent(CablePortsComponent.class).getCablePort(outId, CablePortType.OUT);

        if(type == CablePortType.OUT) {
            switch (c.getPosition()) {
                case RIGHT -> {
                    if (!right.isEmpty()) {
                        for (Entity r : right) {
                            if (r.getComponent(CablePortsComponent.class) != null) {
                                CablePort connect = r.getComponent(CablePortsComponent.class).getCablePort(outId, CablePortType.IN);
                                if (connect != null && connect.getConnectedEntity() == null && connect.getPosition() == CablePortPosition.LEFT) {
                                    connect.setConnectedEntity(e);
                                    out.setConnectedEntity(r);
                                }
                            }
                        }
                    }
                }
                case LEFT -> {
                    if (!left.isEmpty()) {
                        for (Entity r : bottom) {
                            if (r.getComponent(CablePortsComponent.class) != null) {
                                CablePort connect = r.getComponent(CablePortsComponent.class).getCablePort(outId, CablePortType.IN);
                                if (connect != null && connect.getConnectedEntity() == null && connect.getPosition() == CablePortPosition.RIGHT) {
                                    connect.setConnectedEntity(e);
                                    out.setConnectedEntity(r);
                                }
                            }
                        }
                    }
                }
                case BOTTOM -> {
                    if (!bottom.isEmpty()) {
                        for (Entity r : bottom) {
                            if (r.getComponent(CablePortsComponent.class) != null) {
                                CablePort connect = r.getComponent(CablePortsComponent.class).getCablePort(outId, CablePortType.IN);
                                if (connect != null && connect.getConnectedEntity() == null && connect.getPosition() == CablePortPosition.TOP) {
                                    connect.setConnectedEntity(e);
                                    out.setConnectedEntity(r);
                                }
                            }
                        }
                    }
                }
                case TOP -> {
                    if (!top.isEmpty()) {
                        for (Entity r : top) {
                            if (r.getComponent(CablePortsComponent.class) != null) {
                                CablePort connect = r.getComponent(CablePortsComponent.class).getCablePort(outId, CablePortType.IN);
                                if (connect != null && connect.getConnectedEntity() == null && connect.getPosition() == CablePortPosition.BOTTOM) {
                                    connect.setConnectedEntity(e);
                                    out.setConnectedEntity(r);
                                }
                            }
                        }
                    }
                }
            }
        } else if(type == CablePortType.IN) {
            switch (c.getPosition()) {
                case RIGHT -> {
                    if (!right.isEmpty()) {
                        for (Entity r : right) {
                            if (r.getComponent(CablePortsComponent.class) != null) {
                                CablePort connect = r.getComponent(CablePortsComponent.class).getCablePort(inId, CablePortType.OUT);
                                if (connect != null && connect.getConnectedEntity() == null && connect.getPosition() == CablePortPosition.LEFT) {
                                    connect.setConnectedEntity(e);
                                    in.setConnectedEntity(r);
                                }
                            }
                        }
                    }
                }
                case LEFT -> {
                    if (!left.isEmpty()) {
                        for (Entity r : bottom) {
                            if (r.getComponent(CablePortsComponent.class) != null) {
                                CablePort connect = r.getComponent(CablePortsComponent.class).getCablePort(inId, CablePortType.OUT);
                                if (connect != null && connect.getConnectedEntity() == null && connect.getPosition() == CablePortPosition.RIGHT) {
                                    connect.setConnectedEntity(e);
                                    in.setConnectedEntity(r);
                                }
                            }
                        }
                    }
                }
                case BOTTOM -> {
                    if (!bottom.isEmpty()) {
                        for (Entity r : bottom) {
                            if (r.getComponent(CablePortsComponent.class) != null) {
                                CablePort connect = r.getComponent(CablePortsComponent.class).getCablePort(inId, CablePortType.OUT);
                                if (connect != null && connect.getConnectedEntity() == null && connect.getPosition() == CablePortPosition.TOP) {
                                    connect.setConnectedEntity(e);
                                    in.setConnectedEntity(r);
                                }
                            }
                        }
                    }
                }
                case TOP -> {
                    if (!top.isEmpty()) {
                        for (Entity r : top) {
                            if (r.getComponent(CablePortsComponent.class) != null) {
                                CablePort connect = r.getComponent(CablePortsComponent.class).getCablePort(inId, CablePortType.OUT);
                                if (connect != null && connect.getConnectedEntity() == null && connect.getPosition() == CablePortPosition.BOTTOM) {
                                    connect.setConnectedEntity(e);
                                    in.setConnectedEntity(r);
                                }
                            }
                        }
                    }
                }
            }
        }
        // set cable id to either -1 if no component connected or to the in components ID for easier management
        // in component process of group / connected components detection

    }

    private boolean putBackToStack(Entity e) {
        if (e instanceof SimulationEntity) {
            if (e.getComponent(SimulationComponent.class) != null) {
                if (e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CABLE) {
                    if (e.getComponent(CablePortsComponent.class).getCablePort(e.getComponent(CablePortsComponent.class).getOutIds()[0], CablePortType.OUT).getConnectedEntity() != null) {
                        e.getComponent(CablePortsComponent.class).getCablePort(e.getComponent(CablePortsComponent.class).getOutIds()[0], CablePortType.OUT).getConnectedEntity().getComponent(SimulationComponent.class).resetGroupIds();
                    }
                }
                //replace the component
                Pattern p = Pattern.compile("_\\d{3}");
                Matcher m1 = p.matcher(e.getName());
                if (m1.find()) {
                    for (Entity build : Game.scene().current().getEntities()) {
                        Matcher m2 = p.matcher(build.getName());
                        if (m2.find()) {
                            if (build.getComponent(BuildComponent.class) != null) {
                                if (m1.group(0).equals(m2.group(0))) {
                                    build.getComponent(BuildComponent.class).addToAmount();
                                    if (build.getComponent(BuildComponent.class).getAmount() > 100) {
                                        ((TextObject) build.getComponent(RenderComponent.class)
                                                .getRenderObjectsOfType(TextObject.class)
                                                .get(0))
                                                .setText("");
                                    } else {
                                        ((TextObject) build.getComponent(RenderComponent.class)
                                                .getRenderObjectsOfType(TextObject.class)
                                                .get(0))
                                                .setText(String.valueOf(build.getComponent(BuildComponent.class)
                                                        .getAmount()
                                                ));
                                    }

                                    for(CablePort c : e.getComponent(CablePortsComponent.class).getCablePorts()) {
                                        if(c.getConnectedEntity() == null) {
                                            continue;
                                        }
                                        c.getConnectedEntity()
                                                .getComponent(CablePortsComponent.class)
                                                .getCablePort(
                                                        c.getConnectedEntity()
                                                        .getComponent(CablePortsComponent.class)
                                                        .getPortIdOfConnectedEntity(e),
                                                        c.getConnectedEntity()
                                                        .getComponent(CablePortsComponent.class)
                                                        .getPortTypeOfConnectedEntity(e)
                                                )
                                                .setConnectedEntity(null);
                                    }
                                    Game.logger().info("Found component and putting back to build stack.");
                                    break;
                                }
                            }
                        }
                    }
                    Game.logger().info("If no log found for putting component back to stack: add component to levels" +
                            "xml file: " + e.getName());
                }

                Game.scene().current().getEntities().remove(e);
            }
        }

        //TODO: implement not found in build panel
        return true;
    }

    private boolean placeComponent(Entity e) {
        Point gridPos = findEntityGridPosition(e
                .getComponent(RenderComponent.class)
                .getRenderObjects()
                .get(0)
                .getBounds()
                .getBounds()
                .getLocation());

        if (gridPos == null) {
            return false;
        }

        ArrayList<Entity> entitiesAtSameCell;
        entitiesAtSameCell = getEntitiesAtGridPosition(gridPos);

        if (entitiesAtSameCell.size() == 2) {
            for (Entity replace : entitiesAtSameCell) {
                if (replace.getComponent(SimulationComponent.class) != null) {
                    // replace an Entity

                    // check if replacing is possible, otherwise skip Entity
                    if(!replace.isRemovable()) {
                        continue;
                    }
                    if (putBackToStack(replace)) {
                        // Set location of grid
                        e.getComponent(GridComponent.class).setGridLocation(new Point(
                                (int) replace.getComponent(GridComponent.class).getGridLocation().getX(),
                                (int) replace.getComponent(GridComponent.class).getGridLocation().getY()
                        ));
                        // Set Grapics and Collision box
                        e.getComponent(RenderComponent.class).reposition(
                                new Point(replace.getComponent(RenderComponent.class).getRenderObjects().get(0).getLocation())
                        );
                        e.getComponent(ColliderComponent.class).getCollisionObjects().get(0).setCollisionBoundaries(new Rectangle(
                                (Rectangle) e.getComponent(RenderComponent.class).getRenderObjects().get(0).getBounds()
                        ));


                        Game.logger().info("Successfully added a new entity to the grid.\n" +
                                "Name: " + e.getName() +
                                "\nPosition: " + e.getComponent(GridComponent.class).getGridLocation() +
                                "\nRemovable: " + e.isRemovable());

                        ArrayList<Entity> left = new ArrayList<>();
                        ArrayList<Entity> top = new ArrayList<>();
                        ArrayList<Entity> right = new ArrayList<>();
                        ArrayList<Entity> bottom = new ArrayList<>();
                        // left
                        left = getEntitiesAtGridPosition(new Point(gridPos.x - 1, gridPos.y));
                        right = getEntitiesAtGridPosition(new Point(gridPos.x + 1, gridPos.y));
                        top = getEntitiesAtGridPosition(new Point(gridPos.x, gridPos.y - 1));
                        bottom = getEntitiesAtGridPosition(new Point(gridPos.x, gridPos.y + 1));

                        if(!left.isEmpty()) {
                            for(Entity l : left) {
                                if(l.getComponent(CablePortsComponent.class) != null) {
                                    for(CablePort c : l.getComponent(CablePortsComponent.class).getCablePorts()) {
                                        if(c.getType() == CablePortType.OUT) {
                                            e.getComponent(CablePortsComponent.class).getCablePort(c.getPortId(), CablePortType.IN).setConnectedEntity(l);
                                            c.setConnectedEntity(e);
                                        }
                                    }
                                }
                            }
                        }
                        if(!right.isEmpty()) {
                            for(Entity r : right) {
                                if(r.getComponent(CablePortsComponent.class) != null) {
                                    for(CablePort c :r.getComponent(CablePortsComponent.class).getCablePorts()) {
                                        if(c.getType() == CablePortType.IN) {
                                            e.getComponent(CablePortsComponent.class).getCablePort(c.getPortId(), CablePortType.OUT).setConnectedEntity(r);
                                            c.setConnectedEntity(e);
                                        }
                                    }
                                }
                            }
                        }
                        if(!top.isEmpty()) {
                            // TODO: implement corner cables
                        }
                        if(!bottom.isEmpty()) {
                            // TODO: implement corner cables
                        }

                        return true;
                    }
                }
            }
        } else if(entitiesAtSameCell.size() == 1) {
            for (Entity replace : entitiesAtSameCell) {
                if (replace.getComponent(GridComponent.class) != null) {
                    // place a component

                    e.getComponent(GridComponent.class).setGridLocation(new Point(
                            (int) replace.getComponent(GridComponent.class).getGridLocation().getX(),
                            (int) replace.getComponent(GridComponent.class).getGridLocation().getY()
                    ));
                    // Set Grapics and Collision box
                    e.getComponent(RenderComponent.class).reposition(
                            new Point(replace.getComponent(RenderComponent.class).getRenderObjects().get(0).getLocation())
                    );
                    e.getComponent(ColliderComponent.class).getCollisionObjects().get(0).setCollisionBoundaries(new Rectangle(
                            (Rectangle) e.getComponent(RenderComponent.class).getRenderObjects().get(0).getBounds()
                    ));

                    Game.logger().info("Successfully added a new entity to the grid.\n" +
                            "Name: " + e.getName() +
                            "\nPosition: " + e.getComponent(GridComponent.class).getGridLocation() +
                            "\nRemovable: " + e.isRemovable());
                    ArrayList<Entity> left = new ArrayList<>();
                    ArrayList<Entity> top = new ArrayList<>();
                    ArrayList<Entity> right = new ArrayList<>();
                    ArrayList<Entity> bottom = new ArrayList<>();
                    // left
                    left = getEntitiesAtGridPosition(new Point(gridPos.x - 1, gridPos.y));
                    right = getEntitiesAtGridPosition(new Point(gridPos.x + 1, gridPos.y));
                    top = getEntitiesAtGridPosition(new Point(gridPos.x, gridPos.y - 1));
                    bottom = getEntitiesAtGridPosition(new Point(gridPos.x, gridPos.y + 1));

                    if(!left.isEmpty()) {
                        for(Entity l : left) {
                            if(l.getComponent(CablePortsComponent.class) != null) {
                                for(CablePort c : l.getComponent(CablePortsComponent.class).getCablePorts()) {
                                    if(c.getType() == CablePortType.OUT) {
                                        e.getComponent(CablePortsComponent.class).getCablePort(c.getPortId(), CablePortType.IN).setConnectedEntity(l);
                                        c.setConnectedEntity(e);
                                    }
                                }
                            }
                        }
                    }
                    if(!right.isEmpty()) {
                        for(Entity r : right) {
                            if(r.getComponent(CablePortsComponent.class) != null) {
                                for(CablePort c :r.getComponent(CablePortsComponent.class).getCablePorts()) {
                                    if(c.getType() == CablePortType.IN) {
                                        e.getComponent(CablePortsComponent.class).getCablePort(c.getPortId(), CablePortType.OUT).setConnectedEntity(r);
                                        c.setConnectedEntity(e);
                                    }
                                }
                            }
                        }
                    }
                    if(!top.isEmpty()) {
                        // TODO: implement corner cables
                    }
                    if(!bottom.isEmpty()) {
                        // TODO: implement corner cables
                    }

                    return true;
                }
            }
        }
        return false;
    }

    private boolean placeCable(Entity e) {
        Point gridPos = findEntityGridPosition(e
                .getComponent(RenderComponent.class)
                .getRenderObjects()
                .get(0)
                .getBounds()
                .getBounds()
                .getLocation());

        if (gridPos == null) {
            return false;
        }
        System.out.println(e.getComponent(SimulationComponent.class).getSimulationType());

        ArrayList<Entity> entitiesAtSameCell = getEntitiesAtGridPosition(gridPos);


        int inId = e.getComponent(CablePortsComponent.class).getInIds()[0];
        int outId = e.getComponent(CablePortsComponent.class).getInIds()[0];

        // check for other components at the cell
        for(Entity check : entitiesAtSameCell) {
            if (e == check) {
                continue;
            }
            if (check.getComponent(SimulationComponent.class) != null && check.getComponent(SimulationComponent.class).getSimulationType() != SimulationType.CABLE) {
                return false;
            }
            // check if a cable of the same type is here already
            if (check.getComponent(SimulationComponent.class) != null
                    && check.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CABLE
                    && check.getComponent(CablePortsComponent.class).getCablePort(inId, CablePortType.IN) != null) {
                return false;
            }
        }

        for (Entity replace : entitiesAtSameCell) {
            if (replace.getComponent(GridComponent.class) != null) {
                // place a component

                e.getComponent(GridComponent.class).setGridLocation(new Point(
                        (int) replace.getComponent(GridComponent.class).getGridLocation().getX(),
                        (int) replace.getComponent(GridComponent.class).getGridLocation().getY()
                ));
                // Set Grapics and Collision box
                e.getComponent(RenderComponent.class).reposition(
                        new Point(replace.getComponent(RenderComponent.class).getRenderObjects().get(0).getLocation())
                );
                e.getComponent(ColliderComponent.class).getCollisionObjects().get(0).setCollisionBoundaries(new Rectangle(
                        (Rectangle) e.getComponent(RenderComponent.class).getRenderObjects().get(0).getBounds()
                ));

                Game.logger().info("Successfully added a new entity to the grid.\n" +
                        "Name: " + e.getName() +
                        "\nPosition: " + e.getComponent(GridComponent.class).getGridLocation() +
                        "\nRemovable: " + e.isRemovable());

                // connect the cables
                // gridPos (x,y) -> check (x+1,y),(x-1,y),(x,y+1),(x,y-1)
                ArrayList<Entity> left = getEntitiesAtGridPosition(new Point(gridPos.x - 1, gridPos.y));
                ArrayList<Entity> right = getEntitiesAtGridPosition(new Point(gridPos.x + 1, gridPos.y));
                ArrayList<Entity> top = getEntitiesAtGridPosition(new Point(gridPos.x, gridPos.y - 1));
                ArrayList<Entity> bottom = getEntitiesAtGridPosition(new Point(gridPos.x, gridPos.y + 1));

                CablePort in = e.getComponent(CablePortsComponent.class).getCablePort(inId, CablePortType.IN);
                CablePort out = e.getComponent(CablePortsComponent.class).getCablePort(outId, CablePortType.OUT);

                if(!left.isEmpty()) {
                    for(Entity l : left) {
                        if(l.getComponent(CablePortsComponent.class) != null) {
                            CablePort leftOut = l.getComponent(CablePortsComponent.class).getCablePort(inId, CablePortType.OUT);
                            if(leftOut != null && leftOut.getConnectedEntity() == null) {
                                leftOut.setConnectedEntity(e);
                                in.setConnectedEntity(l);
                            }
                        }
                    }
                }
                if(!right.isEmpty()) {
                    for(Entity r : right) {
                        if(r.getComponent(CablePortsComponent.class) != null) {
                            CablePort rightOut = r.getComponent(CablePortsComponent.class).getCablePort(outId, CablePortType.IN);
                            if(rightOut != null && rightOut.getConnectedEntity() == null) {
                                rightOut.setConnectedEntity(e);
                                out.setConnectedEntity(r);
                            }
                        }
                    }
                }

                // set cable id to either -1 if no component connected or to the in components ID for easier management
                // in component process of group / connected components detection

                e.getComponent(CablePortsComponent.class).updateImage();


                return true;
            }
        }
        System.out.println("Returing cos end of method");
        return false;
    }

    /**
     * find the grid position of a component
     * @param p
     * @return
     */
    private Point findEntityGridPosition(Point p) {
        for(Entity e : Game.scene().current().getEntities()) {
            if (e == currentBuilding) {
                continue;
            }
            if (e.getComponent(ColliderComponent.class) != null
                    && e.getComponent(ColliderComponent.class).getCollisionObjects().get(0).getCollisionBoundaries()
                    .contains(p)
                    && e.getComponent(GridComponent.class) != null) {
                return (Point) e.getComponent(GridComponent.class).getGridLocation();
            }
        }
        return null;
    }

    /**
     * Gets all entities at a grid position
     * @param p
     * @return
     */
    private ArrayList<Entity> getEntitiesAtGridPosition(Point p) {
        ArrayList<Entity> entitiesAtGridPosition = new ArrayList<>();

        for(Entity e : Game.scene().current().getEntities()) {
            if(e.getComponent(GridComponent.class) != null) {
                if(e.getComponent(GridComponent.class).getGridLocation().getX() == p.getX()
                        && e.getComponent(GridComponent.class).getGridLocation().getY() == p.getY()) {
                    entitiesAtGridPosition.add(e);
                }
            }
        }
        return entitiesAtGridPosition;
    }

    /**
     * Debugging method that prints all entities and their grid positions
     */
    private void printAllEntitiesGridPosition() {
        System.out.println("------Debugging-------");
        ArrayList<Entity> entities = Game.scene().current().getEntities();

        for(Entity e : entities) {
            if(e.getComponent(GridComponent.class) != null) {
                System.out.println(e.getName() + " at position: " + e.getComponent(GridComponent.class).getGridLocation());
            }
        }
    }

}