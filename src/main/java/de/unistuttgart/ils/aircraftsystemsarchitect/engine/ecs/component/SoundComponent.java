package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component;

import javax.sound.sampled.AudioInputStream;

/**
 * The sound component can be used to store any sound related data of an entity
 */
public class SoundComponent extends Component {
    private AudioInputStream audioInputStream;
    private boolean isPlaying = false;

    /**
     * Set the audio input that should be played from this source
     * @param audioInputStream: the audio input (wav, mp3,...)
     */
    public void setAudioInputStream(AudioInputStream audioInputStream) {
        this.audioInputStream = audioInputStream;
    }

    /**
     * get the stored audio stream
     * @return the audio stream
     */
    public AudioInputStream getAudioInputStream() {
        return audioInputStream;
    }

    /**
     * get playing status of this component
     * @return true if component is already playing
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * set the status of the audio stream component to play
     * @param playing: true if playing, false if not
     */
    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
