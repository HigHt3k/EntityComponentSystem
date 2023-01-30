package com.resource;

import java.util.ArrayList;
import java.util.HashMap;

public class Language {
    private LanguageType languageType;
    private HashMap<Integer, String> language;

    public Language(LanguageType languageType) {
        this.languageType = languageType;
        this.language = new HashMap<>();
    }

    public LanguageType getLanguageType() {
        return languageType;
    }

    public void setLanguage(HashMap<Integer, String> language) {
        this.language = language;
    }

    public void setLanguageType(LanguageType languageType) {
        this.languageType = languageType;
    }

    public HashMap<Integer, String> getLanguage() {
        return language;
    }
}
