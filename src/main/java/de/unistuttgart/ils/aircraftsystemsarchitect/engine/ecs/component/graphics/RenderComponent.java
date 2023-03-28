package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.RenderObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.Component;

import java.awt.*;
import java.util.ArrayList;

public class RenderComponent extends Component {
    private final ArrayList<RenderObject> renderObjects;

    public RenderComponent() {
        this.renderObjects = new ArrayList<>();
        Game.graphics().recollectEntities();
    }

    public ArrayList<RenderObject> getRenderObjects() {
        return renderObjects;
    }

    public void addRenderObject(RenderObject renderObject) {
        renderObjects.add(renderObject);
    }

    public ArrayList<RenderObject> getObjectsAtLayer(Layer layer) {
        ArrayList<RenderObject> objects = new ArrayList<>();
        for (RenderObject renderObject : renderObjects) {
            if (renderObject.getLayer() == layer) {
                objects.add(renderObject);
            }
        }
        return objects;
    }

    public <T extends RenderObject> ArrayList<T> getRenderObjectsOfType(Class<T> objectClass) {
        ArrayList<T> objects = new ArrayList<>();
        for (RenderObject renderObject : renderObjects) {
            if (renderObject.getClass() == objectClass) {
                objects.add(objectClass.cast(renderObject));
            }
        }
        return objects;
    }

    public void reposition(Point p) {
        for (RenderObject r : renderObjects) {
            r.setLocation(new Point(p));
            Rectangle bounds = new Rectangle(p.x, p.y, r.getBounds().getBounds().width, r.getBounds().getBounds().height);
            r.setBounds(bounds);
        }
    }

    public void hideAllObjects() {
        for(RenderObject r : renderObjects) {
            r.setHidden(true);
        }
    }

    public void showAllObjects() {
        for(RenderObject r : renderObjects) {
            r.setHidden(false);
        }
    }
}
