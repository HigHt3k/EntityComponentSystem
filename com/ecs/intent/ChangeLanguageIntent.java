package com.ecs.intent;

import com.Game;
import com.ecs.component.collision.CollisionComponent;
import com.ecs.component.IntentComponent;
import com.resource.lang.LanguageType;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class ChangeLanguageIntent extends Intent {
    private LanguageType type;

    /**
     * Change the language of the game on button click in {@link com.config.GameConfiguration}
     * @param type: language to change to
     */
    public ChangeLanguageIntent(LanguageType type) {
        this.type = type;
    }

    @Override
    public void handleIntent(KeyEvent e) {

    }

    @Override
    public void handleIntent(MouseEvent e) {
        if(e.getButton() != MouseEvent.BUTTON1) {
            return;
        }
        IntentComponent ic = getIntentComponent();
        if(ic != null && ic.getEntity().getComponent(CollisionComponent.class) != null) {
            if(ic.getEntity().getComponent(CollisionComponent.class).contains(e.getPoint())) {
                Game.config().setLanguage(type);
            }
        }
    }
}
