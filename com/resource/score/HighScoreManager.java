package com.resource.score;

import com.Game;
import com.resource.lang.Language;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

    }

    public static void loadScores() {

    }

    public static void saveScores() {

    }
}
