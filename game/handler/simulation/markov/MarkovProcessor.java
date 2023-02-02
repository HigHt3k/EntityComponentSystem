package game.handler.simulation.markov;

import com.Game;
import com.ecs.entity.Entity;
import game.components.CablePortsComponent;
import game.components.SimulationComponent;
import game.handler.SimulationSystem;
import game.handler.simulation.SimulationState;
import game.handler.simulation.SimulationType;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * a generic markov chain processor class that can be called from anywhere
 */
public class MarkovProcessor {

    public static ArrayList<Entity> entities = new ArrayList<>();
    public static int[] groupIds;

    public static MarkovState currentSystemState;
    public static MarkovStateRefactored currentSystemStateRefactored;
    private static final ArrayList<MarkovState> allStates = new ArrayList<>();

    /*

    ### example for a markov chain with conditions:
    1. "Win" Condition: At least 1 actuator with state CORRECT
    2. Actuator Condition: At least 1 CPU with CORRECT as input
    3. Actuator Condition: not more than 0 OUT_OF_CONTROL as input
    4. CPU Condition: At least 1 Sensor with CORRECT as input

    --> each component has a variable "correctSignalsNeeded" and a variable "outOfControlSignalsAccepted"
     */

    public static void generateCurrentSystemStateRefactored() {
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

        currentSystemStateRefactored = new MarkovStateRefactored(null, markovStateObjects, 1.0);
    }
    /**
     * use the current system state to find the probability in the markov chain
     * @return probability of the markov state
     */
    public static double getMarkovStateProbability() {
        double probability = 0.0;

        return probability;
    }

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

    public static void printMarkovSingle(MarkovState state) {
        StringBuilder line1 = new StringBuilder();
        line1.append("|");
        StringBuilder line2 = new StringBuilder();
        line2.append("|");
        for(MarkovStateObject ms : state.getMarkovStateObjects()) {
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
        /*
        for(MarkovState states : allStates) {
            StringBuilder line1 = new StringBuilder();
            line1.append("|");
            StringBuilder line2 = new StringBuilder();
            line2.append("|");
            for(MarkovStateObject ms : states.getMarkovStateObjects()) {
                if(ms.getType() == SimulationType.CABLE) {
                    continue;
                }
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
            System.out.println(line2 + " - " + states.getStateProbability());
            System.out.println("");
        }
        */
        printChainStructure(currentSystemStateRefactored);
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
    public static double[] getProbabilityForStatesWith(MarkovStateRefactored state, int correctActuatorCount, int correctSensorCount, int correctCPUCount, int OOCActuatorCount, int OOCSensorCount, int OOCCPUCount) {
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
            if (currentOOCActuatorCount <= OOCActuatorCount && currentOOCCPUCount <= OOCCPUCount && currentOOCSensorCount <= OOCSensorCount
                    && (currentCorrectActuatorCount == correctActuatorCount - 1
                    || currentCorrectSensorCount == correctSensorCount - 1
                    || currentCorrectCPUCount == correctCPUCount - 1)) {
                System.out.println("Adding passive probability of state: " + state.selfToText() + " : " + state.getStateProbability());
                probabilityPassive = state.getStateProbability();
                state.getNext().clear();
            } else if(currentOOCActuatorCount > OOCActuatorCount || currentOOCCPUCount > OOCCPUCount || currentOOCSensorCount > OOCSensorCount) {
                System.out.println("Adding ooc probability of state: " + state.selfToText() + " : " + state.getStateProbability());
                probabilityOOC = state.getStateProbability();
                state.getNext().clear();
            }
        }

        for(MarkovStateRefactored stateNext : state.getNext()) {
            try {
                double probs[] = getProbabilityForStatesWith(stateNext, correctActuatorCount, correctSensorCount, correctCPUCount,
                        OOCActuatorCount, OOCSensorCount, OOCCPUCount);
                if(probabilityPassive < probs[0])
                    probabilityPassive = probs[0];
                if (probabilityOOC < probs[1])
                    probabilityOOC = probs[1];
            } catch(StackOverflowError ex) {
                ex.printStackTrace();
                printChainStructure(currentSystemStateRefactored);
                System.exit(1);
            }
        }

        return new double[] {probabilityPassive, probabilityOOC};
    }

    public static double getProbabilityForState(MarkovState state) {
        double probability = 0.0;
        for(MarkovState possibleEqual : allStates) {
            if(state.getMarkovStateObjects().size() != possibleEqual.getMarkovStateObjects().size()) {
                continue;
            }

            boolean isSame = true;
            for(int i = 0; i < state.getMarkovStateObjects().size(); i++) {
                if(state.getMarkovStateObjects().get(i).getType() != possibleEqual.getMarkovStateObjects().get(i).getType()
                        && state.getMarkovStateObjects().get(i).getState() != possibleEqual.getMarkovStateObjects().get(i).getState()) {
                    isSame = false;
                    break;
                }
            }
            if(isSame) {
                probability += possibleEqual.getStateProbability();
            }
        }

        return probability;
    }

    public static void printChainStructure(MarkovStateRefactored start) {
        if(start.getPrevious() != null) {
            System.out.println(start.getPrevious().selfToText() + "->" + start.selfToText() + " : " + start.getStateProbability());
        } else {
            System.out.println("root: " + start.structureToText());
            System.out.println("root: " + start.selfToText() + " : " + start.getStateProbability());
        }

        for(MarkovStateRefactored state : start.getNext()) {
            printChainStructure(state);
        }
    }

    public static void markovStart(MarkovStateRefactored start) {
        // if a component state is fail -> create states with OOC and PASSIVE as their children.
        if(start.isFailed()) {
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
            // update connected components
            // update other connected components with the same state.
            // update input states of each list
            //TODO: Delete this ugly for loop; only thing it does is do the same thing multiple times so everything is updated correctly...
            for(int i = 0; i < objectsPassive.size(); i++) {
                for (MarkovStateObject mso : objectsPassive) {
                    ArrayList<SimulationState> inputStates = new ArrayList<>();
                    for (MarkovStateObject msoOther : objectsPassive) {
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
                }

                for (MarkovStateObject mso : objectsOutOfControl) {
                    ArrayList<SimulationState> inputStates = new ArrayList<>();
                    for (MarkovStateObject msoOther : objectsOutOfControl) {
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
                }

                for (MarkovStateObject mso : objectsPassive) {
                    if (mso.getType() == SimulationType.SENSOR) {
                        continue;
                    }
                    if (mso.getState() == SimulationState.PASSIVE || mso.getState() == SimulationState.OUT_OF_CONTROL || mso.getState() == SimulationState.INOPERATIVE) {
                        continue;
                    }

                    if (Collections.frequency(mso.getInputStates(), SimulationState.OUT_OF_CONTROL)
                            > mso.getEntity().getComponent(SimulationComponent.class).getOutOfControlSignalsAccepted()) {
                        mso.setState(SimulationState.OUT_OF_CONTROL);
                    } else if (Collections.frequency(mso.getInputStates(), SimulationState.CORRECT)
                            < mso.getEntity().getComponent(SimulationComponent.class).getCorrectSignalsNeeded()) {
                        mso.setState(SimulationState.PASSIVE);
                    } else {
                        mso.setState(SimulationState.CORRECT);
                    }
                }


                for (MarkovStateObject mso : objectsOutOfControl) {
                    if (mso.getType() == SimulationType.SENSOR) {
                        continue;
                    }
                    if (mso.getState() == SimulationState.PASSIVE || mso.getState() == SimulationState.OUT_OF_CONTROL || mso.getState() == SimulationState.INOPERATIVE) {
                        continue;
                    }
                    if (Collections.frequency(mso.getInputStates(), SimulationState.OUT_OF_CONTROL)
                            > mso.getEntity().getComponent(SimulationComponent.class).getOutOfControlSignalsAccepted()) {
                        mso.setState(SimulationState.OUT_OF_CONTROL);
                    } else if (Collections.frequency(mso.getInputStates(), SimulationState.CORRECT)
                            < mso.getEntity().getComponent(SimulationComponent.class).getCorrectSignalsNeeded()) {
                        mso.setState(SimulationState.PASSIVE);
                    } else {
                        mso.setState(SimulationState.CORRECT);
                    }
                }
            }

            start.addNext(new MarkovStateRefactored(start, objectsPassive, start.getStateProbability() * objectsPassive.get(failedIndex).getFailureRecognitionProbability()));
            start.addNext(new MarkovStateRefactored(start, objectsOutOfControl, start.getStateProbability() * (1 - objectsOutOfControl.get(failedIndex).getFailureRecognitionProbability())));
        } else {
            ArrayList<MarkovStateObject> stateObjects = start.getMarkovStateObjects();
            for (int i = 0; i < stateObjects.size(); i++) {
                MarkovStateObject mso = stateObjects.get(i);
                if (mso.getType() == SimulationType.CABLE) {
                    // continue because cable can't fail with current implementation
                    continue;
                }
                if (mso.getState() == SimulationState.OUT_OF_CONTROL ||
                        mso.getState() == SimulationState.PASSIVE) {
                    // continue because component is p/ooc already
                    continue;
                }
                if (mso.getState() == SimulationState.FAIL) {
                    // return because there is a failed component already
                    return;
                }

                ArrayList<MarkovStateObject> markovStateObjects = new ArrayList<>();
                for (MarkovStateObject object : start.getMarkovStateObjects()) {
                    MarkovStateObject newObject = new MarkovStateObject(
                            object.getEntity(), object.getType(),
                            object.getState(), object.getFailureProbability(),
                            object.getFailureRecognitionProbability(),
                            object.getInputStates());
                    newObject.setInputObjects(newObject.getEntity().getComponent(SimulationComponent.class).getInputIds());
                    markovStateObjects.add(newObject);
                }
                markovStateObjects.get(i).setState(SimulationState.FAIL);

                MarkovStateRefactored newState = new MarkovStateRefactored(start,
                        markovStateObjects, start.getStateProbability() * mso.getFailureProbability());
                newState.setFailed(true);
                start.addNext(newState);

            }
        }

        for(MarkovStateRefactored markovStateRefactored : start.getNext()) {
            try {
                markovStart(markovStateRefactored);
            } catch(StackOverflowError ex) {
                ex.printStackTrace();
                printChainStructure(currentSystemStateRefactored);
                System.exit(1);
            }
        }
    }

}
