package de.unistuttgart.ils.aircraftsystemsarchitect.game.components;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.Component;

public class TooltipComponent extends Component {
    private String tooltipText;
    private String failureRatio;

    private String correctInputSignals;
    private String acceptedOOCSignals;

    private String type;

    @Override
    public void update() {

    }

    public String getType() {
        return type;
    }

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

    public String getFailureRatio() {
        return failureRatio;
    }

    public String getTooltipText() {
        return tooltipText;
    }

    public void setFailureRatio(String failureRatio) {
        this.failureRatio = failureRatio;
    }

    public void setTooltipText(String tooltipText) {
        this.tooltipText = tooltipText;
    }

    public void setAcceptedOOCSignals(String acceptedOOCSignals) {
        this.acceptedOOCSignals = acceptedOOCSignals;
    }

    public String getAcceptedOOCSignals() {
        return acceptedOOCSignals;
    }

    public String getCorrectInputSignals() {
        return correctInputSignals;
    }

    public void setCorrectInputSignals(String correctInputSignals) {
        this.correctInputSignals = correctInputSignals;
    }
}
