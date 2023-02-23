package engine.resource;

import engine.Game;
import engine.IdGenerator;
import engine.ecs.entity.Entity;
import engine.graphics.scene.Scene;
import engine.resource.lang.LanguageManager;
import engine.resource.score.HighScore;
import engine.resource.score.HighScoreManager;
import engine.resource.tiles.TileSet;
import game.components.BuildComponent;
import game.components.GridComponent;
import game.components.SimulationComponent;
import game.handler.simulation.SimulationType;
import game.scenes.BuildScene;
import game.scenes.Difficulty;
import game.scenes.GameScene;
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

public class ResourceManager {
    private final LanguageManager language = new LanguageManager();
    private final HighScoreManager highScoreManager = new HighScoreManager();
    private static DocumentBuilder db;

    private static TileSet tileSet;

    public LanguageManager language() {
        return language;
    }

    public HighScoreManager score() {
        return highScoreManager;
    }

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

                    BufferedImage img = ImageIO.read(new File(imagePath));

                    t.getTiles().put(Integer.parseInt(id), img);
                    t.getDescriptions().put(Integer.parseInt(id), description);
                    t.getMinNonPassives().put(Integer.parseInt(id), Integer.parseInt(minNonPassive));
                }
            }
        } catch (SAXException | IOException e) {
            Game.logger().severe(e.getMessage());
        }

        tileSet = t;
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

                GameScene scene = new GameScene(levelName, mapId, difficulty);
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

                            int correctSignalsNeeded = Integer.parseInt(entity.getAttribute("correctSignalsNeeded"));
                            int outOfControlSignalsAccepted = Integer.parseInt(entity.getAttribute("outOfControlSignalsAccepted"));

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

    public BufferedImage loadTile(int id) {
        return tileSet.getTile(id);
    }

    public TileSet getTileSet() {
        return tileSet;
    }

    // Different user profiles can be added to the game, need to create a main menu first.

    public void loadProfile() {
        //TODO: load a player profile (XML)
    }

    public void saveProfile() {
        //TODO: save a profile file (XML)
    }

    private void createProfile() {
        //TODO: create a new profile file (XML)
    }

    /**
     * load a custom font from a given path
     * @param path: path to the font file
     * @return a font from a file if available or null else
     */
    public Font loadFont(String path, float size) {
        try {
            //create the font to use. Specify the size!
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(size);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
            Game.logger().info("Successfully loaded font: " + customFont.getFontName());
            return customFont;
        } catch (IOException e) {
            Game.logger().severe("Font File not found at path " + path + ".\n" + e.getMessage());
        } catch(FontFormatException e) {
            Game.logger().severe("Font File not according to standard format: " + path + ".\n" + e.getMessage());
        }

        Game.logger().info("No Font loaded, null returned.");
        return null;
    }

    public void saveLevel(Scene scene) {
        if(scene instanceof BuildScene bs) {
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
                goal.setTextContent(String.valueOf(bs.getGoal()));
                root.appendChild(goal);

                Element goalDefinition = doc.createElement("goalDefinition");
                goalDefinition.setAttribute("workingActuators", String.valueOf(bs.getAccGoal())); // change value
                goalDefinition.setAttribute("workingSensors", String.valueOf(bs.getSensGoal())); // change value
                goalDefinition.setAttribute("workingComputers", String.valueOf(bs.getcGoal())); // change value
                root.appendChild(goalDefinition);

                Element tileset = doc.createElement("tileset");
                tileset.setAttribute("source", "base_tiles.xml");
                root.appendChild(tileset);

                Element build = doc.createElement("build");
                // get all buildable
                for(Entity e : bs.getEntities()) {
                    if(e.getComponent(BuildComponent.class) != null) {
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
                File f = new File("game/res/level/custom/" + path + ".xml");
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
     * save a level at its current state (for level create mode)
     * @param id: level id, unique
     * @param name: level name
     */
    public void saveLevel(int id, String name) {
        try {
            Document doc = db.newDocument();

            Element rootElement = doc.createElement("map");
            rootElement.setAttribute("id", String.valueOf(id));
            rootElement.setAttribute("name", name);
            doc.appendChild(rootElement);

            Element tileSetElement = doc.createElement("tileset");
            tileSetElement.setAttribute("source","base_tiles.xml");
            rootElement.appendChild(tileSetElement);

            Element descriptionElement = doc.createElement("description");
            rootElement.appendChild(descriptionElement);

            // TODO: Rework level saver
            /*int[] layerIds = map.getUniqueLayerIds();

            for (int layerId : layerIds) {
                Element layerElement = doc.createElement("layer");
                layerElement.setAttribute("id", String.valueOf(layerId));


                for(Entity e : entities) {
                    if(e.getLayerId() == layerId) {
                        Element ent = doc.createElement("entity");
                        ent.setAttribute("id", String.valueOf(e.getId()));
                        ent.setAttribute("x", String.valueOf((int) (e.getPosition().getX() / Game.config().renderConfiguration().getGridPx())));
                        ent.setAttribute("y", String.valueOf((int) (e.getPosition().getY() / Game.config().renderConfiguration().getGridPx())));
                        ent.setAttribute("interactable", String.valueOf(false));
                        layerElement.appendChild(ent);
                    }
                }

                rootElement.appendChild(layerElement);
            } */


            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(doc);
            File f = new File("test/res/level/" + name + ".xml");
            f.createNewFile();
            StreamResult result = new StreamResult(f);
            transformer.transform(source, result);
        } catch (TransformerException | IOException e) {
            e.printStackTrace();
        }
        System.out.println("File saved!");
    }

    /**
     * from a file, load the description of a component based on its id
     * @param id: the id to look up
     * @return a description of given id
     */
    public String loadDescription(int id) {
        return tileSet.getDescription(id);
    }

    public void saveScore(String name, int score, int level) {
        highScoreManager.addScore(new HighScore(name, score, level));
    }
}
