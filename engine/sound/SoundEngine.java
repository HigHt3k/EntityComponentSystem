package engine.sound;

import engine.Game;
import engine.ecs.Query;
import engine.ecs.component.SoundComponent;
import engine.ecs.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.*;
import javax.sound.sampled.Control.Type;
import javax.sound.sampled.Mixer.Info;

public class SoundEngine {
    public void collectAndPlayEntities() {
        mute(Game.config().getSound().isMuted());

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

    private void mute(boolean mute) {
        Mixer.Info[] infos = AudioSystem.getMixerInfo();
        for(Mixer.Info info : infos) {
            Mixer mixer = AudioSystem.getMixer(info);

            Line[] lines = mixer.getSourceLines();
            for(Line line : lines) {
                BooleanControl bc = (BooleanControl) line.getControl(BooleanControl.Type.MUTE);
                if(bc != null) {
                    bc.setValue(mute);
                }
            }
        }
    }
}
