package de.unistuttgart.ils.aircraftsystemsarchitect.game.handler;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.Query;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.ShapeObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.system.SystemHandle;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.colorpalettes.Bit8;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.components.BuildComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.components.CablePortType;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.components.CablePortsComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.components.SimulationComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.cable.CablePort;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.SimulationState;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.SimulationType;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.builder.BuildHandler;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.markov.MarkovProcessor;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.game.GameScene;

import java.util.*;


public class SimulationSystem extends SystemHandle {
    private ArrayList<Entity> tempGroup = new ArrayList<>();
    private final double eps = 0.00000001; //delta because double has rounding errors
    int frameCount = 0;

    public int calculateScore() {
        //Base score for finishing the level
        int score = 100;

        GameScene scene;
        if (Game.scene().current() instanceof GameScene gameScene) {
            scene = gameScene;
        } else {
            return -1;
        }
        double[] probabilities = MarkovProcessor
                .getProbabilityForStatesWith(
                        MarkovProcessor.currentSystemState,
                        scene.getAccGoal(), scene.getSensGoal(), scene.getcGoal(), 0, 20, 20
                );

        if (Game.scene().current() instanceof GameScene gs) {
            score -= Math.abs(Math.abs(Math.log10(probabilities[0])) - Math.abs(Math.log10(gs.getGoal()))) * 10;

            for (Entity e : gs.getEntities()) {
                if(e.getComponent(BuildComponent.class) != null && e.getComponent(BuildComponent.class).getSimulationType() != SimulationType.CABLE) {
                    score += 10 * e.getComponent(BuildComponent.class).getAmount();
                }
            }
        }

        return score;
    }

    @Override
    public void handle() {
        updateGroupIds();
        updateInputIds();
        updateStates();
        updateGraphics();
    }

    public synchronized void finish() {
        if (Game.scene().current() instanceof GameScene gs) {
            gs.playSky();
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    markov();
                    double[] probabilities = MarkovProcessor
                            .getProbabilityForStatesWith(
                                    MarkovProcessor.currentSystemState,
                                    gs.getAccGoal(), gs.getSensGoal(), gs.getcGoal(), 0, 20, 20
                            );
                    if (validateGoal()) {
                        if (!gs.isLevelPassed()) {
                            gs.setLevelPassed(true);
                            int score = calculateScore();
                            gs.displayLevelFinished(score, probabilities);
                        }
                    } else {
                        gs.displayLevelNotFinished(probabilities);
                    }
                }
            });
            thread.start();
        }
    }

    private boolean validateGoal() {
        GameScene scene;
        if (Game.scene().current() instanceof GameScene gameScene) {
            scene = gameScene;
        } else {
            return false;
        }
        double[] probabilities = MarkovProcessor
                .getProbabilityForStatesWith(
                        MarkovProcessor.currentSystemState,
                        scene.getAccGoal(), scene.getSensGoal(), scene.getcGoal(), 0, 20, 20
                );
        System.out.println("passive: " + probabilities[0]);
        System.out.println("ooc: " + probabilities[1]);

        if (Game.scene().current() instanceof GameScene gs)
            return probabilities[0] - eps <= gs.getGoal() && probabilities[1] - eps <= gs.getGoal();

        return false;
    }

    private void updateInputIds() {
        for(Entity e : gatherRelevantEntities()) {
            if(e.getComponent(CablePortsComponent.class) != null) {
                if(e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.SENSOR) {
                    continue;
                } else {
                    e.getComponent(SimulationComponent.class).resetInputIds();
                    e.getComponent(SimulationComponent.class).resetInputStates();
                }
                for(Entity connected : e.getComponent(CablePortsComponent.class).getAllConnectedEntities()) {
                    if(e.getComponent(SimulationComponent.class).getInputIds().contains(connected.getComponent(SimulationComponent.class).getOwnId())) {
                        continue;
                    }
                    e.getComponent(SimulationComponent.class).addInputId(connected.getComponent(SimulationComponent.class).getOwnId());
                    e.getComponent(SimulationComponent.class).addInputState(connected.getComponent(SimulationComponent.class).getSimulationState());
                }
            }
        }
    }

    private void updateStates() {
        for(Entity e : gatherRelevantEntities()) {
            if(e.getComponent(SimulationComponent.class) == null) {
                continue;
            }

            if(e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.VOTE) {
                e.getComponent(SimulationComponent.class).setSimulationState(voteOutputState(e));
            } else {
                if (Collections.frequency(e.getComponent(SimulationComponent.class).getInputStates(), SimulationState.OUT_OF_CONTROL)
                        > e.getComponent(SimulationComponent.class).getOutOfControlSignalsAccepted()) {
                    e.getComponent(SimulationComponent.class).setSimulationState(SimulationState.OUT_OF_CONTROL);
                } else if (Collections.frequency(e.getComponent(SimulationComponent.class).getInputStates(), SimulationState.CORRECT)
                        >= e.getComponent(SimulationComponent.class).getCorrectSignalsNeeded()) {
                    e.getComponent(SimulationComponent.class).setSimulationState(SimulationState.CORRECT);
                } else if (e.getComponent(SimulationComponent.class).getInputStates().size() < e.getComponent(SimulationComponent.class).getCorrectSignalsNeeded()) {
                    e.getComponent(SimulationComponent.class).setSimulationState(SimulationState.INOPERATIVE);
                } else {
                    e.getComponent(SimulationComponent.class).setSimulationState(SimulationState.PASSIVE);
                }
            }
        }
    }

    public SimulationState voteOutputState(Entity e) {
        ArrayList<SimulationState> inputStates = e.getComponent(SimulationComponent.class).getInputStates();
        int inputs = inputStates.size();
        switch (inputs) {
            case 0:
                return SimulationState.INOPERATIVE;
            case 1:
                return inputStates.get(0);
            case 2:
                if(inputStates.get(0) == inputStates.get(1)) {
                    return inputStates.get(0);
                } else {
                    return SimulationState.PASSIVE;
                }
            case 3:
                if(Collections.frequency(inputStates, SimulationState.CORRECT) >= 2) {
                    return SimulationState.CORRECT;
                } else {
                    return SimulationState.PASSIVE;
                }
            case 4:
                if(Collections.frequency(inputStates, SimulationState.CORRECT) >= 3) {
                    return SimulationState.CORRECT;
                } else if(Collections.frequency(inputStates, SimulationState.class) == 2 && Collections.frequency(inputStates, SimulationState.PASSIVE) >= 1) {
                    return SimulationState.CORRECT;
                } else {
                    return SimulationState.PASSIVE;
                }
        }
        return null;
    }

    private boolean validateGroup(ArrayList<Entity> group) {
        boolean hasCPU = false;
        boolean hasSensor = false;
        boolean hasActuator = false;
        for(Entity e : group) {
            if(e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CPU) {
                hasCPU = true;
            } else if(e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.SENSOR) {
                hasSensor = true;
            } else if(e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.ACTUATOR) {
                hasActuator = true;
            }
        }
        return hasCPU && hasSensor && hasActuator;
    }

    /**
     * Prefiltering method to skip unnecessary Entities such as buttons or cables
     * @return: All relevant entities for the calculation
     */
    private ArrayList<Entity> gatherRelevantEntities() {
        return Query.getEntitiesWithComponent(SimulationComponent.class);
    }

    /**
     * get all entities assigned to a specified group
     * @param id: the group id to look up
     * @return an ArrayList with all Entities in that group
     */
    private ArrayList<Entity> getEntitiesByGroupdId(int id) {
        ArrayList<Entity> relevantEntities = gatherRelevantEntities();
        ArrayList<Entity> group = new ArrayList<>();

        for(Entity relevantEntity : relevantEntities) {
            if(relevantEntity.getComponent(SimulationComponent.class) != null
                    && relevantEntity.getComponent(SimulationComponent.class).getGroupIds().contains(id)) {
                group.add(relevantEntity);
            }
        }

        return group;
    }

    private int[] getDistinctGroupIds() {
        ArrayList<Integer> distinctIds = new ArrayList<>();
        ArrayList<Entity> relevantEntities = gatherRelevantEntities();

        for(Entity relevantEntity : relevantEntities) {
            for(int id : relevantEntity.getComponent(SimulationComponent.class).getGroupIds()) {
                if(!distinctIds.contains(id)) {
                    distinctIds.add(id);
                }
            }
        }

        return distinctIds.stream().mapToInt(i -> i).toArray();
    }

    private void markov() {
        ArrayList<Entity> entities = new ArrayList<>();
        int[] groupIds = getDistinctGroupIds();

        for(int i : groupIds) {
            ArrayList<Entity> group = getEntitiesByGroupdId(i);

            if(validateGroup(group)) {
                for (Entity e : group) {
                    if (e.getComponent(SimulationComponent.class) != null) {
                        if (e.getComponent(SimulationComponent.class).getSimulationState() == SimulationState.INOPERATIVE) {
                            continue;
                        }
                        if (!entities.contains(e))
                            entities.add(e);
                    }
                }
            }
        }

        MarkovProcessor.entities = entities;
        MarkovProcessor.groupIds = groupIds;

        MarkovProcessor.generateCurrentSystemState();
        MarkovProcessor.markovStart(MarkovProcessor.currentSystemState);
    }

    /**
     * Update the group id of each simulation entity based on cable connections
     */
    private void updateGroupIds() {
        // TODO: Implement that cable port in 0 and out 0 only lead to group id update / connection. if 0, 1 and 0 is connected, only 0

        // should be used
        ArrayList<Entity> sensors = getSensors();

        for(Entity sensor : sensors) {
            tempGroup = new ArrayList<>();
            try {
                ArrayList<Entity> connectedTo = getInterconnection(sensor);
                connectedTo.add(sensor);

                for(Entity e : connectedTo) {
                    if(e.getComponent(SimulationComponent.class) != null) {
                        if(!sensors.contains(e)) {
                            if(!e.getComponent(SimulationComponent.class).getGroupIds().contains(sensor.getComponent(SimulationComponent.class).getOwnId())) {
                                e.getComponent(SimulationComponent.class).addGroupId(sensor.getComponent(SimulationComponent.class).getOwnId());
                            }
                        }
                    }
                }
            } catch(StackOverflowError ex) {
                ex.printStackTrace();
                Game.input().getHandler(BuildHandler.class).printAllCablePorts();
                System.exit(1);
            }
        }
    }

    private void updateGraphics() {
        for(Entity e : gatherRelevantEntities()) {
            if (e.getComponent(SimulationComponent.class) == null
                    || e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CABLE
                    || e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.PLACEHOLDER) {
                continue;
            }

            switch (e.getComponent(SimulationComponent.class).getSimulationState()) {
                case CORRECT -> e.getComponent(RenderComponent.class).getRenderObjectsOfType(ShapeObject.class).get(0).setFillColor(Bit8.GREEN);
                case FAIL -> e.getComponent(RenderComponent.class).getRenderObjectsOfType(ShapeObject.class).get(0).setFillColor(Bit8.ORANGE);
                case PASSIVE -> e.getComponent(RenderComponent.class).getRenderObjectsOfType(ShapeObject.class).get(0).setFillColor(Bit8.BLUE);
                case OUT_OF_CONTROL -> e.getComponent(RenderComponent.class).getRenderObjectsOfType(ShapeObject.class).get(0).setFillColor(Bit8.RED);
                case INOPERATIVE -> e.getComponent(RenderComponent.class).getRenderObjectsOfType(ShapeObject.class).get(0).setFillColor(Bit8.YELLOW);
            }
        }
    }

    private synchronized ArrayList<Entity> getSensors() {
        ArrayList<Entity> relevantEntities = new ArrayList<>();

        for(Entity e : (ArrayList<Entity>) Game.scene().current().getEntities().clone()) {
            if(e.getComponent(SimulationComponent.class) != null
                    && e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.SENSOR) {
                relevantEntities.add(e);
            }
        }

        return relevantEntities;
    }

    private ArrayList<Entity> getInterconnection(Entity e) {
        ArrayList<Entity> group = new ArrayList<>();

        if(e.getComponent(CablePortsComponent.class) != null) {
            ArrayList<CablePort> ports = e.getComponent(CablePortsComponent.class).getCablePorts();

            for (CablePort port : ports) {
                if (group.contains(port.getConnectedEntity())) {
                    continue;
                }

                if (port.getType() == CablePortType.IN) {
                    continue;
                }

                if (port.getConnectedEntity() == null) {
                    continue;
                }

                group.add(port.getConnectedEntity());
                group.addAll(getInterconnection(port.getConnectedEntity()));
            }
        }

        return group;
    }
}
