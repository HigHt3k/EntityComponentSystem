package engine.sound;

import engine.Game;
import engine.ecs.Query;
import engine.ecs.component.SoundComponent;
import engine.ecs.entity.Entity;

import java.util.ArrayList;
import javax.sound.sampled.*;

/**
 * The Sound Engine is responsible for playing different audio files from components
 */
public class SoundEngine {

    /**
     * Query all entities with sound components and play their sound files, if they are currently running. Mute or unmute the sound
     */
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

    /**
     * play an audio stream
     * @param audioInputStream: input sound stream
     */
    private synchronized void playSound(AudioInputStream audioInputStream) {
        Clip clip;
        try {
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Integer.MAX_VALUE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Mute the audio system
     * @param mute: true if muted, false if unmuted
     */
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
