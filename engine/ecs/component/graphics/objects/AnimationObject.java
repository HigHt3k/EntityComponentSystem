package engine.ecs.component.graphics.objects;

import java.awt.*;

public class AnimationObject extends RenderObject {
    private Image animation;

    public AnimationObject(Point location, Shape bounds, Layer layer, Image animation) {
        super(location, bounds, layer);
        this.animation = animation;
    }

    public Image getAnimation() {
        return animation;
    }
}
