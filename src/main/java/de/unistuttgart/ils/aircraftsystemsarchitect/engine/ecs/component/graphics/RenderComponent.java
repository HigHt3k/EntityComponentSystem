package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.RenderObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.Component;

import java.awt.*;
import java.util.ArrayList;

/**
 * The RenderComponent is used for data storage of graphical objects (render objects), which are used by the {@link de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.render.RenderingEngine}
 * to display graphics
 */
public class RenderComponent extends Component {
    private final ArrayList<RenderObject> renderObjects;

    /**
     * Create a new render component and recollect all entities used for the graphics rendering process
     */
    public RenderComponent() {
        this.renderObjects = new ArrayList<>();
        Game.graphics().recollectEntities();
    }

    /**
     * get all render objects stored in this component
     * @return the render objects
     */
    public ArrayList<RenderObject> getRenderObjects() {
        return renderObjects;
    }

    /**
     * add a render object to this component
     * @param renderObject: a render object (contains graphics data)
     */
    public void addRenderObject(RenderObject renderObject) {
        renderObjects.add(renderObject);
    }

    /**
     * find render objects at a specified layer
     * @param layer: the alyer
     * @return the list of objects at given layer
     */
    public ArrayList<RenderObject> getObjectsAtLayer(Layer layer) {
        ArrayList<RenderObject> objects = new ArrayList<>();
        for (RenderObject renderObject : renderObjects) {
            if (renderObject.getLayer() == layer) {
                objects.add(renderObject);
            }
        }
        return objects;
    }

    /**
     * find render objects of a specified object class
     * @param objectClass: the class to query
     * @return list of objects of given type
     * @param <T>
     */
    public <T extends RenderObject> ArrayList<T> getRenderObjectsOfType(Class<T> objectClass) {
        ArrayList<T> objects = new ArrayList<>();
        for (RenderObject renderObject : renderObjects) {
            if (renderObject.getClass() == objectClass) {
                objects.add(objectClass.cast(renderObject));
            }
        }
        return objects;
    }

    /**
     * reposition all render objects to a new position
     * @param p: new position (x,y)
     */
    public void reposition(Point p) {
        for (RenderObject r : renderObjects) {
            r.setLocation(new Point(p));
            Rectangle bounds = new Rectangle(p.x, p.y, r.getBounds().getBounds().width, r.getBounds().getBounds().height);
            r.setBounds(bounds);
        }
    }

    /**
     * disable rendering for all objects of this component
     */
    public void hideAllObjects() {
        for(RenderObject r : renderObjects) {
            r.setHidden(true);
        }
    }

    /**
     * enable rendering for all objects of this component
     */
    public void showAllObjects() {
        for(RenderObject r : renderObjects) {
            r.setHidden(false);
        }
    }
}
