package de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.lang;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * Class for managing and parsing language files.
 */
public class LanguageManager {
    private static DocumentBuilder db;
    ArrayList<Language> languages = new ArrayList<>();

    /**
     * Constructor for the LanguageManager class. Initializes the Document Builder Factory.
     */
    public LanguageManager() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            db = dbf.newDocumentBuilder();
            Game.logger().info("Successfully initialized Document Builder Factory to parse XML files.");
        } catch (ParserConfigurationException e) {
            Game.logger().severe("Error while configuring the Document Builder Factory to parse XML files: \n" +
                    e.getMessage());
        }

        Game.logger().info("Successfully initialized language manager");
    }

    /**
     * Parses a generic language file located at the specified path.
     *
     * @param path The path to the language file.
     */
    public void parseLanguageFile(String path) {
        Document doc = null;
        try {
            doc = db.parse(new File(path));
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
        doc.getDocumentElement().normalize();

        Node stringsNode = doc.getElementsByTagName("strings").item(0);

        Element stringsElement = (Element) stringsNode;
        LanguageType type = LanguageType.valueOf(stringsElement.getAttribute("language"));
        Language l = new Language(type);

        NodeList strings = stringsNode.getChildNodes();

        for(int i = 0; i < strings.getLength(); i++) {
            if(strings.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Element string = (Element) strings.item(i);

                int id = Integer.parseInt(string.getAttribute("id"));
                String content = removeLineBreaks(String.valueOf(string.getTextContent()));

                l.getLanguage().put(id, content);
            }
        }
        languages.add(l);
        Game.logger().info("Language " + l.getLanguageType() + " successfully loaded.");
    }

    /**
     * Retrieves a string from a language file by its ID and language type.
     *
     * @param id The ID of the string object.
     * @param languageType The type of the language.
     * @return The requested string.
     */
    public String getStringValue(int id, LanguageType languageType) {
        for(Language l : languages) {
            if(l.getLanguageType() != languageType) {
                continue;
            }
            if (l.getLanguage().get(id) == null) {
                return "@value_not_found: " + id;
            }
            return l.getLanguage().get(id);
        }

        return "@value_not_found: " + id;
    }

    /**
     * Removes line breaks from the input string after parsing an XML file.
     *
     * @param in The input string to remove the line breaks from.
     * @return The converted string without any line breaks.
     */
    private String removeLineBreaks(String in) {
        return in.replaceAll("[\\t\\n\\r]+", " ");
    }

    /**
     * Prints translation values that are missing when comparing languages.
     * 
     * DEBUGGING PURPOSE ONLY
     */
    public void printTranslationMissing() {
        // use english as comparator since this is the implementation language
        Language l = getLanguage(LanguageType.EN_US);
        Language ger = getLanguage(LanguageType.DE_DE);
        Language gerSimple = getLanguage(LanguageType.DE_SIMPLE);

        compareLanguages(l, ger);
        compareLanguages(l, gerSimple);
    }

    /**
     * Compares two languages and prints missing translations.
     *
     * DEBUGGING PURPOSE ONLY
     *
     * @param l1 The first language to compare.
     * @param l2 The second language to compare.
     */
    private void compareLanguages(Language l1, Language l2) {
        System.out.println("Comparing " + l1.getLanguageType() + " and " + l2.getLanguageType());
        for(Map.Entry<Integer, String> languageEntry : l1.getLanguage().entrySet()) {
            int key = languageEntry.getKey();
            String value = languageEntry.getValue();

            String valueOther = l2.getLanguage().get(key);
            if(valueOther == null || valueOther.equals("")) {
                System.out.println("Translation missing: @" + key);
            }
        }
    }

    /**
     * Retrieves a Language object by its language type.
     *
     * @param languageType The type of the language to be retrieved.
     * @return The Language object, or null if not found.
     */
    public Language getLanguage(LanguageType languageType) {
        for(Language l : languages) {
            if(l.getLanguageType() == languageType) {
                return l;
            }
        }
        return null;
    }
}
