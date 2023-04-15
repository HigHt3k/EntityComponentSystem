package de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.SoundComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;

import javax.sound.sampled.AudioInputStream;

public class SoundEntity extends Entity {
    /**
     * Constructs a new SoundEntity object.
     *
     * @param name the name of the entity
     * @param id the ID of the entity
     * @param audio the audio input stream to play
     */
    public SoundEntity(String name, int id, AudioInputStream audio) {
        super(name, id);

        SoundComponent soundComponent = new SoundComponent();
        soundComponent.setAudioInputStream(audio);
        soundComponent.setEntity(this);
        this.addComponent(soundComponent);
    }
}
