package engine.ecs.component.action;

import engine.Game;
import engine.resource.lang.LanguageType;

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
