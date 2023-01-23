package game.handler.simulation.markov;

import java.util.ArrayList;

public class MarkovState {
    private ArrayList<MarkovStateObject> markovStateObjects;
    private double stateProbability;

    public MarkovState(ArrayList<MarkovStateObject> markovStateObjects, double stateProbability) {
        this.markovStateObjects = markovStateObjects;
        this.stateProbability = stateProbability;
    }

    public double getStateProbability() {
        return stateProbability;
    }

    public ArrayList<MarkovStateObject> getMarkovStateObjects() {
        return markovStateObjects;
    }
}
