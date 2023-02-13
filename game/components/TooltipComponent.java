package game.components;

import com.ecs.component.Component;

public class TooltipComponent extends Component {
    private String tooltipText;
    private String failureRatio;

    private String correctInputSignals;
    private String acceptedOOCSignals;

    @Override
    public void update() {

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
