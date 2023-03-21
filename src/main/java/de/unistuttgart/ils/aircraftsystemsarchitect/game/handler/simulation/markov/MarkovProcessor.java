package de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.markov;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.components.SimulationComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.SimulationState;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.SimulationType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * a generic markov chain processor class that can be called from anywhere
 */
public class MarkovProcessor {

    public static ArrayList<Entity> entities = new ArrayList<>();
    public static int[] groupIds;
    public static MarkovState currentSystemState;
    private static final ArrayList<MarkovState> allStates = new ArrayList<>();
    private static int debugVariable = 0;

    /*

    ### example for a markov chain with conditions:
    1. "Win" Condition: At least 1 actuator with state CORRECT
    2. Actuator Condition: At least 1 CPU with CORRECT as input
    3. Actuator Condition: not more than 0 OUT_OF_CONTROL as input
    4. CPU Condition: At least 1 Sensor with CORRECT as input

    --> each component has a variable "correctSignalsNeeded" and a variable "outOfControlSignalsAccepted"
     */

    /**
     * generate the root node for the markov chain by using the current system layout as input
     */
    public static void generateCurrentSystemState() {
        ArrayList<MarkovStateObject> markovStateObjects = new ArrayList<>();

        for(Entity e : entities) {
            ArrayList<SimulationState> inputStates = new ArrayList<>();
            for(SimulationState s : e.getComponent(SimulationComponent.class).getInputStates()) {
                inputStates.add(SimulationState.CORRECT);
            }

            MarkovStateObject newObject = new MarkovStateObject(
                    e,
                    e.getComponent(SimulationComponent.class).getSimulationType(),
                    e.getComponent(SimulationComponent.class).getSimulationState(),
                    e.getComponent(SimulationComponent.class).getFailureRatio(),
                    e.getComponent(SimulationComponent.class).getFailureRecognitionRatio(),
                    inputStates
            );

            newObject.setInputObjects(e.getComponent(SimulationComponent.class).getInputIds());

            markovStateObjects.add(newObject);
        }

        currentSystemState = new MarkovState(null, markovStateObjects, 1.0);
    }
    /**
     * use the current system state to find the probability in the markov chain
     * @return probability of the markov state
     */
    public static double getMarkovStateProbability() {
        double probability = 0.0;

        return probability;
    }

    /**
     * Debugging methid: Print the current systems root node as a markov block.
     */
    public static void printCurrentSystemState() {
        System.out.println("------- CURRENT SYSTEM STATE AS MARKOV BLOCK ------------");
        StringBuilder line1 = new StringBuilder();
        line1.append("|");
        StringBuilder line2 = new StringBuilder();
        line2.append("|");
        for(MarkovStateObject ms : currentSystemState.getMarkovStateObjects()) {
            switch(ms.getType()) {
                case SENSOR -> line1.append("S|");
                case CPU -> line1.append("C|");
                case ACTUATOR -> line1.append("A|");
                case CABLE -> line1.append("W|");
            }
            switch(ms.getState()) {
                case CORRECT -> line2.append("C|");
                case FAIL -> line2.append("F|");
                case PASSIVE -> line2.append("P|");
                case INOPERATIVE -> line2.append("I|");
                case OUT_OF_CONTROL -> line2.append("O|");
            }
        }
        System.out.println(line1);
        System.out.println(line2);
    }

    /**
     * Print the markov chain
     */
    public static void printMarkov() {
        System.out.println("--------Markov Debugging --------");
        printChainStructure(currentSystemState);
    }


    /**
     * calculate the probability for all combined states that have enough actuators working
     * TODO: implement maximum ooc variables
     * @param state: the starting state
     * @param correctActuatorCount: the amount of correct actuators (min)
     * @param correctSensorCount: the amount of correct sensors (min)
     * @param correctCPUCount: the amount of correct CPUs (min)
     * @return the probability for the condition given (combined of all states found)
     */
    public static double[] getProbabilityForStatesWith(MarkovState state, int correctActuatorCount, int correctSensorCount, int correctCPUCount, int OOCActuatorCount, int OOCSensorCount, int OOCCPUCount) {
        double probabilityPassive = 0.0;
        double probabilityOOC = 0.0;

        int currentCorrectActuatorCount = 0;
        int currentCorrectSensorCount = 0;
        int currentCorrectCPUCount = 0;

        int currentOOCActuatorCount = 0;
        int currentOOCSensorCount = 0;
        int currentOOCCPUCount = 0;

        boolean hasFailed = false;
        for(MarkovStateObject object : state.getMarkovStateObjects()) {
            if(object.getState() == SimulationState.CORRECT) {
                switch(object.getType()) {
                    case CPU -> currentCorrectCPUCount++;
                    case ACTUATOR -> currentCorrectActuatorCount++;
                    case CABLE -> {
                    }
                    case SENSOR -> currentCorrectSensorCount++;
                }
            } else if(object.getState() == SimulationState.OUT_OF_CONTROL) {
                switch(object.getType()) {
                    case CPU -> currentOOCCPUCount++;
                    case ACTUATOR -> currentOOCActuatorCount++;
                    case CABLE -> {
                    }
                    case SENSOR -> currentOOCSensorCount++;
                }
            }
            // skip fail, because relevant is passive/ooc
            if(object.getState() == SimulationState.FAIL) {
                hasFailed = true;
            }
        }
        if(!hasFailed) {
            if (
                    currentOOCActuatorCount <= OOCActuatorCount
                            && currentOOCCPUCount <= OOCCPUCount
                            && currentOOCSensorCount <= OOCSensorCount
                            && (currentCorrectActuatorCount <= correctActuatorCount - 1
                            || currentCorrectSensorCount <= correctSensorCount - 1
                            || currentCorrectCPUCount <= correctCPUCount - 1)
            ) {
                System.out.println("Adding passive probability of state: " + state.selfToText() + " : " + state.getStateProbability());
                probabilityPassive += state.getStateProbability();
                state.getNext().clear();
            } else if (currentOOCActuatorCount > OOCActuatorCount || currentOOCCPUCount > OOCCPUCount || currentOOCSensorCount > OOCSensorCount) {
                System.out.println("Adding ooc probability of state: " + state.selfToText() + " : " + state.getStateProbability());
                probabilityOOC += state.getStateProbability();
                state.getNext().clear();
            }
        }

        for(MarkovState stateNext : state.getNext()) {
            try {
                double[] probs = getProbabilityForStatesWith(stateNext, correctActuatorCount, correctSensorCount, correctCPUCount,
                        OOCActuatorCount, OOCSensorCount, OOCCPUCount);
                probabilityPassive += probs[0];
                probabilityOOC += probs[1];
            } catch(StackOverflowError ex) {
                ex.printStackTrace();
                printChainStructure(currentSystemState);
                System.exit(1);
            }
        }

        return new double[] {probabilityPassive, probabilityOOC};
    }

    /**
     * Print the full markov chain, including the probability of each state.
     * @param start
     */
    public static void printChainStructure(MarkovState start) {
        if(start.getPrevious() != null) {
            System.out.println(start.getPrevious().selfToText() + "->" + start.selfToText() + " : " + start.getStateProbability());
        } else {
            System.out.println("root: " + start.structureToText());
            System.out.println("root: " + start.selfToText() + " : " + start.getStateProbability());
        }

        for(MarkovState state : start.getNext()) {
            printChainStructure(state);
        }
    }

    /**
     * Start the Markov Chain generation
     * @param start: node to calculate from; at the beginning this is supposed to be the root node.
     */
    public static void markovStart(MarkovState start) {
        if (Game.config().isDebug()) {
            String state = start.selfToText();
            System.out.println(state);
        }

        if (start.isFailed()) {
            handleFailedState(start);
        } else {
            handleNonFailedState(start);
        }

        for (MarkovState markovState : start.getNext()) {
            try {
                markovStart(markovState);
            } catch (StackOverflowError ex) {
                ex.printStackTrace();
                printChainStructure(currentSystemState);
                System.exit(1);
            }
        }
    }

    private static void handleFailedState(MarkovState start) {
        ArrayList<MarkovStateObject> objectsPassive = new ArrayList<>();
        ArrayList<MarkovStateObject> objectsOutOfControl = new ArrayList<>();
        ArrayList<MarkovStateObject> markovStateObjects = start.getMarkovStateObjects();
        int failedIndex = -1;

        for (int i = 0; i < markovStateObjects.size(); i++) {
            MarkovStateObject mso = markovStateObjects.get(i);
            SimulationState statePassive = mso.getState();
            SimulationState stateOutOfControl = mso.getState();
            if (statePassive == SimulationState.FAIL) {
                statePassive = SimulationState.PASSIVE;
                stateOutOfControl = SimulationState.OUT_OF_CONTROL;
                failedIndex = i;
            }
            MarkovStateObject passive = new MarkovStateObject(
                    mso.getEntity(), mso.getType(),
                    statePassive, mso.getFailureProbability(),
                    mso.getFailureRecognitionProbability(),
                    mso.getInputStates()
            );
            passive.setInputObjects(mso.getInputObjects());
            objectsPassive.add(passive);
            MarkovStateObject outOfControl = new MarkovStateObject(
                    mso.getEntity(), mso.getType(),
                    stateOutOfControl, mso.getFailureProbability(),
                    mso.getFailureRecognitionProbability(),
                    mso.getInputStates()
            );
            outOfControl.setInputObjects(mso.getInputObjects());
            objectsOutOfControl.add(outOfControl);
        }

        boolean updated;
        do {
            updated = false;
            if (updateInputStates(objectsPassive) || updateInputStates(objectsOutOfControl)) {
                updated = true;
            }
            if (updateStateBasedOnInputs(objectsPassive) || updateStateBasedOnInputs(objectsOutOfControl)) {
                updated = true;
            }
        } while (updated);

        start.addNext(new MarkovState(start, objectsPassive, start.getStateProbability() * objectsPassive.get(failedIndex).getFailureRecognitionProbability()));
        start.addNext(new MarkovState(start, objectsOutOfControl, start.getStateProbability() * (1 - objectsOutOfControl.get(failedIndex).getFailureRecognitionProbability())));
    }



    private static void handleNonFailedState(MarkovState start) {
        ArrayList<MarkovStateObject> stateObjects = start.getMarkovStateObjects();
        for (int i = 0; i < stateObjects.size(); i++) {
            MarkovStateObject mso = stateObjects.get(i);
            if (mso.getType() == SimulationType.CABLE) {
                continue;
            }
            if (mso.getState() == SimulationState.OUT_OF_CONTROL || mso.getState() == SimulationState.PASSIVE) {
                continue;
            }
            if (mso.getState() == SimulationState.FAIL) {
                System.out.println(start.selfToText() + " : return because a component is failed");
                return;
            }

            ArrayList<MarkovStateObject> markovStateObjects = cloneMarkovStateObjectList(start.getMarkovStateObjects());
            markovStateObjects.get(i).setState(SimulationState.FAIL);

            MarkovState newState = new MarkovState(start, markovStateObjects, start.getStateProbability() * mso.getFailureProbability());
            newState.setFailed(true);
            start.addNext(newState);
        }
    }

    private static boolean updateInputStates(List<MarkovStateObject> objects) {
        boolean updated = false;

        for (MarkovStateObject mso : objects) {
            ArrayList<SimulationState> inputStates = new ArrayList<>();
            for (MarkovStateObject msoOther : objects) {
                if (msoOther == mso) {
                    continue;
                }
                if (mso.getEntity()
                        .getComponent(SimulationComponent.class)
                        .getInputIds()
                        .contains(msoOther
                                .getEntity()
                                .getComponent(SimulationComponent.class)
                                .getOwnId())) {

                    inputStates.add(msoOther.getState());
                }
            }
            mso.setInputStates(inputStates);

            ArrayList<SimulationState> newInputStates = new ArrayList<>(inputStates);
            if (!mso.getInputStates().equals(newInputStates)) {
                mso.setInputStates(newInputStates);
                updated = true;
            }
        }
        return updated;
    }

    private static boolean updateStateBasedOnInputs(List<MarkovStateObject> objects) {
        boolean updated = false;
        for (MarkovStateObject mso : objects) {
            if (mso.getType() == SimulationType.SENSOR) {
                continue;
            }
            if (mso.getState() == SimulationState.PASSIVE || mso.getState() == SimulationState.OUT_OF_CONTROL || mso.getState() == SimulationState.INOPERATIVE) {
                continue;
            }

            SimulationState newState = null;

            if(mso.getType() == SimulationType.VOTE) {
                int inputCount = mso.getInputStates().size();
                ArrayList<SimulationState> states = mso.getInputStates();
                switch (inputCount) {
                    case 0:
                        continue;
                    case 1:
                        newState = states.get(0);
                        break;
                    case 2:
                        if(states.get(0) == states.get(1)) {
                            newState = states.get(0);
                        } else {
                            newState = SimulationState.PASSIVE;
                        }
                        break;
                    case 3:
                        if(Collections.frequency(states, SimulationState.CORRECT) >= 2) {
                            newState = SimulationState.CORRECT;
                        } else {
                            newState = SimulationState.PASSIVE;
                        }
                        break;
                    case 4:
                        if(Collections.frequency(states, SimulationState.CORRECT) >= 3) {
                            mso.setState(SimulationState.CORRECT);
                            newState = SimulationState.CORRECT;
                        } else if(Collections.frequency(states, SimulationState.CORRECT) == 2 &&
                                Collections.frequency(states, SimulationState.PASSIVE) >= 1) {
                            newState = SimulationState.CORRECT;
                        } else {
                            newState = SimulationState.PASSIVE;
                        }
                }
            } else {
                if (Collections.frequency(mso.getInputStates(), SimulationState.CORRECT)
                        >= mso.getEntity().getComponent(SimulationComponent.class).getCorrectSignalsNeeded()
                        && Collections.frequency(mso.getInputStates(), SimulationState.OUT_OF_CONTROL)
                        <= mso.getEntity().getComponent(SimulationComponent.class).getOutOfControlSignalsAccepted()) {
                    newState = SimulationState.CORRECT;
                } else if (Collections.frequency(mso.getInputStates(), SimulationState.CORRECT)
                        < mso.getEntity().getComponent(SimulationComponent.class).getCorrectSignalsNeeded()
                        && Collections.frequency(mso.getInputStates(), SimulationState.OUT_OF_CONTROL) <=
                        mso.getEntity().getComponent(SimulationComponent.class).getOutOfControlSignalsAccepted() &&
                        Collections.frequency(mso.getInputStates(), SimulationState.OUT_OF_CONTROL) <
                                Collections.frequency(mso.getInputStates(), SimulationState.PASSIVE)) {
                    newState = SimulationState.PASSIVE;
                } else {
                    newState = SimulationState.OUT_OF_CONTROL;
                }
            }

            if (newState != null && mso.getState() != newState) {
                mso.setState(newState);
                updated = true;
            }
        }
        return updated;
    }

    private static ArrayList<MarkovStateObject> cloneMarkovStateObjectList(List<MarkovStateObject> originalList) {
        ArrayList<MarkovStateObject> newList = new ArrayList<>();
        for (MarkovStateObject object : originalList) {
            MarkovStateObject newObject = new MarkovStateObject(
                    object.getEntity(), object.getType(),
                    object.getState(), object.getFailureProbability(),
                    object.getFailureRecognitionProbability(),
                    object.getInputStates());
            newObject.setInputObjects(newObject.getEntity().getComponent(SimulationComponent.class).getInputIds());
            newList.add(newObject);
        }
        return newList;
    }
}
