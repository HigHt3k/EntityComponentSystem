package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.SoundComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;

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
