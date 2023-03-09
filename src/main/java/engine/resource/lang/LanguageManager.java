package engine.resource.lang;

import engine.Game;
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

// TODO: Implement
public class LanguageManager {
    private static DocumentBuilder db;
    ArrayList<Language> languages = new ArrayList<>();

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
     * parse a generic language file
     * @param path
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
     * get a string from a language file by its id
     * @param id: id of the string object
     * @param languageType: type of the language
     * @return the string
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
     * removes line breaks after parsing a xml file
     *
     * @param in: string to remove the linebreaks from
     * @return: converted string without any linebreak
     */
    private String removeLineBreaks(String in) {
        return in.replaceAll("[\\t\\n\\r]+", " ");
    }

    public void printTranslationMissing() {
        // use english as comparator since this is the implementation language
        Language l = getLanguage(LanguageType.EN_US);
        Language ger = getLanguage(LanguageType.DE_DE);
        Language gerSimple = getLanguage(LanguageType.DE_SIMPLE);

        compareLanguages(l, ger);
        compareLanguages(l, gerSimple);
    }

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

    public Language getLanguage(LanguageType languageType) {
        for(Language l : languages) {
            if(l.getLanguageType() == languageType) {
                return l;
            }
        }
        return null;
    }
}
