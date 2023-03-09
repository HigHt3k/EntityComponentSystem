package game.handler.tutorial;

import engine.Game;
import engine.input.gamepad.InputAction;
import engine.input.handler.Handler;
import engine.input.handler.HandlerType;
import game.scenes.game.TutorialScene;
import game.scenes.menu.LevelMenuScene;

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
                return;
            }
        }
    }

    @Override
    public void handle(InputAction e) {

    }
}
