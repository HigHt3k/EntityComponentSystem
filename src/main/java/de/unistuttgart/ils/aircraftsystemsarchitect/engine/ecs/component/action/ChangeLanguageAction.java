package de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.component.action;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.lang.LanguageType;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;

/**
 * This action defines a change to a different language available to the game
 */
public class ChangeLanguageAction extends Action {
    private final LanguageType languageType;

    /**
     * create new language change action
     * @param languageType: the language short name to switch to
     */
    public ChangeLanguageAction(LanguageType languageType) {
        this.languageType = languageType;
    }

    /**
     * set the language type of the game config to the given language
     */
    @Override
    public void handle() {
        Game.config().setLanguage(languageType);
    }
}
