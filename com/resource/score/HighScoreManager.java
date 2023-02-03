package com.resource.score;

import com.Game;
import com.resource.lang.Language;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HighScoreManager {
    private static DocumentBuilder db;



    public HighScoreManager() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            db = dbf.newDocumentBuilder();
            Game.logger().info("Successfully initialized Document Builder Factory to parse XML files.");
        } catch (ParserConfigurationException e) {
            Game.logger().severe("Error while configuring the Document Builder Factory to parse XML files: \n" +
                    e.getMessage());
        }

        Game.logger().info("Successfully initialized highscore manager");
    }

    public static void resetScores() {

    }

    public static void addScore(HighScore score) {
        try {
            Document document = db.parse("game/res/scores/highscores.xml");
            Element root = document.getDocumentElement();

            Element highscore = document.createElement("scoreItem");
            Element name = document.createElement("name");
            name.setTextContent(score.getName());
            highscore.appendChild(name);
            Element score_ = document.createElement("score");
            score_.setTextContent(String.valueOf(score.getScore()));
            highscore.appendChild(score_);
            root.appendChild(highscore);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(document);
            File f = new File("game/res/scores/highscores.xml");
            f.createNewFile();
            StreamResult result = new StreamResult(f);
            transformer.transform(source, result);

        } catch (SAXException | IOException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void loadScores() {

    }

    public static void saveScores() {

    }
}
