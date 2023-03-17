package de.unistuttgart.ils.skyengine.resource.lang;

import java.util.HashMap;

public class Language {
    private LanguageType languageType;
    private final HashMap<Integer, String> language;

    public Language(LanguageType languageType) {
        this.languageType = languageType;
        this.language = new HashMap<>();
    }

    public LanguageType getLanguageType() {
        return languageType;
    }

    public void setLanguageType(LanguageType languageType) {
        this.languageType = languageType;
    }

    public HashMap<Integer, String> getLanguage() {
        return language;
    }
}
