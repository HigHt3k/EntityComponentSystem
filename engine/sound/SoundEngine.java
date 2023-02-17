package engine.sound;

import engine.ecs.Query;
import engine.ecs.component.SoundComponent;
import engine.ecs.entity.Entity;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.ArrayList;

public class SoundEngine {

    public void collectAndPlayEntities() {
        ArrayList<Entity> entities = Query.getEntitiesWithComponent(SoundComponent.class);

        for(Entity e : entities) {
            Clip clip = null;
            try {
                clip = AudioSystem.getClip();
                clip.open(e.getComponent(SoundComponent.class).getAudioInputStream());
            } catch (LineUnavailableException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

        }
    }
}
