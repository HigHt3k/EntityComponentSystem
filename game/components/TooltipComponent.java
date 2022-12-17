package game.components;

import com.ecs.Component;

public class TooltipComponent extends Component {
    private String tooltipText;
    private String failureRatio;

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
}
