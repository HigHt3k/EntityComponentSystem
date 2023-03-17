package de.unistuttgart.ils.skylogic.handler.tutorial;

import de.unistuttgart.ils.skyengine.Game;
import de.unistuttgart.ils.skyengine.input.gamepad.InputAction;
import de.unistuttgart.ils.skyengine.input.handler.Handler;
import de.unistuttgart.ils.skyengine.input.handler.HandlerType;
import de.unistuttgart.ils.skylogic.scenes.game.TutorialScene;
import de.unistuttgart.ils.skylogic.scenes.menu.LevelMenuScene;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class TutorialHandler extends Handler {
    public TutorialHandler() {
        super(HandlerType.EVENT);
    }

    @Override
    public void handle(KeyEvent e) {

    }

    @Override
    public void handle(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            if(Game.scene().current() instanceof LevelMenuScene lms) {
                if (lms.isTutorialRunning()) {
                    lms.showNextTutorialText();
                }
            } else if(Game.scene().current() instanceof TutorialScene ts) {
                if(ts.isTutorialRunning()) {
                    ts.showNextTutorialText();
                }
            } else {
            }
        }
    }

    @Override
    public void handle(InputAction e) {

    }
}
