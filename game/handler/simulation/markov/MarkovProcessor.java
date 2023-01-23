package game.handler.simulation.markov;

import com.ecs.entity.Entity;
import game.components.SimulationComponent;
import game.handler.simulation.SimulationState;
import game.handler.simulation.SimulationType;

import java.util.ArrayList;

/**
 * a generic markov chain processor class that can be called from anywhere
 */
public class MarkovProcessor {

    public static ArrayList<Entity> entities = new ArrayList<>();
    public static int[] groupIds;

    private static MarkovState currentSystemState;
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
            if(e.getComponent(SimulationComponent.class).getSimulationType() == SimulationType.CABLE) {
                continue;
            }
            markovStateObjects.add(
                    new MarkovStateObject(
                        e.getComponent(SimulationComponent.class).getSimulationType(),
                        e.getComponent(SimulationComponent.class).getSimulationState(),
                            e.getComponent(SimulationComponent.class).getFailureRatio(),
                            e.getComponent(SimulationComponent.class).getFailureRecognitionRatio()
                    )
            );
        }
        currentSystemState = new MarkovState(markovStateObjects, 1.0);
        allStates.add(currentSystemState);
    }

    /**
     * start the chain
     */
    public static void startMarkov() {
        // any component can fail at the start, start with element 0, loop through until last element is reached

        // fail each component once for this layer
        for(MarkovState ms : allStates) {

        }

        // passivate each component in layer once & out of control once

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
                switch(ms.getType()) {
                    case SENSOR -> line1.append("S|");
                    case CPU -> line1.append("C|");
                    case ACTUATOR -> line1.append("A|");
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

}
