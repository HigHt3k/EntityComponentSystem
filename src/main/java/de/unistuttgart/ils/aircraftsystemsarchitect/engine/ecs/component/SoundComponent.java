package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component;

import javax.sound.sampled.AudioInputStream;

public class SoundComponent extends Component {
    private AudioInputStream audioInputStream;
    private boolean isPlaying = false;

    @Override
    public void update() {

    }

    public void setAudioInputStream(AudioInputStream audioInputStream) {
        this.audioInputStream = audioInputStream;
    }

    public AudioInputStream getAudioInputStream() {
        return audioInputStream;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }
}
