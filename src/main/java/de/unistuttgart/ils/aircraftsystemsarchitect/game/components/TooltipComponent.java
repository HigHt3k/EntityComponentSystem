package de.unistuttgart.ils.aircraftsystemsarchitect.game.components;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.Component;

/**
 * TooltipComponent is used to display information about an entity in the game.
 * It stores information such as the type, failure ratio, tooltip text,
 * number of correct input signals, and the number of accepted out-of-control signals.
 */
public class TooltipComponent extends Component {
    private String tooltipText;
    private String failureRatio;

    private String correctInputSignals;
    private String acceptedOOCSignals;

    private String type;

    /**
     * Get the type of the entity this component is attached to.
     * @return the type of the entity.
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type of the entity this component is attached to.
     * @param type the type of the entity.
     */
    public void setType(String type) {
        if (type.equals("SENSOR")) {
            this.type = "@66";
        } else if(type.equals("ACTUATOR")) {
            this.type = "@67";
        } else if(type.equals("COMPUTER")) {
            this.type = "@68";
        } else if(type.equals("VOTE")) {
            this.type = "@69";
        } else if(type.equals("CABLE")) {
            this.type = "@70";
        } else {
            this.type = type;
        }
    }

    /**
     * Get the failure ratio of the entity this component is attached to.
     * @return the failure ratio.
     */
    public String getFailureRatio() {
        return failureRatio;
    }

    /**
     * Get the tooltip text of the entity this component is attached to.
     * @return the tooltip text.
     */
    public String getTooltipText() {
        return tooltipText;
    }

    /**
     * Set the failure ratio of the entity this component is attached to.
     * @param failureRatio the failure ratio to set.
     */
    public void setFailureRatio(String failureRatio) {
        this.failureRatio = failureRatio;
    }

    /**
     * Set the tooltip text of the entity this component is attached to.
     * @param tooltipText the tooltip text to set.
     */
    public void setTooltipText(String tooltipText) {
        this.tooltipText = tooltipText;
    }

    /**
     * Set the number of accepted out-of-control signals of the entity this component is attached to.
     * @param acceptedOOCSignals the number of accepted out-of-control signals.
     */
    public void setAcceptedOOCSignals(String acceptedOOCSignals) {
        this.acceptedOOCSignals = acceptedOOCSignals;
    }

    /**
     * Get the number of accepted out-of-control signals of the entity this component is attached to.
     * @return the number of accepted out-of-control signals.
     */
    public String getAcceptedOOCSignals() {
        return acceptedOOCSignals;
    }

    /**
     * Get the number of correct input signals of the entity this component is attached to.
     * @return the number of correct input signals.
     */
    public String getCorrectInputSignals() {
        return correctInputSignals;
    }

    /**
     * Set the number of correct input signals of the entity this component is attached to.
     * @param correctInputSignals the number of correct input signals to set.
     */
    public void setCorrectInputSignals(String correctInputSignals) {
        this.correctInputSignals = correctInputSignals;
    }
}
