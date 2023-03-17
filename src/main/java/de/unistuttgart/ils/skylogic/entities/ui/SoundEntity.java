package de.unistuttgart.ils.skylogic.entities.ui;

import de.unistuttgart.ils.skyengine.ecs.component.SoundComponent;
import de.unistuttgart.ils.skyengine.ecs.entity.Entity;

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
