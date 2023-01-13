package com.resource;

import com.Game;
import game.components.SimulationType;
import game.scenes.GameScene;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
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

public class ResourceManager {
    private static final LanguageManager language = new LanguageManager();
    private static DocumentBuilder db;

    private static TileSet tileSet;

    public LanguageManager language() {
        return language;
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
        System.out.println("loading level: " + levelPath);
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
                String description = level.getElementsByTagName("description").item(0).getTextContent();

                GameScene scene = new GameScene(levelName, mapId);
                scene.setDescription(description);
                scene.setGridSize(width, height);

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
                                System.out.println(safety);
                            }

                            scene.addSimulationElement(x, y, entityId, safety, interactable);
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

                            scene.addToBuildPanel(entityId, amount, failureRatio);
                        }
                    }

                }
                Game.scene().addScene(scene);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
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
}
