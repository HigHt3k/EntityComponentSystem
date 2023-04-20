package de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.game;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.IdGenerator;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.RenderComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.Layer;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.graphics.objects.TextObject;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.ImageEntity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.colorpalettes.Bit8;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.fonts.FontCollection;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui.SimplePanel;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.entities.ui.TextBody;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.tutorial.TutorialHandler;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.tutorial.Tutorial;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.util.Difficulty;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class TutorialScene extends GameScene implements Tutorial {
    private boolean firstTimeOpened = true;
    private boolean tutorialRunning = false;

    private int currentDialogueId;
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

    public void setFirstTimeOpened(boolean firstTimeOpened) {
        this.firstTimeOpened = firstTimeOpened;
    }

    public boolean isFirstTimeOpened() {
        return firstTimeOpened;
    }

    /**
     * Initialize the scene: check if the scene was already opened or if this is the first time opening it. If first time, play the tutorial
     */
    @Override
    public void init() {
        super.init();
        if(firstTimeOpened) {
            tutorialDialogue();
            firstTimeOpened = false;
        }
    }

    /**
     * Show a tutorial dialogue and set up basic text, bubbles and characters which is then adapted on click with other methods.
     * Only applicable, if level id is specified in switch case
     */
    @Override
    public void tutorialDialogue() {
        tutorialRunning = true;
        Game.input().addHandler(new TutorialHandler());

        // switch tutorial case based on level id, 1 is level 1 and so on
        switch(getId()) {
            case 1 -> {
                // create character models
                currentDialogueId = 900;
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
                        570, 560, 64*12-40, 32*6-20, FontCollection.bit8FontLarge, Bit8.DARK_GREY, "@" + currentDialogueId);
                addEntityToScene(textTina);
            }
        }
    }

    @Override
    public void setTutorialRunning() {

    }

    /**
     * Check if the tutorial is currently running
     * @return true if running
     */
    @Override
    public boolean isTutorialRunning() {
        return tutorialRunning;
    }

    /**
     * Show the next part of a tutorial if a tutorial is specified in the switch case for the current scenario
     */
    @Override
    public void showNextTutorialText() {
        currentDialogueId++;
        if (getId() == 1) {
            switch(currentDialogueId) {
                case 901 -> {
                    removeEntityFromScene(getEntityByName("tina"));

                    Entity ingo = null;
                    try {
                        ingo = new ImageEntity("ingo", IdGenerator.generateId(),
                                ImageIO.read(new File("res/character/Ingo-Ingenieur.png")), 1100, 700, 19 * 8, 27 * 8, Layer.UI);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    addEntityToScene(ingo);

                    getEntityByName("text2").getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText("@" + currentDialogueId);
                    Entity menuMarker = new SimplePanel("menu-marker", IdGenerator.generateId(),
                            1800, 930, 64, 64,
                            null, Bit8.RED, null);
                    addEntityToScene(menuMarker);
                }
                case 902 -> {
                    removeEntityFromScene(getEntityByName("menu-marker"));
                    getEntityByName("text2").getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText("@" + currentDialogueId);
                }
                case 903, 905, 906, 904, 907 -> {
                    getEntityByName("text2").getComponent(RenderComponent.class).getRenderObjectsOfType(TextObject.class).get(0).setText("@" + currentDialogueId);
                }
                case 908 -> {
                    removeTutorial();
                }
            }
        }
    }

    @Override
    public void removeTutorial() {
        Game.input().removeHandler(TutorialHandler.class);
        removeEntityFromScene(getEntityByName("text2"));
        removeEntityFromScene(getEntityByName("bubble2"));
        removeEntityFromScene(getEntityByName("tina"));
        removeEntityFromScene(getEntityByName("ingo"));
    }
}
