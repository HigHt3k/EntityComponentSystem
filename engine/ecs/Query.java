package engine.ecs;

import engine.Game;
import engine.ecs.component.Component;
import engine.ecs.entity.Entity;

import java.util.ArrayList;

public class Query {

    public static <T extends Component> ArrayList<Entity> getEntitiesWithComponent(Class<T> componentClass) {
        ArrayList<Entity> entities = new ArrayList<>();
        for(Entity e : Game.scene().current().getEntities()) {
            if(e.getComponent(componentClass) != null) {
                entities.add(e);
            }
        }
        return entities;
    }
}
