package game.handler;

import engine.Game;
import engine.IdGenerator;
import engine.ecs.Query;
import engine.ecs.component.graphics.GraphicsComponent;
import engine.ecs.entity.Entity;
import engine.ecs.entity.GenericButton;
import engine.ecs.system.SystemHandle;
import game.components.*;
import game.entities.CablePort;
import game.entities.ScoreBox;
import game.handler.simulation.SimulationState;
import game.handler.simulation.SimulationType;
import game.handler.simulation.markov.MarkovProcessor;
import game.intent.SaveScoreIntent;
import game.intent.StartIntent;
import game.scenes.GameScene;

import java.awt.*;
import java.util.*;


public class SimulationSystem extends SystemHandle {
    private ArrayList<Entity> tempGroup = new ArrayList<>();
    private double eps = 0.00000001; //delta because double has rounding errors
    int frameCount = 0;

    public int calculateScore() {
        //Base score for finishing the level
        int score = 100;

        double probabilities[] = MarkovProcessor
                .getProbabilityForStatesWith(
                        MarkovProcessor.currentSystemState,
                        1, 0, 0, 0, 20, 20
                );

        if(Game.scene().current() instanceof GameScene gs) {
            score -= Math.abs(Math.abs(Math.log10(probabilities[0])) - Math.abs(Math.log10(gs.getGoal()))) * 10;

            for(Entity e : gs.getEntities()) {
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
        Thread animation = new Thread() {
            @Override
            public void run() {
                while (!this.isInterrupted()) {
                    try {
                        sleep(1000 / 240);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (Game.scene().current() instanceof GameScene gs) {
                        System.out.println("running the animation");
                        gs.playAircraftAnimation();
                    }
                }
            }

            @Override
            public synchronized void start() {
                if (Game.scene().current() instanceof GameScene gs) {
                    gs.setAircraftAnimation();
                }
                super.start();
            }

            @Override
            public void interrupt() {
                super.interrupt();
                if (Game.scene().current() instanceof GameScene gs) {
                    gs.removeAircraftAnimation();
                }
            }
        };
        animation.start();

        markov();

        animation.interrupt();

        if (validateGoal()) {
            if (Game.scene().current() instanceof GameScene gs) {
                if (!gs.isLevelPassed()) {
                    gs.setLevelPassed(true);
                    int score = calculateScore();
                    gs.displayLevelFinished(score);
                }
            }
        } else {
            if(Game.scene().current() instanceof GameScene gs) {
                gs.displayLevelNotFinished();
            }
        }
    }

    private boolean validateGoal() {
        double[] probabilities = MarkovProcessor
                .getProbabilityForStatesWith(
                        MarkovProcessor.currentSystemState,
                        1, 0, 0, 0, 20, 20
                );
        System.out.println("passive: " + probabilities[0]);
        System.out.println("ooc: " + probabilities[1]);

        if(Game.scene().current() instanceof GameScene gs)
        if(probabilities[0]-eps <= gs.getGoal() && probabilities[1]-eps <= gs.getGoal()) {
            return true;
        }

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
                if (Collections.frequency(e.getComponent(SimulationComponent.class).getInputStates(), SimulationState.CORRECT)
                        >= e.getComponent(SimulationComponent.class).getCorrectSignalsNeeded()) {
                    e.getComponent(SimulationComponent.class).setSimulationState(SimulationState.CORRECT);
                } else {
                    e.getComponent(SimulationComponent.class).setSimulationState(SimulationState.INOPERATIVE);
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

    private void medianVoter() {

    }

    private void averageVoter() {

    }

    private void MNVoter() {

    }

    private SimulationState winnerTakesItAllVoter(ArrayList<Entity> inputs) {
        HashMap<SimulationState, Integer> counter = new HashMap<>();

        // fill the hashmap counter
        for(Entity e : inputs) {
            counter.put(e.getComponent(SimulationComponent.class).getSimulationState(), 0);
        }

        // add if same
        for(Entity e : inputs) {
            counter.merge(e.getComponent(SimulationComponent.class).getSimulationState(), 1, Integer::sum);
        }

        Map.Entry<SimulationState, Integer> max = null;
        for(Map.Entry<SimulationState, Integer> entry : counter.entrySet()) {
            if(entry.getValue() > 0) {
                max = entry;
            }
        }

        return max.getKey();
    }

    private void calculateGroupFailureRatio(ArrayList<Entity> group) {
        ArrayList<Entity> sensors = new ArrayList<>();
        ArrayList<Entity> actuators = new ArrayList<>();
        ArrayList<Entity> cpus = new ArrayList<>();

        // distinct components
        for(Entity e : group) {
            if(e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CPU) {
                cpus.add(e);
            } else if(e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.SENSOR) {
                sensors.add(e);
            } else if(e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.ACTUATOR) {
                actuators.add(e);
            }
        }

        // Single Component Failure
        float failureProbability = 0f;
        for(Entity e : group) {
            failureProbability += e.getComponent(SimulationComponent.class).getFailureRatio();
        }
        /*

        System.out.println(group);
        System.out.println("Failure Probability for Single Component Failure: " + failureProbability);
        System.out.println("... for a 2hrs flight: " + failureProbability * 2);

         */
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

    /**
     * checks if a group is connected correctly and is able to simulate
     * @param group: all entities to check for (a group)
     * @return false if not properly connected, return true if properly connected
     */
    private boolean checkGroupState(ArrayList<Entity> group) {


        return false;
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



        // debugger
        /*System.out.println("-----groups---------");
        for(Entity e : gatherRelevantEntities()) {
            if(e.getComponent(SimulationComponent.class) != null) {
                System.out.println(e.getName() + "|||||" + e.getComponent(SimulationComponent.class).getGroupId());
            }
        }
        */

    }

    private void updateGraphics() {
        for(Entity e : gatherRelevantEntities()) {
            if(e.getComponent(SimulationComponent.class) == null || e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CABLE) {
                continue;
            }

            switch(e.getComponent(SimulationComponent.class).getSimulationState()) {
                case CORRECT -> e.getComponent(GraphicsComponent.class).setFillColor(new Color(0, 255, 0, 40));
                case FAIL -> e.getComponent(GraphicsComponent.class).setFillColor(new Color(255, 132, 0, 40));
                case PASSIVE -> e.getComponent(GraphicsComponent.class).setFillColor(new Color(0, 0, 255, 40));
                case OUT_OF_CONTROL -> e.getComponent(GraphicsComponent.class).setFillColor(new Color(255, 0, 0, 40));
                case INOPERATIVE -> e.getComponent(GraphicsComponent.class).setFillColor(new Color(255, 235, 0, 40));
            }
        }
    }

    private ArrayList<Entity> findCPUs(ArrayList<Entity> group) {
        ArrayList<Entity> cpus = new ArrayList<>();

        for(Entity g : group) {
            if(g.getComponent(SimulationComponent.class) != null && g.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CPU) {
                cpus.add(g);
            }
        }
        return cpus;
    }

    private ArrayList<Entity> getSensors() {
        ArrayList<Entity> relevantEntities = new ArrayList<>();

        for(Entity e : Game.scene().current().getEntities()) {
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

            for(CablePort port : ports) {
                if(group.contains(port.getConnectedEntity())) {
                    continue;
                }

                if(port.getType() == CablePortType.IN) {
                    continue;
                }

                if(port.getConnectedEntity() == null) {
                    continue;
                }

                group.add(port.getConnectedEntity());
                group.addAll(getInterconnection(port.getConnectedEntity()));
            }
        }

        // refactored version:

        // TODO: old version (delete when the other one is finished):
        /*
        if(e.getComponent(CablePortsComponent.class) != null) {
            ArrayList<CablePort> ports = e.getComponent(CablePortsComponent.class).getCablePorts();

            for(CablePort port : ports) {
                if(group.contains(port.getConnectedEntity())) {
                    continue;
                }

                if(port.getType() == CablePortType.IN) {
                    continue;
                }

                if(port.getConnectedEntity() == null) {
                    continue;
                }

                group.add(port.getConnectedEntity());
                tempGroup.addAll(group);
                group.addAll(getInterconnection(port.getConnectedEntity()));
                tempGroup.addAll(group);
            }
        }

         */

        return group;
    }
}
