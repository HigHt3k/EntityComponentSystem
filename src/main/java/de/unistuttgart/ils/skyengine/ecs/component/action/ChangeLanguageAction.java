package de.unistuttgart.ils.skyengine.ecs.component.action;

import de.unistuttgart.ils.skyengine.Game;
import de.unistuttgart.ils.skyengine.resource.lang.LanguageType;

public class ChangeLanguageAction extends Action {
    private LanguageType languageType;

    public ChangeLanguageAction(LanguageType languageType) {
        this.languageType = languageType;
    }

    @Override
    public void handle() {
        Game.config().setLanguage(languageType);
    }
}
