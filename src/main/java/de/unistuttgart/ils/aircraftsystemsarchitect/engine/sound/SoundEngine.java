package de.unistuttgart.ils.aircraftsystemsarchitect.engine.sound;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.SoundComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.Query;

import java.util.ArrayList;
import javax.sound.sampled.*;

/**
 * The Sound Engine is responsible for playing different audio files from components.
 */
public class SoundEngine {

    /**
     * Query all entities with sound components and play their sound files, if they are currently running. Mute or unmute the sound.
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
     * Play an audio stream.
     *
     * @param audioInputStream the input sound stream
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
     * Mute or unmute the audio system.
     *
     * @param mute true if the audio system should be muted, false if it should be unmuted
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
