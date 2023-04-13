package de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.builder;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.IdGenerator;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.Query;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.collision.ColliderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.ImageObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.TextObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.gamepad.InputAction;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.handler.Handler;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.input.handler.HandlerType;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.components.*;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.cable.CablePort;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.cable.CablePortPosition;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.simulation.BuildPanelEntity;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.simulation.SimulationEntity;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.SimulationState;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.SimulationType;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.game.BuildScene;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.markov.MarkovProcessor;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.game.GameScene;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BuildHandler extends Handler {
    private BuilderState currentBuildState = BuilderState.NOT_BUILDING;
    private Entity currentBuilding = null;
    private int currentCableLayer = 0;
    private Entity cableBuildRepetitive = null;

    private int cellSize;

    public BuildHandler() {
        super(HandlerType.EVENT);
    }

    public int getCurrentCableLayer() {
        return currentCableLayer;
    }

    public void setCurrentCableLayer(int currentCableLayer) {
        this.currentCableLayer = currentCableLayer;
    }

    public BuilderState getCurrentBuildState() {
        return currentBuildState;
    }

    public void setCurrentBuildState(BuilderState currentBuildState) {
        this.currentBuildState = currentBuildState;
    }

    @Override
    public void handle(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            printAllEntitiesGridPosition();
            return;
        }
        if (e.getKeyCode() == KeyEvent.VK_O) {
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

    @Override
    public void handle(MouseEvent e) {
        Entity prev;
        Entity next;
        if (Game.scene().current() instanceof GameScene gs) {
            cellSize = gs.getCellSize();
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
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("ex1"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("ex2"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("ex3"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("ex4"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("ex5"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("sky-animated"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("goalMarker"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("oocMarker"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("passiveMarker"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("baseScore"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("accuracyScore"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("componentScore"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("totalScore"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("baseScoreContent"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("accuracyScoreContent"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("componentScoreContent"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("totalScoreContent"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("goalMarkerDesc"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("passiveMarkerDesc"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("oocMarkerDesc"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("goalMarkerText"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("passiveMarkerText"));
                        Game.scene().current().removeEntityFromScene(Game.scene().current().getEntityByName("oocMarkerText"));
                    }
                }
            }
        } else if (Game.scene().current() instanceof BuildScene bs) {
            cellSize = bs.getCellSize();
        }

        ArrayList<Entity> entities = (ArrayList<Entity>) Game.scene().current().getEntities().clone();

        for (Entity entity : entities) {
            if (entity.getComponent(TooltipComponent.class) != null) {
                if(Objects.equals(entity.getComponent(TooltipComponent.class).getType(), "PLACEHOLDER")) {
                    continue;
                }
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

        for (Entity entity : entities) {
            // check for click on a next/prev button
            if (entity instanceof BuildPanelEntity buildPanelEntity) {
                if (buildPanelEntity.getComponent(BuildComponent.class).getAmount() < 100)
                    buildPanelEntity.getComponent(RenderComponent.class)
                            .getRenderObjectsOfType(TextObject.class).get(0)
                            .setText(String.valueOf(buildPanelEntity.getComponent(BuildComponent.class).getAmount()));
                else
                    buildPanelEntity.getComponent(RenderComponent.class)
                            .getRenderObjectsOfType(TextObject.class).get(0)
                            .setText("");
            }
            // check the button clicked is left click
            if (e.getButton() == MouseEvent.BUTTON1) {
                // Handle events when builder is not building
                if (currentBuildState == BuilderState.NOT_BUILDING) {
                    // check location of the click
                    if (entity.getComponent(ColliderComponent.class) != null
                            && entity.getComponent(ColliderComponent.class).getCollisionObjects().get(0).getCollisionBoundaries() != null
                            && entity.getComponent(ColliderComponent.class).getCollisionObjects().get(0).getCollisionBoundaries()
                            .contains(Game.scale().upscalePoint(e.getPoint()))
                            && !entity.getComponent(ColliderComponent.class).getCollisionObjects().get(0).isDeactivated()) {
                        // check if the click was on a BuildComponent first -> create new Simulation Entity
                        if (entity.getComponent(BuildComponent.class) != null) {
                            // check amount left, >0? -> new Entity
                            if (entity.getComponent(BuildComponent.class).getAmount() > 0
                                    && entity.getComponent(BuildComponent.class).getSimulationType() != SimulationType.CABLE) {
                                SimulationEntity newEntity = null;

                                if (entity.getComponent(BuildComponent.class).getSimulationType() == SimulationType.COMPUTER) {
                                    newEntity = new SimulationEntity(
                                            entity.getName() + "_simulation", IdGenerator.generateId(),
                                            entity.getComponent(RenderComponent.class).getRenderObjects().get(0).getLocation().x,
                                            entity.getComponent(RenderComponent.class).getRenderObjects().get(0).getLocation().y,
                                            cellSize,
                                            cellSize,
                                            -1, -1,
                                            entity.getComponent(RenderComponent.class).getRenderObjectsOfType(ImageObject.class).get(0)
                                                    .getImage(),
                                            entity.getComponent(BuildComponent.class).getTileId(),
                                            entity.getComponent(BuildComponent.class).getFailureRatio(),
                                            entity.getComponent(BuildComponent.class).getSimulationType(),
                                            entity.getComponent(BuildComponent.class).getCorrectSignalsNeeded(),
                                            entity.getComponent(BuildComponent.class).getOutOfControlSignalsAccepted(),
                                            new int[]{0, 1, 2, 3}, new int[]{0, 1, 2, 3},
                                            true, entity.getComponent(BuildComponent.class).getFailureDetectionRatio()
                                    );
                                } else if (entity.getComponent(BuildComponent.class).getSimulationType() == SimulationType.SENSOR) {
                                    newEntity = new SimulationEntity(
                                            entity.getName() + "_simulation", IdGenerator.generateId(),
                                            entity.getComponent(RenderComponent.class).getRenderObjects().get(0).getLocation().x,
                                            entity.getComponent(RenderComponent.class).getRenderObjects().get(0).getLocation().y,
                                            cellSize,
                                            cellSize,
                                            -1, -1,
                                            entity.getComponent(RenderComponent.class).getRenderObjectsOfType(ImageObject.class).get(0)
                                                    .getImage(),
                                            entity.getComponent(BuildComponent.class).getTileId(),
                                            entity.getComponent(BuildComponent.class).getFailureRatio(),
                                            entity.getComponent(BuildComponent.class).getSimulationType(),
                                            entity.getComponent(BuildComponent.class).getCorrectSignalsNeeded(),
                                            entity.getComponent(BuildComponent.class).getOutOfControlSignalsAccepted(),
                                            new int[]{}, new int[]{0, 1, 2, 3},
                                            true, entity.getComponent(BuildComponent.class).getFailureDetectionRatio()
                                    );
                                    newEntity.getComponent(SimulationComponent.class).setSimulationState(SimulationState.CORRECT);
                                } else if (entity.getComponent(BuildComponent.class).getSimulationType() == SimulationType.ACTUATOR) {
                                    newEntity = new SimulationEntity(
                                            entity.getName() + "_simulation", IdGenerator.generateId(),
                                            entity.getComponent(RenderComponent.class).getRenderObjects().get(0).getLocation().x,
                                            entity.getComponent(RenderComponent.class).getRenderObjects().get(0).getLocation().y,
                                            cellSize,
                                            cellSize,
                                            -1, -1,
                                            entity.getComponent(RenderComponent.class).getRenderObjectsOfType(ImageObject.class).get(0)
                                                    .getImage(),
                                            entity.getComponent(BuildComponent.class).getTileId(),
                                            entity.getComponent(BuildComponent.class).getFailureRatio(),
                                            entity.getComponent(BuildComponent.class).getSimulationType(),
                                            entity.getComponent(BuildComponent.class).getCorrectSignalsNeeded(),
                                            entity.getComponent(BuildComponent.class).getOutOfControlSignalsAccepted(),
                                            new int[]{0, 1, 2, 3}, new int[]{},
                                            true, entity.getComponent(BuildComponent.class).getFailureDetectionRatio()
                                    );
                                } else if (entity.getComponent(BuildComponent.class).getSimulationType() == SimulationType.VOTE) {
                                    newEntity = new SimulationEntity(
                                            entity.getName() + "_simulation", IdGenerator.generateId(),
                                            entity.getComponent(RenderComponent.class).getRenderObjects().get(0).getLocation().x,
                                            entity.getComponent(RenderComponent.class).getRenderObjects().get(0).getLocation().y,
                                            cellSize,
                                            cellSize,
                                            -1, -1,
                                            entity.getComponent(RenderComponent.class).getRenderObjectsOfType(ImageObject.class).get(0)
                                                    .getImage(),
                                            entity.getComponent(BuildComponent.class).getTileId(),
                                            entity.getComponent(BuildComponent.class).getFailureRatio(),
                                            entity.getComponent(BuildComponent.class).getSimulationType(),
                                            entity.getComponent(BuildComponent.class).getCorrectSignalsNeeded(),
                                            entity.getComponent(BuildComponent.class).getOutOfControlSignalsAccepted(),
                                            new int[]{0, 1, 2, 3}, new int[]{0, 1, 2, 3},
                                            true, entity.getComponent(BuildComponent.class).getFailureDetectionRatio()
                                    );
                                    System.out.println("added new vote entity");
                                }

                                currentBuilding = newEntity;
                                Game.scene().current().addEntityToScene(newEntity);

                                entity.getComponent(BuildComponent.class)
                                        .subtractFromAmount();
                                if (entity.getComponent(BuildComponent.class).getAmount() > 100) {
                                    entity.getComponent(RenderComponent.class)
                                            .getRenderObjectsOfType(TextObject.class)
                                            .get(0)
                                            .setText("");
                                } else {
                                    entity.getComponent(RenderComponent.class)
                                            .getRenderObjectsOfType(TextObject.class)
                                            .get(0)
                                            .setText(String.valueOf(entity.getComponent(BuildComponent.class)
                                                    .getAmount()
                                            ));
                                }
                                currentBuildState = BuilderState.BUILDING_SIMULATION;

                                break;
                            } else if (entity.getComponent(BuildComponent.class).getAmount() > 0
                                    && entity.getComponent(BuildComponent.class).getSimulationType() == SimulationType.CABLE) {
                                SimulationEntity newEntity = new SimulationEntity(
                                        entity.getName() + "_cable", IdGenerator.generateId(),
                                        entity.getComponent(RenderComponent.class).getRenderObjects().get(0).getLocation().x,
                                        entity.getComponent(RenderComponent.class).getRenderObjects().get(0).getLocation().y,
                                        cellSize,
                                        cellSize,
                                        -1, -1,
                                        entity.getComponent(RenderComponent.class).getRenderObjectsOfType(ImageObject.class).get(0)
                                                .getImage(),
                                        entity.getComponent(BuildComponent.class).getTileId(),
                                        entity.getComponent(BuildComponent.class).getFailureRatio(),
                                        entity.getComponent(BuildComponent.class).getSimulationType(),
                                        1, 0,
                                        new int[]{entity.getComponent(BuildComponent.class).getPortId()},
                                        new int[]{entity.getComponent(BuildComponent.class).getPortId()},
                                        true, 0f
                                );
                                cableBuildRepetitive = entity;
                                currentBuilding = newEntity;
                                Game.scene().current().addEntityToScene(newEntity);

                                entity.getComponent(BuildComponent.class)
                                        .subtractFromAmount();
                                if (entity.getComponent(BuildComponent.class).getAmount() > 100) {
                                    entity.getComponent(RenderComponent.class)
                                            .getRenderObjectsOfType(TextObject.class)
                                            .get(0)
                                            .setText("");
                                } else {
                                    entity.getComponent(RenderComponent.class)
                                            .getRenderObjectsOfType(TextObject.class)
                                            .get(0)
                                            .setText(String.valueOf(entity.getComponent(BuildComponent.class)
                                                    .getAmount()
                                            ));
                                }

                                currentBuildState = BuilderState.BUILDING_CABLE;
                                break;
                            }
                        }
                    }
                }
                else if(currentBuildState == BuilderState.REMOVING) {
                    removeItem(e);
                }
                // check if state is building
                else if (currentBuildState == BuilderState.BUILDING_SIMULATION) {
                    // try to place the component
                    if (placeComponent(currentBuilding)) {
                        currentBuilding = null;
                        currentBuildState = BuilderState.NOT_BUILDING;
                        break;
                    } else {
                        if(Game.scene().current().getEntityByName("Build Panel")
                                .getComponent(RenderComponent.class).getRenderObjectsOfType(ImageObject.class)
                                .get(0).getBounds().contains(e.getPoint())) {
                            // remove the component
                            if (putBackToStack(currentBuilding)) {
                                currentBuildState = BuilderState.NOT_BUILDING;

                                cableBuildRepetitive = null;
                                currentBuilding = null;
                            }
                        }
                    }
                }
                // check if state is building cable refactored
                else if (currentBuildState == BuilderState.BUILDING_CABLE) {
                    // try to place component
                    if (placeCable(currentBuilding)) {
                        currentBuilding = null;
                        currentBuildState = BuilderState.NOT_BUILDING;

                        if (cableBuildRepetitive.getComponent(BuildComponent.class).getAmount() > 0
                                && cableBuildRepetitive.getComponent(BuildComponent.class).getSimulationType() == SimulationType.CABLE) {
                            SimulationEntity newEntity = new SimulationEntity(
                                    cableBuildRepetitive.getName() + "_cable", IdGenerator.generateId(),
                                    cableBuildRepetitive.getComponent(RenderComponent.class).getRenderObjects().get(0).getLocation().x,
                                    cableBuildRepetitive.getComponent(RenderComponent.class).getRenderObjects().get(0).getLocation().y,
                                    cellSize,
                                    cellSize,
                                    -1, -1,
                                    cableBuildRepetitive.getComponent(RenderComponent.class).getRenderObjectsOfType(ImageObject.class).get(0)
                                            .getImage(),
                                    cableBuildRepetitive.getComponent(BuildComponent.class).getTileId(),
                                    cableBuildRepetitive.getComponent(BuildComponent.class).getFailureRatio(),
                                    cableBuildRepetitive.getComponent(BuildComponent.class).getSimulationType(),
                                    1, 0,
                                    new int[]{cableBuildRepetitive.getComponent(BuildComponent.class).getPortId()},
                                    new int[]{cableBuildRepetitive.getComponent(BuildComponent.class).getPortId()},
                                    true, 0f
                            );

                            currentBuilding = newEntity;
                            Game.scene().current().addEntityToScene(newEntity);

                            cableBuildRepetitive.getComponent(BuildComponent.class)
                                    .subtractFromAmount();
                            if (cableBuildRepetitive.getComponent(BuildComponent.class).getAmount() > 100) {
                                cableBuildRepetitive.getComponent(RenderComponent.class)
                                        .getRenderObjectsOfType(TextObject.class)
                                        .get(0)
                                        .setText("");
                            } else {
                                cableBuildRepetitive.getComponent(RenderComponent.class)
                                        .getRenderObjectsOfType(TextObject.class)
                                        .get(0)
                                        .setText(String.valueOf(cableBuildRepetitive.getComponent(BuildComponent.class)
                                                .getAmount()
                                        ));
                            }
                            currentBuildState = BuilderState.BUILDING_CABLE;

                            break;
                        } else {
                            break;
                        }
                    } else if(Game.scene().current().getEntityByName("Build Panel")
                            .getComponent(RenderComponent.class).getRenderObjectsOfType(ImageObject.class)
                            .get(0).getBounds().contains(e.getPoint())) {
                        // remove the component
                        if (putBackToStack(currentBuilding)) {
                            currentBuildState = BuilderState.NOT_BUILDING;

                            cableBuildRepetitive = null;
                            currentBuilding = null;
                        }
                    }
                }
            }
            // check rotation upwards/downwards to rotate

            else if (e instanceof MouseWheelEvent mouseWheelEvent && currentBuildState == BuilderState.NOT_BUILDING) {
                System.out.println("Mouse Wheel Event Detected");
                if (entity.getComponent(ColliderComponent.class) != null
                        && entity.getComponent(ColliderComponent.class).getCollisionObjects().get(0).getCollisionBoundaries()
                        .contains(Game.scale().upscalePoint(e.getPoint()))) {
                    System.out.println("Is in bounds of entity");
                    if(mouseWheelEvent.getWheelRotation() < 0) {
                        if (entity.getComponent(SimulationComponent.class) != null) {
                            if (entity.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CABLE) {
                                int inId = entity.getComponent(CablePortsComponent.class).getInIds()[0];
                                if (inId == currentCableLayer) {
                                    entity.getComponent(CablePortsComponent.class).getCablePort(inId, CablePortType.IN).cyclePosition();
                                    entity.getComponent(CablePortsComponent.class).updateImage();
                                    updateCablePorts(entity, false);
                                    return;
                                }

                            }
                        }
                    } else if(mouseWheelEvent.getWheelRotation() > 0) {
                        if (entity.getComponent(SimulationComponent.class) != null) {
                            if (entity.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CABLE) {
                                int outId = entity.getComponent(CablePortsComponent.class).getOutIds()[0];
                                if (outId == currentCableLayer) {
                                    entity.getComponent(CablePortsComponent.class).getCablePort(outId, CablePortType.OUT).cyclePosition();
                                    entity.getComponent(CablePortsComponent.class).updateImage();
                                    updateCablePorts(entity, false);
                                    return;
                                }
                            }
                        }
                    }
                }
            }

            // check the button clicked is right click
            else if (e.getButton() == MouseEvent.BUTTON3) {
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
                    if (removeItem(e)) return;

                }
                // check if no button is clicked
            } else if (e.getButton() == MouseEvent.NOBUTTON) {
                if (currentBuildState == BuilderState.BUILDING_SIMULATION || currentBuildState == BuilderState.BUILDING_CABLE) {
                    currentBuilding.getComponent(RenderComponent.class).reposition(Game.scale().upscalePoint(e.getPoint()));
                }
            }
            if (e instanceof MouseWheelEvent mouseWheelEvent && currentBuildState == BuilderState.BUILDING_CABLE) {
                if(mouseWheelEvent.getWheelRotation() < 0) {
                    System.out.println("Mouse event <0");
                    currentBuilding.getComponent(CablePortsComponent.class).updateImage();
                    if (currentBuilding.getComponent(SimulationComponent.class) != null) {
                        if (currentBuilding.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CABLE) {
                            int inId = currentBuilding.getComponent(CablePortsComponent.class).getInIds()[0];
                            currentBuilding.getComponent(CablePortsComponent.class).getCablePort(inId, CablePortType.IN).cyclePosition();
                            currentBuilding.getComponent(CablePortsComponent.class).updateImage();
                        }
                    }
                } else if(mouseWheelEvent.getWheelRotation() > 0) {
                    System.out.println("Mouse event >0");
                    currentBuilding.getComponent(CablePortsComponent.class).updateImage();
                    if (currentBuilding.getComponent(SimulationComponent.class) != null) {
                        if (currentBuilding.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CABLE) {
                            int outId = currentBuilding.getComponent(CablePortsComponent.class).getOutIds()[0];
                            currentBuilding.getComponent(CablePortsComponent.class).getCablePort(outId, CablePortType.OUT).cyclePosition();
                            currentBuilding.getComponent(CablePortsComponent.class).updateImage();
                        }

                    }
                }
                return;
            }
        }
    }

    private boolean removeItem(MouseEvent e) {
        Point gridLocation = findEntityGridPosition(Game.scale().upscalePoint(e.getPoint()));
        if (gridLocation == null) {
            return true;
        } else {
            ArrayList<Entity> entitiesAtSameCell = new ArrayList<>();
            entitiesAtSameCell = getEntitiesAtGridPosition(gridLocation);

            for (Entity remove : entitiesAtSameCell) {
                if (remove.isRemovable() && remove.getComponent(SimulationComponent.class) != null) {
                    if (putBackToStack(remove)) {
                        updateCablePorts(remove, true);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void handle(InputAction e) {

    }

    private boolean putBackToStack(Entity e) {
        if (e instanceof SimulationEntity se) {
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
                                        build.getComponent(RenderComponent.class)
                                                .getRenderObjectsOfType(TextObject.class)
                                                .get(0)
                                                .setText("");
                                    } else {
                                        build.getComponent(RenderComponent.class)
                                                .getRenderObjectsOfType(TextObject.class)
                                                .get(0)
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

                        updateCablePorts(e, false);

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

                    updateCablePorts(e, false);

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

                updateCablePorts(e, false);


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
                    if(e.getComponent(SimulationComponent.class) != null && e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.PLACEHOLDER) {
                        continue;
                    }
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

    /**
     * Update the cable ports of an entity and all surrounding entities accordingly
     * @param e: the entity to update
     */
    private void updateCablePorts(Entity e, boolean remove) {
        // Check if has CablePortsComponent
        if(e.getComponent(CablePortsComponent.class) == null) {
            Game.logger().info("Can't find a Cable Port component for Entity: " + e.getName());
            return;
        }

        // check if component is removed, if so, set all connections to "null". return after, as no connections
        // need to be set here.
        if(remove) {
            for(Entity possibleConnections : Query.getEntitiesWithComponent(CablePortsComponent.class)) {
                for(CablePort cablePort : possibleConnections.getComponent(CablePortsComponent.class).getCablePorts()) {
                    if(cablePort.getConnectedEntity() == e) {
                        cablePort.setConnectedEntity(null);
                    }
                }
            }

            return;
        }

        // take the entities grid position and find entities on the left, right, bottom and top of it.
        if(e.getComponent(GridComponent.class) == null) {
            Game.logger().info("Can't find a grid component for Entity: " + e.getName());
            return;
        }
        Point gridPosition = (Point) e.getComponent(GridComponent.class).getGridLocation();

        ArrayList<Entity> leftFrom = getEntitiesAtGridPosition(new Point(gridPosition.x - 1, gridPosition.y))
                .stream()
                .filter(f -> f.getComponent(CablePortsComponent.class) != null)
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Entity> bottomFrom = getEntitiesAtGridPosition(new Point(gridPosition.x, gridPosition.y + 1))
                .stream()
                .filter(f -> f.getComponent(CablePortsComponent.class) != null)
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Entity> rightFrom = getEntitiesAtGridPosition(new Point(gridPosition.x + 1, gridPosition.y))
                .stream()
                .filter(f -> f.getComponent(CablePortsComponent.class) != null)
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Entity> topFrom = getEntitiesAtGridPosition(new Point(gridPosition.x, gridPosition.y - 1))
                .stream()
                .filter(f -> f.getComponent(CablePortsComponent.class) != null)
                .collect(Collectors.toCollection(ArrayList::new));

        // iterate through each of the lists and reconnect the cable ports
        connectPortsOnSides(e, leftFrom, CablePortPosition.LEFT, CablePortPosition.RIGHT);

        connectPortsOnSides(e, rightFrom, CablePortPosition.RIGHT, CablePortPosition.LEFT);

        connectPortsOnSides(e, bottomFrom, CablePortPosition.BOTTOM, CablePortPosition.TOP);

        connectPortsOnSides(e, topFrom, CablePortPosition.TOP, CablePortPosition.BOTTOM);
    }

    private void connectPortsOnSides(Entity e, ArrayList<Entity> entities, CablePortPosition port1, CablePortPosition port2) {
        for(Entity entity : entities) {
            for(CablePort connectToCablePort : entity.getComponent(CablePortsComponent.class).getCablePorts()) {
                for(CablePort connectFromCablePort : e.getComponent(CablePortsComponent.class).getCablePorts()) {
                    // only update if the port id is equal
                    if(connectFromCablePort.getPortId() == connectToCablePort.getPortId()) {
                        if(CablePortType.canConnect(connectFromCablePort.getType(), connectToCablePort.getType())) {
                            if(connectFromCablePort.getPosition() == port1
                                    && connectToCablePort.getPosition() == port2) {
                                connectFromCablePort.setConnectedEntity(entity);
                                connectToCablePort.setConnectedEntity(e);
                            }
                        }
                    }
                }
            }
        }
    }
}