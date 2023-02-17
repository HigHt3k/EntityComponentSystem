package engine.ecs.component;

import javax.sound.sampled.AudioInputStream;

public class SoundComponent extends Component {
    private AudioInputStream audioInputStream;

    @Override
    public void update() {

    }

    public void setAudioInputStream(AudioInputStream audioInputStream) {
        this.audioInputStream = audioInputStream;
    }

    public AudioInputStream getAudioInputStream() {
        return audioInputStream;
    }
}
