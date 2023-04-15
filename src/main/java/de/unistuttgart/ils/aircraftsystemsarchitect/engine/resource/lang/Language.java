package de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.lang;

import java.util.HashMap;

/**
 * Class representing a language, including its language type and translations.
 */
public class Language {
    private LanguageType languageType;
    private final HashMap<Integer, String> language;

    /**
     * Constructor for the Language class.
     *
     * @param languageType The type of language to be represented.
     */
    public Language(LanguageType languageType) {
        this.languageType = languageType;
        this.language = new HashMap<>();
    }

    /**
     * Gets the language type.
     *
     * @return The language type.
     */
    public LanguageType getLanguageType() {
        return languageType;
    }

    /**
     * Sets the language type.
     *
     * @param languageType The language type to be set.
     */
    public void setLanguageType(LanguageType languageType) {
        this.languageType = languageType;
    }

    /**
     * Gets the language translations.
     *
     * @return A HashMap containing the language translations.
     */
    public HashMap<Integer, String> getLanguage() {
        return language;
    }
}
