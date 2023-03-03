package game.entities.ui;

import engine.ecs.component.SoundComponent;
import engine.ecs.entity.Entity;

import javax.sound.sampled.AudioInputStream;

public class SoundEntity extends Entity {
    public SoundEntity(String name, int id, AudioInputStream audio) {
        super(name, id);

        SoundComponent soundComponent = new SoundComponent();
        soundComponent.setAudioInputStream(audio);
        soundComponent.setEntity(this);
        this.addComponent(soundComponent);
    }
}
