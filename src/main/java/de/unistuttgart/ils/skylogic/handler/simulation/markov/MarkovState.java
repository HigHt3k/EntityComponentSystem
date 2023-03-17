package de.unistuttgart.ils.skylogic.handler.simulation.markov;

import de.unistuttgart.ils.skylogic.handler.simulation.SimulationType;

import java.util.ArrayList;

public class MarkovState {
    private boolean handled = false;
    private boolean failed = false;
    private MarkovState previous;
    private ArrayList<MarkovState> next;
    private double stateProbability;
    private ArrayList<MarkovStateObject> markovStateObjects;

    public MarkovState(MarkovState previous, ArrayList<MarkovStateObject> markovStateObjects, double stateProbability) {
        this.previous = previous;
        this.markovStateObjects = new ArrayList<>(markovStateObjects);
        this.stateProbability = stateProbability;
        this.next = new ArrayList<>();
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public boolean isHandled() {
        return handled;
    }

    public void setHandled(boolean handled) {
        this.handled = handled;
    }

    public void addNext(MarkovState next) {
        this.next.add(next);
    }

    public String structureToText() {
        StringBuilder out = new StringBuilder("|");
        for(MarkovStateObject object : markovStateObjects) {
            if(object.getType() == SimulationType.CABLE) {
                continue;
            }
            switch(object.getType()) {

                case CPU -> {
                    out.append("C|");
                }
                case ACTUATOR -> {
                    out.append("A|");
                }
                case CABLE -> {
                    out.append("W|");
                }
                case SENSOR -> {
                    out.append("S|");
                }
                case VOTE -> {
                    out.append("V|");
                }
            }
        }
        return out.toString();
    }

    public String selfToText() {
        StringBuilder out = new StringBuilder("|");
        for(MarkovStateObject object : markovStateObjects) {
            if(object.getType() == SimulationType.CABLE) {
                continue;
            }
            switch(object.getState()) {

                case CORRECT -> {
                    out.append("C|");
                }
                case FAIL -> {
                    out.append("F|");
                }
                case PASSIVE -> {
                    out.append("P|");
                }
                case OUT_OF_CONTROL -> {
                    out.append("O|");
                }
                case INOPERATIVE -> {
                    out.append("I|");
                }
            }
        }
        return out.toString();
    }

    public void setStateProbability(double stateProbability) {
        this.stateProbability = stateProbability;
    }

    public ArrayList<MarkovStateObject> getMarkovStateObjects() {
        return markovStateObjects;
    }

    public ArrayList<MarkovState> getNext() {
        return next;
    }

    public MarkovState getPrevious() {
        return previous;
    }

    public double getStateProbability() {
        return stateProbability;
    }

    public void setMarkovStateObjects(ArrayList<MarkovStateObject> markovStateObjects) {
        this.markovStateObjects = markovStateObjects;
    }

    public void setNext(ArrayList<MarkovState> next) {
        this.next = next;
    }

    public void setPrevious(MarkovState previous) {
        this.previous = previous;
    }
}
