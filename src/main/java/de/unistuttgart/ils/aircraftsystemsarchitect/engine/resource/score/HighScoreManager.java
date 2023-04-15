package de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.score;

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
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * The HighScoreManager class is responsible for managing the high scores
 * of the game. It provides methods for adding, loading, and resetting high scores,
 * as well as retrieving high scores for a specific level.
 */
public class HighScoreManager {
    /**
     * The DocumentBuilder object used for parsing XML files.
     */
    private static DocumentBuilder db;
    /**
     * The ArrayList of HighScore objects representing the high scores.
     */
    private ArrayList<HighScore> scores;

    /**
     * Constructs a new HighScoreManager object and initializes the DocumentBuilder
     * object for parsing XML files.
     */
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

    /**
     * Resets all high scores to 0.
     */
    public void resetScores() {
        scores = new ArrayList<>();
    }

    /**
     * Adds a new high score to the list of high scores.
     *
     * @param score The HighScore object representing the new high score to be added.
     */
    public void addScore(HighScore score) {
        try {
            Document document = db.parse("res/scores/highscores.xml");
            Element root = document.getDocumentElement();

            Element highscore = document.createElement("scoreItem");
            Element name = document.createElement("name");
            name.setTextContent(score.getName());
            highscore.appendChild(name);
            Element score_ = document.createElement("score");
            score_.setTextContent(String.valueOf(score.getScore()));
            highscore.appendChild(score_);
            Element level_ = document.createElement("level");
            level_.setTextContent(String.valueOf(score.getLevelId()));
            highscore.appendChild(level_);
            root.appendChild(highscore);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(document);
            File f = new File("res/scores/highscores.xml");
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

    /**
     * Loads the high scores from the specified XML file.
     */
    public void loadScores(String path) {
        ArrayList<HighScore> scoreHashMap = new ArrayList<>();
        try {
            Document doc = db.parse(new File(path));
            doc.getDocumentElement().normalize();

            NodeList scores = doc.getElementsByTagName("scores");

            if (scores.item(0).getNodeType() == Node.ELEMENT_NODE) {
                NodeList scoreItems = scores.item(0).getChildNodes();
                for (int i = 0; i < scoreItems.getLength(); i++) {
                    NodeList scoreItem = scoreItems.item(i).getChildNodes();
                    int score = 0;
                    String name = "";
                    int level = -1;
                    for (int j = 0; j < scoreItem.getLength(); j++) {
                        if (scoreItem.item(j).getNodeName() == "score") {
                            score = Integer.parseInt(scoreItem.item(j).getTextContent());
                        } else if (scoreItem.item(j).getNodeName() == "name") {
                            name = scoreItem.item(j).getTextContent();
                        } else if (scoreItem.item(j).getNodeName() == "level") {
                            level = Integer.parseInt(scoreItem.item(j).getTextContent());
                        }
                    }
                    scoreHashMap.add(new HighScore(name, score, level));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        scores = scoreHashMap;
        Game.logger().info("Successfully loaded scores from scores file. \n" + scores.size());
    }

    /**
     * Saves the high scores to an XML file.
     */
    public static void saveScores() {
        // Not implemented yet
    }

    /**
     * Retrieves the high scores for a specific level.
     *
     * @param id The ID of the level for which to retrieve high scores.
     * @return The ArrayList of HighScore objects representing the high scores for the specified level.
     */
    public ArrayList<HighScore> getLevelScores(int id) {
        ArrayList<HighScore> highscores = new ArrayList<>();

        for (HighScore score : scores) {
            if (score.getLevelId() == id) {
                highscores.add(score);
            }
        }

        highscores.sort((o1, o2) -> {
            if (o1.getScore() > o2.getScore()) {
                return -1;
            } else if (o1.getScore() == o2.getScore()) {
                return 0;
            } else {
                return 1;
            }
        });

        return highscores;
    }

    /**
     * Retrieves all high scores.
     *
     * @return The ArrayList of HighScore objects representing all high scores.
     */
    public ArrayList<HighScore> getScores() {
        return scores;
    }
}
