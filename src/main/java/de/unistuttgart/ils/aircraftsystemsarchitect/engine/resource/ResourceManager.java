package de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource;

import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.lang.LanguageManager;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.score.HighScore;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.score.HighScoreManager;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.resource.tiles.TileSet;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.ecs.entity.Entity;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.Game;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.IdGenerator;
import de.unistuttgart.ils.aircraftsystemsarchitect.engine.graphics.scene.Scene;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.components.BuildComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.components.GridComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.components.SimulationComponent;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.handler.simulation.SimulationType;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.game.BuildScene;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.game.TutorialScene;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.util.Difficulty;
import de.unistuttgart.ils.aircraftsystemsarchitect.game.scenes.game.GameScene;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The ResourceManager class is responsible for managing the game's resources, such as language files and high scores.
 */
public class ResourceManager {
    /**
     * The LanguageManager instance used for managing language files.
     */
    private final LanguageManager language = new LanguageManager();

    /**
     * The HighScoreManager instance used for managing high scores.
     */
    private final HighScoreManager highScoreManager = new HighScoreManager();

    /**
     * The DocumentBuilder instance used for parsing XML files.
     */
    private static DocumentBuilder db;

    /**
     * The TileSet instance used for managing tile sets.
     */
    private static TileSet tileSet;

    /**
     * Returns the LanguageManager instance used for managing language files.
     *
     * @return the LanguageManager instance
     */
    public LanguageManager language() {
        return language;
    }

    /**
     * Returns the HighScoreManager instance used for managing high scores.
     *
     * @return the HighScoreManager instance
     */
    public HighScoreManager score() {
        return highScoreManager;
    }

    /**
     * Constructs a new ResourceManager object and initializes the DocumentBuilder instance for parsing XML files.
     */
    public ResourceManager() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            db = dbf.newDocumentBuilder();
            Game.logger().info("Successfully initialized Document Builder Factory to parse XML files.");
        } catch (ParserConfigurationException e) {
            Game.logger().severe("Error while configuring the Document Builder Factory to parse XML files: \n" +
                    e.getMessage());
        }
    }

    /**
     * Loads a TileSet from the given file path, parsing the XML file and extracting the tile information.
     *
     * @param tilesetPath the file path of the TileSet XML file to load
     */
    public void loadTileSet(String tilesetPath) {
        Game.logger().info("Loading TileSet from path: " + tilesetPath);
        TileSet t = new TileSet();

        try {
            Document doc = db.parse(new File(tilesetPath));
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("tile");

            for(int temp = 0; temp < list.getLength(); temp++) {
                Node node = list.item(temp);

                if(node.getNodeType() == Node.ELEMENT_NODE) {
                    Element tile = (Element) node;

                    String id = tile.getAttribute("id");
                    String type = tile.getAttribute("type");
                    String imagePath = ((Element) tile.getElementsByTagName("image").item(0)).getAttribute("source");
                    String description = ((Element) tile.getElementsByTagName("image").item(0)).getAttribute("description");
                    String minNonPassive = ((Element) tile.getElementsByTagName("image").item(0)).getAttribute("minNonPassive");

                    if(description.equals("")) {
                        description = "Default description";
                    }
                    if(SimulationType.contains(type)) {
                        SimulationType simType = SimulationType.valueOf(type);
                        t.getTypes().put(Integer.parseInt(id), SimulationType.valueOf(type));
                    }
                    if(minNonPassive.equals("")) {
                        minNonPassive = "1";
                    }

                    BufferedImage img = null;
                    System.out.println(imagePath);
                    try {
                        img = ImageIO.read(new File(imagePath));
                    } catch (IOException e) {
                        Game.logger().severe("Couldn't load tile: " + id + " - " + e.getMessage());
                    }

                    t.getTiles().put(Integer.parseInt(id), img);
                    t.getDescriptions().put(Integer.parseInt(id), description);
                    t.getMinNonPassives().put(Integer.parseInt(id), Integer.parseInt(minNonPassive));
                }
            }
        } catch (SAXException | IOException e) {
            Game.logger().severe(e.getMessage());
        }

        tileSet = t;

        Game.logger().info("Tileset loaded.");
    }


    /**
     * load a level from a specified path (xml file)
     * @param levelPath: the path to the xml file
     */
    public void loadLevel(String levelPath) {
        //Todo: exception handling
        //TODO: change xml files so that each component contains its attributes
        String levelName = "Default";

        try {
            Document doc = db.parse(new File(levelPath));
            doc.getDocumentElement().normalize();

            NodeList list = doc.getElementsByTagName("map");

            if(list.item(0).getNodeType() == Node.ELEMENT_NODE) {
                Element level = (Element) list.item(0);

                levelName = level.getAttribute("name");
                int mapId = Integer.parseInt(level.getAttribute("id"));
                int width = Integer.parseInt(level.getAttribute("width"));
                int height = Integer.parseInt(level.getAttribute("height"));

                ArrayList<String> descriptions = new ArrayList<>();

                NodeList parts = doc.getElementsByTagName("part");
                for(int i = 0; i < parts.getLength(); i++) {
                    descriptions.add(parts.item(i).getTextContent());
                }

                Difficulty difficulty = Difficulty.valueOf(level.getAttribute("difficulty"));
                GameScene scene;
                if(difficulty == Difficulty.TUTORIAL) {
                    scene = new TutorialScene(levelName, mapId, difficulty);
                    scene.setScenePath(levelPath);
                } else {
                    scene = new GameScene(levelName, mapId, difficulty);
                    scene.setScenePath(levelPath);
                }
                if(descriptions.size() > 0) {
                    scene.setDescription(descriptions.get(0));
                }
                scene.setDescriptions(descriptions);
                scene.setGridSize(width, height);

                NodeList unlocks = level.getElementsByTagName("unlock");
                for(int i = 0; i < unlocks.getLength(); i++) {
                    scene.addUnlockNeeded(Integer.parseInt(unlocks.item(i).getTextContent()));
                }

                if(level.getElementsByTagName("goal").item(0) != null) {
                    double goal = Double.parseDouble(level.getElementsByTagName("goal").item(0).getTextContent());
                    scene.setGoal(goal);
                }

                if(level.getElementsByTagName("goalDefinition").item(0) != null) {
                    Element goalDefinition = (Element) level.getElementsByTagName("goalDefinition").item(0);
                    int accGoal = Integer.parseInt(goalDefinition.getAttribute("workingActuators"));
                    int sensGoal = Integer.parseInt(goalDefinition.getAttribute("workingSensors"));
                    int cGoal = Integer.parseInt(goalDefinition.getAttribute("workingComputers"));
                    scene.setGoal(accGoal, sensGoal, cGoal);
                }

                // Add background to scene
                for(int i = 0; i < width; i++) {
                    for (int j = 0; j < height; j++) {
                        scene.addGridElement(i, j);
                    }
                }

                NodeList layerNodes = level.getElementsByTagName("layer");

                for(int temp = 0; temp < layerNodes.getLength(); temp++) {
                    if(layerNodes.item(temp).getNodeType() == Node.ELEMENT_NODE) {
                        Element layer = (Element) layerNodes.item(temp);

                        int id = Integer.parseInt(layer.getAttribute("id"));
                        NodeList entityNodes = layer.getElementsByTagName("entity");
                        for (int e = 0; e < entityNodes.getLength(); e++) {
                            Element entity = (Element) entityNodes.item(e);

                            int x = Integer.parseInt(entity.getAttribute("x"));
                            int y = Integer.parseInt(entity.getAttribute("y"));
                            int entityId = Integer.parseInt(entity.getAttribute("id"));
                            boolean interactable = false;
                            if (!entity.getAttribute("interactable").equals(""))
                                interactable = Boolean.parseBoolean(entity.getAttribute("interactable"));
                            float safety = 0f;
                            if(!entity.getAttribute("safety").equals("")) {
                                safety = Float.parseFloat(entity.getAttribute("safety"));
                            }
                            float failureDetectionRatio = 0.9f;
                            if(!entity.getAttribute("failureDetectionRatio").equals("")) {
                                failureDetectionRatio = Float.parseFloat(entity.getAttribute("failureDetectionRatio"));
                            }

                            int correctSignalsNeeded = 0;
                            if(!entity.getAttribute("correctSignalsNeeded").equals("")) {
                                correctSignalsNeeded = Integer.parseInt(entity.getAttribute("correctSignalsNeeded"));
                            }

                            int outOfControlSignalsAccepted = 0;
                            if(!entity.getAttribute("outOfControlSignalsAccepted").equals("")) {
                                outOfControlSignalsAccepted = Integer.parseInt(entity.getAttribute("outOfControlSignalsAccepted"));
                            }
                            scene.addSimulationElement(x, y, entityId, safety,correctSignalsNeeded, outOfControlSignalsAccepted, interactable, failureDetectionRatio);
                        }
                    }
                }

                NodeList buildNodes = level.getElementsByTagName("build");
                for(int temp=0; temp<buildNodes.getLength(); temp++) {
                    if(buildNodes.item(temp).getNodeType() == Node.ELEMENT_NODE) {
                        Element build = (Element) buildNodes.item(temp);
                        NodeList entityNodes = build.getElementsByTagName("entity");
                        for (int e = 0; e < entityNodes.getLength(); e++) {
                            Element entity = (Element) entityNodes.item(e);

                            int entityId = Integer.parseInt(entity.getAttribute("id"));
                            int amount = Integer.parseInt(entity.getAttribute("amount"));
                            float failureRatio = Float.parseFloat(entity.getAttribute("safety"));
                            int correctSignalsNeeded = Integer.parseInt(entity.getAttribute("correctSignalsNeeded"));
                            int outOfControlSignalsAccepted = Integer.parseInt(entity.getAttribute("outOfControlSignalsAccepted"));

                            float failureDetectionRatio = 0.9f;
                            if(!entity.getAttribute("failureDetectionRatio").equals("")) {
                                failureDetectionRatio = Float.parseFloat(entity.getAttribute("failureDetectionRatio"));
                            }

                            scene.addToBuildPanel(entityId, amount, failureRatio, correctSignalsNeeded, outOfControlSignalsAccepted, failureDetectionRatio);
                        }
                    }

                }
                Game.scene().addScene(scene);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        Game.logger().info("Loaded level, now available levels: " + Game.scene().getSceneAmount());
    }

    /**
     * Returns the image of the tile with the given ID.
     *
     * @param id the ID of the tile to load
     * @return the image of the tile with the given ID
     */
    public BufferedImage loadTile(int id) {
        return tileSet.getTile(id);
    }

    /**
     * Returns the TileSet instance.
     *
     * @return the TileSet instance
     */
    public TileSet getTileSet() {
        return tileSet;
    }

    /**
     * Loads a player profile file.
     */
    public void loadProfile() {
        //TODO: load a player profile (XML)
    }

    /**
     * Saves the player profile file.
     */
    public void saveProfile() {
        //TODO: save a profile file (XML)
    }

    /**
     * Creates a new player profile file.
     */
    private void createProfile() {
        //TODO: create a new profile file (XML)
    }

    /**
     * Loads a custom font from the given file path and size.
     *
     * @param path the file path of the font file
     * @param size the size of the font to create
     * @return a font from the file if available, or null otherwise
     */
    public Font loadFont(String path, float size) {
        try {
            //create the font to use. Specify the size!
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
            return customFont;
        } catch (IOException e) {
            Game.logger().severe("Font File not found at path " + path + ".\n" + e.getMessage());
        } catch(FontFormatException e) {
            Game.logger().severe("Font File not according to standard format: " + path + ".\n" + e.getMessage());
        }

        Game.logger().info("No Font loaded, null returned.");
        return null;
    }

    /**
     * Saves the level to an XML file, prompting the user to enter a level name and set the level goals.
     *
     * @param scene the current scene, must be an instance of BuildScene
     */
    public void saveLevel(Scene scene) {
        if(scene instanceof BuildScene bs) {
            JTextField fail = new JTextField(8);
            JTextField actuatorsCorrect = new JTextField(5);
            JTextField computersCorrect = new JTextField(5);
            JTextField sensorsCorrect = new JTextField(5);
            JTextField actuatorsOOC = new JTextField(5);
            JTextField computersOOC = new JTextField(5);
            JTextField sensorsOOC = new JTextField(5);

            JPanel myPanel = new JPanel();
            myPanel.setLayout(new GridLayout(7, 3));
            myPanel.add(new JLabel("Failure Probability: "));
            myPanel.add(fail);
            myPanel.add(Box.createHorizontalStrut(15)); // a spacer
            myPanel.add(new JLabel("Correct Sensors Minimum: "));
            myPanel.add(sensorsCorrect);
            myPanel.add(Box.createHorizontalStrut(15)); // a spacer
            myPanel.add(new JLabel("OOC Sensors Maximum: "));
            myPanel.add(sensorsOOC);
            myPanel.add(Box.createHorizontalStrut(15)); // a spacer
            myPanel.add(new JLabel("Correct Computers Minimum: "));
            myPanel.add(computersCorrect);
            myPanel.add(Box.createHorizontalStrut(15)); // a spacer
            myPanel.add(new JLabel("OOC Computers Maximum: "));
            myPanel.add(computersOOC);
            myPanel.add(Box.createHorizontalStrut(15)); // a spacer
            myPanel.add(new JLabel("Correct Actuators Minimum: "));
            myPanel.add(actuatorsCorrect);
            myPanel.add(Box.createHorizontalStrut(15)); // a spacer
            myPanel.add(new JLabel("OOC Actuators Maximum: "));
            myPanel.add(actuatorsOOC);

            int correctActuatorCount;
            int correctComputerCount;
            int correctSensorCount;
            float failureProbability;

            int res = JOptionPane.showConfirmDialog(null, myPanel,
                    "Set the level goal", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                try {
                    correctActuatorCount = Integer.parseInt(actuatorsCorrect.getText());
                    correctComputerCount = Integer.parseInt(computersCorrect.getText());
                    correctSensorCount = Integer.parseInt(sensorsCorrect.getText());
                    failureProbability = Float.parseFloat(fail.getText());
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "invalid number formats");
                    return;
                }
            } else {
                return;
            }

            String path = JOptionPane.showInputDialog("Enter a level name");
            if (path == null) {
                return;
            }
            try {
                Document doc = db.newDocument();

                Element root = doc.createElement("map");
                root.setAttribute("id", String.valueOf(IdGenerator.generateId()));
                root.setAttribute("name", path);
                root.setAttribute("width", String.valueOf(bs.getxMax()));
                root.setAttribute("height", String.valueOf(bs.getyMax()));
                doc.appendChild(root);

                Element description = doc.createElement("description");
                description.setTextContent(bs.getDescription());
                root.appendChild(description);

                Element goal = doc.createElement("goal");
                goal.setTextContent(String.valueOf(failureProbability));
                root.appendChild(goal);

                Element goalDefinition = doc.createElement("goalDefinition");
                goalDefinition.setAttribute("workingActuators", String.valueOf(correctActuatorCount)); // change value
                goalDefinition.setAttribute("workingSensors", String.valueOf(correctSensorCount)); // change value
                goalDefinition.setAttribute("workingComputers", String.valueOf(correctComputerCount)); // change value
                root.appendChild(goalDefinition);

                Element tileset = doc.createElement("tileset");
                tileset.setAttribute("source", "base_tiles.xml");
                root.appendChild(tileset);

                Element build = doc.createElement("build");
                // get all buildable
                for (Entity e : bs.getEntities()) {
                    if (e.getComponent(BuildComponent.class) != null) {
                        Element entity = doc.createElement("entity");
                        entity.setAttribute("id", String.valueOf(e.getComponent(BuildComponent.class).getTileId()));
                        entity.setAttribute("amount", String.valueOf(e.getComponent(BuildComponent.class).getAmount()));
                        entity.setAttribute("safety", String.valueOf(e.getComponent(BuildComponent.class).getFailureRatio()));
                        entity.setAttribute("correctSignalsNeeded", String.valueOf(e.getComponent(BuildComponent.class).getCorrectSignalsNeeded()));
                        entity.setAttribute("outOfControlSignalsAccepted", String.valueOf(e.getComponent(BuildComponent.class).getOutOfControlSignalsAccepted()));
                        build.appendChild(entity);
                    }
                }
                root.appendChild(build);

                Element layer = doc.createElement("layer");
                layer.setAttribute("id", "0");
                layer.setAttribute("name", "Tile layer 0");
                // get all elements on screen
                for(Entity e : bs.getEntities()) {
                    if(e.getComponent(GridComponent.class) != null && e.getComponent(SimulationComponent.class) != null) {
                        Element entity = doc.createElement("entity");
                        entity.setAttribute("x", String.valueOf(e.getComponent(GridComponent.class).getGridLocation().getX()));
                        entity.setAttribute("y", String.valueOf(e.getComponent(GridComponent.class).getGridLocation().getY()));
                        entity.setAttribute("id", String.valueOf(e.getComponent(SimulationComponent.class).getTileId()));
                        entity.setAttribute("interactable", String.valueOf(e.isRemovable()));
                        entity.setAttribute("safety", String.valueOf(e.getComponent(SimulationComponent.class).getFailureRatio()));
                        entity.setAttribute("correctSignalsNeeded", String.valueOf(e.getComponent(SimulationComponent.class).getCorrectSignalsNeeded()));
                        entity.setAttribute("outOfControlSignalsAccepted", String.valueOf(e.getComponent(SimulationComponent.class).getOutOfControlSignalsAccepted()));
                        layer.appendChild(entity);
                    }
                }
                root.appendChild(layer);



                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                DOMSource source = new DOMSource(doc);
                File f = new File("res/level/custom/" + path + ".xml");
                boolean fileCreated = f.createNewFile();
                if(!fileCreated) {
                    Game.logger().severe("The file was not created - no level was saved!");
                    return;
                }
                StreamResult result = new StreamResult(f);
                transformer.transform(source, result);
            } catch (TransformerException | IOException e) {
                e.printStackTrace();
            }
        } else {
            Game.logger().severe("Level not saveable, not instance of Build Scene");
        }
    }

    /**
     * from a file, load the description of a component based on its id
     * @param id: the id to look up
     * @return a description of given id
     */
    public String loadDescription(int id) {
        return tileSet.getDescription(id);
    }

    /**
     * Saves a score using the HighScoreManager
     *
     * @param name: player name
     * @param score: score achieved
     * @param level: level id of the level the score was achieved in
     */
    public void saveScore(String name, int score, int level) {
        highScoreManager.addScore(new HighScore(name, score, level));
    }
}
