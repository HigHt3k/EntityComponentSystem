package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.lang.LanguageType;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;

public class ChangeLanguageAction extends Action {
    private final LanguageType languageType;

    public ChangeLanguageAction(LanguageType languageType) {
        this.languageType = languageType;
    }

    @Override
    public void handle() {
        Game.config().setLanguage(languageType);
    }
}
