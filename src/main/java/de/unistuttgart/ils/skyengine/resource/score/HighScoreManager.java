package de.unistuttgart.ils.skyengine.resource.score;

import de.unistuttgart.ils.skyengine.Game;
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

public class HighScoreManager {
    private static DocumentBuilder db;
    private ArrayList<HighScore> scores;


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

    public void resetScores() {

    }

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

    public void loadScores(String path) {
        ArrayList<HighScore> scoreHashMap = new ArrayList<>();
        try {
            Document doc = db.parse(new File(path));
            doc.getDocumentElement().normalize();

            NodeList scores = doc.getElementsByTagName("scores");

            if(scores.item(0).getNodeType() == Node.ELEMENT_NODE) {
                NodeList scoreItems = scores.item(0).getChildNodes();
                for(int i = 0; i < scoreItems.getLength(); i++) {
                    NodeList scoreItem = scoreItems.item(i).getChildNodes();
                    int score = 0;
                    String name = "";
                    int level = -1;
                    for(int j = 0; j < scoreItem.getLength(); j++) {
                        if(scoreItem.item(j).getNodeName() == "score") {
                            score = Integer.parseInt(scoreItem.item(j).getTextContent());
                        } else if(scoreItem.item(j).getNodeName() == "name") {
                            name = scoreItem.item(j).getTextContent();
                        } else if(scoreItem.item(j).getNodeName() == "level") {
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

    public static void saveScores() {

    }

    public ArrayList<HighScore> getLevelScores(int id) {
        ArrayList<HighScore> highscores = new ArrayList<>();

        for(HighScore score : scores) {
            if(score.getLevelId() == id) {
                highscores.add(score);
            }
        }

        return highscores;
    }

    public ArrayList<HighScore> getScores() {
        return scores;
    }
}
