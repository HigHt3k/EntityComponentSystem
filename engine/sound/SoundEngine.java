package engine.sound;

import engine.ecs.Query;
import engine.ecs.component.SoundComponent;
import engine.ecs.entity.Entity;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.util.ArrayList;

public class SoundEngine {

    public void collectAndPlayEntities() {
        ArrayList<Entity> entities = Query.getEntitiesWithComponent(SoundComponent.class);

        for(Entity e : entities) {
            if(!e.getComponent(SoundComponent.class).isPlaying()) {
                playSound(e.getComponent(SoundComponent.class).getAudioInputStream());
                e.getComponent(SoundComponent.class).setPlaying(true);
            }
        }
    }

    private synchronized void playSound(AudioInputStream audioInputStream) {
        Clip clip;
        try {
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
