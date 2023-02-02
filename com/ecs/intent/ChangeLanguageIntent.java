package com.ecs.intent;

import com.Game;
import com.ecs.component.CollisionComponent;
import com.ecs.component.GraphicsComponent;
import com.ecs.component.IntentComponent;
import com.resource.LanguageType;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class ChangeLanguageIntent extends Intent {
    private LanguageType type;

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