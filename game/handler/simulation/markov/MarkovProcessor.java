package game.handler.simulation.markov;

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
    private static final ArrayList<MarkovState> allStates = new ArrayList<>();

    /*

    ### example for a markov chain with conditions:
    1. "Win" Condition: At least 1 actuator with state CORRECT
    2. Actuator Condition: At least 1 CPU with CORRECT as input
    3. Actuator Condition: not more than 0 OUT_OF_CONTROL as input
    4. CPU Condition: At least 1 Sensor with CORRECT as input

    --> each component has a variable "correctSignalsNeeded" and a variable "outOfControlSignalsAccepted"
     */


    /**
     * generate the starting point
     */
    public static void generateStartingNode() {

    }

    public static void generateCurrentSystemState() {
        allStates.clear();

        ArrayList<MarkovStateObject> markovStateObjects = new ArrayList<>();

        for(Entity e : entities) {
            markovStateObjects.add(
                    new MarkovStateObject(
                            e,
                            e.getComponent(SimulationComponent.class).getSimulationType(),
                            e.getComponent(SimulationComponent.class).getSimulationState(),
                            e.getComponent(SimulationComponent.class).getFailureRatio(),
                            e.getComponent(SimulationComponent.class).getFailureRecognitionRatio(),
                            e.getComponent(SimulationComponent.class).getInputStates()
                    )
            );
        }
        currentSystemState = new MarkovState(null, markovStateObjects, 1.0);
    }

    /**
     * start the markov chain
     */
    public static void startMarkov(MarkovState start) {
        // any component can fail at the start, start with element 0, loop through until last element is reached
        ArrayList<MarkovState> temporaryStates = new ArrayList<>();

        for(MarkovState state : allStates) {
            int countSame = 0;
            for(int i = 0; i < state.getMarkovStateObjects().size(); i++) {
                if(start.getMarkovStateObjects().get(i).getEntity() == state.getMarkovStateObjects().get(i).getEntity()
                && start.getMarkovStateObjects().get(i).getState() == state.getMarkovStateObjects().get(i).getState()
                && start.getMarkovStateObjects().get(i).getType() == state.getMarkovStateObjects().get(i).getType()) {
                    countSame++;
                }
            }
            if(countSame == start.getMarkovStateObjects().size()) {
                System.out.println("Returning because state is the same as another one");
                state.setStateProbability(state.getStateProbability() + start.getStateProbability());
                printMarkovSingle(start);
                return;
            }
        }

        int countFail = 0;
        for(int i = 0; i < start.getMarkovStateObjects().size(); i++) {
            if(start.getMarkovStateObjects().get(i).getState() == SimulationState.OUT_OF_CONTROL ||
                    start.getMarkovStateObjects().get(i).getState() == SimulationState.PASSIVE) {
                countFail++;
            }
        }


        allStates.add(start);

        // fail each component once for this layer
        for(int i = 0; i < start.getMarkovStateObjects().size(); i++) {
            if(start.getMarkovStateObjects().get(i).getType() == SimulationType.CABLE) {
                continue;
            }
            if(start.getMarkovStateObjects().get(i).getState() == SimulationState.OUT_OF_CONTROL ||
                    start.getMarkovStateObjects().get(i).getState() == SimulationState.PASSIVE) {
                System.out.println("Continue because P/OOC");
                continue;
            }
            if(start.getMarkovStateObjects().get(i).getState() == SimulationState.FAIL) {
                System.out.println("Returing because the state is already FAIL");
                printMarkovSingle(start);
                return;
            }

            ArrayList<MarkovStateObject> markovStateObjects = new ArrayList<>();
            for(MarkovStateObject mso : start.getMarkovStateObjects()) {
                markovStateObjects.add(new MarkovStateObject(
                        mso.getEntity(), mso.getType(),
                        mso.getState(), mso.getFailureProbability(),
                        mso.getFailureRecognitionProbability(),
                        mso.getInputStates()));
            }

            markovStateObjects.get(i).setState(SimulationState.FAIL);
            MarkovState markovState = new MarkovState(start, markovStateObjects, start.getStateProbability() * markovStateObjects.get(i).getFailureProbability());
            temporaryStates.add(markovState);
        }

        // find equal nodes (e.g. S1 = S2)
        //TODO: Implement

        // passivate each component in layer once & out of control once
        ArrayList<MarkovState> tempStates = new ArrayList<>(temporaryStates);
        System.out.println("Temp States Size:" + tempStates.size());

        for(int i = 0; i < tempStates.size(); i++) {
            for(int j = 0; j < tempStates.get(i).getMarkovStateObjects().size(); j++) {
                if(tempStates.get(i).getMarkovStateObjects().get(j).getState() == SimulationState.FAIL) {
                    ArrayList<MarkovStateObject> markovStateObjects = new ArrayList<>();

                    for(MarkovStateObject mso : tempStates.get(i).getMarkovStateObjects()) {
                        markovStateObjects.add(new MarkovStateObject(
                                mso.getEntity(), mso.getType(),
                                mso.getState(), mso.getFailureProbability(),
                                mso.getFailureRecognitionProbability(),
                                mso.getInputStates()));
                    }
                    markovStateObjects.get(j).setState(SimulationState.PASSIVE);

                    // update other connected components with the same state.
                    for(MarkovStateObject mso : markovStateObjects) {
                        for(MarkovStateObject msoOther : markovStateObjects) {
                            if(msoOther == mso) {
                                continue;
                            }

                            if(mso.getEntity().getComponent(SimulationComponent.class).getInputIds().contains(msoOther.getEntity().getComponent(SimulationComponent.class).getOwnId())) {
                                int index = mso.getEntity().getComponent(SimulationComponent.class).getInputIds().indexOf(msoOther.getEntity().getComponent(SimulationComponent.class).getOwnId());
                                mso.getInputStates().set(index, SimulationState.PASSIVE);
                            }
                        }
                    }

                    for(MarkovStateObject mso : markovStateObjects) {
                        if(mso.getType() == SimulationType.SENSOR) {
                            continue;
                        }
                        if(Collections.frequency(mso.getInputStates(), SimulationState.CORRECT)
                                >= mso.getEntity().getComponent(SimulationComponent.class).getCorrectSignalsNeeded()) {
                            mso.setState(SimulationState.CORRECT);
                        } else {
                            mso.setState(SimulationState.PASSIVE);
                        }
                    }

                    temporaryStates.add(new MarkovState(tempStates.get(i),
                            markovStateObjects,
                            tempStates.get(i).getStateProbability() * markovStateObjects.get(j).getFailureRecognitionProbability()));

                    markovStateObjects = new ArrayList<>();
                    for(MarkovStateObject mso : tempStates.get(i).getMarkovStateObjects()) {
                        markovStateObjects.add(new MarkovStateObject(
                                mso.getEntity(), mso.getType(),
                                mso.getState(), mso.getFailureProbability(),
                                mso.getFailureRecognitionProbability(),
                                mso.getInputStates()));
                    }
                    markovStateObjects.get(j).setState(SimulationState.OUT_OF_CONTROL);

                    // update other connected components with the same state.
                    for(MarkovStateObject mso : markovStateObjects) {

                        for(MarkovStateObject msoOther : markovStateObjects) {
                            if(msoOther == mso) {
                                continue;
                            }

                            if(mso.getEntity().getComponent(SimulationComponent.class).getInputIds().contains(msoOther.getEntity().getComponent(SimulationComponent.class).getOwnId())) {
                                int index = mso.getEntity().getComponent(SimulationComponent.class).getInputIds().indexOf(msoOther.getEntity().getComponent(SimulationComponent.class).getOwnId());
                                mso.getInputStates().set(index, SimulationState.OUT_OF_CONTROL);
                            }
                        }
                    }

                    for(MarkovStateObject mso : markovStateObjects) {
                        if(mso.getType() == SimulationType.SENSOR) {
                            continue;
                        }
                        if(Collections.frequency(mso.getInputStates(), SimulationState.OUT_OF_CONTROL)
                                <= mso.getEntity().getComponent(SimulationComponent.class).getOutOfControlSignalsAccepted()) {
                            mso.setState(SimulationState.CORRECT);
                        } else {
                            mso.setState(SimulationState.OUT_OF_CONTROL);
                        }
                    }

                    temporaryStates.add(new MarkovState(tempStates.get(i),
                            markovStateObjects,
                            tempStates.get(i).getStateProbability() * (1 - markovStateObjects.get(j).getFailureRecognitionProbability())));
                }
            }
        }

        for(MarkovState state : temporaryStates) {
            if(!allStates.contains(state))
                try {
                    startMarkov(state);
                } catch(StackOverflowError ex) {
                    ex.printStackTrace();
                    printMarkov();
                    System.exit(1);
                }
        }
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
    }

    public static double getProbabilityForState(MarkovState state) {
        for(MarkovState possibleEqual : allStates) {
            if(state.getMarkovStateObjects().size() != possibleEqual.getMarkovStateObjects().size()) {
                continue;
            }

            boolean returnHere = true;
            for(int i = 0; i < state.getMarkovStateObjects().size(); i++) {
                if(state.getMarkovStateObjects().get(i).getType() != possibleEqual.getMarkovStateObjects().get(i).getType()
                        && state.getMarkovStateObjects().get(i).getState() != possibleEqual.getMarkovStateObjects().get(i).getState()) {
                    returnHere = false;
                    break;
                }
            }
            if(returnHere) {
                return possibleEqual.getStateProbability();
            }
        }

        return 0.0;
    }

}
