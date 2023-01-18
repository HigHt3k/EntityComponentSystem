package game.handler;

import com.Game;
import com.IdGenerator;
import com.ecs.component.CollisionComponent;
import com.ecs.component.GraphicsComponent;
import com.ecs.entity.Entity;
import com.input.handler.Handler;
import com.input.handler.HandlerType;
import game.components.*;
import game.entities.*;
import game.scenes.GameScene;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildHandler extends Handler {
    public BuilderState currentBuildState = BuilderState.NOT_BUILDING;
    public Entity currentBuilding = null;
    public int currentCableLayer = 0;
    public Entity cableBuildRepetitive = null;

    public BuildHandler() {
        super(HandlerType.EVENT);
    }

    @Override
    public void handle(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_D) {
            printAllEntitiesGridPosition();
        }
        if(e.getKeyCode() == KeyEvent.VK_O) {
            printAllCablePorts();
        }
        if(e.getKeyCode() == KeyEvent.VK_P) {
            printAllSimComponents();
        }
    }

    public void setCurrentCableLayer(int id) {
        this.currentCableLayer = id;
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
                    System.out.println("");
                }
            }
        }
    }

    public void printAllCablePorts() {
        if(Game.scene().current() instanceof GameScene gs) {
            ArrayList<Entity> entities = gs.getEntities();

            System.out.println("--------- CABLE PORTS ---------");
            for(Entity e : entities) {
                if(e.getComponent(CablePortsComponent.class) != null) {
                    System.out.println("------- " + e.getName() + " - " + e.getId() + " --------");
                    if(e.getComponent(SimulationComponent.class) != null) {
                        System.out.println("Group ID: " + e.getComponent(SimulationComponent.class).getGroupIds());
                    }
                    if(e.getComponent(GridComponent.class) != null)
                        System.out.println("at position: " + e.getComponent(GridComponent.class).getGridLocation());
                    for(CablePort cpe : e.getComponent(CablePortsComponent.class).getCablePorts()) {
                        if(cpe.getConnectedEntity() != null)
                            System.out.println(cpe.getPortId() + "-" + cpe.getType() + "-" + cpe.getPosition() + ": " + cpe.getConnectedEntity().getName() + "-" + cpe.getConnectedEntity().getId());
                        else
                            System.out.println(cpe.getPortId() + "-" + cpe.getType() + "-" + cpe.getPosition() + ": " + cpe.getConnectedEntity());
                    }
                    System.out.println("");
                }
            }
        }
    }

    @Override
    public void handle(MouseEvent e) {
        ArrayList<Entity> entities = (ArrayList<Entity>) Game.scene().current().getEntities().clone();

        for(Entity entity : entities) {
            // check the button clicked is left click
            if (e.getButton() == MouseEvent.BUTTON1) {
                // Handle events when builder is not building
                if (currentBuildState == BuilderState.NOT_BUILDING) {
                    // check location of the click
                    if (entity.getComponent(CollisionComponent.class) != null
                            && entity.getComponent(CollisionComponent.class).contains(e.getPoint())) {
                        // check if the click was on a BuildComponent first -> create new Simulation Entity
                        if (entity.getComponent(BuildComponent.class) != null) {
                            // check amount left, >0? -> new Entity
                            if (entity.getComponent(BuildComponent.class).getAmount() > 0
                                    && entity.getComponent(BuildComponent.class).getSimulationType() != SimulationType.CABLE) {
                                SimulationEntity newEntity = null;

                                if(entity.getComponent(BuildComponent.class).getSimulationType() == SimulationType.CPU) {
                                    newEntity = new SimulationEntity(
                                            entity.getName() + "_simulation", IdGenerator.generateId(),
                                            entity.getComponent(GraphicsComponent.class).get_BOUNDS().x,
                                            entity.getComponent(GraphicsComponent.class).get_BOUNDS().y,
                                            entity.getComponent(GraphicsComponent.class).get_BOUNDS().width,
                                            entity.getComponent(GraphicsComponent.class).get_BOUNDS().height,
                                            -1, -1,
                                            entity.getComponent(GraphicsComponent.class).getImage(),
                                            entity.getComponent(BuildComponent.class).getFailureRatio(),
                                            entity.getComponent(BuildComponent.class).getSimulationType(),
                                            entity.getComponent(BuildComponent.class).getCorrectSignalsNeeded(),
                                            entity.getComponent(BuildComponent.class).getOutOfControlSignalsAccepted(),
                                            new int[] {0,1,2,3}, new int[] {0,1,2,3},
                                            true
                                    );
                                } else if(entity.getComponent(BuildComponent.class).getSimulationType() == SimulationType.SENSOR) {
                                    newEntity = new SimulationEntity(
                                            entity.getName() + "_simulation", IdGenerator.generateId(),
                                            entity.getComponent(GraphicsComponent.class).get_BOUNDS().x,
                                            entity.getComponent(GraphicsComponent.class).get_BOUNDS().y,
                                            entity.getComponent(GraphicsComponent.class).get_BOUNDS().width,
                                            entity.getComponent(GraphicsComponent.class).get_BOUNDS().height,
                                            -1, -1,
                                            entity.getComponent(GraphicsComponent.class).getImage(),
                                            entity.getComponent(BuildComponent.class).getFailureRatio(),
                                            entity.getComponent(BuildComponent.class).getSimulationType(),
                                            entity.getComponent(BuildComponent.class).getCorrectSignalsNeeded(),
                                            entity.getComponent(BuildComponent.class).getOutOfControlSignalsAccepted(),
                                            new int[] {}, new int[] {0,1,2,3},
                                            true
                                    );
                                } else if(entity.getComponent(BuildComponent.class).getSimulationType() == SimulationType.ACTUATOR) {
                                    newEntity = new SimulationEntity(
                                            entity.getName() + "_simulation", IdGenerator.generateId(),
                                            entity.getComponent(GraphicsComponent.class).get_BOUNDS().x,
                                            entity.getComponent(GraphicsComponent.class).get_BOUNDS().y,
                                            entity.getComponent(GraphicsComponent.class).get_BOUNDS().width,
                                            entity.getComponent(GraphicsComponent.class).get_BOUNDS().height,
                                            -1, -1,
                                            entity.getComponent(GraphicsComponent.class).getImage(),
                                            entity.getComponent(BuildComponent.class).getFailureRatio(),
                                            entity.getComponent(BuildComponent.class).getSimulationType(),
                                            entity.getComponent(BuildComponent.class).getCorrectSignalsNeeded(),
                                            entity.getComponent(BuildComponent.class).getOutOfControlSignalsAccepted(),
                                            new int[] {0,1,2,3}, new int[] {},
                                            true
                                    );
                                }

                                currentBuilding = newEntity;
                                Game.scene().current().addEntityToScene(newEntity);

                                entity.getComponent(BuildComponent.class)
                                        .subtractFromAmount();
                                if(entity.getComponent(BuildComponent.class).getAmount() > 100) {
                                    entity.getComponent(GraphicsComponent.class)
                                            .getTexts()
                                            .set(0, "");
                                } else {
                                    entity.getComponent(GraphicsComponent.class)
                                            .getTexts()
                                            .set(0,
                                                    String.valueOf(entity.getComponent(BuildComponent.class)
                                                            .getAmount()
                                                    )
                                            );
                                }
                                currentBuildState = BuilderState.BUILDING_SIMULATION;
                                break;
                            }
                            else if(entity.getComponent(BuildComponent.class).getAmount() > 0
                                    && entity.getComponent(BuildComponent.class).getSimulationType() == SimulationType.CABLE) {
                                SimulationEntity newEntity = new SimulationEntity(
                                        entity.getName() + "_cable", IdGenerator.generateId(),
                                        entity.getComponent(GraphicsComponent.class).get_BOUNDS().x,
                                        entity.getComponent(GraphicsComponent.class).get_BOUNDS().y,
                                        entity.getComponent(GraphicsComponent.class).get_BOUNDS().width,
                                        entity.getComponent(GraphicsComponent.class).get_BOUNDS().height,
                                        -1, -1,
                                        entity.getComponent(GraphicsComponent.class).getImage(),
                                        entity.getComponent(BuildComponent.class).getFailureRatio(),
                                        entity.getComponent(BuildComponent.class).getSimulationType(),
                                        0, 0,
                                        new int[] {entity.getComponent(BuildComponent.class).getPortId()},
                                        new int[] {entity.getComponent(BuildComponent.class).getPortId()},
                                        true
                                );
                                cableBuildRepetitive = entity;
                                currentBuilding = newEntity;
                                Game.scene().current().addEntityToScene(newEntity);

                                entity.getComponent(BuildComponent.class)
                                        .subtractFromAmount();
                                if(entity.getComponent(BuildComponent.class).getAmount() > 100) {
                                    entity.getComponent(GraphicsComponent.class)
                                            .getTexts()
                                            .set(0, "");
                                } else {
                                    entity.getComponent(GraphicsComponent.class)
                                            .getTexts()
                                            .set(0,
                                                    String.valueOf(entity.getComponent(BuildComponent.class)
                                                            .getAmount()
                                                    )
                                            );
                                }
                                currentBuildState = BuilderState.BUILDING_CABLE_REFACTORED;
                                break;
                            }
                        }
                        // check if clicked component is a cable; left click changes the output direction, mid click the input direction
                        else if(entity.getComponent(SimulationComponent.class) != null) {
                            if(entity.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CABLE) {
                                int outId = entity.getComponent(CablePortsComponent.class).getOutIds()[0];
                                if(outId == currentCableLayer) {
                                    entity.getComponent(CablePortsComponent.class).getCablePort(outId, CablePortType.OUT).cyclePosition();
                                    entity.getComponent(CablePortsComponent.class).updateImage();
                                    updateConnection(entity, CablePortType.OUT);
                                }
                            }
                        }
                    }
                }
                // check if state is building
                else if (currentBuildState == BuilderState.BUILDING_SIMULATION) {
                    // try to place the component
                    if(placeComponent(currentBuilding)) {
                        currentBuilding = null;
                        currentBuildState = BuilderState.NOT_BUILDING;
                        break;
                    }
                }
                // check if state is building cable refactored
                else if(currentBuildState == BuilderState.BUILDING_CABLE_REFACTORED) {
                    // try to place component
                    if(placeCableRefactored(currentBuilding)) {
                        currentBuilding = null;
                        currentBuildState = BuilderState.NOT_BUILDING;

                        if(cableBuildRepetitive.getComponent(BuildComponent.class).getAmount() > 0
                                && cableBuildRepetitive.getComponent(BuildComponent.class).getSimulationType() == SimulationType.CABLE) {
                            SimulationEntity newEntity = new SimulationEntity(
                                    cableBuildRepetitive.getName() + "_cable", IdGenerator.generateId(),
                                    cableBuildRepetitive.getComponent(GraphicsComponent.class).get_BOUNDS().x,
                                    cableBuildRepetitive.getComponent(GraphicsComponent.class).get_BOUNDS().y,
                                    cableBuildRepetitive.getComponent(GraphicsComponent.class).get_BOUNDS().width,
                                    cableBuildRepetitive.getComponent(GraphicsComponent.class).get_BOUNDS().height,
                                    -1, -1,
                                    cableBuildRepetitive.getComponent(GraphicsComponent.class).getImage(),
                                    cableBuildRepetitive.getComponent(BuildComponent.class).getFailureRatio(),
                                    cableBuildRepetitive.getComponent(BuildComponent.class).getSimulationType(),
                                    0, 0,
                                    new int[]{cableBuildRepetitive.getComponent(BuildComponent.class).getPortId()},
                                    new int[]{cableBuildRepetitive.getComponent(BuildComponent.class).getPortId()},
                                    true
                            );

                            currentBuilding = newEntity;
                            Game.scene().current().addEntityToScene(newEntity);

                            cableBuildRepetitive.getComponent(BuildComponent.class)
                                    .subtractFromAmount();
                            if (cableBuildRepetitive.getComponent(BuildComponent.class).getAmount() > 100) {
                                cableBuildRepetitive.getComponent(GraphicsComponent.class)
                                        .getTexts()
                                        .set(0, "");
                            } else {
                                cableBuildRepetitive.getComponent(GraphicsComponent.class)
                                        .getTexts()
                                        .set(0,
                                                String.valueOf(cableBuildRepetitive.getComponent(BuildComponent.class)
                                                        .getAmount()
                                                )
                                        );
                            }
                            currentBuildState = BuilderState.BUILDING_CABLE_REFACTORED;
                            break;
                        }
                    }
                }
                // check if state is building cable
                else if (currentBuildState == BuilderState.BUILDING_CABLE) {
                    // try to connect the cable to a neighboring grid tile.
                    if(placeCable(currentBuilding, e.getPoint())) {
                        colorizeAndPositionCable((CableEntity) currentBuilding);
                        currentBuilding = null;
                        currentBuildState = BuilderState.NOT_BUILDING;
                        break;
                    }
                }
            }
            // check mid click to rotate

            else if(e.getButton() == MouseEvent.BUTTON2) {
                if (entity.getComponent(CollisionComponent.class) != null
                        && entity.getComponent(CollisionComponent.class).contains(e.getPoint())) {
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

            // check the button clicked is right click
            else if (e.getButton() == MouseEvent.BUTTON3) {
                // handle if is building simulation component
                if (currentBuildState == BuilderState.BUILDING_SIMULATION || currentBuildState == BuilderState.BUILDING_CABLE_REFACTORED) {
                    // remove the component
                    if(putBackToStack(currentBuilding)) {
                        currentBuildState = BuilderState.NOT_BUILDING;
                        cableBuildRepetitive = null;
                        currentBuilding = null;
                    }
                }
                // handle if building cable -> remove cable
                else if(currentBuildState == BuilderState.BUILDING_CABLE) {
                    System.out.println("Removing cable");
                    ArrayList<CablePort> ports = currentBuilding.getComponent(CablePortsComponent.class).getCablePorts();
                    // remove connections from port
                    for(CablePort port : ports) {
                        if(port.getConnectedEntity() == null) {
                            continue;
                        }
                        for(CablePort other: port.getConnectedEntity().getComponent(CablePortsComponent.class).getCablePorts()) {
                            if(other.getConnectedEntity() == currentBuilding) {
                                other.setConnectedEntity(null);
                            }
                        }
                        port.setConnectedEntity(null);
                    }

                    Game.scene().current().removeEntityFromScene(currentBuilding);

                    currentBuildState = BuilderState.NOT_BUILDING;
                    currentBuilding = null;
                }
                // handle if not building state -> remove component
                else if(currentBuildState == BuilderState.NOT_BUILDING) {
                    // remove component at positon
                    Point gridLocation = findEntityGridPosition(e.getPoint());
                    if(gridLocation == null) {
                        return;
                    } else {
                        ArrayList<Entity> entitiesAtSameCell = new ArrayList<>();
                        entitiesAtSameCell = getEntitiesAtGridPosition(gridLocation);

                        for(Entity remove : entitiesAtSameCell) {
                            if(remove.isRemovable() && remove.getComponent(SimulationComponent.class) != null) {
                                if(putBackToStack(remove)) {
                                    return;
                                }
                            }
                        }
                    }
                }
            // check if no button is clicked
            } else if(e.getButton() == MouseEvent.NOBUTTON) {
                if (currentBuildState == BuilderState.BUILDING_SIMULATION || currentBuildState == BuilderState.BUILDING_CABLE_REFACTORED) {
                    currentBuilding.getComponent(GraphicsComponent.class).reposition(e.getPoint());
                }
                else if(currentBuildState == BuilderState.BUILDING_CABLE) {
                    // Reposition the cable end
                    currentBuilding
                            .getComponent(GraphicsComponent.class)
                            .setLine(currentBuilding
                                    .getComponent(GraphicsComponent.class)
                                    .getLineStart(),
                                    e.getPoint()
                            );
                }
            }
        }
    }

    private void updateConnection(Entity e, CablePortType type) {
        int id;
        if(type == CablePortType.IN) {
            id = e.getComponent(CablePortsComponent.class).getInIds()[0];
        } else {
            id = e.getComponent(CablePortsComponent.class).getOutIds()[0];
        }

        // disconnect the component
        CablePort c = e.getComponent(CablePortsComponent.class).getCablePort(id, type);

        if(c.getConnectedEntity() != null) {
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
    }

    private boolean putBackToStack(Entity e) {
        if(e instanceof SimulationEntity se) {
            if (e.getComponent(SimulationComponent.class) != null) {
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
                                    if(build.getComponent(BuildComponent.class).getAmount() > 100) {
                                        build.getComponent(GraphicsComponent.class).getTexts().set(0, "");
                                    }
                                    else {
                                        build.getComponent(GraphicsComponent.class).getTexts().set(0,
                                                String.valueOf(build.getComponent(BuildComponent.class).getAmount()));
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
        } else if(e instanceof CableEntity) {
            Game.scene().current().getEntities().remove(e);
        }
        //TODO: implement not found in build panel
        return true;
    }

    private boolean placeComponent(Entity e) {
        Point gridPos = findEntityGridPosition(e.getComponent(GraphicsComponent.class).getBounds().getLocation());

        if(gridPos == null) {
            return false;
        }

        ArrayList<Entity> entitiesAtSameCell = new ArrayList<>();
        entitiesAtSameCell = getEntitiesAtGridPosition(gridPos);

        if(entitiesAtSameCell.size() == 2) {
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
                        e.getComponent(GraphicsComponent.class).reposition(new Point(replace.getComponent(GraphicsComponent.class)
                                .getBounds().getLocation()));
                        e.getComponent(CollisionComponent.class)
                                .setCollisionBox(new Rectangle(e.getComponent(GraphicsComponent.class).get_BOUNDS()));



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
                    e.getComponent(GraphicsComponent.class).reposition(new Point(replace.getComponent(GraphicsComponent.class)
                            .getBounds().getLocation()));
                    e.getComponent(GraphicsComponent.class).setShape(new Rectangle(e.getComponent(GraphicsComponent.class).get_BOUNDS()));
                    e.getComponent(CollisionComponent.class)
                            .setCollisionBox(new Rectangle(e.getComponent(GraphicsComponent.class).get_BOUNDS()));

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

    private boolean placeCableRefactored(Entity e) {
        Point gridPos = findEntityGridPosition(e.getComponent(GraphicsComponent.class).getBounds().getLocation());

        if(gridPos == null) {
            return false;
        }

        ArrayList<Entity> entitiesAtSameCell = getEntitiesAtGridPosition(gridPos);


        int inId = e.getComponent(CablePortsComponent.class).getInIds()[0];
        int outId = e.getComponent(CablePortsComponent.class).getInIds()[0];

        // check for other components at the cell
        for(Entity check : entitiesAtSameCell) {
            if(check.getComponent(SimulationComponent.class) != null && check.getComponent(SimulationComponent.class).getSimulationType() != SimulationType.CABLE) {
                return false;
            }
            // check if a cable of the same type is here already
            if(check.getComponent(SimulationComponent.class) != null
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
                e.getComponent(GraphicsComponent.class).reposition(new Point(replace.getComponent(GraphicsComponent.class)
                        .getBounds().getLocation()));
                e.getComponent(GraphicsComponent.class).setShape(new Rectangle(e.getComponent(GraphicsComponent.class).get_BOUNDS()));
                e.getComponent(CollisionComponent.class)
                        .setCollisionBox(new Rectangle(e.getComponent(GraphicsComponent.class).get_BOUNDS()));

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
                if(!top.isEmpty()) {
                    // TODO: implement corner cables
                }
                if(!bottom.isEmpty()) {
                    // TODO: implement corner cables
                }
                e.getComponent(CablePortsComponent.class).updateImage();

                return true;
            }
        }
        return false;
    }

    /**
     * find the grid position of a component
     * @param p
     * @return
     */
    private Point findEntityGridPosition(Point p) {
        for(Entity e : Game.scene().current().getEntities()) {
            if(e.getComponent(CollisionComponent.class) != null
                    && e.getComponent(CollisionComponent.class).contains(p)
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
     * Calculate the distance between 2 points (x,y)1, (x,y)2
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private double calculateDistanceBetweenPoints(
            double x1,
            double y1,
            double x2,
            double y2) {
        return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }

    /**
     * Calculate the distance between 2 points (p1, p2)
     * @param p1
     * @param p2
     * @return
     */
    private double calculateDistanceBetweenPoints(
            Point p1, Point p2) {
        double distance = Math.sqrt((p2.y - p1.y) * (p2.y - p1.y) + (p2.x - p1.x) * (p2.x - p1.x));
        return distance;
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

    /**
     * Refactored cable placing method. Creates a cable combiner if not available on the field clicked; connects cables if
     * possible.
     * @param e
     * @param mousePos
     * @return
     */
    private boolean placeCable(Entity e, Point mousePos) {
        Point gridPos = findEntityGridPosition(e.getComponent(GraphicsComponent.class).getBounds().getLocation());
        ArrayList<Entity> clickedEntities = new ArrayList<>();

        Point mouseGridPos = findEntityGridPosition(mousePos);
        // if mouse not in grid, dont place
        if(mouseGridPos == null) {
            return false;
        }
        clickedEntities.addAll(getEntitiesAtGridPosition(mouseGridPos));

        // If not entity was clicked, dont place
        if(clickedEntities.isEmpty()) {
            Game.logger().info("any of the two lists is empty");
            return false;
        }

        for(Entity entity : clickedEntities) {
            // If clicked entity is not a neighbor, dont place.
            if (e.getComponent(CablePortsComponent.class).getCablePort(0, CablePortType.IN).getConnectedEntity() != null) {
                if(!isNeighbor(e.getComponent(CablePortsComponent.class).getCablePort(0, CablePortType.IN).getConnectedEntity(), entity)) {
                    return false;
                }
                // Cant connect components besides each other without a space in between for cables.
                if(entity.getComponent(SimulationComponent.class) != null
                        && e.getComponent(CablePortsComponent.class)
                        .getCablePort(0, CablePortType.IN)
                        .getConnectedEntity()
                        .getComponent(SimulationComponent.class) != null) {
                    return false;
                }
            } else if(e.getComponent(CablePortsComponent.class).getCablePort(0, CablePortType.OUT).getConnectedEntity() != null) {
                if(!isNeighbor(e.getComponent(CablePortsComponent.class).getCablePort(0, CablePortType.OUT).getConnectedEntity(), entity)) {
                    return false;
                }
                // Cant connect components besides each other without a space in between for cables.
                if(entity.getComponent(SimulationComponent.class) != null
                        && e.getComponent(CablePortsComponent.class)
                        .getCablePort(0, CablePortType.OUT)
                        .getConnectedEntity()
                        .getComponent(SimulationComponent.class) != null) {
                    return false;
                }
            }
        }

        CablePortType connectingToType;
        CablePortType connectingFromType;

        if(e.getComponent(CablePortsComponent.class).getCablePort(0, CablePortType.OUT).getConnectedEntity() == null) {
            connectingToType = CablePortType.IN;
            connectingFromType = CablePortType.OUT;
        } else {
            connectingToType = CablePortType.OUT;
            connectingFromType = CablePortType.IN;
        }

        // check if clicked instance is a cable combiner
        for(Entity entity : clickedEntities) {
            if(entity instanceof CableCombinerEntity ccer) {
                if(ccer.getComponent(CablePortsComponent.class).getNextFreePort(connectingToType) == null) {
                    return false;
                }
                ccer.getComponent(CablePortsComponent.class).getNextFreePort(connectingToType).setConnectedEntity(e);
                e.getComponent(CablePortsComponent.class).getNextFreePort(connectingFromType).setConnectedEntity(ccer);
                return true;
            }
        }

        // check if clicked instance is a sim entity
        for(Entity entity : clickedEntities) {
            if(entity instanceof SimulationEntity se) {
                if(se.getComponent(CablePortsComponent.class).getNextFreePort(connectingToType) == null) {
                    return false;
                }
                se.getComponent(CablePortsComponent.class).getNextFreePort(connectingToType).setConnectedEntity(e);
                e.getComponent(CablePortsComponent.class).getNextFreePort(connectingFromType).setConnectedEntity(se);
                return true;
            }
        }

        // check if clicked entity is a grid entity -> create new cablecombiner here and connect the cable automatically
        for(Entity entity : clickedEntities) {
            if(entity instanceof GridEntity) {
                CableCombinerEntity ccer = new CableCombinerEntity(
                        "cable_combiner", IdGenerator.generateId(),
                        entity.getComponent(GraphicsComponent.class).get_BOUNDS().x,
                        entity.getComponent(GraphicsComponent.class).get_BOUNDS().y,
                        entity.getComponent(GraphicsComponent.class).get_BOUNDS().width,
                        entity.getComponent(GraphicsComponent.class).get_BOUNDS().height,
                        (int) entity.getComponent(GridComponent.class).getGridLocation().getX(),
                        (int) entity.getComponent(GridComponent.class).getGridLocation().getY()
                );
                Game.scene().current().addEntityToScene(ccer);
                ccer.getComponent(CablePortsComponent.class).getNextFreePort(connectingToType).setConnectedEntity(e);
                e.getComponent(CablePortsComponent.class).getNextFreePort(connectingFromType).setConnectedEntity(ccer);
                return true;
            }
        }

        return false;
    }

    private boolean isNeighbor(Entity e1, Entity e2) {
        return calculateDistanceBetweenPoints(
                (Point) e1.getComponent(GridComponent.class).getGridLocation(),
                (Point) e2.getComponent(GridComponent.class).getGridLocation()
        ) <= 1.0;
    }

    private void colorizeAndPositionCable(CableEntity e) {
        CablePortsComponent cpc = e.getComponent(CablePortsComponent.class);
        int leftId = cpc.getCablePort(0, CablePortType.IN)
                .getConnectedEntity()
                .getComponent(CablePortsComponent.class)
                .getPortIdOfConnectedEntity(e);
        int rightId = cpc.getCablePort(0, CablePortType.OUT)
                .getConnectedEntity()
                .getComponent(CablePortsComponent.class)
                .getPortIdOfConnectedEntity(e);

        switch (leftId) {
            case 0 -> e.getComponent(GraphicsComponent.class).setLineColor(CableColors.PORT_1_COLOR);
            case 1 -> e.getComponent(GraphicsComponent.class).setLineColor(CableColors.PORT_2_COLOR);
            case 2 -> e.getComponent(GraphicsComponent.class).setLineColor(CableColors.PORT_3_COLOR);
            case 3 -> e.getComponent(GraphicsComponent.class).setLineColor(CableColors.PORT_4_COLOR);
        }

        e.getComponent(GraphicsComponent.class).setLine(new Point(
                        cpc.getCablePort(0, CablePortType.IN).getConnectedEntity().getComponent(GraphicsComponent.class).getBounds().x +
                        cpc.getCablePort(0, CablePortType.IN).getConnectedEntity().getComponent(GraphicsComponent.class).getBounds().width/2+40,
                        (int) (cpc.getCablePort(0, CablePortType.IN).getConnectedEntity().getComponent(GraphicsComponent.class).getBounds().y + (leftId%4 + 1.0) *
                                                cpc.getCablePort(0, CablePortType.IN).getConnectedEntity().getComponent(GraphicsComponent.class).getBounds().height/5)
                ),
                new Point(
                        cpc.getCablePort(0, CablePortType.OUT).getConnectedEntity().getComponent(GraphicsComponent.class).getBounds().x +
                                cpc.getCablePort(0, CablePortType.OUT).getConnectedEntity().getComponent(GraphicsComponent.class).getBounds().width/2-40,
                        (int) (cpc.getCablePort(0, CablePortType.OUT).getConnectedEntity().getComponent(GraphicsComponent.class).getBounds().y + (rightId%4 + 1.0) *
                                cpc.getCablePort(0, CablePortType.OUT).getConnectedEntity().getComponent(GraphicsComponent.class).getBounds().height/5)
                )
        );
    }
}