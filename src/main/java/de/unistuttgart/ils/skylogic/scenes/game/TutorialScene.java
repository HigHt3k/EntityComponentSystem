package de.unistuttgart.ils.skylogic.scenes.game;

import de.unistuttgart.ils.skyengine.Game;
import de.unistuttgart.ils.skyengine.IdGenerator;
import de.unistuttgart.ils.skyengine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.skyengine.ecs.entity.Entity;
import de.unistuttgart.ils.skyengine.ecs.entity.ImageEntity;
import de.unistuttgart.ils.skyengine.resource.colorpalettes.Bit8;
import de.unistuttgart.ils.skyengine.resource.fonts.FontCollection;
import de.unistuttgart.ils.skylogic.entities.ui.TextBody;
import de.unistuttgart.ils.skylogic.handler.tutorial.TutorialHandler;
import de.unistuttgart.ils.skylogic.scenes.tutorial.Tutorial;
import de.unistuttgart.ils.skylogic.scenes.util.Difficulty;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class TutorialScene extends GameScene implements Tutorial {
    private boolean firstTimeOpened = true;
    private boolean tutorialRunning = false;
    /**
     * create a new Tutorial Scene object;
     *
     * @param name
     * @param id
     * @param difficulty
     */
    public TutorialScene(String name, int id, Difficulty difficulty) {
        super(name, id, difficulty);
    }

    @Override
    public void init() {
        super.init();
        if(firstTimeOpened) {
            tutorialDialogue();
            firstTimeOpened = false;
        }
    }

    @Override
    public void tutorialDialogue() {
        tutorialRunning = true;
        Game.input().addHandler(new TutorialHandler());

        // switch tutorial case based on level id, 1 is level 1 and so on
        switch(getId()) {
            case 1 -> {
                // create character models
                Entity tina = null;
                try {
                    tina = new ImageEntity("tina", IdGenerator.generateId(),
                            ImageIO.read(new File("res/character/Tina-Technik.png")), 1100, 700, 19 * 8, 27 * 8, Layer.UI);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                addEntityToScene(tina);

                // Speech bubble for character Tina
                Entity speechBubble2 = null;
                try {
                    speechBubble2 = new ImageEntity("bubble2", IdGenerator.generateId(),
                            ImageIO.read(new File("res/menus/speechbubble.png")), 550, 550, 64*12, 32*6, Layer.UI);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                addEntityToScene(speechBubble2);
                Entity textTina = new TextBody("text2", IdGenerator.generateId(),
                        570, 560, 64*12-40, 32*6-20, FontCollection.bit8FontLarge, Bit8.DARK_GREY, "@900");
                addEntityToScene(textTina);
            }
            case 2 -> {

            }
        }
    }

    @Override
    public void setTutorialRunning() {

    }

    @Override
    public boolean isTutorialRunning() {
        return tutorialRunning;
    }

    @Override
    public void showNextTutorialText() {
        switch(getId()) {
            case 1 -> {
                removeTutorial();
            }
        }
    }

    @Override
    public void removeTutorial() {
        Game.input().removeHandler(TutorialHandler.class);
        removeEntityFromScene(getEntityByName("text2"));
        removeEntityFromScene(getEntityByName("bubble2"));
        removeEntityFromScene(getEntityByName("tina"));
    }
}
